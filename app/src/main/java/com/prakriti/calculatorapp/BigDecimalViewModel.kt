package com.prakriti.calculatorapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.math.BigDecimal

class BigDecimalViewModel : ViewModel() { // modified Double -> BigDecimal for financial calculations

    private val TAG = "BigDecimalViewModel"

    private var operand1: BigDecimal? = null
    private var pendingOperation: String = "="

    private val result = MutableLiveData<BigDecimal>()
    val stringResult: LiveData<String> get() = Transformations.map(result) { it.toString() }

    private val newNumber = MutableLiveData<String>()
    val stringNewNumber: LiveData<String> get() = newNumber

    private val operation = MutableLiveData<String>()
    val stringOperation: LiveData<String> get() = operation


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
            val value = newNumber.value?.toBigDecimal()
            if (value != null) {
                performOperation(value, op)
            }
        } catch (e: NumberFormatException) {
            newNumber.value = ""
        }
        pendingOperation = op
        operation.value = pendingOperation
    }


    fun negPressed() {
        val value = newNumber.value
        if (value == null || value.isEmpty()) {
            newNumber.value = "-"
        } else {
            try {
                var number = value.toBigDecimal()
                number *= BigDecimal.valueOf(-1)
                newNumber.value = number.toString()
            } catch (e: java.lang.NumberFormatException) {
                // newNum is - or .
                newNumber.value = ""
            }
        }
    }


    private fun performOperation(value: BigDecimal, op: String) {
        Log.i(TAG, "performOperation")
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = op
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == BigDecimal.valueOf(0.0)) {
                    BigDecimal.valueOf(Double.NaN)
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.value = operand1!!
        newNumber.value = ""
    }

}