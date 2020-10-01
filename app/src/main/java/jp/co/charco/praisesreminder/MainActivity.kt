package jp.co.charco.praisesreminder

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import jp.co.charco.praisesreminder.data.db.entity.Praise
import jp.co.charco.praisesreminder.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), OnInputSubmitListener {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private var bottomSheetFragment: BottomSheetDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // TODO: SAM変換が効かない
        val adapter = PraiseListAdapter(object : OnItemClickListener {
            override fun onDeleteClick(praise: Praise) {
                viewModel.delete(praise)
            }
        })
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            showInputBottomSheet()
        }

        binding.bottomAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.select_date) showSelectDateDialog()
            return@setOnMenuItemClickListener true
        }

        viewModel.savedPraises.observe(this) {
            adapter.submitList(it)
        }

        viewModel.successSubmit.observe(this) {
            dismissInputBottomSheet()
            val inputMethodManager = getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }

    override fun onSubmit(input: String) {
        viewModel.submit(input)
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

    private fun showInputBottomSheet() {
        bottomSheetFragment = PraiseInputBottomSheetFragment.newInstance()
        bottomSheetFragment?.show(
            supportFragmentManager,
            PraiseInputBottomSheetFragment::class.simpleName
        )
    }

    private fun dismissInputBottomSheet() {
        bottomSheetFragment?.dismiss()
        bottomSheetFragment = null
    }
}