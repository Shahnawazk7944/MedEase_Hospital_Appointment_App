import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    //Auth
    @Serializable object SignUpScreen : Routes()
    @Serializable object SignInScreen : Routes()

    //Home
    @Serializable object HomeScreen : Routes()


}