package br.com.livroandroid.carros.activity.carros

import br.com.livroandroid.carros.domain.Carro

interface CarrosView {
    fun showProgress()
    fun alert(msg: String)
    fun setCarros(carros: List<Carro>)

}
