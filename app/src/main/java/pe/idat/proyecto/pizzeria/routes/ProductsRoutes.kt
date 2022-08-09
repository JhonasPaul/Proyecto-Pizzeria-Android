package pe.idat.proyecto.pizzeria.routes

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.Product
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import retrofit2.Call
import retrofit2.http.*

interface ProductsRoutes {

    /*4 listar produtos por categoria: listar producatos por categorias*/
    @GET("products/findByCategory/{id_category}")
    fun findByCategory(
        /*parametros*/
        @Path("id_category") id_category:String,/*parametro de la categoria*/
        @Header("Authorization") token: String /*token. --> ProductsProvider*/
    ):Call<ArrayList<Product>>

    @Multipart
    /*ruta del backend*/
    @POST("products/create")
    fun create(
        @Part images: Array<MultipartBody.Part?>,/*lista de imagenes*/
        @Part("product") products: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
}