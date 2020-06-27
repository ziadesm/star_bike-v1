package echomachine.com.star_bike_v1.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import echomachine.com.star_bike_v1.Constants
import echomachine.com.star_bike_v1.pojo.FirebaseRegisterDataUser

class SaveRegisterDataWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {
    companion object{
        private const val TAG = "ZiadReceiver"
        private val auth = FirebaseAuth.getInstance()
    }
    override fun doWork(): Result {
        val username: String = inputData.getString(Constants.USERNAME_DATA_KEY).toString()
        val phone: String = inputData.getString(Constants.PHONE_DATA_KEY).toString()
        val email: String = inputData.getString(Constants.EMAIL_DATA_KEY).toString()
        val address: String = inputData.getString(Constants.ADDRESS_DATA_KEY).toString()
        val ref = FirebaseDatabase.getInstance().getReference("users")
        val key: String = auth.currentUser?.uid.toString()

        // Saving Data to SharingPreference to show it in other fragments
        val pref = applicationContext
            .getSharedPreferences(Constants.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
        with(pref.edit()) {
            putString(Constants.USERNAME_PREF_KEY, username)
            putString(Constants.PHONE_PREF_KEY, phone)
            putString(Constants.EMAIL_PREF_KEY, email)
            putString(Constants.ADDRESS_PREF_KEY, address)
            commit()
        }

        // Saving data to Firebase RealTime Database
        val reg = FirebaseRegisterDataUser(username, email, phone, address)
        ref.child(key).setValue(reg)

        return Result.success()
    }
}