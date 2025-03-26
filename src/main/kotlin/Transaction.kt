class Transaction {
    var name: String? = null
    var amount: Amount? = null
    override fun toString(): String {
        return "Transaction{name='$name', amount=$amount}"
    }
}


class Amount {
    var value: String? = null // Field is a String
    var currency: String? = null
    override fun toString(): String {
        return "Amount{value='$value', currency='$currency'}"
    }
}

