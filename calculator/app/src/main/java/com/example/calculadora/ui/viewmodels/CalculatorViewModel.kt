import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    // Mutable states for managing the calculator's data
    var display = mutableStateOf("0")
        private set
    private var oldValue: String = "0"
    private var operator: String = ""
    private var isNewValue: Boolean = false

    // Function to handle number button clicks
    fun onNumberClick(number: String) {
        if (isNewValue || display.value == "0") {
            display.value = number
            isNewValue = false
        } else {
            display.value += number
        }
    }

    // Function to handle operator clicks (+, -, *, /)
    fun onOperatorClick(op: String) {
        operator = op
        oldValue = display.value
        isNewValue = true
    }

    // Function to handle equals button (=)
    fun onEqualClick() {
        val result = performCalculation(operator, oldValue, display.value)
        display.value = result
        oldValue = result
        isNewValue = true
    }

    // Function to handle clear/reset (C)
    fun onClearClick() {
        display.value = "0"
        oldValue = "0"
        operator = ""
        isNewValue = false
    }

    // Perform calculation based on operator
    private fun performCalculation(op: String, oldValue: String, newValue: String): String {
        return try {
            val oldNum = oldValue.toDouble()
            val newNum = newValue.toDouble()
            when (op) {
                "+" -> (oldNum + newNum).toString()
                "-" -> (oldNum - newNum).toString()
                "x" -> (oldNum * newNum).toString()
                "/" -> (oldNum / newNum).toString()
                else -> "0"
            }
        } catch (e: Exception) {
            "ERROR"
        }
    }
}
