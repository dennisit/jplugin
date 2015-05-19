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
package com.plugin.ibatis.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * description: 动态数据源routing
 * @author dennisit@163.com
 * @version 1.0
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    private static AtomicInteger counter = new AtomicInteger(0);

    private String[] readDataSourceKeys;

    @Override
    protected Object determineCurrentLookupKey() {
        counter.incrementAndGet();
        String type = DataSourceContextHolder.getDBType();
        String dataSourceKey = DataSourceContextHolder.WRITE;
        if(DataSourceContextHolder.READ.equals(type)){
            dataSourceKey = randomReadDataSourceKey();
        }

        return dataSourceKey;
    }

    public String randomReadDataSourceKey(){
        if(readDataSourceKeys == null|| readDataSourceKeys.length == 0){
            throw new RuntimeException("readDataSourceKeys is empty");
        }
        return readDataSourceKeys[(int)(Math.random() * readDataSourceKeys.length)];
    }

    public String[] getReadDataSourceKeys() {
        return readDataSourceKeys;
    }

    public void setReadDataSourceKeys(String[] readDataSourceKeys) {
        this.readDataSourceKeys = readDataSourceKeys;
    }


    /**
     * 数据源类型Holder
     */
    public static class DataSourceContextHolder {

        private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

        public static final String READ = "READ";
        public static final String WRITE = "WRITE";

        public static void setCustomerType(String type) {
            if(null == getDBType()){
                contextHolder.set(type);
            }
        }

        public static void directSetCustomerType(String type) {
            contextHolder.remove();
            contextHolder.set(type);
        }

        public static String getDBType() {
            return  contextHolder.get();
        }

        public static void clearType() {
            contextHolder.remove();
        }
    }

}
