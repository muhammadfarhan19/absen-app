package com.app.payroll.ui.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.payroll.data.remote.response.AttendanceResponse
import com.app.payroll.databinding.ItemAttendanceBinding
import java.text.SimpleDateFormat
import java.util.*

class AdminAttendanceAdapter : RecyclerView.Adapter<AdminAttendanceAdapter.ViewHolder>() {

    private val items = mutableListOf<AttendanceResponse>()

    fun submitList(newItems: List<AttendanceResponse>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ItemAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AttendanceResponse) {
            binding.tvName.text = item.name ?: "Unknown"
            binding.tvDate.text = formatDate(item.date)
            binding.tvStatus.text = item.status
            binding.tvCheckInTime.text = item.checkIn ?: "-"
            binding.tvCheckOutTime.text = item.checkOut ?: "-"
            binding.tvTimes.text = "09:00 - 18:00"
        }

        private fun formatDate(dateStr: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(dateStr)
                val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
                outputFormat.format(date!!)
            } catch (e: Exception) {
                dateStr
            }
        }
    }
}
