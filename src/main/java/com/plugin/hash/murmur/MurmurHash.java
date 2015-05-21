//--------------------------------------------------------------------------
// Copyright (c) 2010-2020, En.dennisit or Cn.苏若年
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// Neither the name of the dennisit nor the names of its contributors
// may be used to endorse or promote products derived from this software
// without specific prior written permission.
// Author: dennisit@163.com | dobby | 苏若年
//--------------------------------------------------------------------------
package com.plugin.hash.murmur;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * MurmurHash算法：高运算性能，低碰撞率.
 * 由Austin Appleby创建于2008年,现已应用到Hadoop、libstdc++、nginx、libmemcached等开源系统。
 * 2011年Appleby被Google雇佣，随后Google推出其变种的CityHash算法。
 * This is a very fast, non-cryptographic hash suitable for general hash-based
 * lookup. See http://murmurhash.googlepages.com/ for more details.
 * <p>The C version of MurmurHash 2.0 found at that site was ported to Java by Andrzej Bialecki (ab at getopt org).
 * </p>
 */
public class MurmurHash implements Hashing {
    /**
     * Hashes bytes in an array.
     *
     * @param data
     *            The bytes to hash.
     * @param seed
     *            The seed for the hash.
     * @return The 32 bit hash of the bytes in question.
     */
    public static int hash(byte[] data, int seed) {
        return hash(ByteBuffer.wrap(data), seed);
    }

    /**
     * Hashes bytes in part of an array.
     *
     * @param data
     *            The data to hash.
     * @param offset
     *            Where to start munging.
     * @param length
     *            How many bytes to process.
     * @param seed
     *            The seed to start with.
     * @return The 32-bit hash of the data in question.
     */
    public static int hash(byte[] data, int offset, int length, int seed) {
        return hash(ByteBuffer.wrap(data, offset, length), seed);
    }

    /**
     * Hashes the bytes in a buffer from the current position to the limit.
     *
     * @param buf
     *            The bytes to hash.
     * @param seed
     *            The seed for the hash.
     * @return The 32 bit murmur hash of the bytes in the buffer.
     */
    public static int hash(ByteBuffer buf, int seed) {
        // save byte order for later restoration
        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        int m = 0x5bd1e995;
        int r = 24;

        int h = seed ^ buf.remaining();

        int k;
        while (buf.remaining() >= 4) {
            k = buf.getInt();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(4).order(
                    ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, use this first:
            // finish.position(4-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getInt();
            h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        buf.order(byteOrder);
        return h;
    }

    public static long hash64A(byte[] data, int seed) {
        return hash64A(ByteBuffer.wrap(data), seed);
    }

    public static long hash64A(byte[] data, int offset, int length, int seed) {
        return hash64A(ByteBuffer.wrap(data, offset, length), seed);
    }

    public static long hash64A(ByteBuffer buf, int seed) {
        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }

    public long hash(byte[] key) {
        return hash64A(key, 0x1234ABCD);
    }

    public long hash(String key) {
        return hash(SafeEncoder.encode(key));
    }
}
