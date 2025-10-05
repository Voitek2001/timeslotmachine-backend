package pl.edu.agh.timeslotmachine.backend.solver.ilp

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.data.geo.Point
import pl.edu.agh.timeslotmachine.backend.core.EID
import pl.edu.agh.timeslotmachine.backend.event.*
import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.instructor.Instructor
import pl.edu.agh.timeslotmachine.backend.place.Place
import pl.edu.agh.timeslotmachine.backend.preference.Preference
import pl.edu.agh.timeslotmachine.backend.term.Term
import pl.edu.agh.timeslotmachine.backend.user.Role
import pl.edu.agh.timeslotmachine.backend.user.User
import java.time.LocalDateTime

class ILPModelTest {

    private val users = listOf(
        User(username = "user1", passwordHash = "p", name = "", surname = "", email = "", indexNo = 0, role = Role.User, id = 0),
        User(username = "user2", passwordHash = "p", name = "", surname = "", email = "", indexNo = 0, role = Role.User, id = 1),
        User(username = "user3", passwordHash = "p", name = "", surname = "", email = "", indexNo = 0, role = Role.User, id = 2),
        User(username = "user4", passwordHash = "p", name = "", surname = "", email = "", indexNo = 0, role = Role.User, id = 3),
        User(username = "user5", passwordHash = "p", name = "", surname = "", email = "", indexNo = 0, role = Role.User, id = 4),
    )

    private val slots = listOf(
        LocalDateTime.of(2024,5,27,8,0) to LocalDateTime.of(2024,5,27,9,30),
        LocalDateTime.of(2024,5,27,9,45) to LocalDateTime.of(2024,5,27,11,15),
        LocalDateTime.of(2024,5,27,11,30) to LocalDateTime.of(2024,5,27,13,0),
        LocalDateTime.of(2024,5,27,13,15) to LocalDateTime.of(2024,5,27,14,45),
        LocalDateTime.of(2024,5,27,15,0) to LocalDateTime.of(2024,5,27,16,30),
        LocalDateTime.of(2024,5,27,16,45) to LocalDateTime.of(2024,5,27,18,15),
        LocalDateTime.of(2024,5,27,18,30) to LocalDateTime.of(2024,5,27,20,0)
    )

    val instructor = Instructor("Alice Smith", 0)
    val place = Place("Building A", "Room 101", Point(1.0, -1.0), "3.26", 0)
    val group = Group("Group-2024", "Computer Science", 10, 0)
    // ---------- Helpers ----------
    private fun createEvent(id: EID, short: String, form: ActivityForm, limits: PointLimits): Event {
        return Event(
            id = id,
            name = short,
            shortName = short,
            description = "",
            color = "",
            group = group,
            pointLimits = limits
        )
    }

    private fun createConcreteEvent(id: EID, event: Event, userLimit:Int, form: ActivityForm, slot: Pair<LocalDateTime,LocalDateTime>): ConcreteEvent {
        val ce = ConcreteEvent(id = id, event = event, userLimit = userLimit, activityForm = form, place = place)
        ce.terms.add(Term(slot.first, slot.second, ce, instructor))
        return ce
    }

    private fun pref(user: User, ce: ConcreteEvent, value: Int, impossible: Boolean=false) =
        Preference(user, ce, value, false, isImpossible = impossible)

