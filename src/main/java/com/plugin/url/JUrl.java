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
package com.plugin.url;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class JUrl {

    private static final long serialVersionUID = -1985165475234910535L;

    private final String protocol;

    private final String username;

    private final String password;

    private final String host;

    private final int port;

    private final String path;

    private final Map<String, String> parameters;

    private volatile transient String ip;

    private final transient Map<String, Number> numbers = new ConcurrentHashMap<String, Number>();

    protected JUrl() {
        this.protocol = null;
        this.username = null;
        this.password = null;
        this.host = null;
        this.port = 0;
        this.path = null;
        this.parameters = null;
    }

    public JUrl(String protocol, String host, int port) {
        this(protocol, null, null, host, port, null, (Map<String, String>) null);
    }


    public JUrl(String protocol, String host, int port, Map<String, String> parameters) {
        this(protocol, null, null, host, port, null, parameters);
    }

    public JUrl(String protocol, String host, int port, String path) {
        this(protocol, null, null, host, port, path, (Map<String, String>) null);
    }

    public JUrl(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this(protocol, null, null, host, port, path, parameters);
    }

    public JUrl(String protocol, String username, String password, String host, int port, String path) {
        this(protocol, username, password, host, port, path, (Map<String, String>) null);
    }


    public JUrl(String protocol, String username, String password, String host, int port, String path, Map<String, String> parameters) {
        if ((username == null || username.length() == 0)
                && password != null && password.length() > 0) {
            throw new IllegalArgumentException("Invalid url, password without username!");
        }
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = (port < 0 ? 0 : port);
        this.path = path;
        // trim the beginning "/"
        while(path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        } else {
            parameters = new HashMap<String, String>(parameters);
        }

        this.parameters = Collections.unmodifiableMap(parameters);
    }

    /**
     * Parse url string
     *
     * @param url JUrl string
     * @return JUrl instance
     * @see JUrl
     */
    public static JUrl valueOf(String url) {
        if (url == null || (url = url.trim()).length() == 0) {
            throw new IllegalArgumentException("url == null");
        }
        String protocol = null;
        String username = null;
        String password = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = null;
        int i = url.indexOf("?"); // seperator between body and parameters 
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("\\&");
            parameters = new HashMap<String, String>();
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        parameters.put(part.substring(0, j), part.substring(j + 1));
                    } else {
                        parameters.put(part, part);
                    }
                }
            }
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if(i == 0) throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        }
        else {
            // case: file:/path/to/file.txt
            i = url.indexOf(":/");
            if(i>=0) {
                if(i == 0) throw new IllegalStateException("url missing protocol: \"" + url + "\"");
                protocol = url.substring(0, i);
                url = url.substring(i + 1);
            }
        }

        i = url.indexOf("/");
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }
        i = url.indexOf("@");
        if (i >= 0) {
            username = url.substring(0, i);
            int j = username.indexOf(":");
            if (j >= 0) {
                password = username.substring(j + 1);
                username = username.substring(0, j);
            }
            url = url.substring(i + 1);
        }
        i = url.indexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            port = Integer.parseInt(url.substring(i + 1));
            url = url.substring(0, i);
        }
        if(url.length() > 0) host = url;
        return new JUrl(protocol, username, password, host, port, path, parameters);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return port <= 0 ? host : host + ":" + port;
    }

    public String getPath() {
        return path;
    }

    public String getAbsolutePath() {
        if (path != null && !path.startsWith("/")) {
            return "/" + path;
        }
        return path;
    }

    public JUrl setProtocol(String protocol) {
        return new JUrl(protocol, username, password, host, port, path, getParameters());
    }

    public JUrl setUsername(String username) {
        return new JUrl(protocol, username, password, host, port, path, getParameters());
    }

    public JUrl setPassword(String password) {
        return new JUrl(protocol, username, password, host, port, path, getParameters());
    }

    public JUrl setAddress(String address) {
        int i = address.lastIndexOf(':');
        String host;
        int port = this.port;
        if (i >= 0) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
        }
        return new JUrl(protocol, username, password, host, port, path, getParameters());
    }

    public JUrl setHost(String host) {
        return new JUrl(protocol, username, password, host, port, path, getParameters());
    }

    public JUrl setPort(int port) {
        return new JUrl(protocol, username, password, host, port, path, getParameters());
    }

    public JUrl setPath(String path) {
        return new JUrl(protocol, username, password, host, port, path, getParameters());
    }

    public Map<String, String> getParameters() {
        return parameters;
    }


    public String getParameter(String key) {
        String value = parameters.get(key);
        return value;
    }

    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public double getParameter(String key, double defaultValue) {
        Number n = numbers.get(key);
        if (n != null) {
            return n.doubleValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        double d = Double.parseDouble(value);
        numbers.put(key, d);
        return d;
    }

    public float getParameter(String key, float defaultValue) {
        Number n = numbers.get(key);
        if (n != null) {
            return n.floatValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        float f = Float.parseFloat(value);
        numbers.put(key, f);
        return f;
    }

    public long getParameter(String key, long defaultValue) {
        Number n = numbers.get(key);
        if (n != null) {
            return n.longValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        long l = Long.parseLong(value);
        numbers.put(key, l);
        return l;
    }

    public int getParameter(String key, int defaultValue) {
        Number n = numbers.get(key);
        if (n != null) {
            return n.intValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        numbers.put(key, i);
        return i;
    }

    public short getParameter(String key, short defaultValue) {
        Number n = numbers.get(key);
        if (n != null) {
            return n.shortValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        short s = Short.parseShort(value);
        numbers.put(key, s);
        return s;
    }

    public byte getParameter(String key, byte defaultValue) {
        Number n = numbers.get(key);
        if (n != null) {
            return n.byteValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        byte b = Byte.parseByte(value);
        numbers.put(key, b);
        return b;
    }

    public float getPositiveParameter(String key, float defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        float value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public double getPositiveParameter(String key, double defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        double value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public long getPositiveParameter(String key, long defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        long value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public int getPositiveParameter(String key, int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        int value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public short getPositiveParameter(String key, short defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        short value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public byte getPositiveParameter(String key, byte defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        byte value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public char getParameter(String key, char defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value.charAt(0);
    }

    public boolean getParameter(String key, boolean defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean hasParameter(String key) {
        String value = getParameter(key);
        return value != null && value.length() > 0;
    }

    public String getMethodParameter(String method, String key) {
        String value = parameters.get(method + "." + key);
        if (value == null || value.length() == 0) {
            return getParameter(key);
        }
        return value;
    }

    public String getMethodParameter(String method, String key, String defaultValue) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public double getMethodParameter(String method, String key, double defaultValue) {
        String methodKey = method + "." + key;
        Number n = numbers.get(methodKey);
        if (n != null) {
            return n.intValue();
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        double d = Double.parseDouble(value);
        numbers.put(methodKey, d);
        return d;
    }

    public float getMethodParameter(String method, String key, float defaultValue) {
        String methodKey = method + "." + key;
        Number n = numbers.get(methodKey);
        if (n != null) {
            return n.intValue();
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        float f = Float.parseFloat(value);
        numbers.put(methodKey, f);
        return f;
    }

    public long getMethodParameter(String method, String key, long defaultValue) {
        String methodKey = method + "." + key;
        Number n = numbers.get(methodKey);
        if (n != null) {
            return n.intValue();
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        long l = Long.parseLong(value);
        numbers.put(methodKey, l);
        return l;
    }

    public int getMethodParameter(String method, String key, int defaultValue) {
        String methodKey = method + "." + key;
        Number n = numbers.get(methodKey);
        if (n != null) {
            return n.intValue();
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        numbers.put(methodKey, i);
        return i;
    }

    public short getMethodParameter(String method, String key, short defaultValue) {
        String methodKey = method + "." + key;
        Number n = numbers.get(methodKey);
        if (n != null) {
            return n.shortValue();
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        short s = Short.parseShort(value);
        numbers.put(methodKey, s);
        return s;
    }

    public byte getMethodParameter(String method, String key, byte defaultValue) {
        String methodKey = method + "." + key;
        Number n = numbers.get(methodKey);
        if (n != null) {
            return n.byteValue();
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        byte b = Byte.parseByte(value);
        numbers.put(methodKey, b);
        return b;
    }

    public double getMethodPositiveParameter(String method, String key, double defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        double value = getMethodParameter(method, key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public float getMethodPositiveParameter(String method, String key, float defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        float value = getMethodParameter(method, key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public long getMethodPositiveParameter(String method, String key, long defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        long value = getMethodParameter(method, key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public int getMethodPositiveParameter(String method, String key, int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        int value = getMethodParameter(method, key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public short getMethodPositiveParameter(String method, String key, short defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        short value = getMethodParameter(method, key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public byte getMethodPositiveParameter(String method, String key, byte defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        byte value = getMethodParameter(method, key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public char getMethodParameter(String method, String key, char defaultValue) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value.charAt(0);
    }

    public boolean getMethodParameter(String method, String key, boolean defaultValue) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean hasMethodParameter(String method, String key) {
        if (method == null) {
            String suffix = "." + key;
            for (String fullKey : parameters.keySet()) {
                if (fullKey.endsWith(suffix)) {
                    return true;
                }
            }
            return false;
        }
        if (key == null) {
            String prefix = method + ".";
            for (String fullKey : parameters.keySet()) {
                if (fullKey.startsWith(prefix)) {
                    return true;
                }
            }
            return false;
        }
        String value = getMethodParameter(method, key);
        return value != null && value.length() > 0;
    }

    public JUrl addParameter(String key, boolean value) {
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, char value) {
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, byte value) {
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, short value) {
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, int value) {
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, long value) {
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, float value) {
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, double value) {
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, Enum<?> value) {
        if(value == null) return this;
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, Number value) {
        if(value == null) return this;
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, CharSequence value) {
        if(value == null || value.length() == 0) return this;
        return addParameter(key, String.valueOf(value));
    }

    public JUrl addParameter(String key, String value) {
        if (key == null || key.length() == 0
                || value == null || value.length() == 0) {
            return this;
        }
        Map<String, String> map = new HashMap<String, String>(getParameters());
        map.put(key, value);
        return new JUrl(protocol, username, password, host, port, path, map);
    }

    public JUrl addParameterIfAbsent(String key, String value) {
        if (key == null || key.length() == 0
                || value == null || value.length() == 0) {
            return this;
        }
        if (hasParameter(key)) {
            return this;
        }
        Map<String, String> map = new HashMap<String, String>(getParameters());
        map.put(key, value);
        return new JUrl(protocol, username, password, host, port, path, map);
    }

    /**
     * Add parameters to a new url.
     *
     * @param parameters
     * @return A new JUrl 
     */
    public JUrl addParameters(Map<String, String> parameters) {
        if (parameters == null || parameters.size() == 0) {
            return this;
        }
        Map<String, String> map = new HashMap<String, String>(getParameters());
        map.putAll(parameters);
        return new JUrl(protocol, username, password, host, port, path, map);
    }

    public JUrl addParametersIfAbsent(Map<String, String> parameters) {
        if (parameters == null || parameters.size() == 0) {
            return this;
        }
        Map<String, String> map = new HashMap<String, String>(parameters);
        map.putAll(getParameters());
        return new JUrl(protocol, username, password, host, port, path, map);
    }

    public JUrl addParameters(String... pairs) {
        if (pairs == null || pairs.length == 0) {
            return this;
        }
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Map pairs can not be odd number.");
        }
        Map<String, String> map = new HashMap<String, String>();
        int len = pairs.length / 2;
        for (int i = 0; i < len; i ++) {
            map.put(pairs[2 * i], pairs[2 * i + 1]);
        }
        return addParameters(map);
    }

    public JUrl removeParameter(String key) {
        if (key == null || key.length() == 0) {
            return this;
        }
        return removeParameters(key);
    }

    public JUrl removeParameters(Collection<String> keys) {
        if (keys == null || keys.size() == 0) {
            return this;
        }
        return removeParameters(keys.toArray(new String[0]));
    }

    public JUrl removeParameters(String... keys) {
        if (keys == null || keys.length == 0) {
            return this;
        }
        Map<String, String> map = new HashMap<String, String>(getParameters());
        for (String key : keys) {
            map.remove(key);
        }
        if (map.size() == getParameters().size()) {
            return this;
        }
        return new JUrl(protocol, username, password, host, port, path, map);
    }

    public JUrl cleatParameters() {
        return new JUrl(protocol, username, password, host, port, path, new HashMap<String, String>());
    }

    public String toString() {
        return buildString(false, true); // no show username and password
    }

    public String toString(String... parameters) {
        return buildString(false, true, parameters); // no show username and password
    }

    public String toIdentityString() {
        return buildString(false, false); // only return identity message, see the method "equals" and "hashCode"
    }

    public String toIdentityString(String... parameters) {
        return buildString(false, false, parameters); // only return identity message, see the method "equals" and "hashCode"
    }

    public String toFullString() {
        return buildString(true, true);
    }

    public String toFullString(String... parameters) {
        return buildString(true, true, parameters);
    }

    public String toParameterString() {
        return toParameterString(new String[0]);
    }

    public String toParameterString(String... parameters) {
        StringBuilder buf = new StringBuilder();
        buildParameters(buf, false, parameters);
        return buf.toString();
    }

    private void buildParameters(StringBuilder buf, boolean concat, String[] parameters) {
        if (getParameters() !=null && getParameters().size() > 0) {
            List<String> includes = (parameters == null || parameters.length == 0 ? null : Arrays.asList(parameters));
            boolean first = true;
            for (Map.Entry<String, String> entry : new TreeMap<String, String>(getParameters()).entrySet()) {
                if (entry.getKey() != null && entry.getKey().length() > 0
                        && (includes == null || includes.contains(entry.getKey()))) {
                    if (first) {
                        if (concat) {
                            buf.append("?");
                        }
                        first = false;
                    } else {
                        buf.append("&");
                    }
                    buf.append(entry.getKey());
                    buf.append("=");
                    buf.append(entry.getValue() == null ? "" : entry.getValue().trim());
                }
            }
        }
    }

    private String buildString(boolean u, boolean p, String... parameters) {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append(protocol);
            buf.append("://");
        }
        if (u && username != null && username.length() > 0) {
            buf.append(username);
            if (password != null && password.length() > 0) {
                buf.append(":");
                buf.append(password);
            }
            buf.append("@");
        }
        if(host != null && host.length() > 0) {
            buf.append(host);
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        if (path != null && path.length() > 0) {
            buf.append("/");
            buf.append(path);
        }
        if (p) {
            buildParameters(buf, true, parameters);
        }
        return buf.toString();
    }

    public java.net.URL toJavaURL() {
        try {
            return new java.net.URL(toString());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }


    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + port;
        result = prime * result
                + ((protocol == null) ? 0 : protocol.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JUrl other = (JUrl) obj;
        if (host == null) {
            if (other.host != null)
                return false;
        } else if (!host.equals(other.host))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (port != other.port)
            return false;
        if (protocol == null) {
            if (other.protocol != null)
                return false;
        } else if (!protocol.equals(other.protocol))
            return false;
        return true;
    }

    public JUrl addParameter(String key, String[] values) {
        if(values != null){
            StringBuffer sb = new StringBuffer();
            for(String v: values){
                sb.append(v).append(",");
            }
            if(sb.length() > 1){
                sb.deleteCharAt(sb.length() -1);
            }
            return addParameter(key, sb.toString());
        }
        return this;
    }

}
