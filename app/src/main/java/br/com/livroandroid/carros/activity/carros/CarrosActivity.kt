package br.com.livroandroid.carros.activity.carros

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.BaseActivity
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.extensions.setupToolbar
import kotlinx.android.synthetic.main.activity_carros.*
import kotlinx.android.synthetic.main.include_progress.*
import org.jetbrains.anko.toast

class CarrosActivity : BaseActivity() {
    private var carros = listOf<Carro>()

    private val presenter: CarrosPresenter by lazy {
        CarrosPresenter(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_carros)

        setupToolbar(R.id.toolbar)

        // Views
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()

        presenter.taskCarros()
    }

    private fun onClickCarro(carro: Carro) {
        toast(carro.nome)

        // GA
        val bundle = Bundle()
        bundle.putString("carro",carro.nome)
        mFirebaseAnalytics.logEvent("click_carro", bundle)
    }

    /**
     * View
     */
    private val view = object: CarrosView {
        override fun alert(msg: String) {
            toast(msg)
        }

        override fun setCarros(carros: List<Carro>) {
            val adapter = CarroAdapter(carros) { carro -> onClickCarro(carro) }
            recyclerView.adapter = adapter
            progress.visibility = View.INVISIBLE
            toast("AH")
        }

        override fun showProgress() {
            progress.visibility = View.VISIBLE
        }

    }
}
