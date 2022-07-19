package pe.idat.proyecto.pizzeria.api

import pe.idat.proyecto.pizzeria.routes.CategoriesRoutes
import pe.idat.proyecto.pizzeria.routes.ProductsRoutes
import pe.idat.proyecto.pizzeria.routes.UsersRoutes

class ApiRoutes {
    val api_URL = "http://192.168.18.138:3000/api/"
    val retrofit = RetrofitClient()

    fun getUserRoutes(): UsersRoutes {
        return retrofit.getClient(api_URL).create(UsersRoutes::class.java)
    }

    /*peticiones con token*/
    fun getUserRoutesWithToken(token:String): UsersRoutes {
        return retrofit.getClientWithToken(api_URL, token).create(UsersRoutes::class.java)
    }

    fun getCategoriesRouts(token:String): CategoriesRoutes {
        return retrofit.getClientWithToken(api_URL, token).create(CategoriesRoutes::class.java)
    }

    fun getProductsRouts(token:String): ProductsRoutes {
        return retrofit.getClientWithToken(api_URL, token).create(ProductsRoutes::class.java)
    }
}