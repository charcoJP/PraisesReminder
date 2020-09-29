package jp.co.charco.praisesreminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import jp.co.charco.praisesreminder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = PraiseListAdapter()
        binding.recyclerView.adapter = adapter

        binding.prisesInput.addTextChangedListener {
            viewModel.praisesInputChanged(it?.toString() ?: "")
        }

        viewModel.savedPraises.observe(this) {
            adapter.submitList(it)
        }

        viewModel.successSubmit.observe(this) {
            binding.prisesInput.clearFocus()
            val inputMethodManager = getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }
}