package br.com.livroandroid.carros.activity.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.livroandroid.carros.BuildConfig
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.BaseActivity
import br.com.livroandroid.carros.activity.carros.CarrosActivity
import br.com.livroandroid.carros.extensions.setupToolbar
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

class LoginActivity : BaseActivity() {

    private val presenter: LoginPresenter by lazy {
        LoginPresenter(view)
    }


    companion object {
        private const val TAG = "livroandroid"
        private const val GOOGLE_SIGN_IN = 123
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        val user = FirebaseAuth.getInstance().currentUser
        if(user != null) {
            go(user)
        }

        initFirebaseConfig()

        // GA
        val bundle = Bundle()
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupToolbar(R.id.toolbar)

        btLogin.setOnClickListener { onClickLogin() }

        btGoogle.setOnClickListener { onClickGoogle() }

        presenter.onCreate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                if(user != null) {
                    go(user)
                }
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                toast(response?.error?.message!!)
            }
        }
    }

    private fun go(user: FirebaseUser) {
        toast("User logado ${user.email}")
        startActivity<CarrosActivity>()
        finish()
    }

    @SuppressLint("CheckResult")
    private fun onClickLogin() {

        val login = tLogin.text.toString()
        val senha = tSenha.text.toString()

        //Crashlytics.getInstance().crash()
        //test

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

    private fun onClickGoogle() {
        val providers = Arrays.asList(
                AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                GOOGLE_SIGN_IN)
    }

    private fun initDynamicLink(intent: Intent) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this) {
                    var deepLink: Uri? = null
                    if (it != null) {
                        deepLink = it.link


                        val protocol = deepLink.scheme
                        val server = deepLink.authority
                        val path = deepLink.path
                        val args = deepLink.queryParameterNames
                        val p = deepLink.getQueryParameter("p")
                        val nome = deepLink.getQueryParameter("nome")

                        Log.d(Companion.TAG, "-------------------")
                        Log.d(Companion.TAG, "deepLink $deepLink")
                        Log.d(Companion.TAG, "protocol $protocol")
                        Log.d(Companion.TAG, "server $server")
                        Log.d(Companion.TAG, "path $path")
                        Log.d(Companion.TAG, "args $args")
                        Log.d(Companion.TAG, "p $p")
                        Log.d(Companion.TAG, "nome $nome")
                        Log.d(Companion.TAG, "-------------------")
                    }
                }
                .addOnFailureListener(this) {
                    e ->
                    Log.w(TAG, "getDynamicLink:onFailure", e)
                }
    }

}
