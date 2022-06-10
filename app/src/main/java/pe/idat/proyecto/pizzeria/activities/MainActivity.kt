package pe.idat.proyecto.pizzeria.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.activities.client.home.ClientHomeActivity
import pe.idat.proyecto.pizzeria.databinding.ActivityMainBinding
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.providers.UserProvider
import pe.idat.proyecto.pizzeria.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    var userProvider = UserProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener(this)
        binding.imageviewGoToRegister.setOnClickListener(this)

        getUserFromSession()
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnLogin -> login()
            R.id.imageviewGoToRegister -> goToRegiter()
        }
    }

      private fun login() {
          val email = binding.edittexEmail.text.toString()
          val passwword = binding.edittexPassword.text.toString()

        if(isValidateForm(email, passwword)){
            userProvider.login(email, passwword)?.enqueue(object : Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d("MainActivity", "Response: ${response.body()}")

                    if(response.body()?.isSuccess == true){
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        /*si ellogin se realiza, almacena el usuario*/
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Los datos no son correctos", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("MainActivity", "Hubo un error: ${t.message}")
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
          }else{
              Toast.makeText(this, "No es valido", Toast.LENGTH_LONG).show()
          }

        }

    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        startActivity(i)
    }

    private fun saveUserInSession(data:String){
        val sharedPref =SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
    }

    /*validar email, se le puede aplicar este metodo a cualquier string*/
    fun String.isEmailVailValid(): Boolean{
        return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    /*si elusuario existe en sesio*/
    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        val gson = Gson()

        /*si el usuario existe en la sesion*/
        if (!sharedPref.getData("user").isNullOrBlank()){
            /*obtener la informacion*/
            val user = gson.fromJson(sharedPref.getData("user"),User::class.java)/*convertir la informacion a tipo usuario*/
            goToClientHome()
        }
    }

    private fun isValidateForm(email: String, password: String): Boolean {
        if(email.isBlank()){
            return false
        }
        if(password.isBlank()){
            return false
        }

        if(!email.isEmailVailValid()){
            return false
        }
        return true
    }

    private fun goToRegiter() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }
}
