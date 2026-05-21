package com.app.payroll.ui.salary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.payroll.data.remote.response.SalaryResponse
import com.app.payroll.databinding.ItemSalaryBinding
import java.text.NumberFormat
import java.util.Locale

class SalaryAdapter : RecyclerView.Adapter<SalaryAdapter.ViewHolder>() {

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
            binding.tvEmployeeName.text = item.name ?: "User"
            binding.tvTotalGaji.text = formatCurrency(item.totalGaji)
            binding.tvTotalPotongan.text = "Potongan: ${formatCurrency(item.totalPotongan)}"

            // Bind Field Baru BPJS (Gunakan nilai default 0 jika data bernilai null/lama)
            val bpjsKes = item.bpjsKesehatan ?: 0.0
            val bpjsKet = item.bpjsKetenagakerjaan ?: 0.0
            binding.tvBpjsKesehatan.text = "BPJS Kesehatan: ${formatCurrency(bpjsKes)}"
            binding.tvBpjsKetenagakerjaan.text = "BPJS Ketenagakerjaan: ${formatCurrency(bpjsKet)}"
        }

        private fun formatCurrency(amount: Double): String {
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            return format.format(amount)
        }
    }
}
