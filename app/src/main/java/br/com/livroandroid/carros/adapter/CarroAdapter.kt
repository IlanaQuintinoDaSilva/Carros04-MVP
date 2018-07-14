package br.com.livroandroid.carros.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.extensions.loadUrl
import kotlinx.android.synthetic.main.adapter_carro.view.*

class CarroAdapter(
        val carros: HashMap<String, Carro>,
        val onClick: (Carro) -> Unit) :
        RecyclerView.Adapter<CarroAdapter.CarrosViewHolder>() {

    override fun getItemCount(): Int {
        return this.carros.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_carro,
                parent, false)
        return CarrosViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarrosViewHolder, position: Int) {
        val list = carros.values.toList()
        val carro = list[position]
        val view = holder.itemView
        with(view) {
            tNome.text = carro.nome
            tDesc.text = carro.desc
            img.loadUrl(carro.urlFoto, progress)
            setOnClickListener { onClick(carro) }
        }
    }

    // ViewHolder fica vazio pois usamos o import do Android Kotlin Extensions
    class CarrosViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
