package jp.co.charco.praisesreminder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import jp.co.charco.praisesreminder.data.db.entity.Praise
import jp.co.charco.praisesreminder.databinding.ActivityMainBinding
import jp.co.charco.praisesreminder.ui.praises.PraiseListFragment
import jp.co.charco.praisesreminder.util.EventObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnInputSubmitListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private var bottomSheetFragment: BottomSheetDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        binding.viewPager.setCurrentItem(pagerAdapter.centerPosition, false)
        binding.viewPager.registerOnPageChangeCallback (object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val additionalDate = position - pagerAdapter.centerPosition
                viewModel.changeDate(additionalDate)
            }
        })

        binding.fab.setOnClickListener {
            showInputBottomSheet(Praise.empty())
        }

        binding.bottomAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.select_date) showSelectDateDialog()
            return@setOnMenuItemClickListener true
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            PopupMenu(this, it).apply {
                setOnMenuItemClickListener {
                    showRequestInputForm()
                    return@setOnMenuItemClickListener true
                }
                menuInflater.inflate(R.menu.menu_bottom_app_bar_popup, menu)
            }.show()
        }

        viewModel.successSubmit.observe(this, EventObserver {
            dismissInputBottomSheet()
            val inputMethodManager = getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        })

        viewModel.showInputBottomSheet.observe(this, EventObserver {
            showInputBottomSheet(it)
        })
    }

    override fun onSubmit(praise: Praise) {
        viewModel.submit(praise)
    }

    @ExperimentalCoroutinesApi
    private fun showSelectDateDialog() {
        MaterialDatePicker.Builder
            .datePicker()
            .setSelection(viewModel.currentEpochMilli)
            .setTitleText(R.string.select_date_picker_title)
            .build().apply {
                addOnPositiveButtonClickListener { time: Long ->
                    viewModel.changeDate(time)
                }
            }.show(supportFragmentManager, MaterialDatePicker::class.simpleName)
    }

    private fun showInputBottomSheet(praise: Praise) {
        bottomSheetFragment = PraiseInputBottomSheetFragment.newInstance(praise)
        bottomSheetFragment?.show(
            supportFragmentManager,
            PraiseInputBottomSheetFragment::class.simpleName
        )
    }

    private fun dismissInputBottomSheet() {
        bottomSheetFragment?.dismiss()
        bottomSheetFragment = null
    }

    private fun showRequestInputForm() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/NuT4uFDjSxmxEWg2A"))
        startActivity(intent)
    }

    private inner class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = Int.MAX_VALUE
        @ExperimentalCoroutinesApi
        override fun createFragment(position: Int): Fragment {
            val additionalDate = position - centerPosition
            return PraiseListFragment.newInstance(additionalDate)
        }

        val centerPosition = itemCount / 2
    }
}