package pe.idat.proyecto.pizzeria.routes

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.idat.proyecto.pizzeria.models.ResponseHttp
import pe.idat.proyecto.pizzeria.models.User
import retrofit2.Call
import retrofit2.http.*

interface UsersRoutes
{
    @POST("users/create")
    fun register(@Body user: User): Call<ResponseHttp>

    @FormUrlEncoded
    @POST("users/login")
    /*parametros del metodo login en el back*/
    fun login(@Field("email")email:String, @Field("password")password:String): Call<ResponseHttp>


    @Multipart
    @PUT("users/update")
    fun update(
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody
    ): Call<ResponseHttp>
}