package pe.idat.proyecto.pizzeria.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.activities.commom.AppMensaje
import pe.idat.proyecto.pizzeria.activities.commom.TipoMensaje
import pe.idat.proyecto.pizzeria.adapters.CategoriesAdapter
import pe.idat.proyecto.pizzeria.databinding.ActivityRegisterBinding
import pe.idat.proyecto.pizzeria.databinding.FragmentRestaurantProductBinding
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.Product
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.providers.CategoriesProvider
import pe.idat.proyecto.pizzeria.providers.ProductsProvider
import pe.idat.proyecto.pizzeria.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RestaurantProductFragment : Fragment(){
    private lateinit var binding: FragmentRestaurantProductBinding

    val TAG = "ProductFragment"
    var myView: View? = null
    var editTextName: EditText? = null
    var editTextDescription: EditText? = null
    var editTextPrice: EditText? = null
    var imageViewProduct1: ImageView? = null
    var imageViewProduct2: ImageView? = null
    var imageViewProduct3: ImageView? = null
    var bottonCreate: Button? = null
    var spinnerCategories: Spinner? = null

    var imageFile1:File? = null
    var imageFile2:File? = null
    var imageFile3:File? = null

    var categoriesProvider: CategoriesProvider? = null
    var productsProvider: ProductsProvider? = null

    var user: User? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()
    var idCategory = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentRestaurantProductBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_restaurant_product, container, false)
        editTextName = myView?.findViewById(R.id.edittex_name)
        editTextDescription = myView?.findViewById(R.id.edittex_description)
        editTextPrice = myView?.findViewById(R.id.edittex_price)
        imageViewProduct1 = myView?.findViewById(R.id.imageview_image1)
        imageViewProduct2 = myView?.findViewById(R.id.imageview_image2)
        imageViewProduct3 = myView?.findViewById(R.id.imageview_image3)
        bottonCreate = myView?.findViewById(R.id.btn_create)
        spinnerCategories = myView?.findViewById(R.id.spinner_categories)

       bottonCreate?.setOnClickListener { createProduct() }
        imageViewProduct1?.setOnClickListener{selectImage(101)}
        imageViewProduct2?.setOnClickListener{selectImage(102)}
        imageViewProduct3?.setOnClickListener{selectImage(103)}


        sharedPref = SharedPref(requireActivity())

        getUserFromSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        productsProvider = ProductsProvider(user?.sessionToken!!)

        getCategories()
        return myView
    }

    private fun getCategories() {
        categoriesProvider?.getAll()?.enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse(call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>>
            ) {
                if (response.body() != null) {
                    categories = response.body()!!
                    val arrayAdapter = ArrayAdapter<Category>(requireActivity(), android.R.layout.simple_dropdown_item_1line, categories)
                    spinnerCategories?.adapter = arrayAdapter
                    /*selecionar producto*/
                    spinnerCategories?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(adapterview: AdapterView<*>?, view: View?, position: Int, l: Long) {
                            idCategory = categories[position].id!!
                            Log.d(TAG, "id category : ${idCategory}")
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error:  ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUserFromSession() {
        val gson = Gson()
        /*si el usuario existe en la sesion*/
        if (!sharedPref?.getData("user").isNullOrBlank()){
            /*obtener la informacion*/
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)/*convertir la informacion a tipo usuario*/
        }
    }

    /*crear producto*/
    private fun createProduct() {
        val name = editTextName?.text.toString()
        val description = editTextDescription?.text.toString()
        val priceText = editTextPrice?.text.toString()
        val files = ArrayList<File>()

        if (isValidForm(name, description, priceText)) {
            val product = Product(
                name = name,
                description = description,
                price = priceText.toDouble(),
                idCategory = idCategory
            )
            files.add(imageFile1!!)
            files.add(imageFile2!!)
            files.add(imageFile3!!)


            ProgressDialogFragment.showProgressBar(requireActivity())
            productsProvider?.create(files, product)?.enqueue(object : Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d(TAG, "Response: ${response}")
                    Log.d(TAG, "Body: ${response.body()}")
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                    if (response.body()?.isSuccess == true) {
                        resetForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error:  ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    /*limpiar formulario*/
    private fun resetForm() {
        editTextName?.setText("")
        editTextDescription?.setText("")
        editTextPrice?.setText("")
        imageFile1 = null
        imageFile2 = null
        imageFile3 = null
        imageViewProduct1?.setImageResource(R.drawable.ic_image)
        imageViewProduct2?.setImageResource(R.drawable.ic_image)
        imageViewProduct3?.setImageResource(R.drawable.ic_image)
    }

    /*validacion*/
    private fun isValidForm(name:String, descripcion:String, price:String): Boolean {
        if (name.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Ingresa el nombre del producto", Toast.LENGTH_SHORT).show()
            return false
        }
        if (descripcion.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Ingresa la descripción del producto", Toast.LENGTH_SHORT).show()

            return false
        }
        if (price.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Ingresa el precio del producto", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile1 == null) {
            Toast.makeText(requireContext(), "Seleccina la primera imagen", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile2 == null) {
            Toast.makeText(requireContext(), "Seleccina la segunda imagen", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile3 == null) {
            Toast.makeText(requireContext(), "Seleccina la tercera imagen", Toast.LENGTH_SHORT).show()
            return false
        }
        if (idCategory.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Seleccina la categoria delproducto", Toast.LENGTH_SHORT).show()

            return false
        }
        return true
    }

    /*imagen*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
            if (requestCode == 101) {
                imageFile1 = File(fileUri?.path)/*el archivo que vamos a guardar como imagen en el servidor*/
                imageViewProduct1?.setImageURI(fileUri)/*establece la imagen que el usuario selecciona*/
            }
            else if (requestCode == 102) {
                imageFile2 = File(fileUri?.path)/*el archivo que vamos a guardar como imagen en el servidor*/
                imageViewProduct2?.setImageURI(fileUri)/*establece la imagen que el usuario selecciona*/
            }
            else  if (requestCode == 103) {
                imageFile3 = File(fileUri?.path)/*el archivo que vamos a guardar como imagen en el servidor*/
                imageViewProduct3?.setImageURI(fileUri)/*establece la imagen que el usuario selecciona*/
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .start(requestCode)
    }
}