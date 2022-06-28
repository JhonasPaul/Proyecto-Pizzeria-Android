package pe.idat.proyecto.pizzeria.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.activities.client.home.ClientHomeActivity
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.providers.UsersProvider
import pe.idat.proyecto.pizzeria.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {
     val TAG = "SaveImageActivity"
     lateinit var  circleImageUser: CircleImageView
     lateinit var  buttonNext: Button
     lateinit var  buttonConfirm: Button

    lateinit var imageFile: File

    var usersProvider = UsersProvider()
    private lateinit var user: User
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        sharedPref = SharedPref(this)

        getUserFromSession()

        circleImageUser = findViewById(R.id.circleimage_user)
        buttonNext = findViewById(R.id.btn_next)
        buttonConfirm = findViewById(R.id.btn_confirm)

        circleImageUser?.setOnClickListener{selectImage()}

        buttonNext.setOnClickListener{goToClientHome()}
        buttonConfirm.setOnClickListener{saveImage()}
    }

    private fun saveImage() {

        if (imageFile != null && user != null) {
            usersProvider.update(imageFile, user).enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    saveUserInSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
        else {
            Toast.makeText(this, "La imagen no puede ser nula ni tampoco los datos de sesion del usuario", Toast.LENGTH_LONG).show()
        }

    }

    /*guarda el usuario en sesion*/
    private fun saveUserInSession(data:String){
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
        goToClientHome()

    }

    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        /*elimina el historial de pantalla*/
        i.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getUserFromSession() {
        val gson = Gson()

        /*si el usuario existe en la sesion*/
        if (!sharedPref.getData("user").isNullOrBlank()){
            /*obtener la informacion*/
             user = gson.fromJson(sharedPref.getData("user"), User::class.java)/*convertir la informacion a tipo usuario*/
        }
    }

    private val starImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        /*captura el dato que el usuario selecciono*/
        val data = result.data

        /*si el usuario selecciona un usuario correctamente*/
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = File(fileUri?.path)/*el archivo que vamos a guardar como imagen en el servidor*/
            circleImageUser.setImageURI(fileUri)/*establece la imagen que el usuario selecciona*/
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "La tarea se cancelo", Toast.LENGTH_LONG).show()

        }
    }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent: Intent ->
                starImageForResult.launch(intent)
            }
    }
}