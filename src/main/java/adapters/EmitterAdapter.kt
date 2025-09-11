package adapters

import interpreter.PrintEmitter

class EmitterAdapter: PrintEmitter {
    override fun print(message: String) {
        message
    }
}