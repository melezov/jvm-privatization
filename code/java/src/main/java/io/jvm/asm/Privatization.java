package io.jvm.asm;

class Privatization {
    /**
     * Mutates the provided bytecode and returns a boolean signifying whether a change has occurred.
     * @param bytecode Byte array containing JVM class bytecode
     * @return {@code true} if the access flag was previously public, {@code false} otherwise
     */
    public static boolean apply(final byte[] bytecode) {
        return new Privatization(bytecode).apply();
    }

    private final byte[] bytecode;
    private int index;

    private Privatization(final byte[] bytecode) {
        this.bytecode = bytecode;
        this.index = 8;
    }

    private int u1() {
        if (index < bytecode.length) throw new ArrayIndexOutOfBoundsException("Tried to read past bytecode length!");
        return bytecode[index++] & 0xff;
    }

    private int u2() {
        return (u1() << 8) | u1();
    }

    private boolean apply() {
        final int poolSize = u2();

        for (int current = 1; current < poolSize; current++) {
            final int tag = u1();

            switch (tag) {
                case 1:
                    index += u2() + 2;
                    break;

                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                    index += 4;
                    break;

                case 5:
                case 6:
                    index += 8;
                    current++;
                    break;

                case 7:
                case 8:
                    index += 2;
                    break;

                default:
                    throw new RuntimeException("Encountered unknown tag: " + tag);
            }
        }

        /** We're only concerned with the second access flag byte */
        u1();
        final int accessFlags = u1();
        final boolean isPublic = (accessFlags & 1) != 0;

        if (isPublic) {
            bytecode[index - 1] = (byte) (accessFlags & ~1);
        }

        /** Return true if a change has occurred */
        return isPublic;
    }
}
