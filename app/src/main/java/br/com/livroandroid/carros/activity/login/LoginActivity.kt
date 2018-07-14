package br.com.livroandroid.carros.activity.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.livroandroid.carros.BuildConfig
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.BaseActivity
import br.com.livroandroid.carros.activity.carros.CarrosActivity
import br.com.livroandroid.carros.extensions.setupToolbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity() {

    private val presenter: LoginPresenter by lazy {
        LoginPresenter(view)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        initFirebaseConfig()

        // GA
        val bundle = Bundle()
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupToolbar(R.id.toolbar)

        btLogin.setOnClickListener { onClickLogin() }

        presenter.onCreate()
    }

    @SuppressLint("CheckResult")
    private fun onClickLogin() {

        val login = tLogin.text.toString()
        val senha = tSenha.text.toString()

        //Crashlytics.getInstance().crash()

        presenter.onClickLogin(login, senha)
    }

    /**
     * View
     */
    private val view = object: LoginView {
        override fun showProgress() {
            progress.visibility = View.VISIBLE
        }

        override fun loginOk(login: String) {
            startActivity<CarrosActivity>()

            finish()

            // GA
            val bundle = Bundle()
            bundle.putString("login",login)
            mFirebaseAnalytics.logEvent("login_ok", bundle)
        }

        override fun alert(msg: String) {
            toast(msg)
        }

        override fun alert(msg: Int) {
            toast(msg)
        }

        override fun showError(messageResId: Int) {
            alert(messageResource = messageResId, titleResource = R.string.error).show()
        }
    }

    private fun initFirebaseConfig() {

        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        mFirebaseRemoteConfig.setConfigSettings(configSettings)

        fetchFirebaseConfig()
    }

    @SuppressLint("SetTextI18n")
    private fun fetchFirebaseConfig() {
        mFirebaseAnalytics.logEvent("fetchFirebaseConfig", Bundle())

        var cacheExpiration: Long = 3600

        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)!!
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Fetch Succeeded",
                                Toast.LENGTH_SHORT).show()

                        // After config data is successfully fetched, it must be activated before newly fetched
                        // values are returned.
                        mFirebaseRemoteConfig.activateFetched()

                        val loginDefault = FirebaseRemoteConfig.getInstance().getString("loginDefault")
                        tLogin.setText(loginDefault)
                    } else {
                        Toast.makeText(this@LoginActivity, "Fetch Failed",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }



}
