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
package com.plugin.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class SolrJClient {

    private Logger LOG = LoggerFactory.getLogger(SolrJClient.class);

    /**
     * solrj server address
     */
    private String serverAddress; // solrj服务器地址

    /**
     * query fields
     */
    private Set<String> queryFields;

    /**
     * query order field | ASC or DESC
     */
    private Map<String, SolrQuery.ORDER> orderFields;

    /**
     * query filter field
     */
    private Map<String,Object> filterFields;


    /**
     * SolrJClient实例
     */
    private static SolrJClient clientInstance = null;



    private SolrJClient(String serverAddress){
        this.serverAddress = serverAddress;
    }

    /**
     * 获取SolrjUtil实例
     * @param serverAddress solrj服务器地址
     * @return
     */
    public static SolrJClient getInstance(String serverAddress){
        if( null == clientInstance ) {
            synchronized(SolrJClient.class) {
                clientInstance = new SolrJClient(serverAddress);
            }
        }else{
            clientInstance.setServerAddress(serverAddress);
        }
        return clientInstance;
    }


    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Set<String> getQueryFields() {
        return queryFields;
    }

    public void setQueryFields(Set<String> queryFields) {
        this.queryFields = queryFields;
    }

    public Map<String, SolrQuery.ORDER> getOrderFields() {
        return orderFields;
    }

    public void setOrderFields(Map<String, SolrQuery.ORDER> orderFields) {
        this.orderFields = orderFields;
    }

    public Map<String, Object> getFilterFields() {
        return filterFields;
    }

    public void setFilterFields(Map<String, Object> filterFields) {
        this.filterFields = filterFields;
    }


    /**
     * 构建查询字段集合
     * @param queryFields
     * @return
     */
    public Set<String> buildQueryFields(Set<String> queryFields) {
        if( null == queryFields ) {
            Set<String> set = new HashSet<String>();
            this.setQueryFields(set);
            return set;
        } else {
            this.setQueryFields(queryFields);
            return queryFields;
        }
    }

    /**
     * 增加查询字段
     * @param queryField
     * @return
     */
    public Set<String> addQueryField(String queryField) {
        Set<String> set = buildQueryFields(queryFields);
        if( null != queryField ) {
            set.add(queryField);
        }
        setQueryFields(set);
        return set;
    }

    /**
     * 构建排序字段集合，key为要排序的字段，value为ORDER的实例
     * @param map
     * @return
     */
    public Map<String, SolrQuery.ORDER> buildOrderMap(Map<String, SolrQuery.ORDER> map) {
        if( null == map ) {
            this.orderFields = new HashMap<String, SolrQuery.ORDER>();
        } else {
            setOrderFields(map);
        }
        return this.getOrderFields();
    }

    /**
     * 想排序字段集合中加入信息排序字段
     * @param key  要排序的字段
     * @param value ORDER类的实例
     */
    public Map<String, SolrQuery.ORDER> addOrderField(String key, SolrQuery.ORDER value) {
        if( null!=key && value!=null ) {
            Map<String, SolrQuery.ORDER> map = buildOrderMap(null);
            map.put(key,value);
            setOrderFields(map);
            return map;
        } else {
            return  this.getOrderFields();
        }
    }

    /**
     * 构建区间查询类容
     * @param upLine  上限
     * @param downLine  下限
     * @return
     */
    public String buildRangeQueryContent(Object upLine,Object downLine) {
        String result = null;
        if( null != upLine && null != downLine ) {
            StringBuilder builder = new StringBuilder();
            builder.append("[ ");
            builder.append(downLine);
            builder.append(" TO ");
            builder.append(upLine);
            builder.append(" ]");
            return builder.toString();
        }
        return result;
    }

    /**
     * 构建条件过滤集合 ，key为条件字段，value为条件对应的值
     * @param map
     * @return
     */
    public Map<String,Object> buildFilterFields(Map<String,Object> map) {
        if( null == map ) {
            map = new HashMap<String,Object>();
        }
        setFilterFields(map);
        return this.getFilterFields();
    }

    /**
     * 添加条件过滤字段
     * @param fieldName  字段名
     * @param value  值
     * @return
     */
    public Map<String,Object> addFilterField(String fieldName,Object value) {
        if( null != fieldName && null != value ) {
            Map<String,Object> map = clientInstance.getFilterFields();
            if( null == map ) {
                map = new HashMap<String,Object>();
            }
            map.put(fieldName,value);
            setFilterFields(map);
        }
        return this.getFilterFields();
    }

    /**
     * 根据查询获取结果
     * @param query
     * @return
     */
    public QueryResponse getResponseByQuery(SolrQuery query) {
        QueryResponse response = null;
        if( null != query ){
            try {
                CommonsHttpSolrServer server = this.getCommonsHttpSolrServer();
                response = server.query(query);
            } catch (SolrServerException e) {
                LOG.error("query error " + e.getMessage() , e);
            }
        }
        return response;
    }


    /**
     * 获取SolorServer对象
     * @return
     */
    public CommonsHttpSolrServer getCommonsHttpSolrServer(){
        CommonsHttpSolrServer solrServer = null;
        try {
            solrServer = new CommonsHttpSolrServer(clientInstance.serverAddress);
            solrServer.setSoTimeout(3000); // socket read timeout
            solrServer.setConnectionTimeout(1000);
            solrServer.setDefaultMaxConnectionsPerHost(1000);
            solrServer.setMaxTotalConnections(10);
            solrServer.setFollowRedirects(false); // defaults to false
            solrServer.setAllowCompression(true);
            solrServer.setMaxRetries(1);
            //提升性能采用流输出方式
            solrServer.setRequestWriter(new BinaryRequestWriter());
            return solrServer;
        } catch (MalformedURLException e) {
            LOG.error("CommonsHttpSolrServer init error");
        }
        return null;
    }


    /**
     * 构建Solr查询对象
     * @param queryPattern
     * @param queryFields  需要查询的字段
     * @param filterFields 查询条件字段
     * @param orderFields   排序字段map集合，key为要排序的字段名，值为Order类的实例
     * @param start  获取数据的起始位置
     * @param maxResult  获取数据的行数
     * @param isQueryAll 是否查询所有
     * @return
     */
    public SolrQuery buildQuery(String queryPattern, Set<String> queryFields, Map<String,Object> filterFields, Map<String, SolrQuery.ORDER> orderFields, int start,int maxResult,boolean isQueryAll) {

        String queryStr = null;
        // 构造返回对象
        SolrQuery query = new SolrQuery();
        StringBuilder builder = new StringBuilder();

        // 添加查询条件信息
        if(null!=filterFields && filterFields.size()>0) {
            for(Map.Entry<String,Object> entry:filterFields.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if(null!= key && null!=value) {
                    String str = key+":"+value.toString();
                    builder.append(str+" AND ");
                }
            }
        }

        if(builder.length()>0) {
            int index = builder.lastIndexOf("AND");
            if(index!=-1) {
                queryStr = builder.substring(0,index).trim();
            }
        }

        // 初始化查询对象
        query.setQuery((queryStr==null?"*":queryStr));

        // 添加查询字段
        if(null!= queryFields && queryFields.size()>0) {
            for(String queryField:queryFields) {
                query.addField(queryField);
            }
        }

        // 添加排序字段信息
        if(null!=orderFields && orderFields.size()>0) {
            for(Map.Entry<String, SolrQuery.ORDER> entry:orderFields.entrySet()) {
                String key = entry.getKey();
                SolrQuery.ORDER value = entry.getValue();
                if(null!=key && null!=value) {
                    query.addSortField(key,value);
                }
            }
        }

        // 设置获取数据的起始行数和获取记录的条数，如果没有设置则获取前20条记录
        if(!isQueryAll) {
            if(start<=0){
                start = 0;
            }
            if(maxResult<=0) {
                maxResult = 20;
            }
            query.setStart(start);
            query.setRows(maxResult);
        }
        return query;
    }



}
