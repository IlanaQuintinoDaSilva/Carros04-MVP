package br.com.livroandroid.carros.domain.api

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object LoginService {

    private const val baseUrl = "http://livrowebservices.com.br/rest/"
    private var api: LoginAPI

    init {
        api = RetrofitUtil.getRetrofit(baseUrl).create(LoginAPI::class.java)
    }

    fun login(login: String, senha:String): Observable<Response> {
        return api.login(login,senha)

//        val response = api.login(login,senha).execute().body()
//        return response ?: Response.error("Erro no login")

//        val json = HttpHelper.postForm(LoginService.url, "login" to login, "senha" to senha)
//        return fromJson(json)
    }
}