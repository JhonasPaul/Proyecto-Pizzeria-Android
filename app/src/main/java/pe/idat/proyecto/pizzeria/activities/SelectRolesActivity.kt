package pe.idat.proyecto.pizzeria.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.adapters.RolesAdapter
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.utils.SharedPref

class SelectRolesActivity : AppCompatActivity() {

    lateinit var recyclerViewRoles: RecyclerView
    lateinit var user:User
    lateinit var adapter:RolesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)

        recyclerViewRoles = findViewById(R.id.recyclerview_roles)
        recyclerViewRoles.layoutManager = LinearLayoutManager(this)

        getUserFromSession()

        adapter = RolesAdapter(this, user.roles!!)
        recyclerViewRoles.adapter = adapter
    }

    /*USUARIO Y SESION*/
    /*si elusuario existe en sesio*/
    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        val gson = Gson()

        /*si el usuario existe en la sesion*/
        if (!sharedPref.getData("user").isNullOrBlank()){
            /*obtener la informacion*/
             user = gson.fromJson(sharedPref.getData("user"), User::class.java)/*convertir la informacion a tipo usuario*/

        }
    }
}