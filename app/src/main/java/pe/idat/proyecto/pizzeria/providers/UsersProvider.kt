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

class UsersProvider(val token:String? = null) {
    private lateinit  var userRaoutes: UsersRoutes
    private lateinit  var userRaoutesToken: UsersRoutes

    init {
        val api = ApiRoutes()
        userRaoutes = api.getUserRoutes()

        if (token != null) {

        userRaoutesToken = api.getUserRoutesWithToken(token)
        }
    }

    fun register(user:User): Call<ResponseHttp>?{
        return userRaoutes.register(user)
    }

    fun login(email:String, password:String): Call<ResponseHttp>?{
        return userRaoutes.login(email, password)
    }

    fun updateWithoutImage(user:User): Call<ResponseHttp>?{
        return userRaoutesToken.updateWithoutImage(user, token!!)
    }

    fun update(file: File, user: User): Call<ResponseHttp> {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), user.toJson())

        return userRaoutesToken.update(image,requestBody, token!!)
    }
}
