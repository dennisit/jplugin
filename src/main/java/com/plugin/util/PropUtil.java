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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class PropUtil {

    private static Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);

    public static final String DEFAULT = "system";

    private static Properties p = null;


    static {
        try {
            p = init(DEFAULT);
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
    }


    public static String getValue(String key) {
        return getValue( key, PropUtil.DEFAULT );
    }


    public static String getValue(String key, String propertiesName) {
        if (p == null || p.size() == 0) {
            p = new Properties();
            try {
                p.load(PropUtil.class.getResourceAsStream("/" + propertiesName + ".properties"));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return String.valueOf(p.getProperty(key, ""));
    }

    public static Properties init(String resourceName) throws IOException {
       return PropertiesLoaderUtils.loadAllProperties(resourceName);
    }


}
