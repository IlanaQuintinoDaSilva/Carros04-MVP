package br.com.livroandroid.carros.activity

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    // Propriedade para acessar o contexto de qualquer lugar
    protected val context: Context get() = this
    // Métodos comuns para todas activities aqui...

    protected val mFirebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(this)
    }

    protected val mFirebaseRemoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }
}
