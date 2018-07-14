package br.com.livroandroid.carros.domain.api

import br.com.livroandroid.carros.domain.Carro
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface CarrosAPI {

    @GET("tipo/{tipo}")
    fun getCarros(@Path("tipo") tipo: String): Observable<List<Carro>>

}