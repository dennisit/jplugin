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


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class HttpUtil {

    private static Logger LOG = LoggerFactory.getLogger(HttpUtil.class);


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

    public static String executeHttpRequestString(String url, int timeout) {
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        try {
            getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
            getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
            getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeout);
            int statusCode = client.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                return "";
            }
            String responseBody = getMethod.getResponseBodyAsString();
            return responseBody;
        } catch (Exception e) {
            LOG.error("----------- executeHttpRequestString Error. -----------", e);
        } finally {
            getMethod.releaseConnection();
        }
        return null;
    }

    public static void postData(Reader data, Writer output, URL targetUrl, String POST_ENCODING) throws Exception {
        HttpURLConnection urlc = null;
        IOException e;
        OutputStream out;
        urlc = (HttpURLConnection)targetUrl.openConnection();
        try{
            //GET POST HEAD OPTIONS PUT DELETE TRACE
            urlc.setRequestMethod("POST");
        }
        catch(IOException ee){
            throw new Exception("Shouldn't happen: HttpURLConnection doesn't support POST!!", ee);
        }
        urlc.setDoOutput(true);
        urlc.setDoInput(true);
        //不用缓存，true使用任何可以的缓存
        urlc.setUseCaches(false);
        urlc.setAllowUserInteraction(false);
        urlc.setRequestProperty("Content-type", (new StringBuilder()).append("text/xml; charset=").append(POST_ENCODING).toString());
        out = urlc.getOutputStream();
        try{
            Writer writer = new OutputStreamWriter(out, POST_ENCODING);
            pipe(data, writer);
            writer.close();
        }catch(IOException ee)
        {
            throw new Exception("IOException while posting data", ee);
        }
        if(out != null){
            try{
                out.close();
            }catch(Exception ee){}
        }
        InputStream in = urlc.getInputStream();
        try{
            Reader reader = new InputStreamReader(in);
            pipe(reader, output);
            reader.close();
        }catch(IOException ee){
            throw new Exception("IOException while reading response", ee);
        }
        if(in != null)
            try{
                in.close();
            }catch(Exception ee){}
        if(urlc != null){
            try{
                urlc.disconnect();
            }catch(Exception ee){
                throw new Exception((new StringBuilder())
                        .append("Connection error (is server running at ")
                        .append(targetUrl).append(" ?): ")
                        .append(out).toString());
            }
        }
    }


    private static void pipe(Reader reader, Writer writer) throws IOException {
        char buf[] = new char[1024];
        StringBuffer sb = new StringBuffer();
        for (int read = 0; (read = reader.read(buf)) >= 0;){
            writer.write(buf, 0, read);
            sb.append(String.valueOf(buf).trim());
        }
        writer.flush();
    }
}
