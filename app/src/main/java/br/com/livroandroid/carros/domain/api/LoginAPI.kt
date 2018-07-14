package br.com.livroandroid.carros.domain.api

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface LoginAPI {

    @POST("login")
    @FormUrlEncoded
    fun login(@Field("login") login: String,@Field("senha") senha:String): Observable<Response>
}
