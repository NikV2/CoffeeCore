package me.nik.coffeecore.utils.custom;

/**
 * Bad packet limiter without any compensation, Do not use in production.
 */
public class PacketBucket {

    private long start;
    private int count;

    public boolean increment() {

        final long currentTime = System.currentTimeMillis();

        if (currentTime - this.start >= 1000L) {
            this.start = currentTime;
            this.count = 0;
        }

        return this.count++ > 300;
    }
}