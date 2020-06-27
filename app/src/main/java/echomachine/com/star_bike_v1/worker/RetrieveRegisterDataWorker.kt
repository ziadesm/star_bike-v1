package echomachine.com.star_bike_v1.worker
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import echomachine.com.star_bike_v1.Constants

class RetrieveRegisterDataWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {
    companion object{
        private const val TAG = "ZiadReceiver"
    }
    override fun doWork(): Result {
        val userId = inputData.getString(Constants.UUID_DATA_KEY).toString()
        Log.d(TAG, "doWork: $userId")
        val ref = FirebaseDatabase.getInstance().getReference("users")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val pref = applicationContext
                    .getSharedPreferences(Constants.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
                val value = snapshot.child(userId).child("username").value.toString()
                val value2 = snapshot.child(userId).child("phone").value.toString()
                val value3 = snapshot.child(userId).child("email").value.toString()
                val value4 = snapshot.child(userId).child("address").value.toString()
                with(pref.edit()) {
                    putString(Constants.USERNAME_PREF_KEY, value)
                    putString(Constants.EMAIL_PREF_KEY, value2)
                    putString(Constants.PHONE_PREF_KEY, value3)
                    putString(Constants.ADDRESS_PREF_KEY, value4)
                    commit()
                }

                Log.d(TAG,"Retrieve " + "${pref.getString(Constants.USERNAME_PREF_KEY, "default")}")
                Log.d(TAG,"Retrieve " + "${pref.getString(Constants.PHONE_PREF_KEY, "default")}")
                Log.d(TAG,"Retrieve " + "${pref.getString(Constants.EMAIL_PREF_KEY, "default")}")
                Log.d(TAG,"Retrieve " + "${pref.getString(Constants.ADDRESS_PREF_KEY, "default")}")
            }
        })
        return Result.success()
    }

}