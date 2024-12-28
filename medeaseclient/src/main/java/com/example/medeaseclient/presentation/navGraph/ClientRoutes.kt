import com.example.medeaseclient.domain.model.Bed
import com.example.medeaseclient.domain.model.Doctor
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
    data class DoctorScreen(val hospitalId: String) : ClientRoutes()

    @Serializable
    data class AddDoctorScreen(val doctor: Doctor, val hospitalId: String) : ClientRoutes()

    @Serializable
    data class BedScreen(val hospitalId: String) : ClientRoutes()

    @Serializable
    data class UpdateBedScreen(val hospitalId: String, val bed: Bed) : ClientRoutes()
}