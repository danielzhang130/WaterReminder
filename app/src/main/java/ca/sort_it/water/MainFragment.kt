/*
 *  Copyright (c) 2024 danielzhang130
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.sort_it.water

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.sort_it.water.databinding.FragmentMainBinding
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val viewModel: MainViewModel by viewModels()

        val todayVolumeAdapter = TodayVolumeAdapter(layoutInflater)
        binding.todayVolumeList.adapter = todayVolumeAdapter
        binding.todayVolumeList.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        val addVolumeAdapter = AddVolumeAdapter(layoutInflater,
            onNormalClick = {
                viewModel.addWater(it)
            },
            onOtherClick = {})
        binding.addList.adapter = addVolumeAdapter
        binding.addList.layoutManager = FlexboxLayoutManager(context).apply {
            justifyContent = JustifyContent.SPACE_EVENLY

        }

        viewModel.todayVolume.observe(viewLifecycleOwner) {
            binding.todayText.text =
                getString(R.string.today_total, it.sum(), viewModel.targetVolume.value)
            todayVolumeAdapter.todayVolume = it
            todayVolumeAdapter.onEvent(viewModel.event)
        }

        viewModel.targetVolume.observe(viewLifecycleOwner) {
            binding.todayText.text = getString(
                R.string.today_total,
                viewModel.todayVolume.value.orEmpty().sum(),
                viewModel.targetVolume.value
            )
        }

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}