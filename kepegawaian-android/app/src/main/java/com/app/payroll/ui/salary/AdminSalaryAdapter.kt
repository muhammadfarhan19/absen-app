package com.app.payroll.ui.salary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.payroll.data.remote.response.SalaryResponse
import com.app.payroll.databinding.ItemSalaryBinding
import java.text.NumberFormat
import java.util.Locale

class AdminSalaryAdapter : RecyclerView.Adapter<AdminSalaryAdapter.ViewHolder>() {

    private val items = mutableListOf<SalaryResponse>()

    fun submitList(newItems: List<SalaryResponse>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSalaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ItemSalaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SalaryResponse) {
            binding.tvMonth.text = item.month
            binding.tvEmployeeName.text = item.name ?: "Unknown"
            binding.tvTotalGaji.text = formatCurrency(item.totalGaji)
            binding.tvTotalPotongan.text = "Potongan: ${formatCurrency(item.totalPotongan)}"
        }

        private fun formatCurrency(amount: Double): String {
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            return format.format(amount)
        }
    }
}
