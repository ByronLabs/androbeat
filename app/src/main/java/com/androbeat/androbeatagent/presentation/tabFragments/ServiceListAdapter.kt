// ServiceAdapter.kt
package com.androbeat.androbeatagent.presentation.tabFragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androbeat.androbeatagent.R

enum class ServiceAvailability {
    AVAILABLE,
    UNAVAILABLE,
    UNSUPPORTED
}

class ServiceAdapter(
    private var services: List<String>,
    private var serviceStatus: Map<String, ServiceAvailability>
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView2)
        val imageView: ImageView = itemView.findViewById(R.id.imageView8)
        val divider: View = itemView.findViewById(R.id.divider5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.services_list_view, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.textView.text = service

        when (serviceStatus[service]) {
            ServiceAvailability.AVAILABLE -> {
                holder.imageView.setImageResource(R.mipmap.frame1)
            }
            ServiceAvailability.UNAVAILABLE -> {
                holder.imageView.setImageResource(R.drawable.ic_x_red)
            }
            ServiceAvailability.UNSUPPORTED, null -> {
                holder.imageView.setImageResource(R.drawable.grey_line)
            }
        }
    }

    override fun getItemCount(): Int {
        return services.size
    }

    fun updateServiceStatus(newServiceStatus: Map<String, ServiceAvailability>) {
        serviceStatus = newServiceStatus
        notifyDataSetChanged()
    }
}