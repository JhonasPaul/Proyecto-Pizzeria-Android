package pe.idat.proyecto.pizzeria.providers

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.idat.proyecto.pizzeria.api.ApiRoutes
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.routes.UsersRoutes
import retrofit2.Call
import java.io.File

class UsersProvider {
    private lateinit  var userRaoutes: UsersRoutes

    init {
        val api = ApiRoutes()
        userRaoutes = api.getUserRoutes()
    }

    fun register(user:User): Call<ResponseHttp>?{
        return userRaoutes.register(user)
    }

    fun login(email:String, password:String): Call<ResponseHttp>?{
        return userRaoutes.login(email, password)
    }

    fun update(file: File, user: User): Call<ResponseHttp> {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), user.toJson())

        return userRaoutes.update(image,requestBody)
    }
}
