import kotlinx.serialization.Serializable

@Serializable
sealed class ClientRoutes {
    //Auth
    @Serializable object SignUpScreen : ClientRoutes()
    @Serializable object SignInScreen : ClientRoutes()

    //Home
    @Serializable object HomeScreen : ClientRoutes()


}