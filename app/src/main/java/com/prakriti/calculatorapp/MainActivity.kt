package com.prakriti.calculatorapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

//private const val STATE_PENDING_OP = "Pending_Operation"
//private const val STATE_OPERAND1 = "Operand1"
//private const val STATE_OPERAND1_STORED = "Operand1_Stored"

private const val TAG = "MainActivity"

// modified to use ViewModel
class MainActivity : AppCompatActivity() {

//    private lateinit var result: EditText // by def, kotlin properties cant be null -> protection from null pointer exceptions
    // lateinit -> non null var, will be initialised later. only for var, not val
//    private lateinit var newNumber: EditText

//    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.txtOperation) } // can be used for val, for access after onCreate is called
    // lazy -> define a function, called to assign a value to a property. only for vals, not vars
    // once called, value is cached and function is not called again
    // is also thread-safe; (...NONE) mode disables thread-safety in cases where only single thread will access the function


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // using extensions

        val viewModel: CalculatorViewModel by viewModels()
        // Use the 'by viewModels()' Kotlin property delegate from the activity-ktx artifact
//        val viewModel: BigDecimalViewModel by viewModels()

        // update UI
        viewModel.stringResult.observe(this, Observer { stringResult ->
            edtResult.setText(stringResult)
        })
        viewModel.stringNewNumber.observe(this, Observer { stringNewNumber ->
            edtNewNumber.setText(stringNewNumber)
        })
        viewModel.stringOperation.observe(this, Observer { stringOperation ->
            txtOperation.text = stringOperation
        })


/*
        result = findViewById(R.id.edtResult)
        newNumber = findViewById(R.id.edtNewNumber)

        // data input buttons
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.buttonDot)

        // operation buttons
        val buttonEquals: Button = findViewById(R.id.buttonEqual)
        val buttonAdd: Button = findViewById(R.id.buttonAdd)
        val buttonSubtract: Button = findViewById(R.id.buttonSubtract)
        val buttonMultiply: Button = findViewById(R.id.buttonMultiply)
        val buttonDivide: Button = findViewById(R.id.buttonDivide)
*/
        val listener =
            View.OnClickListener { v -> // ref to listener instance, for all data input buttons
//                val b = v as Button // casting in kotlin
//                edtNewNumber.append(b.text) // use the exact same ID as given in XML file
                // calling from viewmodel
                viewModel.digitPressed((v as Button).text.toString())
            }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
//            val op = (v as Button).text.toString()
            viewModel.operandPressed((v as Button).text.toString())

        }

        buttonEqual.setOnClickListener(opListener)
        buttonAdd.setOnClickListener(opListener)
        buttonSubtract.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)

        val negListener = View.OnClickListener { v ->
            viewModel.negPressed()
        }

        buttonNegative.setOnClickListener(negListener)

    }


    /*
    override fun onSaveInstanceState(outState: Bundle) { // only called if bundle is not null
        super.onSaveInstanceState(outState)
        if(operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OP, pendingOperation) // kotlin doesn't allow method calls on nullable types, put !! to assert not-null
        // !! will give Exception if obj is null
        // or use ? safe call operator on nullable types -> wont make call if obj is null
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) { // only called if Bundle is not null, by def
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if(savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) { // if true, put the value to op1
            savedInstanceState.getDouble(STATE_OPERAND1) // def val for getDouble() is 0.0, but we want null
        } else {
            null
        }

        pendingOperation = savedInstanceState.getString(STATE_PENDING_OP).toString()
        txtOperation.text = pendingOperation
    }
    */

}