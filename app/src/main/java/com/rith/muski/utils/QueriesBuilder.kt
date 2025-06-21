package com.rith.muski.utils

object QueriesBuilder {
    fun insertUser(name: String, email: String, phone: String, address: String): String {
        return "INSERT INTO user(name, email, phone, address) VALUES('$name', '$email', '$phone', '$address');"
    }

    fun insertWater(wType: String, rate: Double, quantity: String): String {
        return "INSERT INTO water(w_type, w_rate, w_quantity) VALUES('$wType', $rate, '$quantity');"
    }

    fun insertOrder(oId: Int, uId: Int, total: Double, payment: String, date: String): String {
        return "INSERT INTO orders(o_id, u_id, o_total, o_payment, o_date) VALUES($oId, $uId, $total, '$payment', '$date');"
    }

    fun insertOrderItem(oId: Int, wId: Int): String {
        return "INSERT INTO order_items(o_id, w_id) VALUES($oId, $wId);"
    }

    fun insertPayment(date: String, oId: Int, mode: String, paid: Double, due: Double): String {
        return "INSERT INTO payment(p_date, o_id, p_mode, p_paid_amount, p_due_amount) VALUES('$date', $oId, '$mode', $paid, $due);"
    }

    fun insertCost(wId: Int, amount: Double): String {
        return "INSERT INTO cost(w_id, c_amount) VALUES($wId, $amount);"
    }

    fun getAllUsers(): String {
    return  "SELECT * FROM user;"
    }

}