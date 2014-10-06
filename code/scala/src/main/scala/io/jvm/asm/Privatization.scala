package io.jvm.asm

object Privatization {
  /**
    * Mutates the provided bytecode and returns a boolean signifying whether a change has occurred.
    * @param bytecode Byte array containing JVM class bytecode
    * @return {@code true} if the access flag was previously public, {@code false} otherwise
    */
  def apply(bytecode: Array[Byte]): Boolean = {
    var index = 8

    def u1() = {
      require(index < bytecode.length, "Tried to read past bytecode length!")
      val res = bytecode(index) & 0xff
      index += 1
      res
    }

    def u2() = (u1() << 8) | u1()

    val poolSize = u2()
    var current = 1

    while (current < poolSize) {
      u1() match {
        case 1 =>
          index += u2() + 2
          current += 1

        case 3 | 4 | 9 | 10 | 11 | 12 =>
          index += 4
          current += 1

        case 5 | 6 =>
          index += 8
          current += 2

        case 7 | 8 =>
          index += 2
          current += 1

        case tag =>
          sys.error("Encountered unknown tag: " + tag)
      }
    }

    /* We're only concerned with the second access flag byte */
    val accessFlags = { u1(); u1() }
    val isPublic = (accessFlags & 1) != 0

    if (isPublic) {
      bytecode(index - 1) = accessFlags & ~1 toByte
    }

    /* Return true if a change has occurred */
    isPublic
  }
}
