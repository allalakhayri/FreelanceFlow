    import android.content.Context
    import android.content.SharedPreferences
    import com.google.firebase.auth.FirebaseAuth

    class SessionManager private constructor(context: Context) {

        private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        private val editor: SharedPreferences.Editor = sharedPreferences.edit()

        fun saveAuthToken(token: String) {
            editor.putString(KEY_AUTH_TOKEN, token)
            editor.apply()
        }

        fun getAuthToken(): String? {
            return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
        }

        fun isLoggedIn(): Boolean {
            return getAuthToken() != null
        }

        fun logout() {
            FirebaseAuth.getInstance().signOut()
            editor.clear()
            editor.apply()
        }

        companion object {
            private const val PREF_NAME = "MyAppPref"
            private const val PRIVATE_MODE = 0
            private const val KEY_AUTH_TOKEN = "auth_token"

            @Volatile
            private var instance: SessionManager? = null

            fun getInstance(context: Context): SessionManager =
                instance ?: synchronized(this) {
                    instance ?: SessionManager(context).also { instance = it }
                }
        }
    }
