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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val layoutInflater: LayoutInflater) :
    RecyclerView.Adapter<HistoryAdapter.HistoryVH>() {

    var history: List<Pair<String, List<Int>>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryVH {
        return HistoryVH(
            layoutInflater.inflate(R.layout.history_item, parent, false),
            layoutInflater
        )
    }

    override fun getItemCount() = history.size

    override fun onBindViewHolder(holder: HistoryVH, position: Int) {
        holder.bind(history[position])
    }

    class HistoryVH(itemView: View, private val layoutInflater: LayoutInflater) :
        RecyclerView.ViewHolder(itemView) {
        private val list: RecyclerView
        private val adapter: TodayVolumeAdapter

        init {
            itemView.findViewById<RecyclerView>(R.id.history_list).apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                this@HistoryVH.adapter = TodayVolumeAdapter(layoutInflater)
                adapter = this@HistoryVH.adapter
                list = this
            }
        }

        fun bind(history: Pair<String, List<Int>>) {
            itemView.findViewById<TextView>(R.id.history_date).text = history.first
            itemView.findViewById<TextView>(R.id.history_total).text =
                itemView.context.getString(R.string.volume, history.second.sum())
            adapter.todayVolume = history.second
        }
    }
}
