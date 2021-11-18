package com.prakriti.calculatorapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

// no synthetic imports or references to the activity

class CalculatorViewModel : ViewModel() {

    private val TAG = "CalculatorViewModel"

    // vars to hold the operands and operations
    private var operand1: Double? = null // declare null as type, to check if user entered a value or not

    // nullable Double is not the same as non null Double, treated as diff types
//    private var operand2: Double = 0.0 -> local scope only
    private var pendingOperation: String = "="

    // Live Data handles unsubscribing any observers if they're no longer active
    private val result = MutableLiveData<Double>() // inside VM, this holds a double value
    val stringResult: LiveData<String>
        get() = Transformations.map(result) { it.toString() }
    // use Transformations class to convert types

    private val newNumber = MutableLiveData<String>()
    val stringNewNumber: LiveData<String> // viewmodel owner cannot use value/postValue attributes for LiveData<> object
        get() = newNumber

    private val operation = MutableLiveData<String>()
    val stringOperation: LiveData<String>
        get() = operation

    // ==================================================================

    fun digitPressed(caption: String) {
        if(newNumber.value != null) {
            newNumber.value = newNumber.value + caption
        } else {
            newNumber.value = caption
        }
    }


    fun operandPressed(op: String) {
        try { // catch NumFormEx here for single "." input
            val value = newNumber.value?.toDouble() // newNumber live data may be null
            if (value != null) {
                performOperation(value, op)
            }
        } catch (e: NumberFormatException) {
            newNumber.value = ""
        }
//            if(value.isNotEmpty()) { }
        pendingOperation = op
//        txtOperation.text = pendingOperation // textview is set to operation to be performed
        operation.value = pendingOperation
    }


    fun negPressed() {
        val value = newNumber.value
        if (value == null || value.isEmpty()) {
            newNumber.value = "-"
        } else {
            try {
                var number = value.toDouble()
                number *= -1
                newNumber.value = number.toString()
            } catch (e: java.lang.NumberFormatException) {
                // newNum is - or .
                newNumber.value = ""
            }
        }
    }


    private fun performOperation(value: Double, op: String) {
        Log.i(TAG, "performOperation")
        if (operand1 == null) {
            operand1 = value
        } else {
//           operand2 = value -> use value directly
            if (pendingOperation == "=") {
                pendingOperation = op
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN // undefined for divide by zero
                } else {
                    operand1!! / value // !! -> gives NullPtrEx if used on null object
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.value = operand1!! //.toString()
        newNumber.value = ""
        //edtResult.setText(operand1.toString()) // for internationalization, avoid converting numbers to strings
        //edtNewNumber.text.clear()

    }
}