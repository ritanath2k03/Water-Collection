package com.rith.muski.utils

object QueriesBuilder {
    fun insertUser(name: String, email: String, phone: String, address: String): String {
        return "INSERT INTO user(name, email, phone, address) VALUES('$name', '$email', '$phone', '$address');"
    }

    fun insertWater(wType: String, rate: Double, quantity: String): String {
        return "INSERT INTO water(w_type, w_rate, w_quantity) VALUES('$wType', $rate, '$quantity');"
    }

    fun insertOrder(

        uId: Long?,
        amount: Double,
        paid: Double,
        due: Double,
        date: String,
        fiveMl: Int,
        oneL: Int,
        twentyL: Int
    ): String {
        return """
        INSERT INTO orders(
             u_id, o_amount, o_paid, o_due_payment, o_date,
            o_five_ml, o_one_l, o_twenty_l
        ) VALUES(
            $uId, $amount, $paid, $due, '$date',
            $fiveMl, $oneL, $twentyL
        );
    """.trimIndent()
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

    fun getFiveHundredMlOrderByWaterId(wId: Int): String {
    return """
            SELECT o.*, u.* FROM orders o
            JOIN user u ON o.u_id = u.u_id
            WHERE o_five_ml > 0;
        """
    }
fun getOrdersForOrderList():String {
    return """
            SELECT o.*, u.* FROM orders o
            JOIN user u ON o.u_id = u.u_id;
            
        """
}
    fun getOneLOrderByWaterId(wId: Int):String {
        return """
            SELECT o.*, u.* FROM orders o
            JOIN user u ON o.u_id = u.u_id
            WHERE o_one_l > 0;
        """
    }
    fun getTwentyLOrderByWaterId(wId: Int):String {
        return """
            SELECT o.*, u.* FROM orders o
            JOIN user u ON o.u_id = u.u_id
            WHERE o_twenty_l > 0;
        """
    }
    fun getOrderDataForPieChartInDashboard():String{return """SELECT 
    SUM(o_paid) AS paid_amount,
    SUM(o_due_payment) AS due_amount,
    SUM(o_five_ml * 10.0) AS fiveH,
    SUM(o_one_l * 20.0) AS oneL,
    SUM(o_twenty_l * 30.0) AS twentyL
FROM orders;"""}
}