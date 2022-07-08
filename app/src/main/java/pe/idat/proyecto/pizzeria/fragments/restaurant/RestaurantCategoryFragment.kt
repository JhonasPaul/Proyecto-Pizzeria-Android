package pe.idat.proyecto.pizzeria.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.providers.CategoriesProvider
import pe.idat.proyecto.pizzeria.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RestaurantCategoryFragment : Fragment() {
    val TAG = "CategoryFragment"

    var myView: View? = null
    var imageViewCategory: ImageView? = null
    var ediTextCategory: EditText? = null
    var buttomCreate: Button? = null

    private var imageFile: File? = null

    var categoriesProvider:CategoriesProvider? = null

    var sharedPref: SharedPref? = null
    var user:User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_restaurant_category, container, false)

        sharedPref = SharedPref(requireActivity())

        imageViewCategory = myView?.findViewById(R.id.imageview_category)
        ediTextCategory = myView?.findViewById(R.id.edittext_category)
        imageViewCategory = myView?.findViewById(R.id.imageview_category)
        buttomCreate = myView?.findViewById(R.id.btn_create)

        imageViewCategory?.setOnClickListener{selectImage()}
        buttomCreate?.setOnClickListener { createCategory() }

        getUserFromSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        return myView
    }

    private fun getUserFromSession() {
        val gson = Gson()
        /*si el usuario existe en la sesion*/
        if (!sharedPref?.getData("user").isNullOrBlank()){
            /*obtener la informacion*/
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)/*convertir la informacion a tipo usuario*/
        }
    }

    private fun createCategory() {
        val name = this.ediTextCategory?.text.toString()

        if (imageFile != null) {

            val category = Category(name = name)

            categoriesProvider?.create(imageFile!!, category)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()

                    if (response.body()?.isSuccess == true) {
                        crearForm()
                    }
                }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }else{
            Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun crearForm() {
        ediTextCategory?.setText("")
        imageFile = null
        imageViewCategory?.setImageResource(R.drawable.ic_image)
    }

    private val starImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        /*captura el dato que el usuario selecciono*/
        val data = result.data

        /*si el usuario selecciona un usuario correctamente*/
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = File(fileUri?.path)/*el archivo que vamos a guardar como imagen en el servidor*/
            imageViewCategory?.setImageURI(fileUri)/*establece la imagen que el usuario selecciona*/
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(requireContext(), "La tarea se cancelo", Toast.LENGTH_LONG).show()

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