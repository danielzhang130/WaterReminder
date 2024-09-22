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
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class AddVolumeAdapter(
    private val layoutInflater: LayoutInflater,
    private val onNormalClick: (Int) -> Unit,
    private val onOtherClick: () -> Unit
) :
    RecyclerView.Adapter<AddVolumeAdapter.AddVolumeVH>() {

    private val list = listOf(100, 200, 300, 330, 400, 500, 600)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddVolumeVH {
        val view = layoutInflater.inflate(R.layout.add_volume_item, parent, false)
        return if (viewType == AddVolumeVH.AddVolumeNormalVH.TYPE)
            AddVolumeVH.AddVolumeNormalVH(view)
        else AddVolumeVH.AddVolumeOtherVH(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= list.size) AddVolumeVH.AddVolumeOtherVH.TYPE
        else AddVolumeVH.AddVolumeNormalVH.TYPE
    }

    override fun getItemCount() = list.size/* + 1*/

    override fun onBindViewHolder(holder: AddVolumeVH, position: Int) {
        val volume = if (position >= list.size) -1 else list[position]
        holder.bind(volume, if (position >= list.size) {
            { onOtherClick() }
        } else {
            { onNormalClick(volume) }
        })
    }

    sealed class AddVolumeVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(volume: Int, onClick: () -> Unit)

        class AddVolumeNormalVH(itemView: View) : AddVolumeVH(itemView) {
            companion object {
                const val TYPE = 0
            }

            override fun bind(volume: Int, onClick: () -> Unit) {
                (itemView as? Button)?.apply {
                    text = itemView.context.getString(R.string.volume, volume)
                    setOnClickListener {
                        onClick()
                    }
                }
            }
        }

        class AddVolumeOtherVH(itemView: View) : AddVolumeVH(itemView) {
            companion object {
                const val TYPE = 1
            }

            override fun bind(volume: Int, onClick: () -> Unit) {
                (itemView as? Button)?.apply {
                    text = itemView.context.getString(R.string.other)
                    setOnClickListener {
                        onClick()
                    }
                }
            }
        }
    }
}
