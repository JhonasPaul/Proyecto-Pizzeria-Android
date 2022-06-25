package pe.idat.proyecto.pizzeria.activities.client.home

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.activities.MainActivity
import pe.idat.proyecto.pizzeria.activities.commom.AppMensaje
import pe.idat.proyecto.pizzeria.activities.commom.TipoMensaje
import pe.idat.proyecto.pizzeria.databinding.ActivityClientHomeBinding
import pe.idat.proyecto.pizzeria.databinding.ActivityMainBinding
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.utils.SharedPref

class ClientHomeActivity : AppCompatActivity(), View.OnClickListener {

    private val Tag = "ClientHomeActivity"

    private lateinit var binding: ActivityClientHomeBinding
    lateinit var  sharedPref:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_client_home)

        binding = ActivityClientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        /*se ejecuta cuando se haga click sobre logout*/
        binding.btnLogout.setOnClickListener{logout()}

        getUserFromSession()
    }

    private fun logout() {
        sharedPref.remove("user")
        val i = Intent(this, MainActivity::class.java)
        Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_LONG).show()
        startActivity(i)
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
            R.id.btn_logout -> logout()
        }
    }
}