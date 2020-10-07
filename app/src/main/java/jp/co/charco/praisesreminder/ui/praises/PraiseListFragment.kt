package jp.co.charco.praisesreminder.ui.praises

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import jp.co.charco.praisesreminder.OnInputSubmitListener
import jp.co.charco.praisesreminder.PraiseInputBottomSheetFragment
import jp.co.charco.praisesreminder.R
import jp.co.charco.praisesreminder.data.db.entity.Praise
import jp.co.charco.praisesreminder.databinding.FragmentPraiseListBinding
import jp.co.charco.praisesreminder.ui.common.autoCleared
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PraiseListFragment : Fragment(), OnInputSubmitListener {

    private val viewModel: PraiseListViewModel by viewModels()

    private var binding by autoCleared<FragmentPraiseListBinding>()

    private var bottomSheetFragment: BottomSheetDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPraiseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = PraiseListAdapter(
            onItemClick = { showInputBottomSheet(it) },
            onDeleteClick = { viewModel.delete(it) },
        )
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            showInputBottomSheet(Praise.empty())
        }

        binding.bottomAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.select_date) showSelectDateDialog()
            return@setOnMenuItemClickListener true
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            PopupMenu(requireContext(), it).apply {
                setOnMenuItemClickListener {
                    showRequestInputForm()
                    return@setOnMenuItemClickListener true
                }
                menuInflater.inflate(R.menu.menu_bottom_app_bar_popup, menu)
            }.show()
        }

        viewModel.savedPraises.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.successSubmit.observe(viewLifecycleOwner) {
            dismissInputBottomSheet()
            val inputMethodManager = requireContext().getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
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
            }.show(childFragmentManager, MaterialDatePicker::class.simpleName)
    }

    private fun showInputBottomSheet(praise: Praise) {
        bottomSheetFragment = PraiseInputBottomSheetFragment.newInstance(praise)
        bottomSheetFragment?.show(
            childFragmentManager,
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
}