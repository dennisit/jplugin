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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public interface Hashing {

    public static final Hashing MURMUR_HASH = new MurmurHash();

    public ThreadLocal<MessageDigest> md5Holder = new ThreadLocal<MessageDigest>();

    public static final Hashing MD5 = new Hashing() {
        public long hash(String key) {
            return hash(SafeEncoder.encode(key));
        }
        public long hash(byte[] key) {
            try {
                if (md5Holder.get() == null) {
                    md5Holder.set(MessageDigest.getInstance("MD5"));
                }
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("++++ no md5 algorythm found");
            }
            MessageDigest md5 = md5Holder.get();

            md5.reset();
            md5.update(key);
            byte[] bKey = md5.digest();
            long res = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF);
            return res;
        }
    };

    public long hash(String key);

    public long hash(byte[] key);
}

