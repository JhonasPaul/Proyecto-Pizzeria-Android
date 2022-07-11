package pe.idat.proyecto.pizzeria.routes

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import retrofit2.Call
import retrofit2.http.*

interface CategoriesRoutes {

    @GET("categories/getAll")
    fun getAll(
        @Header("Authorization") token: String
    ):Call<ArrayList<Category>>

    @Multipart
    @POST("categories/create")
    fun create(
        @Part image: MultipartBody.Part,
        @Part("category") category: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
}