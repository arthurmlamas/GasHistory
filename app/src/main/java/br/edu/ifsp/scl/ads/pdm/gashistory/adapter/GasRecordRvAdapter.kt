package br.edu.ifsp.scl.ads.pdm.gashistory.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.ads.pdm.gashistory.R
import br.edu.ifsp.scl.ads.pdm.gashistory.databinding.LayoutRecordBinding
import br.edu.ifsp.scl.ads.pdm.gashistory.model.GasRecord
import br.edu.ifsp.scl.ads.pdm.gashistory.onClickListener.OnGasRecordClickListener

class GasRecordRvAdapter(
    private val onGasRecordClickListener: OnGasRecordClickListener,
    private val gasRecordsList: MutableList<GasRecord>
) : RecyclerView.Adapter<GasRecordRvAdapter.GasLayoutHolder>() {

    var gasRecordPosition = -1

    inner class GasLayoutHolder(layoutGasRecordBinding: LayoutRecordBinding): RecyclerView.ViewHolder(layoutGasRecordBinding.root), View.OnCreateContextMenuListener {
        val gasRecordDateTv: TextView = layoutGasRecordBinding.gasRecordDateTv
        val gasRecordPriceTv: TextView = layoutGasRecordBinding.gasRecordPriceTv
        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.content_menu_main, menu)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GasLayoutHolder {
        val layoutGasRecordBinding = LayoutRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GasLayoutHolder(layoutGasRecordBinding)
    }

    override fun onBindViewHolder(holder: GasLayoutHolder, position: Int) {
        val gasRecord = gasRecordsList[position]

        with(holder) {
            gasRecordDateTv.text = gasRecord.date.toString()
            gasRecordPriceTv.text = gasRecord.price.toString()
            itemView.setOnClickListener {
                onGasRecordClickListener.onGasRecordClick(position)
            }
            itemView.setOnLongClickListener {
                gasRecordPosition = position
                false
            }
        }
    }

    override fun getItemCount(): Int = gasRecordsList.size
}