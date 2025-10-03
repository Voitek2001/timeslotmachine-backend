package pl.edu.agh.timeslotmachine.backend.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import pl.edu.agh.timeslotmachine.backend.user.UserAuthDetails
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val SECRET_KEY: String,

    @Value("\${jwt.expiration}")
    private val EXPIRATION_TIME: Int,

    @Value("\${jwt.refresh.expiration}")
    val REFRESH_EXPIRATION_TIME: Int
) {

    fun extractUsername(token: String): String = extractAllClaims(token).subject

    fun extractExpiration(token: String): Date = extractAllClaims(token).expiration

    fun generateToken(userDetails: UserDetails): String =
        createToken(hashMapOf("roles" to userDetails.authorities), userDetails.username, EXPIRATION_TIME)


    fun validateToken(token: String) = !isTokenExpired(token)

    fun generateRefreshToken(userDetails: UserAuthDetails): String =
        createToken(mapOf(), userDetails.username, REFRESH_EXPIRATION_TIME)

    private fun isTokenExpired(token: String) = extractExpiration(token).before(Date())

    private fun extractAllClaims(token: String?): Claims = Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .payload


    private fun createToken(claims: Map<String, Any?>, subject: String, expirationTime: Int) = Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(key())
            .compact()


    private fun key(): SecretKey =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))


}