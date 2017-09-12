//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Device {
    public static final int DEFAULT_SIZE = 4;
    public static final int DEFAULT_PEEKS = 2;
    public static final char VALUE_TRUE = 'T';
    public static final char VALUE_FALSE = 'F';
    private int size; // a
    private int bitsPerPeek; // b
    private boolean[] bits; //a[]
    private Device.State state; // a
    private CharSequence peekPattern; // a

    public Device() {
        this(DEFAULT_SIZE, DEFAULT_PEEKS);
    }

    public Device(int size, int bitsPerPeek) {
        this.size = size;
        this.bitsPerPeek = bitsPerPeek;
        this.bits = new boolean[this.size];

        for(int i = 0; i < this.size; ++i) {
            this.bits[i] = Math.random() >= 0.5D;
        }

        this.state = Device.State.SPUN;
    }

    public Device(boolean[] initialBits, int bitsPerPeek) {
        this.size = initialBits.length;
        this.bitsPerPeek = bitsPerPeek;
        this.bits = Arrays.copyOf(initialBits, this.size);
        this.state = Device.State.SPUN;
    }

    public boolean spin() {
        if (this.state == Device.State.SPUN ||
            this.state == Device.State.PEEKED ||
            this.state == Device.State.POKED) {
            for(int numSpins = (int)(Math.random() * (double)this.size);
                numSpins > 0; --numSpins) {
                Device current = this;
                boolean temp = this.bits[0];

                for(int i = 1; i < current.size; ++i) {
                    current.bits[i - 1] = current.bits[i];
                }

                current.bits[current.size - 1] = temp;
            }

            this.state = Device.State.SPUN;
        }

        return !IntStream.range(0, this.bits.length).mapToObj((var1x) -> {
            return this.bits[var1x];
        }).anyMatch((var0) -> {
            return !var0.booleanValue();
        }) || !IntStream.range(0, this.bits.length).mapToObj((var1x) -> {
            return this.bits[var1x];
        }).anyMatch((var0) -> {
            return var0.booleanValue();
        });
    }

    public CharSequence peek(CharSequence pattern) {
        char[] peekArr = new char[this.size];
        if (this.state == Device.State.SPUN &&
            pattern.length() == this.size &&
            (long)this.bitsPerPeek >= pattern.chars().filter((x) -> {
                return x == '?';
            }).count()) {
            this.peekPattern = pattern;
            this.state = Device.State.PEEKED;

            for(int i = 0; i < this.size; ++i) {
                if (pattern.charAt(i) == '?') {
                    peekArr[i] = (this.bits[i] ? VALUE_TRUE : VALUE_FALSE);
                } else {
                    peekArr[i] = pattern.charAt(i);
                }
            }
        }

        return new String(peekArr);
    }

    public void poke(CharSequence pattern) {
        if (this.state == Device.State.PEEKED &&
            pattern.length() == this.size &&
            this.peekPattern != null &&
            this.peekPattern.length() == this.size &&
            (long)this.bitsPerPeek >= this.peekPattern.chars().filter((x) -> {
                return x == '?';
            }).count()) {
            this.state = Device.State.POKED;

            for(int i = 0; i < this.size; ++i) {
                if (this.peekPattern.charAt(i) == '?') {
                    char patternBit;
                    if ((patternBit = pattern.charAt(i)) == VALUE_TRUE) {
                        this.bits[i] = true;
                    } else if (patternBit == VALUE_FALSE) {
                        this.bits[i] = false;
                    }
                }
            }
        }

    }

    public String toString() {
        return "Device state: " + this.state;
    }

    private static enum State {
        SPUN,
        PEEKED,
        POKED;
    }
}
