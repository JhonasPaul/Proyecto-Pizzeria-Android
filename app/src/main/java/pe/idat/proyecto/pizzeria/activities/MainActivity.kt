package pe.idat.proyecto.pizzeria.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.databinding.ActivityMainBinding
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.providers.UserProvider
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

                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
          }else{
              Toast.makeText(this, "No es valido", Toast.LENGTH_LONG).show()
          }

//          Log.d("MainActivity", "El password es: $passwword")
      }

    /*validar email, se le puede aplicar este metodo a cualquier string*/
    fun String.isEmailVailValid(): Boolean{
        return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
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
