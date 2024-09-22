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
import androidx.recyclerview.widget.RecyclerView

class TodayVolumeAdapter(private val layoutInflater: LayoutInflater) :
    RecyclerView.Adapter<TodayVolumeAdapter.TodayVolumeVH>() {

    var todayVolume: List<Int> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayVolumeVH {
        return TodayVolumeVH(
            layoutInflater.inflate(R.layout.today_volume_item, parent, false)
        )
    }

    override fun getItemCount() = todayVolume.size

    override fun onBindViewHolder(holder: TodayVolumeVH, position: Int) {
        holder.bind(todayVolume[position])
    }

    class TodayVolumeVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(volume: Int) {
            itemView.findViewById<TextView>(R.id.today_volume_item_text).text =
                itemView.context.getString(R.string.volume, volume)
        }
    }
}
