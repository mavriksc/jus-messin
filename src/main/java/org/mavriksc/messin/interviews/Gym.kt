package org.mavriksc.messin.interviews

import java.time.Instant
import java.util.*


fun main() {
    val memberships = setupTestdata()
    memberships.getMembers().map {
        val avgWorkoutTime = if (it.workouts.isNotEmpty()) it.workouts.map { wo -> wo.endTime.time - wo.startTime.time }
            .average().toInt() / 1000 else null
        Pair(it.id, avgWorkoutTime)
    }.sortedBy { it.first }.forEach {
        println("memberID:${it.first}\tavg WO time:${it.second ?: "NULL"}")
    }
}


data class Member(
    val fName: String,
    val lName: String,
    val age: Int,
    val id: Int,
    val membershipType: MembershipType = MembershipType.BRONZE
) {
    private val _workouts: MutableList<Workout> = mutableListOf()
    val workouts: List<Workout>
        get() = _workouts.toList()

    override fun toString(): String {
        return "Member(fName='$fName', lName='$lName', age=$age, id=$id, membershipType=$membershipType)"
    }

    fun addWorkout(workout: Workout) {
        _workouts.add(workout)
    }
}

data class Workout(val id: Int, val date: Date, val startTime: Date, val endTime: Date)

class Memberships {
    private val _members: MutableList<Member> = mutableListOf()

    fun addMember(member: Member) {
        _members.add(member)
    }

    fun getMembers(): List<Member> {
        return _members.toList()
    }
}

enum class MembershipType {
    BRONZE, SILVER, GOLD
}


fun setupTestdata(): Memberships {
    val memberships = Memberships()
    val member1 = Member("John", "Doe", 30, 1)
    val member2 = Member("Jane", "Smith", 25, 2, MembershipType.GOLD)
    val member3 = Member("Bob", "Johnson", 35, 3, MembershipType.SILVER)
    val member4 = Member("Alice", "Brown", 40, 4, MembershipType.BRONZE)

    // Create test workout data for member1
    val workout1 = Workout(
        1, Date.from(Instant.parse("2026-06-01T10:00:00Z")),
        Date.from(Instant.parse("2026-06-01T10:35:00Z")),
        Date.from(Instant.parse("2026-06-01T11:30:00Z"))
    )
    val workout2 = Workout(
        2, Date.from(Instant.parse("2026-06-03T14:00:00Z")),
        Date.from(Instant.parse("2026-06-03T14:12:00Z")),
        Date.from(Instant.parse("2026-06-03T15:38:00Z"))
    )

    // Create test workout data for member2
    val workout3 = Workout(
        3, Date.from(Instant.parse("2026-06-02T09:00:00Z")),
        Date.from(Instant.parse("2026-06-02T09:06:00Z")),
        Date.from(Instant.parse("2026-06-02T11:50:00Z"))
    )
    val workout4 = Workout(
        4, Date.from(Instant.parse("2026-06-04T16:00:00Z")),
        Date.from(Instant.parse("2026-06-04T16:00:00Z")),
        Date.from(Instant.parse("2026-06-04T17:34:00Z"))
    )
    val workout5 = Workout(
        3, Date.from(Instant.parse("2026-06-02T09:00:00Z")),
        Date.from(Instant.parse("2026-06-02T09:00:00Z")),
        Date.from(Instant.parse("2026-06-02T10:23:00Z"))
    )
    val workout6 = Workout(
        4, Date.from(Instant.parse("2026-06-04T16:00:00Z")),
        Date.from(Instant.parse("2026-06-04T16:09:00Z")),
        Date.from(Instant.parse("2026-06-04T17:30:00Z"))
    )



    member1.addWorkout(workout1)
    member1.addWorkout(workout2)
    member2.addWorkout(workout3)
    member2.addWorkout(workout4)
    member3.addWorkout(workout5)
    member3.addWorkout(workout6)

    memberships.addMember(member3)
    memberships.addMember(member4)
    memberships.addMember(member1)
    memberships.addMember(member2)
    return memberships
}
