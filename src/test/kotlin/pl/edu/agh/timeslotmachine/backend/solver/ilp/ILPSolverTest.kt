package pl.edu.agh.timeslotmachine.backend.solver.ilp

import org.junit.jupiter.api.Test
import org.springframework.data.geo.Point
import pl.edu.agh.timeslotmachine.backend.event.ActivityForm
import pl.edu.agh.timeslotmachine.backend.event.ConcreteEvent
import pl.edu.agh.timeslotmachine.backend.event.Event
import pl.edu.agh.timeslotmachine.backend.event.PointLimits
import pl.edu.agh.timeslotmachine.backend.group.Group
import pl.edu.agh.timeslotmachine.backend.instructor.Instructor
import pl.edu.agh.timeslotmachine.backend.place.Place
import pl.edu.agh.timeslotmachine.backend.preference.Preference
import pl.edu.agh.timeslotmachine.backend.term.Term
import pl.edu.agh.timeslotmachine.backend.user.Role
import pl.edu.agh.timeslotmachine.backend.user.User
import java.time.LocalDateTime

class ILPModelTest {

    private val user1: User = User(
        username = "user1",
        passwordHash = "password1",
        name = "John",
        surname = "Doe",
        email = "john.doe@example.com",
        indexNo = 1001,
        role = Role.User,
        id = 0,
    )
    private val user2: User = User(
        username = "user2",
        passwordHash = "password2",
        name = "Jane",
        surname = "Doe",
        email = "jane.doe@example.com",
        indexNo = 1002,
        role = Role.User,
        id = 1,
    )
    private val user3: User = User(
        username = "user3",
        passwordHash = "password3",
        name = "Jim",
        surname = "Beam",
        email = "jim.beam@example.com",
        indexNo = 1003,
        role = Role.User,
        id = 2,
    )
    private val user4: User = User(
        username = "user4",
        passwordHash = "password4",
        name = "Jack",
        surname = "Daniels",
        email = "jack.daniels@example.com",
        indexNo = 1004,
        role = Role.User,
        id = 3,
    )
    private val user5: User = User(
        username = "user5",
        passwordHash = "password5",
        name = "Jill",
        surname = "Valentine",
        email = "jill.valentine@example.com",
        indexNo = 1005,
        role = Role.User,
        id = 4,
    )

    val monHourStart1 = LocalDateTime.of(2024, 5, 27, 8, 0)
    val monHourEnd1 = LocalDateTime.of(2024, 5, 27, 9, 30)
    val monHourStart2 = LocalDateTime.of(2024, 5, 27, 9, 45)
    val monHourEnd2 = LocalDateTime.of(2024, 5, 27, 11, 15)
    val monHourStart3 = LocalDateTime.of(2024, 5, 27, 11, 30)
    val monHourEnd3 = LocalDateTime.of(2024, 5, 27, 13, 0)
    val monHourStart4 = LocalDateTime.of(2024, 5, 27, 13, 15)
    val monHourEnd4 = LocalDateTime.of(2024, 5, 27, 14, 45)
    val monHourStart5 = LocalDateTime.of(2024, 5, 27, 15, 0)
    val monHourEnd5 = LocalDateTime.of(2024, 5, 27, 16, 30)
    val monHourStart6 = LocalDateTime.of(2024, 5, 27, 16, 45)
    val monHourEnd6 = LocalDateTime.of(2024, 5, 27, 18, 15)
    val monHourStart7 = LocalDateTime.of(2024, 5, 27, 18, 30)
    val monHourEnd7 = LocalDateTime.of(2024, 5, 27, 20, 0)

