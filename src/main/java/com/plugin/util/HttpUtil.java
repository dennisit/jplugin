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


import java.io.IOException;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.NameValuePair;
/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class HttpUtil {

    public static String getUrlBody(String urlString) {
        String responseStr = "";
        HttpClient httpClient = new HttpClient();
        String url = urlString.trim();
        HttpMethod getMethod = new GetMethod(url);
        // 使用系统提供的默认的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(getMethod);

        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
        // 301或者302
        if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
            // 从头中取出转向的地址
            Header locationHeader = getMethod.getResponseHeader("location");
            String location = null;
            if (locationHeader != null) {
                location = locationHeader.getValue();
                responseStr = "The page was redirected to:" + location;
            } else {
                responseStr = "Location field value is null.";
            }
            return responseStr;
        } else {

            try {
                responseStr = getMethod.getResponseBodyAsString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getMethod.releaseConnection();
        return responseStr;
    }

    public static String postData(String url2, NameValuePair[] data) {
        String responseStr="";
        HttpClient httpClient = new HttpClient();
        String url = url2;
        PostMethod postMethod = new PostMethod(url);
        // 将表单的值放入postMethod中
        postMethod.setRequestBody(data);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
        // 执行postMethod
        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(postMethod);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
        // 301或者302
        if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
            // 从头中取出转向的地址
            Header locationHeader = postMethod.getResponseHeader("location");
            String location = null;
            if (locationHeader != null) {
                location = locationHeader.getValue();
                responseStr="The page was redirected to:" + location;
            } else {
                responseStr="Location field value is null.";
            }
            return responseStr;
        } else {

            try {
                responseStr = postMethod.getResponseBodyAsString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        postMethod.releaseConnection();
        return responseStr;
    }
}
