package br.com.livroandroid.carros.activity.carros

import android.annotation.SuppressLint
import android.view.View
import br.com.livroandroid.carros.R.id.progress
import br.com.livroandroid.carros.R.id.recyclerView
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.api.CarroService
import br.com.livroandroid.carros.extensions.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_carros.*

class CarrosPresenter(private val view: CarrosView) {

    @SuppressLint("CheckResult")
    fun taskCarros() {
        view.showProgress()

        CarroService.getCarros(TipoCarro.Classicos)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                    { carros ->
                        view.setCarros(carros)
                    },
                    {
                        view.alert("Erro na requisição")
                    }
            )
    }
}