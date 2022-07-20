package pe.idat.proyecto.pizzeria.providers

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.idat.proyecto.pizzeria.api.ApiRoutes
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.Product
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.routes.CategoriesRoutes
import pe.idat.proyecto.pizzeria.routes.ProductsRoutes
import retrofit2.Call
import java.io.File

class ProductsProvider(val token:String) {
    private var productsRoutes: ProductsRoutes? = null

    init {
        val api = ApiRoutes()

        productsRoutes  = api.getProductsRouts(token)
    }

//    fun getAll(): Call<ArrayList<Category>>? {
//        return categoriesRoutes?.getAll(token)
//    }

    /*-->RestaurantProductFragment*/
    fun create(files: List<File>, product: Product): Call<ResponseHttp>? {

        /*almacenar el arreglo de imagenes*/
        val images = arrayOfNulls<MultipartBody.Part>(files.size)

        /*recorrer cada uno de los archivos que recibimos por párametro*/
        for (i in 0 until files.size) {
            val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i])
            /*inserta las iamgenes al arreglo "images"*/
            images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile)
        }


        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJson())

        return productsRoutes?.create(images,requestBody, token)
    }
}