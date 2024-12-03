import kotlinx.serialization.Serializable

@Serializable
sealed class ClientRoutes {
    //Auth
    @Serializable
    object SignUpScreen : ClientRoutes()

    @Serializable
    object SignInScreen : ClientRoutes()

    //Home
    @Serializable
    object HomeScreen : ClientRoutes()

    @Serializable
    data class ProfileScreen(
        val hospitalName: String,
        val hospitalEmail: String,
        val hospitalPhone: String,
        val hospitalCity: String,
        val hospitalPinCode: String,
    ) : ClientRoutes()

    @Serializable
    object AppointmentScreen : ClientRoutes()

    @Serializable
    object DoctorScreen : ClientRoutes()

    @Serializable
    object DrugScreen : ClientRoutes()

    @Serializable
    object PrescriptionScreen : ClientRoutes()

    @Serializable
    object BedScreen : ClientRoutes()

    @Serializable
    object CheckUpScreen : ClientRoutes()

    @Serializable
    object CareScreen : ClientRoutes()

    @Serializable
    object EmergencyScreen : ClientRoutes()

}