    @Test
    fun `should assign lecture and classes according to preferences`() {
        val lectureLimits = PointLimits(ActivityForm::class.java).apply {
            this[ActivityForm.Lecture] = Event.ActivityFormLimits(minPointsPerActivity = 5)
        }
        val classLimits = PointLimits(ActivityForm::class.java).apply {
            this[ActivityForm.Classes] = Event.ActivityFormLimits(minPointsPerActivity = 5)
        }

        val math = createEvent(0,"Math",ActivityForm.Lecture,lectureLimits)
        val prog = createEvent(1,"Prog101",ActivityForm.Classes,classLimits)

        val mathLecture = createConcreteEvent(10, math, 5,ActivityForm.Lecture, slots[0])
        val progClass1 = createConcreteEvent(20, prog, 5, ActivityForm.Classes, slots[4])
        val progClass2 = createConcreteEvent(21, prog, 5, ActivityForm.Classes, slots[5])

        val prefs = listOf(
            pref(users[0], mathLecture, 10),
            pref(users[1], mathLecture, 0),
            pref(users[0], progClass1, 10),
            pref(users[0], progClass2, 0),
            pref(users[1], progClass1, 10),
            pref(users[1], progClass2, 0)
        )

        val solution = ILPSolver().schedule(prefs)

        // Assertions
        val user0Assignments = solution.filter { it.user == users[0] && it.isAssigned }
        val user1Assignments = solution.filter { it.user == users[1] && it.isAssigned }
        // user0 strongly prefers mathLecture and progClass1
        assertTrue(user0Assignments.any { it.concreteEvent == mathLecture })
        assertTrue(user0Assignments.any { it.concreteEvent == progClass1 })
        assertTrue(user0Assignments.none { it.concreteEvent == progClass2 })

        // user1 prefers progClass1 and has no preference for mathLecture
        assertTrue(user1Assignments.any { it.concreteEvent == progClass1 })
        assertTrue(user1Assignments.none { it.concreteEvent == progClass2 })

    }

    @Test
    fun `should assign student to preferred lecture when enough capacity`() {
        val limits = PointLimits(ActivityForm::class.java).apply {
            this[ActivityForm.Lecture] = Event.ActivityFormLimits(minPointsPerActivity = 1)
        }
        val lectureEvent = createEvent(100, "Lecture1", ActivityForm.Lecture, limits)
        val lecture = createConcreteEvent(101, lectureEvent, 5,ActivityForm.Lecture, slots[0])

        val prefs = listOf(
            pref(users[0], lecture, 10),
            pref(users[1], lecture, 5)
        )

        val solution = ILPSolver().schedule(prefs)

        // Assertions
        assert(solution.any { it.user == users[0] && it.concreteEvent == lecture })
        assert(solution.any { it.user == users[1] && it.concreteEvent == lecture })
    }

    @Test
    fun `should not assign user to impossible preference`() {
        val limits = PointLimits(ActivityForm::class.java).apply {
            this[ActivityForm.Classes] = Event.ActivityFormLimits(minPointsPerActivity = 1)
        }
        val classEvent = createEvent(200, "Class1", ActivityForm.Classes, limits)
        val class1 = createConcreteEvent(201, classEvent, 5,ActivityForm.Classes, slots[1])

        val prefs = listOf(
            pref(users[0], class1, 10, impossible = true),
            pref(users[1], class1, 5)
        )

        val solution = ILPSolver().schedule(prefs)


        assert(solution.none { it.user == users[0] && it.concreteEvent == class1 })
        assert(solution.any { it.user == users[1] && it.concreteEvent == class1 })
    }

    @Test
    fun `should handle overlapping terms by assigning only one`() {
        val limits = PointLimits(ActivityForm::class.java).apply {
            this[ActivityForm.Classes] = Event.ActivityFormLimits(minPointsPerActivity = 1)
        }
        val event = createEvent(300, "Prog", ActivityForm.Classes, limits)

        val class1 = createConcreteEvent(301, event, 5, ActivityForm.Classes, slots[4]) // 15:00-16:30
        val class2 = createConcreteEvent(302, event, 5, ActivityForm.Classes, slots[4]) // overlapping same slot

        val prefs = listOf(
            pref(users[0], class1, 10),
            pref(users[0], class2, 10)
        )

        val solution = ILPSolver().schedule(prefs)

        // Assertions: user0 should not be in both overlapping classes
        val assigned = solution.filter { it.user == users[0] && it.isAssigned }
        assert(assigned.size == 1)
    }

