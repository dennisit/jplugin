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
package com.plugin.hash.mod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

/**
 * description:
 * @author dennisit@163.com
 * @version 1.0
 */
public class ModHash {

    private static final Logger LOG = LoggerFactory.getLogger(ModHash.class);

    public static long hashcodeMod(String key, int size){
        return hashKey(ModType.HASHCODE,key,size);
    }

    public static long crc32Mod(String key, int size){
        return hashKey(ModType.CRC32,key,size);
    }

    public static long hashKey(ModType modType, String key, int size){
        if(modType.equals(ModType.CRC32)){
            CRC32 crc32 = new CRC32();
            try {
                crc32.update(key.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                LOG.error("key error, key:{}", key, e);
            }
            return (crc32.getValue() % size);
        }
        if(modType.equals(ModType.HASHCODE)){
            return Math.abs(key.hashCode() % size);
        }
        throw new RuntimeException("modType unsupport!");
    }

}
