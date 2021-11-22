import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

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
            //check if it contains decimal point

            if (number == "/back") {
                break;
            }

            println("Conversion result: ${converterFromSourceToTarget(sourceBase, targetBase, number)}")
        }
    }
}

fun converterFromSourceToTarget(sourceBase: String, targetBase: String, number: String): String {
    val TEN = "10"
    val indexRange = 0..4

    if (targetBase == TEN) {
        if (containsDot(number)) {
            val resultArray = toDecimal(sourceBase, number).split('.').map() { it }.toMutableList()
            return "${resultArray.first()}.${resultArray.last().slice(indexRange)}"
        } else {
            return toDecimal(sourceBase, number)
        }
    } else {
        return fromDecimalTo(targetBase, toDecimal(sourceBase, number))
    }
}

fun fromDecimalTo(targetBase: String, numberPassed: String): String {
    if (containsDot(numberPassed)) {
        return intFromDecimalTo(BigInteger(targetBase), numberPassed) + fractFromDecimalTo(BigDecimal(targetBase), numberPassed)
    } else {
        return intFromDecimalTo(BigInteger(targetBase), numberPassed)
    }
}

fun fractFromDecimalTo(targetBase: BigDecimal, numberPassed: String): String {
    val zeroString = ".00000"
    val repeatFive = 5
    val eightySeven = 87
    val zero = "0"

    var number = numberPassed.toBigDecimal().setScale(10, RoundingMode.DOWN);
    var result: String = ""
    var fractionalPart: BigDecimal = BigDecimal.ZERO

    if (number.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
        return zeroString
    }

    repeat(repeatFive) {
        fractionalPart = number.remainder( BigDecimal.ONE )
        if (fractionalPart.compareTo(BigDecimal.ZERO) != 0) {
            number = fractionalPart * targetBase
            result += convertFromNumberToCharBD(number)
        } else {
            result += zero
        }
    }
    return ".$result"
}

fun intFromDecimalTo(targetBase: BigInteger, numberPassed: String):String {
    var number = getInt(numberPassed).toBigInteger()
    var result = ""

    if (number == BigInteger.ZERO) {
        return "0"
    }

    when (targetBase.toInt()) {
        10 -> {
            return numberPassed
        }
        else -> {
            while (number != BigInteger.ZERO) {
                result += convertFromNumberToCharBI(number, targetBase)
                number /= targetBase
            }
        }
    }
    return result.reversed()
}

fun toDecimal(sourceBase: String, number: String): String {
    return if (containsDot(number)) {
        (intToDecimal(sourceBase, number) + fracToDecimal(sourceBase, number)).toString()
    } else {
        intToDecimal(sourceBase, number).toString()
    }
}

fun fracToDecimal(sourceBase: String, number: String): BigDecimal {
    val twenty = 20
    val five = 5
    val fractNumber = getFract(number)
    var result: BigDecimal = BigDecimal.ZERO
    val bigDecBase = BigDecimal(sourceBase)
    var convertedNumber: BigDecimal = BigDecimal.ZERO

    for (i in fractNumber.indices) {
        convertedNumber = convertFromCharToNumberBF(fractNumber[i])
        result += BigDecimal.ONE.setScale(twenty, RoundingMode.DOWN)/bigDecBase.pow((i+1)).setScale(twenty, RoundingMode.DOWN) * convertedNumber.setScale(twenty, RoundingMode.DOWN)
    }

    if (result.compareTo(BigDecimal.ZERO) == 0) {
        return BigDecimal.ZERO.setScale(five, RoundingMode.DOWN)
    } else {
        return result
    }
}


fun intToDecimal(base: String, numberPassed: String): BigDecimal {
    var sourceBase = BigInteger(base)
    var stringNumber = numberPassed.split(".").map() { it }.toMutableList()[0].reversed() //TODO
    var bigInteger: BigInteger = BigInteger.ZERO
    var convertedNumber: BigInteger = BigInteger.ZERO

    if (stringNumber.equals("0")) {
        return BigDecimal.ZERO
    }

    for (i in stringNumber.indices) {
        convertedNumber = convertFromCharToNumberBI(stringNumber[i])
        bigInteger += sourceBase.pow(i) * convertedNumber
    }
    return BigDecimal(bigInteger)
}

fun containsDot(number: String): Boolean {
    var dot = '.'
    return number.contains(dot)
}

fun getFract(number: String): String{
    return number.split(".").map() { it }.toMutableList().last()
}

fun getInt(number: String): String {
    return number.split(".").map() { it }.toMutableList().first()
}

fun convertFromCharToNumberBI(letter: Char): BigInteger {
    val eightySeven = 87

    return if(letter.isDigit()) {
        BigInteger(letter.toString())
    } else {
        (letter.code - eightySeven).toBigInteger()
    }
}

fun convertFromCharToNumberBF(letter: Char): BigDecimal {
    val eightySeven = 87

    return if(letter.isDigit()) {
        BigDecimal(letter.toString())
    } else {
        (letter.code - eightySeven).toBigDecimal()
    }
}

fun convertFromNumberToCharBI(number: BigInteger, targetBase: BigInteger): String {
    return if (number % targetBase >= BigInteger.valueOf(10) && number % targetBase <= BigInteger.valueOf(35)) {
        ((number % targetBase) + BigInteger.valueOf(87)).toInt().toString()
    } else  {
        "${number % targetBase}"
    }
}

fun convertFromNumberToCharBD(number: BigDecimal): String {
    val eightySeven = 87

    return if (number.toInt() in 10..35) {
        (number.toInt() + eightySeven).toChar().toString()
    } else {
        number.toInt().toString()
    }
}



