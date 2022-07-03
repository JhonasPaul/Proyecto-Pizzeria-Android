package pe.idat.proyecto.pizzeria.api

import pe.idat.proyecto.pizzeria.routes.UsersRoutes

class ApiRoutes {
    val api_URL = "http://192.168.208.1:3000/api/"
    val retrofit = RetrofitClient()

    fun getUserRoutes(): UsersRoutes {
        return retrofit.getClient(api_URL).create(UsersRoutes::class.java)
    }
}