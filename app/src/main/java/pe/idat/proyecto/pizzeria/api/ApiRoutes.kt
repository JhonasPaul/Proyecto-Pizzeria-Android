package pe.idat.proyecto.pizzeria.api

import pe.idat.proyecto.pizzeria.routes.UsersRoutes

class ApiRoutes {
    val api_URL = "http://172.26.176.1:3000/api/"
    val retrofit = RetrofitClient()

    fun getUserRoutes(): UsersRoutes {
        return retrofit.getClient(api_URL).create(UsersRoutes::class.java)
    }
}