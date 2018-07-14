package br.com.livroandroid.carros.activity.carros

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.BaseActivity
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.extensions.setupToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_carros.*
import kotlinx.android.synthetic.main.include_progress.*
import org.jetbrains.anko.toast

class CarrosActivity : BaseActivity() {



    companion object {
        private const val TAG = "up"
    }
    private var carros = listOf<Carro>()

    private val presenter: CarrosPresenter by lazy {
        CarrosPresenter(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_carros)

        setupToolbar(R.id.toolbar)

        // Views
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_carros, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {
                val user = FirebaseAuth.getInstance().currentUser
                if(user != null) {
                    FirebaseAuth.getInstance().signOut()
                    finish()
                }
                return true
            }
            R.id.action_add_carros -> {
                addCarros()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addCarros() {
        val database = FirebaseDatabase.getInstance()
        val refCarros = database.reference.child("carros")

        val carros = mutableListOf<Carro>()
        carros.add(Carro(1, "Fusca", "http://www.livroandroid.com.br/livro/carros/classicos/Tucker.png"))
        carros.add(Carro(2, "Ferrari", "https://s3-sa-east-1.amazonaws.com/livro-aws/fotos/Ferrari_FF.png"))

        refCarros.setValue(carros)

        // Read from the database
        refCarros.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "onDataChange: $dataSnapshot")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    override fun onResume() {
        super.onResume()

       // presenter.taskCarros()
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
           /* val adapter = CarroAdapter(carros) { carro -> onClickCarro(carro) }
            recyclerView.adapter = adapter
            progress.visibility = View.INVISIBLE
            toast("AH")*/
        }

        override fun showProgress() {
            progress.visibility = View.VISIBLE
        }

    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)

        val carros = hashMapOf<String,Carro>()
        recyclerView.adapter = CarroAdapter(carros) { c ->
            toast(c.nome)
        }

        val database = FirebaseDatabase.getInstance()
        val refCarros = database.reference.child("carros")

        refCarros.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG,"onCancelled")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.d(TAG,"onChildMoved $p0")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d(TAG,"onChildChanged $p0 $p1")

                onChildAdded(p0,p1)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d(TAG,"onChildAdded $p0 $p1")

                val c = p0.getValue(Carro::class.java)

                Log.d(TAG,"json ${p0.value.toString()}")

                carros[p0.key!!] = c!!
                recyclerView.adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                Log.d(TAG,"onChildRemoved")
                carros.remove(p0.key)
                recyclerView.adapter.notifyDataSetChanged()
            }
        })
    }
}
