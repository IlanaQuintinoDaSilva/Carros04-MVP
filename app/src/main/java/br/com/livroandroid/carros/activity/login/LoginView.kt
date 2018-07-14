package br.com.livroandroid.carros.activity.login

interface LoginView {
    fun showError(msg: Int)
    fun alert(msg: Int)
    fun alert(msg: String)
    fun showProgress()
    fun loginOk(login: String)
}