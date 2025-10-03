package pl.edu.agh.timeslotmachine.backend.instructor

import org.springframework.stereotype.Service
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.util.algorithm.validateNotNull
import kotlin.jvm.optionals.getOrNull

@Service
class InstructorService(
    private val instructorRepository: InstructorRepository
) {
    fun getById(id: EID) =
        instructorRepository.findById(id).getOrNull() ?: throw InstructorNotFoundException(id)

    fun getByFullName(fullName: String) =
        instructorRepository.findByFullName(fullName)

    fun getAllInstructors(): List<Instructor> =
        instructorRepository.findAll()

    fun getByFullNameOrCreate(instructorInfo: InstructorInfo) = instructorInfo.apply {
        validateNotNull(::fullName)
    }.let {
        getByFullName(it.fullName!!) ?: createInstructor(it)
    }

    fun createInstructor(instructorInfo: InstructorInfo) = instructorInfo.apply {
        validateNotNull(::fullName)
    }.run {
        Instructor(
            fullName = fullName!!,
        ).also { instructorRepository.save(it) }
    }

    fun deleteById(id: EID) =
        instructorRepository.deleteById(id)

    fun updateInstructorById(instructorInfo: InstructorInfo) = instructorInfo.apply {
        validateNotNull(::fullName, ::id)
    }.let {
        getById(instructorInfo.id!!).run {
            fullName = instructorInfo.fullName!!
            instructorRepository.save(this)
        }
    }
}