package com.rith.muski.fragments

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rith.muski.Model.Order
import com.rith.muski.Model.User
import com.rith.muski.R
import com.rith.muski.databinding.FragmentMakeOrderBinding
import com.rith.muski.viewmodel.AddUserViewModel
import com.rith.muski.viewmodel.OrderViewmodel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class MakeOrderFramgent : Fragment() {
    private lateinit var makeOrderBinding: FragmentMakeOrderBinding
    private lateinit var orderViewModel: OrderViewmodel
    private lateinit var addUserViewModel: AddUserViewModel
    private lateinit var userList: List<User>
    private lateinit var selectedUser: User
    val fiveHundredMl = ObservableField<String>("")
    val oneL = ObservableField<String>("")
    val twentyL = ObservableField<String>("")
    val paidAmount = ObservableField<String>("")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        makeOrderBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_make_order, container, false)
        addUserViewModel = ViewModelProvider(this)[AddUserViewModel::class.java]
        orderViewModel = ViewModelProvider(this)[OrderViewmodel::class.java]
        addUserViewModel.getAllUser()
        listenUserList()
        makeOrderBinding.orderDateInput.setText(getCurrentTime())

        setupPriceCalculation()
        setListners()
        return makeOrderBinding.root

    }

    private fun setListners() {
        makeOrderBinding.submitButton.setOnClickListener {
            val total = makeOrderBinding.totalInput.text.toString().toDoubleOrNull() ?: 0.0
            val paid = makeOrderBinding.paidInput.text.toString().toDoubleOrNull() ?: 0.0
            val due = makeOrderBinding.dueInput.text.toString().toDoubleOrNull() ?: 0.0
            val five = makeOrderBinding.fiveHundredInput.text.toString().toIntOrNull() ?: 0
            val one = makeOrderBinding.oneLitreInput.text.toString().toIntOrNull() ?: 0
            val twenty = makeOrderBinding.twentyLitreInput.text.toString().toIntOrNull() ?: 0
            val date = makeOrderBinding.orderDateInput.text.toString()

            val order = Order(
                u_id = selectedUser.uId,
                o_amount = total,
                o_date = date,
                o_paid = paid,
                o_due_payment = due,
                o_five_ml = five,
                o_one_l = one,
                o_twenty_l = twenty, null
            )
            orderViewModel.insertOrder(order)
            makeOrderBinding.orderDateInput.setText(getCurrentTime())
            makeOrderBinding.fiveHundredInput.setText("")
            makeOrderBinding.oneLitreInput.setText("")
            makeOrderBinding.twentyLitreInput.setText("")
            makeOrderBinding.paidInput.setText("")
            makeOrderBinding.totalInput.setText("")
            makeOrderBinding.dueInput.setText("")
            // Now use the order object (e.g., save to DB)
            Log.d("OrderSubmit", order.toString())
        }


    }

    private fun setupPriceCalculation() {

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                calculatePrice()
            }
        }

        // Attach to all relevant inputs
        makeOrderBinding.fiveHundredInput.addTextChangedListener(textWatcher)
        makeOrderBinding.oneLitreInput.addTextChangedListener(textWatcher)
        makeOrderBinding.twentyLitreInput.addTextChangedListener(textWatcher)
        makeOrderBinding.paidInput.addTextChangedListener(textWatcher)
    }

    private fun calculatePrice() {
        val rates = mapOf(
            "five" to 10.0,
            "one" to 20.0,
            "twenty" to 30.0
        )
        val fiveQty = makeOrderBinding.fiveHundredInput.text.toString().toIntOrNull() ?: 0
        val oneQty = makeOrderBinding.oneLitreInput.text.toString().toIntOrNull() ?: 0
        val twentyQty = makeOrderBinding.twentyLitreInput.text.toString().toIntOrNull() ?: 0
        val pAmount = makeOrderBinding.paidInput.text.toString().toDoubleOrNull() ?: 0.0

        val total = (fiveQty * 10.0) + (oneQty * 20.0) + (twentyQty * 30.0)
        val due = total - pAmount

        makeOrderBinding.totalInput.setText(String.format("%.2f", total))
        makeOrderBinding.dueInput.setText(String.format("%.2f", if (due < 0) 0.0 else due))
    }


    private fun listenUserList() {
        addUserViewModel.userList.observe(viewLifecycleOwner) { users ->
            userList = users
            users.forEach { user ->
                Log.d(
                    "UserList",
                    "uId: ${user.uId}, name: ${user.name}, email: ${user.email}, phone: ${user.phone}, address: ${user.address}"
                )
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                users.map { it.name })
            makeOrderBinding.merchantInput.setAdapter(adapter)

            // Filter as user types
            makeOrderBinding.merchantInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val filteredNames = userList.filter {
                        it.name.contains(s.toString(), ignoreCase = true)
                    }.map { it.name }

                    val filteredAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        filteredNames
                    )
                    makeOrderBinding.merchantInput.setAdapter(filteredAdapter)
                    filteredAdapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            makeOrderBinding.merchantInput.setOnItemClickListener { _, _, position, _ ->
                val name = makeOrderBinding.merchantInput.adapter.getItem(position) as String
                selectedUser = userList.first { it.name == name }
                Log.d("MerchantSelect", "Selected user = $selectedUser")
            }
        }


    }

    fun getCurrentTime(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Kolkata"))
            val formattedDate = formatter.format(Instant.now())
            return formattedDate
        }
        return ""
    }
}