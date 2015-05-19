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
package com.plugin.util;

import java.util.Properties;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class ProperUtil {

    public static final String DEFAULT = "system";

    private static Properties p = null;

    public static String getValue(String key) {
        return getValue( key, ProperUtil.DEFAULT );
    }

    public static String getValue(String key, String propertiesName) {
        if (p == null || p.size() == 0) {
            p = new Properties();
            try {
                p.load(ProperUtil.class.getResourceAsStream("/" + propertiesName + ".properties"));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return String.valueOf(p.getProperty(key, ""));
    }
}
