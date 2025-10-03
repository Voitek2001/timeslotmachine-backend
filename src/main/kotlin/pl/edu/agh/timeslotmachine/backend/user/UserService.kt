package pl.edu.agh.timeslotmachine.backend.user

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.preference.Preference
import pl.edu.agh.timeslotmachine.backend.term.TimeSlot
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEventOverview
import pl.edu.agh.timeslotmachine.backend.term.toHourMinutePair
import pl.edu.agh.timeslotmachine.backend.auth.Credentials
import pl.edu.agh.timeslotmachine.backend.auth.NewUserInfo
import pl.edu.agh.timeslotmachine.backend.preference.PreferenceInfo
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEventService
import pl.edu.agh.timeslotmachine.backend.preference.PreferenceService
import pl.edu.agh.timeslotmachine.backend.security.AuthGrantedAuthorities
import pl.edu.agh.timeslotmachine.backend.util.algorithm.md5
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository,
    private val concreteEventService: ConcreteEventService,
    private val prefService: PreferenceService
) : UserDetailsService {
    fun findById(id: EID): User =
        userRepository.findById(id).orElseThrow { UserNotFoundException(id) }

    fun findByCredentials(credentials: Credentials) = credentials.run {
        userRepository.findByUsernameAndPasswordHash(username, password.md5()) ?: throw InvalidCredentialsException()
    }

    override fun loadUserByUsername(username: String) =
        userRepository.findByUsername(username).run {
            UserAuthDetails(
                authorities = mutableSetOf(AuthGrantedAuthorities(role)),
                password = passwordHash,
                username = username,
                isAccountNonExpired = true,
                isAccountNonLocked = true,
                isCredentialsNonExpired = true,
                isEnabled = true
            )
    }


    fun getByIndexNo(indexNo: IndexNo) =
        userRepository.findUserByIndexNo(indexNo).getOrNull() ?: throw UserNotFoundException(indexNo.toEID())

    @Transactional
    fun addEvents(userId: EID, events: List<EID>) =
        userRepository.addEvents(userId, events.toTypedArray())

    // TODO: verify if the events belong to the specified group
    @Transactional
    fun setEvents(userId: EID, groupId: EID, events: List<EID>) =
        userRepository.setGroupEvents(userId, groupId, events.toTypedArray())

    @Transactional
    fun putPreferences(user: User, preferenceInfo: List<PreferenceInfo>) {
        val lookupInfo = preferenceInfo.associateByTo(HashMap()) { it.concreteEventId }
        val concretes = concreteEventService.getByIds(preferenceInfo.map { it.concreteEventId })

        // TODO: check if the user is enrolled in the event
        // TODO: check point limits

        concretes.forEach { concrete ->
            val info = lookupInfo[concrete.id!!]!!

            concrete.preferences.find {
                it.id == Preference.Id(user.id!!, concrete.id!!)
            }?.run {
                points = info.points
                isImpossible = info.isImpossible ?: false
                impossibilityJustification = info.impossibilityJustification
            } ?: run {
                concrete.preferences += Preference(
                    id = Preference.Id(user.id!!, concrete.id!!),
                    points = info.points,
                    isAssigned = false,
                    isImpossible = info.isImpossible ?: false,
                    impossibilityJustification = info.impossibilityJustification
                ).also {
                    // TODO: investigate:
                    //  attempted to assign id from null one-to-one property [Preference.concreteEvent]
                    it.concreteEvent = concrete
                    it.user = user
                }
            }
        }

        concreteEventService.saveAll(concretes)
    }

    fun getConcreteEventsOverview(groupId: EID, userId: EID) = prefService.getAssigned(groupId, userId).map { pref ->
        val concreteEvent = pref.concreteEvent!!

        ConcreteEventOverview(
            concreteEvent = concreteEvent,
            event = concreteEvent.event,
            uniqueTerms = concreteEvent.terms.mapTo(HashSet()) {
                TimeSlot(it.start.toHourMinutePair(), it.end.toHourMinutePair())
            },
            uniqueInstructors = concreteEvent.terms.mapTo(HashSet()) {
                it.instructor
            }
        )
    }

    fun createWithDefaultRole(newUser: NewUserInfo) = userRepository.run {
        if (existsByUsernameOrEmail(newUser.username, newUser.email))
            throw UserAlreadyExistException()

        save(User(
            username = newUser.username, // TODO: check
            passwordHash = newUser.password.md5(),
            name = newUser.name,
            surname = newUser.surname,
            email = newUser.email, //TODO: check
            indexNo = newUser.indexNumber, //TODO: check
            role = Role.User)
        )
    }

    fun findByUsername(username: String) = userRepository.findByUsername(username)
}