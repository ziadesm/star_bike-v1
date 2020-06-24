package echomachine.com.star_bike_v1.receiver

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import echomachine.com.star_bike_v1.Constants
import echomachine.com.star_bike_v1.interfaces.GetLocationFromReceiverListener

class AddressRequestReceiver(handler: Handler?) :
    ResultReceiver(handler) {
    private var listener: GetLocationFromReceiverListener? = null
    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        super.onReceiveResult(resultCode, resultData)
        if (resultCode == Constants.RESULT_SUCCESS) {
            val stick =
                resultData.getString(Constants.RESULT_LOCATION_DATA_KEY)
            listener!!.setLocation(stick!!)
            Log.d(TAG, "" + stick)
        } else {
            Log.d(TAG, "onReceiveResult: Fail")
        }
    }

    fun setDataLocation(listener: GetLocationFromReceiverListener?) {
        this.listener = listener
    }

    companion object {
        private const val TAG = "AddressRequests"
    }
}