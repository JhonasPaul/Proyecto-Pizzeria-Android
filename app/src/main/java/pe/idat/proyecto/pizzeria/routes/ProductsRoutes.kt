package pe.idat.proyecto.pizzeria.routes

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import retrofit2.Call
import retrofit2.http.*

interface ProductsRoutes {

//    @GET("products/getAll")
//    fun getAll(
//        @Header("Authorization") token: String
//    ):Call<ArrayList<Category>>

    @Multipart
    /*ruta del backend*/
    @POST("products/create")
    fun create(
        @Part images: Array<MultipartBody.Part?>,/*lista de imagenes*/
        @Part("product") products: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
}