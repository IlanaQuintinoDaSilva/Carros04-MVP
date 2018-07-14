package br.com.livroandroid.carros.domain.api

import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.TipoCarro
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object CarroService {
    private const val baseUrl = "http://livrowebservices.com.br/rest/carros/"
    private var service: CarrosAPI

    init {
        service = RetrofitUtil.getRetrofit(baseUrl).create(CarrosAPI::class.java)
    }

    fun getCarros(tipo: TipoCarro): Observable<List<Carro>> {
//        val url = "$BASE_URL/tipo/${tipo.name}"
//        val json = HttpHelper.get(url)
//        return fromJson(json)

//        val call = service.getCarros(tipo.name)
//        val carros = call.execute().body()
//        return carros?: listOf()

        return service.getCarros(tipo.name)
    }
}