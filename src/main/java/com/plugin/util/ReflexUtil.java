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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class ReflexUtil {

    /**
     * 得到某个类的所有属性名称和该属性对应的类型
     *
     * @param clazz
     * @return
     */
    public Map<String, Class<?>> getAllFields(Class clazz) {
        Map<String, Class<?>> map = new HashMap<String, Class<?>>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            map.put(f.getName(), f.getType());
        }
        return map;
    }

    /**
     * 通过url向某台机器发起一个请求，然后获得该url请求的内容
     * @param urlStr
     *            请求的url地址
     * @return 返回该地址请求的字符串
     */
    public String getUrlContent(String urlStr) throws Exception {
        StringBuffer frontText = new StringBuffer();
        URL url = new URL(urlStr);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str = null;
        while ((str = in.readLine()) != null) {
            frontText.append(str);
        }
        return frontText.toString();
    }

}
