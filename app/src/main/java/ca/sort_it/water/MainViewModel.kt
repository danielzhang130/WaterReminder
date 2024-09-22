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

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel(application: Application) : AndroidViewModel(application),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val _todayVolume = MutableLiveData<List<Int>>()
    val todayVolume: LiveData<List<Int>> = _todayVolume

    private val _history = MutableLiveData<List<Pair<String, List<Int>>>>()
    val history: MutableLiveData<List<Pair<String, List<Int>>>> = _history

    private val _targetVolume = MutableLiveData<Int>()
    val targetVolume: LiveData<Int> = _targetVolume

    private val sharedPref = getApplication<Application>()
        .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
    private val settings = PreferenceManager.getDefaultSharedPreferences(application)

    init {
        val record = sharedPref.getString(todayString(), "").orEmpty()
        if (record.isNotBlank()) {
            _todayVolume.value = record.split(",").map {
                it.toInt()
            }
        }

        _history.value = sharedPref.all.map {
            val date = LocalDate.parse(it.key, DATE_FORMAT)
            Triple(date, it.key, it.value.toString().split(",").map { it.toInt() })
        }
            .sortedByDescending { it.first }
            .map {
                it.second to it.third
            }

        _targetVolume.value = (settings.getString("target_volume", "3000") as String).toInt()
        settings.registerOnSharedPreferenceChangeListener(this)
    }

    fun addWater(volume: Int) {
        val newVolume = _todayVolume.value.orEmpty() + volume
        _todayVolume.value = newVolume

        sharedPref.edit {
            putString(todayString(), newVolume.joinToString(","))
        }
    }

    companion object {
        private fun todayString(): String {
            val today = LocalDate.now()
            return today.format(DATE_FORMAT)
        }

        private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private const val PREF_KEY = "MainActivity"
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == "target_volume") {
            _targetVolume.value = sharedPreferences.getString(key, "3000")?.toInt() ?: 3000
        }
    }
}