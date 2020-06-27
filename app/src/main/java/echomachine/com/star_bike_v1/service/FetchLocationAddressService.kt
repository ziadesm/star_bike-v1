package echomachine.com.star_bike_v1.service
import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import echomachine.com.star_bike_v1.Constants
import java.lang.Exception
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FetchLocationAddressService:
    IntentService("Fetch Location Address") {
    private lateinit var resultReceiver: ResultReceiver
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER_KEY)
            var location: Location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA)
                ?: return
            var geocoder = Geocoder(this, Locale.getDefault())
            var addressList: List<Address>? = null
            try {
                addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            } catch (exception: Exception) {
                Toast.makeText(this, "" + exception.message, LENGTH_SHORT).show()
            }
            if (addressList == null || addressList.isEmpty()) {
                deliverResultToReceiver(Constants.RESULT_FAILURE, "fail")
            } else {
                var address: String = addressList[0].getAddressLine(0)
                deliverResultToReceiver(Constants.RESULT_SUCCESS, address)
            }
        }
    }

    private fun deliverResultToReceiver(resultCode: Int, addressMessage: String) {
        var bundle = Bundle()
        bundle.putString(Constants.RESULT_LOCATION_DATA_KEY, addressMessage)
        resultReceiver.send(resultCode, bundle)
    }
}