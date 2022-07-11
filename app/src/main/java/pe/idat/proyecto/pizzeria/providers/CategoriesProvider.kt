package pe.idat.proyecto.pizzeria.providers

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.idat.proyecto.pizzeria.api.ApiRoutes
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.routes.CategoriesRoutes
import retrofit2.Call
import java.io.File

class CategoriesProvider(val token:String) {
    private var categoriesRoutes: CategoriesRoutes? = null

    init {
        val api = ApiRoutes()

        categoriesRoutes  = api.getCategoriesRouts(token)
    }

    fun getAll(): Call<ArrayList<Category>>? {
        return categoriesRoutes?.getAll(token)
    }

    fun create(file: File, category: Category): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), category.toJson())

        return categoriesRoutes?.create(image,requestBody, token)
    }
}