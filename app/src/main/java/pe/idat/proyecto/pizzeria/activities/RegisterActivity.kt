package pe.idat.proyecto.pizzeria.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View

import android.widget.Toast
import com.google.gson.Gson
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.activities.client.home.ClientHomeActivity
import pe.idat.proyecto.pizzeria.databinding.ActivityRegisterBinding
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.providers.UserProvider
import pe.idat.proyecto.pizzeria.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = "RegisterActivity"

    private lateinit var binding: ActivityRegisterBinding

   /* var imageViewGoToLogin : ImageView? = null
    var editTextName: EditText?=  null
    var editTextLastName: EditText?=  null
    var editTextEmail: EditText?=  null
    var editTextPhone: EditText?=  null
    var editTextPassword: EditText?=  null
    var editTextConfirmPassword: EditText?=  null
    var buttonRegister: Button?=  null*/

    var usersPrivider = UserProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegiter.setOnClickListener(this)
        binding.imageviewGoToLogin.setOnClickListener(this)

      /*  setContentView(R.layout.activity_register)
        imageViewGoToLogin = findViewById(R.id.imageview_go_to_register)
        editTextName = findViewById(R.id.edittext_name)
        editTextLastName = findViewById(R.id.edittext_lastname)
        editTextEmail = findViewById(R.id.edittext_mail)
        editTextPhone = findViewById(R.id.edittext_phone)
        editTextPassword = findViewById(R.id.edittext_password)
        editTextConfirmPassword = findViewById(R.id.edittext_confirm_password)
        buttonRegister = findViewById(R.id.btn_regiter)


        imageViewGoToLogin?.setOnClickListener{goToLogin()}
        buttonRegister?.setOnClickListener { register() }*/
    }
    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnRegiter -> register()
//            R.id.imageviewGoToLogin -> goToLogin()
        }
    }

    private fun register() {
        val name = binding.edittextName.text.toString()
        val lastname = binding.edittextLastname.text.toString()
        val email = binding.edittextMail.text.toString()
        val phone = binding.edittextPhone.text.toString()
        val password = binding.edittextPassword.text.toString()
        val confirmPassword = binding.edittextConfirmPassword.text.toString()

        if(isValidateForm(name = name, password = password, confirmpassword =  confirmPassword, email = email, lastname = lastname, phone = phone)){
//            Toast.makeText(this, "El formulario es valido", Toast.LENGTH_LONG).show()
            val user = User(
                name = name,
                lastname = lastname,
                email = email,
                phone = phone,
                password = password,
            )
            usersPrivider.register(user)?.enqueue(object : Callback<ResponseHttp>{
                /*en el caso que el servidor no retorne una repsuesta*/
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    if (response.body()?.isSuccess == true) {
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }
                                                    /*captura mensajes del back(controller)*/
                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    /*Toast.makeText(this@RegisterActivity, "Ya puedes iniciar sesion", Toast.LENGTH_LONG).show()
                    goToLogin()*/
                    /*respuesta de errores del json del servidor*/
                    Log.d(TAG, "Response ${response}")
                    Log.d(TAG, "Body ${response.body()}")
                }
                    /*si hay fallos al lanzar la peticion(de conexion)*/
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error ${t.message}")
                        Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        Log.d(TAG, "El nombre es: $name")
        Log.d(TAG, "El apellido es: $lastname")
        Log.d(TAG, "El email es: $email")
        Log.d(TAG, "El telefono es: $phone")
        Log.d(TAG, "El password es: $password")
        Log.d(TAG, "El confirmacion del password es: $confirmPassword")

    }

    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        startActivity(i)
    }

    private fun saveUserInSession(data:String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
    }

    /*validar email, se le puede aplicar este metodo a cualquier string*/
    fun String.isEmailVailValid(): Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidateForm(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
        confirmpassword: String
    ): Boolean {
        if(name.isBlank()){
            Toast.makeText(this, "Debes ingresar el nombre", Toast.LENGTH_LONG).show()
            return false
        }
        if(lastname.isBlank()){
            Toast.makeText(this, "Debes ingresar el apellido", Toast.LENGTH_LONG).show()

            return false
        }
        if(phone.isBlank()){
            Toast.makeText(this, "Debes ingresar el telefono", Toast.LENGTH_LONG).show()

            return false
        }
        if(email.isBlank()){
            Toast.makeText(this, "Debes ingresar el email", Toast.LENGTH_LONG).show()

            return false
        }
        if(password.isBlank()){
            Toast.makeText(this, "Debes ingresar el password", Toast.LENGTH_LONG).show()

            return false
        }
        if(confirmpassword.isBlank()){
            Toast.makeText(this, "Debes ingresar la confirmacion del password", Toast.LENGTH_LONG).show()

            return false
        }

        if(!email.isEmailVailValid()){
            Toast.makeText(this, "El main es invalido", Toast.LENGTH_LONG).show()

            return false
        }
        if(password != confirmpassword){
            Toast.makeText(this, "Las contraseñas deben ser iguales ", Toast.LENGTH_LONG).show()

            return false
        }
        return true
    }

//    private fun goToLogin() {
//        val i = Intent(this, MainActivity::class.java)
//        startActivity(i)
//    }


}