    @Test
    fun smallScheduleTest() {
        val instructor = Instructor("Alice Smith", 0)
        val place = Place("Building A", "Room 101", Point(1.0, -1.0), "3.26", 0)
        val group = Group("Group-2024", "Computer Science", 10, 0)
        val preferenceList: MutableList<Preference> = mutableListOf()
        val pointsLimits = PointLimits(ActivityForm::class.java)
        pointsLimits[ActivityForm.Lecture] = Event.ActivityFormLimits(minPointsPerActivity = 5)
        pointsLimits[ActivityForm.Classes] = Event.ActivityFormLimits(minPointsPerActivity = 5)

        val pointsLimitsClassesOnly = PointLimits(ActivityForm::class.java)
        pointsLimitsClassesOnly[ActivityForm.Classes] = Event.ActivityFormLimits(minPointsPerActivity = 5)

        val pointsLimitsLectureOnly = PointLimits(ActivityForm::class.java)
        pointsLimitsLectureOnly[ActivityForm.Lecture] = Event.ActivityFormLimits(minPointsPerActivity = 5)

        val mathEvent = Event(
            id = 0,
            name = "Mathematics",
            shortName = "Math",
            description = "Advanced Mathematics Course",
            color = "blue",
            group = group,
            pointLimits = pointsLimitsLectureOnly
        )
        val mathLecture = ConcreteEvent(
            id = 1,
            event = mathEvent,
            userLimit = 5,
            activityForm = ActivityForm.Lecture,
            place = place
        )

        val termMathLecture = Term(
            start = monHourStart1,
            end = monHourEnd1,
            concreteEvent = mathLecture,
            instructor = instructor
        )
        mathLecture.terms.add(termMathLecture)

        val programmingCourse = Event(
            id = 2,
            name = "Programming 101",
            shortName = "Prog101",
            description = "Introduction to Programming",
            color = "green",
            group = group,
            pointLimits = pointsLimitsClassesOnly
        )

        val programmingClass1 = ConcreteEvent(
            id = 3,
            event = programmingCourse,
            userLimit = 5,
            activityForm = ActivityForm.Classes,
            place = place
        )

        val termProgrammingClass1 = Term(
            start = monHourStart4,
            end = monHourEnd4,
            concreteEvent = programmingClass1,
            instructor = instructor
        )
        programmingClass1.terms.add(termProgrammingClass1)

        val programmingClass2 = ConcreteEvent(
            id = 4,
            event = programmingCourse,
            userLimit = 5,
            place = place,
            activityForm = ActivityForm.Classes
        )

        val termProgrammingClass2 = Term(
            start = monHourStart5,
            end = monHourEnd5,
            concreteEvent = programmingClass2,
            instructor = instructor
        )
        programmingClass2.terms.add(termProgrammingClass2)

        val programmingClass3 = ConcreteEvent(
            id = 5,
            event = programmingCourse,
            userLimit = 5,
            place = place,
            activityForm = ActivityForm.Classes
        )

        val termProgrammingClass3 = Term(
            start = monHourStart6,
            end = monHourEnd6,
            concreteEvent = programmingClass3,
            instructor = instructor
        )
        programmingClass3.terms.add(termProgrammingClass3)




        val programmingClass4 = ConcreteEvent(
            id = 6,
            event = programmingCourse,
            userLimit = 5,
            place = place,
            activityForm = ActivityForm.Classes
        )

        val termProgrammingClass4 = Term(
            start = monHourStart7,
            end = monHourEnd7,
            concreteEvent = programmingClass4,
            instructor = instructor
        )
        programmingClass4.terms.add(termProgrammingClass4)


        val calculusCourse = Event(
            id = 6,
            name = "Calculus",
            shortName = "Calc",
            description = "Advanced Calculus Course",
            color = "red",
            group = group,
            pointLimits = pointsLimitsLectureOnly
        )

        val calculusLecture = ConcreteEvent(
            id = 7,
            event = calculusCourse,
            userLimit = 5,
            activityForm = ActivityForm.Lecture,
            place = place
        )

        val termCalculusLecture = Term(
            start = monHourStart5,
            end = monHourEnd5,
            concreteEvent = calculusLecture,
            instructor = instructor
        )
        calculusLecture.terms.add(termCalculusLecture)

        preferenceList.add(Preference(user1, mathLecture, 10, false))
        preferenceList.add(Preference(user2, mathLecture, 0, false))
//        preferenceList.add(Preference(user3, mathLecture, 0, false))
//        preferenceList.add(Preference(user4, mathLecture, 0, false))
//        preferenceList.add(Preference(user5, mathLecture, 0, false))

        preferenceList.add(Preference(user1, calculusLecture, 0, false))
        preferenceList.add(Preference(user2, calculusLecture, 0, false))
        preferenceList.add(Preference(user3, calculusLecture, 0, false))
        preferenceList.add(Preference(user4, calculusLecture, 0, false))
        preferenceList.add(Preference(user5, calculusLecture, 0, false))

        preferenceList.add(Preference(user1, programmingClass2, 10, false))
        preferenceList.add(Preference(user2, programmingClass2, 10, false))
        preferenceList.add(Preference(user3, programmingClass2, 10, false))
        preferenceList.add(Preference(user4, programmingClass2, 10, false))
        preferenceList.add(Preference(user5, programmingClass2, 5, false))

        preferenceList.add(Preference(user1, programmingClass3, 10, isAssigned = false, isImpossible = true))
        preferenceList.add(Preference(user2, programmingClass3, 0, false))
        preferenceList.add(Preference(user3, programmingClass3, 0, false))
        preferenceList.add(Preference(user4, programmingClass3, 0, false))
        preferenceList.add(Preference(user5, programmingClass3, 0, false))


        preferenceList.add(Preference(user1, programmingClass4, 0, false))
        preferenceList.add(Preference(user2, programmingClass4, 0, false))
        preferenceList.add(Preference(user3, programmingClass4, 0, false))
        preferenceList.add(Preference(user4, programmingClass4, 0, false))
        preferenceList.add(Preference(user5, programmingClass4, 0, false))

        val calcPref = ILPSolver().schedule(preferenceList)
        calcPref.forEach { println(it) }
    }

}
