package com.example.mushroomidentification

//import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView


class Adapter(private val mushroomItemList: MutableList<MushroomItem>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val common_name: String = mushroomItemList[position].c_name
        val scientific_name: String = mushroomItemList[position].sci_name
        val accuracy = mushroomItemList[position].accuracy
        holder.setData(common_name, scientific_name, accuracy)
    }

    override fun getItemCount(): Int {
        return mushroomItemList.size
    }

    inner class ViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val common_name_view: TextView
        private val scientific_name_view: TextView
        private val accuracy_view: TextView

        init {
            common_name_view = itemView.findViewById(R.id.cname)
            scientific_name_view = itemView.findViewById(R.id.sname)
            accuracy_view = itemView.findViewById(R.id.accuracy)
        }

        fun setData(common_name: String?, scientific_name: String?, accuracy: String?) {
            common_name_view.text = common_name
            scientific_name_view.text = scientific_name
            accuracy_view.text = accuracy
        }
    }
}
