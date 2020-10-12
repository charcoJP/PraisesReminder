package jp.co.charco.praisesreminder

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import jp.co.charco.praisesreminder.databinding.ActivityMainBinding
import jp.co.charco.praisesreminder.ui.praises.PraiseListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

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