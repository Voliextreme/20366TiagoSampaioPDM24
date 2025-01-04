package com.example.lojapdm.domain.model

class Cart {
    val users = mutableListOf<User>()
    val cars = mutableListOf<Car>()
    val totalValue: Double
        get() {
            var total = 0.0
            cars.forEach {
                total += it.price
            }
            return total
        }
}