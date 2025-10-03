package pl.edu.agh.timeslotmachine.backend.security

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class MD5PasswordEncoder : PasswordEncoder {
    @OptIn(ExperimentalStdlibApi::class)
    override fun encode(rawPassword: CharSequence?): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(rawPassword.toString().toByteArray())
        return digest.toHexString()
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        val currEncoded = encode(rawPassword)
        return currEncoded == encodedPassword
    }
}