package pe.idat.proyecto.pizzeria.activities.client.home

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.activities.MainActivity
import pe.idat.proyecto.pizzeria.databinding.ActivityClientHomeBinding
import pe.idat.proyecto.pizzeria.fragments.client.ClientCategoriesFragment
import pe.idat.proyecto.pizzeria.fragments.client.ClientOrdersFragment
import pe.idat.proyecto.pizzeria.fragments.client.ClientProfileFragment
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.utils.SharedPref

class ClientHomeActivity : AppCompatActivity(), View.OnClickListener {

    private val Tag = "ClientHomeActivity"

    var bottomNavigation: BottomNavigationView? = null

    private lateinit var binding: ActivityClientHomeBinding
    lateinit var  sharedPref:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_client_home)

        binding = ActivityClientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        /*se ejecuta cuando se haga click sobre logout*/
//        binding.btnLogout.setOnClickListener{logout()}

        openFragment(ClientCategoriesFragment())


        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home ->{
                    openFragment(ClientCategoriesFragment())
                    true
                }

                R.id.item_orders ->{
                    openFragment(ClientOrdersFragment())
                    true
                }
                R.id.item_profile ->{
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }
        }
        getUserFromSession()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        val gson = Gson()

        /*si el usuario existe en la sesion*/
        if (!sharedPref.getData("user").isNullOrBlank()){
            /*obtener la informacion*/
            val user = gson.fromJson(sharedPref.getData("user"),User::class.java)/*convertir la informacion a tipo usuario*/
            Log.d(Tag, "Usuario: ${user}")
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
//            R.id.btn_logout -> logout()
        }
    }
}