package jp.co.charco.praisesreminder

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jp.co.charco.praisesreminder.data.db.entity.Praise
import jp.co.charco.praisesreminder.databinding.ActivityMainBinding

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