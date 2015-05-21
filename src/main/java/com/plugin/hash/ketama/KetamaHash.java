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
package com.plugin.hash.ketama;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class KetamaHash {

    private static long hash(byte[] digest, int nTime) {
        long rv = ((long) (digest[3+nTime*4] & 0xFF) << 24)
                | ((long) (digest[2+nTime*4] & 0xFF) << 16)
                | ((long) (digest[1+nTime*4] & 0xFF) << 8)
                | (digest[0+nTime*4] & 0xFF);

        return rv & 0xffffffffL; /* Truncate to 32-bits */
    }

    /**
     * Get the md5 of the given key.
     */
    private static byte[] computeMd5(String k) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
        md5.reset();
        byte[] keyBytes = null;
        try {
            keyBytes = k.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unknown string :" + k, e);
        }
        md5.update(keyBytes);
        return md5.digest();
    }

    /**
     * Get the hash of the given key.
     */
    public static long hashKey(String key){
        return hash(computeMd5(key), 0);
    }

    public static long hashKey(String key, int size){
        return hash(computeMd5(key),size);
    }

}
