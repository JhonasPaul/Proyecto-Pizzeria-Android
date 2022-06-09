package pe.idat.proyecto.pizzeria.providers

import pe.idat.proyecto.pizzeria.api.ApiRoutes
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.routes.UsersRoutes
import retrofit2.Call

class UserProvider {
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
}