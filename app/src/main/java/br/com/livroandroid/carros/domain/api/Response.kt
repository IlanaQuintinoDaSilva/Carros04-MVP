package br.com.livroandroid.carros.domain.api

data class Response(val id: Long, private val status: String, val msg: String, val url: String) {
    fun isOk() = "OK".equals(status, ignoreCase = true)

    companion object {
        fun error(msg: String): Response {
            return Response(0, "error", msg, "")
        }
    }
}
