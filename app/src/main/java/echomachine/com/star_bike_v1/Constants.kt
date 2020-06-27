package echomachine.com.star_bike_v1

class Constants{
    companion object{
        // Location Constants
        const val GPS_REQUEST: Int = 3
        private const val PACKAGE_NAME: String = "echomachine.com.star_bike_v1"
        const val RESULT_LOCATION_DATA_KEY: String = "$PACKAGE_NAME.RESULT_LOCATION"
        const val LOCATION_DATA_EXTRA: String = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
        const val RECEIVER_KEY: String = "$PACKAGE_NAME.RECEIVER"
        const val RESULT_SUCCESS: Int = 1
        const val RESULT_FAILURE: Int = 2

        // Saving Registration Data
        const val USERNAME_DATA_KEY: String = "username-key"
        const val EMAIL_DATA_KEY: String = "email-key"
        const val PHONE_DATA_KEY: String = "phone-key"
        const val ADDRESS_DATA_KEY: String = "address-key"

        const val UUID_DATA_KEY: String = "user-random-id"

        // Saving Registration data in shared preferences
        const val SHARED_PREFERENCE_KEY: String = "pref-shared"
        const val USERNAME_PREF_KEY: String = "username-pref"
        const val EMAIL_PREF_KEY: String = "email-pref"
        const val PHONE_PREF_KEY: String = "phone-pref"
        const val ADDRESS_PREF_KEY: String = "address-pref"
    }
}