    @Test
    fun `should distribute users among multiple groups when capacity reached`() {
        val limits = PointLimits(ActivityForm::class.java).apply {
            this[ActivityForm.Classes] = Event.ActivityFormLimits(minPointsPerActivity = 1)
        }
        val event = createEvent(400, "Lab", ActivityForm.Classes, limits)

        val lab1 = createConcreteEvent(401, event, 1, ActivityForm.Classes, slots[5])
        val lab2 = createConcreteEvent(402, event, 5, ActivityForm.Classes, slots[6])

        val prefs = users.map { pref(it, lab1, 10) } + users.map { pref(it, lab2, 0) }

        val solution = ILPSolver().schedule(prefs)

        // capacity of lab1 is 1, so some must be moved to lab2
        val lab1Assigned = solution.count { it.concreteEvent == lab1 && it.isAssigned }
        val lab2Assigned = solution.count { it.concreteEvent == lab2 && it.isAssigned }
        assert(lab1Assigned == 1)
        assert(lab2Assigned == users.size - 1)
        assert(lab1Assigned + lab2Assigned == users.size)
    }

    @Test
    fun `should not assign user to impossible preference bigger test`() {

        val lectureLimits = PointLimits(ActivityForm::class.java).apply {
            this[ActivityForm.Lecture] = Event.ActivityFormLimits(minPointsPerActivity = 5)
        }
        val classLimits = PointLimits(ActivityForm::class.java).apply {
            this[ActivityForm.Classes] = Event.ActivityFormLimits(minPointsPerActivity = 5)
        }

        val mathEvent = createEvent(0, "Math", ActivityForm.Lecture, lectureLimits)
        val mathLecture = createConcreteEvent(1, mathEvent, 5, ActivityForm.Lecture, slots[0])

        val programmingEvent = createEvent(2, "Prog101", ActivityForm.Classes, classLimits)
        val progClass1 = createConcreteEvent(3, programmingEvent, 5, ActivityForm.Classes, slots[3])
        val progClass2 = createConcreteEvent(4, programmingEvent, 5, ActivityForm.Classes, slots[4])
        val progClass3 = createConcreteEvent(5, programmingEvent, 5, ActivityForm.Classes, slots[5])
        val progClass4 = createConcreteEvent(6, programmingEvent, 5, ActivityForm.Classes, slots[6])

        val calculusEvent = createEvent(7, "Calc", ActivityForm.Lecture, lectureLimits)
        val calculusLecture = createConcreteEvent(8, calculusEvent, 5, ActivityForm.Lecture, slots[4])

        val prefs = mutableListOf<Preference>().apply {
            add(pref(users[0], mathLecture, 10))
            add(pref(users[1], mathLecture, 0))

            add(pref(users[0], calculusLecture, 0))
            add(pref(users[1], calculusLecture, 0))
            add(pref(users[2], calculusLecture, 0))
            add(pref(users[3], calculusLecture, 0))
            add(pref(users[4], calculusLecture, 0))

            add(pref(users[0], progClass2, 10))
            add(pref(users[1], progClass2, 10))
            add(pref(users[2], progClass2, 10))
            add(pref(users[3], progClass2, 10))
            add(pref(users[4], progClass2, 5))

            add(pref(users[0], progClass3, 10, impossible = false))
            add(pref(users[1], progClass3, 0))
            add(pref(users[2], progClass3, 0))
            add(pref(users[3], progClass3, 0))
            add(pref(users[4], progClass3, 0))

            add(pref(users[0], progClass4, 0))
            add(pref(users[1], progClass4, 0))
            add(pref(users[2], progClass4, 0))
            add(pref(users[3], progClass4, 0))
            add(pref(users[4], progClass4, 0))
        }

        val solution = ILPSolver().schedule(prefs)


        // User0 prefers mathLecture, should be assigned there
        assertTrue(solution.any { it.user == users[0] && it.concreteEvent == mathLecture && it.isAssigned })
        // User1 has 0 preference for mathLecture, should be forced there
        assertTrue(solution.any { it.user == users[1] && it.concreteEvent == mathLecture && it.isAssigned })
        // User0 had impossible preference for progClass3, must NOT be assigned to them
        assertTrue(solution.none { it.user == users[0] && it.concreteEvent == progClass3 && !it.isAssigned })

        // Ensure no user is assigned to overlapping Prog classes at once
        users.forEach { u -> val assignedProg = solution.filter { it.user == u && it.concreteEvent!!.event == programmingEvent && it.isAssigned }
            assertTrue(assignedProg.size <= 1, "User ${u.username} assigned to multiple Prog classes") }

    }



}
