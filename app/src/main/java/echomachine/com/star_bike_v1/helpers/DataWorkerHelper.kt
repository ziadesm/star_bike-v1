package echomachine.com.star_bike_v1.helpers

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import echomachine.com.star_bike_v1.Constants
import echomachine.com.star_bike_v1.worker.RetrieveRegisterDataWorker
import echomachine.com.star_bike_v1.worker.SaveRegisterDataWorker

class DataWorkerHelper {
    companion object {
        private const val TAG = "ZiadReceiver"
        fun sendDataToWorker(name: String, email: String, phone:String, address: String, context: Context) {
            val work = OneTimeWorkRequest.Builder(SaveRegisterDataWorker::class.java)
                .setInputData(createDataForWorker(name, email, phone, address))
                .build()

            WorkManager.getInstance(context).enqueue(work)
        }

        private fun createDataForWorker(name: String, email: String
                                        , phone:String, address: String): Data {
            val data = Data.Builder()
            data.putString(Constants.USERNAME_DATA_KEY, name)
            data.putString(Constants.EMAIL_DATA_KEY, email)
            data.putString(Constants.PHONE_DATA_KEY, phone)
            data.putString(Constants.ADDRESS_DATA_KEY, address)
            return data.build()
        }

        fun getDataByWorker(userId: String, context: Context) {
            val data = Data.Builder()
            data.putString(Constants.UUID_DATA_KEY, userId)
            Log.d(TAG, "getDataByWorker: $userId")

            val request = OneTimeWorkRequest
                .Builder(RetrieveRegisterDataWorker::class.java)
                .setInputData(data.build())
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }
    }
}