import java.math.BigInteger

fun main() {

    while(true) {
        print("\nEnter two numbers in format: {source base} {target base} (To quit type /exit) ")
        var sourceBase: String = ""
        var targetBase: String = ""
        var number = ""
        var input: String = readLine()!!
        var commandInput = MutableList(2) {""}

        if (input == "/exit") {
            break;
        } else {
            commandInput = input.split(" ").map { it }.toMutableList()
            sourceBase = commandInput[0]
            targetBase = commandInput[1]
        }

        while(true) {
            print("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
            number = readLine()!!

            if (number == "/back") {
                break;
            }

            println("Conversion result: ${converterFromSourceToTarget(sourceBase.toBigInteger(), targetBase.toBigInteger(), number)}")
        }
    }
}

fun fromDecimalTo(targetBase: BigInteger, numberPassed: String):String {
    var number = numberPassed.toBigInteger()
    var result = ""

    when (targetBase.toInt()) {
        10 -> {
            return numberPassed
        }
        in 11..36 -> {
            while (number != BigInteger.ZERO) {
                result += if (number % targetBase >= BigInteger.valueOf(10) && number % targetBase <= BigInteger.valueOf(35)) {
                    ((number % targetBase) + BigInteger.valueOf(87)).toInt().toChar()
                } else  {
                    "${number % targetBase}"
                }
                number /= targetBase
            }
        }
        in 2..9 -> {
            while (number != BigInteger.ZERO) {
                result = result + "${number % targetBase}"
                number /= targetBase
            }
        }
    }
    return result.reversed()
}

fun converterFromSourceToTarget(sourceBase: BigInteger, targetBase: BigInteger, number: String): String {

    when (targetBase.toInt()) {
        in 2..9 -> {
            return fromDecimalTo(targetBase, toDecimal(sourceBase, number))
        }
        10 -> {
            return toDecimal(sourceBase, number)
        }
        in 11..36 -> {
            return fromDecimalTo(targetBase, toDecimal(sourceBase, number))
        }
        else -> return ""
    }
}

fun toDecimal(sourceBase: BigInteger, number: String): String {
    var stringNumber = number.reversed()
    var bigInteger: BigInteger = BigInteger.ZERO

    if (sourceBase.toInt() == 10) {
        return number
    }
    if (sourceBase.toInt() in 2..9) {
        for (i in stringNumber.indices) {
            bigInteger += sourceBase.pow(i) * BigInteger(stringNumber[i].toString())
        }
    } else if (sourceBase.toInt() in 11..36) {
        var convertedNumber: BigInteger = BigInteger.ZERO
        for (i in stringNumber.indices) {
            convertedNumber = if(stringNumber[i].isDigit()) {
                BigInteger(stringNumber[i].toString())
            } else {
                (stringNumber[i].code - 87).toBigInteger()
            }
            bigInteger += sourceBase.pow(i) * convertedNumber
        }
    }
    return bigInteger.toString()
}