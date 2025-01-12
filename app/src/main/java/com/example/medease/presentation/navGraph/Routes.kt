import com.example.medease.domain.model.AppointmentDetails
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    //Auth
    @Serializable object SignUpScreen : Routes()
    @Serializable object SignInScreen : Routes()

    //Home
    @Serializable object HomeScreen : Routes()

    //User Options
    @Serializable data class MyAppointmentsScreen(val userId: String) : Routes()
    @Serializable data class HealthRecordsScreen(val userId: String) : Routes()
    @Serializable data class TransactionsScreen(val userId: String) : Routes()
    @Serializable data class ProfileScreen(val userId: String) : Routes()
    @Serializable data class BookingSuccessScreen(val appointmentId: String, val transactionId: String, val userId: String) : Routes()
    @Serializable data class PaymentScreen(val appointmentDetails: AppointmentDetails) : Routes()
}