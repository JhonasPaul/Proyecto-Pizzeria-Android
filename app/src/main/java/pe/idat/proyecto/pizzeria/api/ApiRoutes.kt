package pe.idat.proyecto.pizzeria.api

import pe.idat.proyecto.pizzeria.routes.UsersRoutes

class ApiRoutes {
    val api_URL = "http://172.23.240.1:3000/api/"
    val retrofit = RetrofitClient()

    fun getUserRoutes(): UsersRoutes {
        return retrofit.getClient(api_URL).create(UsersRoutes::class.java)
    }

    /*peticiones con token*/
    fun getUserRoutesWithToken(token:String): UsersRoutes {
        return retrofit.getClientWithToken(api_URL, token).create(UsersRoutes::class.java)
    }

}