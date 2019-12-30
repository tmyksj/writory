package writory.service.authentication

interface AuthenticationService {

    fun signUp(email: String, passwordRaw: String)

}
