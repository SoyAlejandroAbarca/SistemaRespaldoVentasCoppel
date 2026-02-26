package com.example.sistemarespaldoventascoppel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VentaAdapter(
    private var listaVentas: List<Venta>,
    private val onDetalleClick: (Venta) -> Unit
) : RecyclerView.Adapter<VentaAdapter.VentaViewHolder>() {

    class VentaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPedido: TextView = view.findViewById(R.id.tvItemPedido)
        val tvCliente: TextView = view.findViewById(R.id.tvItemCliente)
        val tvProducto: TextView = view.findViewById(R.id.tvItemProducto)
        val btnDetalle: Button = view.findViewById(R.id.btnItemDetalle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venta, parent, false)
        return VentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VentaViewHolder, position: Int) {
        val venta = listaVentas[position]
        holder.tvPedido.text = "Pedido: #${venta.pedido}"
        holder.tvCliente.text = "Cliente: ${venta.nombre} ${venta.apPat} ${venta.apMat}"
        holder.tvProducto.text = "Producto: ${venta.producto}"
        
        holder.btnDetalle.setOnClickListener { onDetalleClick(venta) }
    }

    override fun getItemCount(): Int = listaVentas.size

    fun actualizarLista(nuevaLista: List<Venta>) {
        listaVentas = nuevaLista
        notifyDataSetChanged()
    }
}
