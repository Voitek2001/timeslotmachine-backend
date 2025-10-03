package pl.edu.agh.timeslotmachine.backend.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.timeslotmachine.backend.core.EID
import java.util.UUID

@Repository
interface AuthTokenRepository : JpaRepository<AuthToken, EID> {
    fun findByToken(token: UUID): AuthToken?
}