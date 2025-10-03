package pl.edu.agh.timeslotmachine.backend.instructor

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.timeslotmachine.backend.core.EID

@Repository
interface InstructorRepository : JpaRepository<Instructor, EID> {
    fun findByFullName(fullName: String): Instructor?
}