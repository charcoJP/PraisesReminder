package jp.co.charco.praisesreminder.ui.praises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jp.co.charco.praisesreminder.MainViewModel
import jp.co.charco.praisesreminder.databinding.FragmentPraiseListBinding
import jp.co.charco.praisesreminder.ui.common.autoCleared
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PraiseListFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: PraiseListViewModel by viewModels()

    private var binding by autoCleared<FragmentPraiseListBinding>()

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
            onItemClick = { mainViewModel.showInputBottomSheet(it) },
            onDeleteClick = { viewModel.delete(it) },
        )
        binding.recyclerView.adapter = adapter

        viewModel.savedPraises.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    companion object {
        const val KEY_ADDITIONAL_DATE = "KEY_ADDITIONAL_DATE"

        fun newInstance(additionalDate: Int) = PraiseListFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_ADDITIONAL_DATE, additionalDate.toLong())
            }
        }
    }
}