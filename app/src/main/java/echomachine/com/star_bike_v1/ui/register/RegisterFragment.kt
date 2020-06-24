package echomachine.com.star_bike_v1.ui.register
import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import echomachine.com.star_bike_v1.Constants
import echomachine.com.star_bike_v1.R
import echomachine.com.star_bike_v1.helpers.GpsUtils
import echomachine.com.star_bike_v1.helpers.NavigationsHelper
import echomachine.com.star_bike_v1.helpers.NetworkCheckHelper
import echomachine.com.star_bike_v1.interfaces.GetLocationFromReceiverListener
import echomachine.com.star_bike_v1.receiver.AddressRequestReceiver
import echomachine.com.star_bike_v1.service.FetchLocationAddressService
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlin.properties.Delegates

class RegisterFragment : Fragment(), GetLocationFromReceiverListener{

    private val LOCATION_ACCESS_PERMISSION_REQUEST = 34
    private val TAG = "ZiadReceiver"
    private lateinit var auth: FirebaseAuth
    private lateinit var locationTv: TextView
    private lateinit var receiver: AddressRequestReceiver
    var isGPS by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        receiver = AddressRequestReceiver(Handler())
        receiver.setDataLocation(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        isCurrentLocationPermissionGranted()
        view.register_layout_btn_register.setOnClickListener { registerTheUser() }
        locationTv = view.findViewById(R.id.register_layout_et_location)
        return view
    }

    private fun registerTheUser() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.error_dialog_design)
        dialog.setCancelable(false)
        if (locationTv.text.isEmpty()) {
            getCurrentLocation()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        if (context?.let { !NetworkCheckHelper.isOnline(it) }!!) {
            dialog.show()
            return
        }

        val name = view?.register_layout_et_name?.text.toString()
        val email = view?.register_layout_et_email?.text.toString()
        val password = view?.register_layout_et_password?.text.toString()
        val phone = view?.register_layout_et_phone_number?.text.toString()
        val location = view?.register_layout_et_location?.text.toString()

        if (TextUtils.isEmpty(name)) {
            view?.register_layout_et_name?.error = "Please we need your name"
            view?.register_layout_et_name?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(email)) {
            view?.register_layout_et_email?.error = "Please fill right email address"
            view?.register_layout_et_email?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(location)) {
            view?.register_layout_et_location?.error = "Please fill right location address"
            GpsUtils(requireContext()).turnGPSOn(object : GpsUtils.onGpsListener {
                override fun gpsStatus(isGPSEnable: Boolean) {
                    // turn on GPS
                    isGPS = isGPSEnable
                }
            })
            return
        } else {
            view?.register_layout_et_location?.error = null
        }
        if (TextUtils.isEmpty(phone)) {
            view?.register_layout_et_phone_number?.error = "Please enter your phone to reach you"
            view?.register_layout_et_phone_number?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(password) || password.length < 6) {
            view?.register_layout_et_password?.error = "Empty or less than 6 characters"
            view?.register_layout_et_password?.requestFocus()
            return
        }
        dialog.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                Handler().postDelayed({
                    if (task.isSuccessful) {
                        dialog.dismiss()
                        NavigationsHelper.navigateToThisDestination(
                            R.id.login_fragment,
                            requireActivity()
                        )
                    } else {
                        view?.register_layout_et_email?.error = "we have this email or wrong one"
                        dialog.dismiss()
                    }
                }, 1500)
            }
    }

    private fun isCurrentLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity()
                    , arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    , LOCATION_ACCESS_PERMISSION_REQUEST)
            } else {
                getCurrentLocation()
            }
        }
    }

    private fun getCurrentLocation() {
        val locationRequest: LocationRequest =
            LocationRequest().setInterval(1500)
            .setFastestInterval(1000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity()
                , arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                , LOCATION_ACCESS_PERMISSION_REQUEST)
            GpsUtils(requireContext()).turnGPSOn(object : GpsUtils.onGpsListener {
                override fun gpsStatus(isGPSEnable: Boolean) {
                    // turn on GPS
                    isGPS = isGPSEnable
                }
            })
        } else {
            LocationServices.getFusedLocationProviderClient(requireActivity())
                .requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult?) {
                        super.onLocationResult(p0)
                        LocationServices.getFusedLocationProviderClient(requireActivity())
                            .removeLocationUpdates(this)
                        if (p0 != null && p0.locations.size > 0) {
                            var latestLocationIndex: Int = p0.locations.size - 1
                            var latitude: Double = p0.locations[latestLocationIndex].latitude
                            var longitude: Double = p0.locations[latestLocationIndex].longitude
                            Log.d(TAG, "Longitude:  $longitude + Latitude:  $latitude")
                            var location = Location("providerNA")
                            location.latitude = latitude
                            location.longitude = longitude
                            fetchAddressFromLongLatit(location)
                        }
                    }
                }, Looper.getMainLooper())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.GPS_REQUEST) {
            isGPS = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_ACCESS_PERMISSION_REQUEST && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Searching for location -> NOT FOUND", LENGTH_LONG).show()
            }
        }
    }

    private fun fetchAddressFromLongLatit(location: Location) {
        var intent = Intent(requireContext(), FetchLocationAddressService::class.java)
        intent.putExtra(Constants.RECEIVER_KEY, receiver)
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location)
        requireContext().startService(intent)
    }

    override fun setLocation(string: String) {
        locationTv.text = string
    }
}