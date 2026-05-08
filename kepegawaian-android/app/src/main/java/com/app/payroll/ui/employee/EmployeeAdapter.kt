package com.app.payroll.ui.employee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.payroll.data.remote.response.UserResponse
import com.app.payroll.databinding.ItemEmployeeBinding

class EmployeeAdapter : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    private val items = mutableListOf<UserResponse>()

    fun submitList(newItems: List<UserResponse>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserResponse) {
            binding.tvName.text = item.name
            binding.tvEmail.text = item.email
            binding.tvRole.text = item.role
        }
    }
}
