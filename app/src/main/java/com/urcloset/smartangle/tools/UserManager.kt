package com.urcloset.smartangle.tools




import android.content.Context
import android.content.SharedPreferences
import com.urcloset.smartangle.model.User


class UserManager(private val context: Context) {
    private val preferences: SharedPreferences

    //user.setChat_token(preferences.getString("CHAT_TOKEN", ""));
    val user: User
        get() {
            val user = User(
                preferences.getString("USER_NAME", ""),
                preferences.getString("DB_JWT", "")

            )
            //user.setToken(preferences.getString("DB_JWT", ""))
            return user
        }

    val passwordDB: String?
        get() = preferences.getString("PASSWORD", "")

    val userToken: String?
        get() = preferences.getString("DB_JWT", "")

    init {
        preferences = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
    }

    fun storeUser(user: User?) {
        val editor = preferences.edit()
//        editor.putString("ID", user.getId())
//        //editor.putString("PASSWORD", user.getPassword());
//        editor.putString("EMAIL", user.getEmail())
//        editor.putString("FIRSTNAME", user.getProfile().getFirstName())
//        editor.putString("LASTNAME", user.getProfile().getLastName())
        editor.putString("DB_JWT", user?.token)
        editor.apply()
    }

    fun storePassword(password: String) {
        val editor = preferences.edit()
        editor.putString("PASSWORD", password)
        editor.apply()
    }


    fun clearUserData() {
        preferences.edit().remove("ID").apply()
        preferences.edit().remove("EMAIL").apply()
        preferences.edit().remove("FIRSTNAME").apply()
        preferences.edit().remove("LASTNAME").apply()
        preferences.edit().remove("PASSWORD").apply()
        preferences.edit().remove("DB_JWT").apply()


    }
}
