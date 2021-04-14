package org.mavriksc.messin

fun main() {
    // filter for month group by and count.
    val customers = listOf(
        Customer("thing", "TX", 5),
        Customer("thing", "TX", 5),
        Customer("asdf", "MN", 5),
        Customer("dfghfgf", "MN", 5),
        Customer("nm,nm,nm", "HI", 5),
        Customer("thxcvxcvxcing", "TX", 5)
    )
    print(projectCouponPrints(customers, 5))

}

fun projectCouponPrints(customers: List<Customer>, month: Int): Map<String, Int> {
    return customers.filter { it.bDayMonth == month }
        .groupingBy { it.state }
        .eachCount()

}

class Customer(val name: String, var state: String, val bDayMonth: Int)
