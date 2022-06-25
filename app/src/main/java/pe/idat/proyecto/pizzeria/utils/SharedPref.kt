package pe.idat.proyecto.pizzeria.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

/*almacenar la sesion del usuario*/
class SharedPref(activity:Activity) {

    /*interfaz para la persistencia de datos*/
    private lateinit var  prefs: SharedPreferences

    init {
        prefs = activity.getSharedPreferences("pe.idat.proyecto.pizzeria", Context.MODE_PRIVATE)
    }

    fun save(key:String, objeto:Any){
        try {
            /*convierte os objetos eb json*/
            val gson = Gson()
            /*transforma el objeto a son*/
            val json = gson.toJson(objeto)
            with(prefs.edit()){
                this.putString(key, json)
                this.commit()/*se guarda en SharedPreferences() almacenamiento en sesion*/
            }
        } catch (e: Exception) {
            Log.d("ERROR", "Err: ${e.message}")
        }
    }

    /*key es un atributo del json*/
    fun getData(key:String):String?{
        val data = prefs.getString(key, "")
        return data
    }

    /*cerrar sesion*/
    fun remove(key: String) {
        /*eliminar la informacion de SharedPreferences*/
        prefs.edit().remove(key).apply()
    }
}