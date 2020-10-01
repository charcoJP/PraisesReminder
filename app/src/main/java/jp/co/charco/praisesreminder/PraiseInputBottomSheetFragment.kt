package jp.co.charco.praisesreminder

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jp.co.charco.praisesreminder.databinding.FragmetPraiseInputBottomSheetBinding

interface OnInputSubmitListener {
    fun onSubmit(input: String)
}

class PraiseInputBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmetPraiseInputBottomSheetBinding

    private lateinit var listener: OnInputSubmitListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (context is OnInputSubmitListener) {
            listener = context as OnInputSubmitListener
        } else throw IllegalStateException()

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragmet_praise_input_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.prisesInput.addTextChangedListener {
            binding.submit.isEnabled = it?.isNotBlank() == true
        }

        binding.submit.setOnClickListener {
            listener.onSubmit(binding.prisesInput.text.toString())
        }

        binding.prisesInput.requestFocus()
        val inputMethodManager = context?.getSystemService<InputMethodManager>()
        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onDismiss(dialog: DialogInterface) {
        val inputMethodManager = context?.getSystemService<InputMethodManager>()
        inputMethodManager?.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)

        super.onDismiss(dialog)
    }

    companion object {
        fun newInstance() = PraiseInputBottomSheetFragment()
    }
}