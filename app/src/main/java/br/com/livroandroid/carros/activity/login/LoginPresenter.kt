package br.com.livroandroid.carros.activity.login

import android.annotation.SuppressLint
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.api.LoginService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenter(private val view: LoginView) {

    fun onCreate() {

    }

    @SuppressLint("CheckResult")
    fun onClickLogin(login: String, senha: String) {
        if (login.isEmpty() || senha.isEmpty()) {
            view.showError(R.string.usuario_senha_incorreto)
            return
        }

        view.showProgress();

        LoginService.login(login, senha).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
            { response ->
                if (response.isOk()) {
                    view.loginOk(login)
                } else {
                    view.alert(response.msg)
                }
            },
            { error ->
                view.alert("Erro no login $error")
            })

    }
}
