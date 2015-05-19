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

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.event.RowHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientOperations;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import com.plugin.ibatis.datasource.RoutingDataSource.DataSourceContextHolder;
import java.util.List;
import java.util.Map;

/**
 * description: Ibatis持久层操作时根据类型注入动态数据源
 * @author dennisit@163.com
 * @version 1.0
 */
public class IbatisRoutingBaseDao extends SqlMapClientTemplate implements SqlMapClientOperations {

    protected final static Logger LOG = LoggerFactory.getLogger(IbatisRoutingBaseDao.class);

    @Override
    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }


    @Override
    public Object execute(SqlMapClientCallback action) throws DataAccessException {
        if( null == DataSourceContextHolder.getDBType() ){
            logger.error("sqlRunner not set db type context, use default config!!!");
        }
        Object result =  super.execute(action);
        DataSourceContextHolder.clearType();
        return result;
    }

    @Override
    public Object queryForObject(String statementName) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        return super.queryForObject(statementName);
    }

    @Override
    public Object queryForObject(String statementName, Object parameterObject) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        return super.queryForObject(statementName, parameterObject);
    }

    @Override
    public Object queryForObject(String statementName, Object parameterObject, Object resultObject) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        return super.queryForObject(statementName, parameterObject, resultObject);
    }

    @Override
    public List queryForList(String statementName) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        return super.queryForList(statementName);
    }

    @Override
    public List queryForList(String statementName, Object parameterObject) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        return super.queryForList(statementName, parameterObject);
    }

    @Override
    public List queryForList(String statementName, int skipResults, int maxResults) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        return super.queryForList(statementName, skipResults, maxResults);
    }

    @Override
    public List queryForList(String statementName, Object parameterObject, int skipResults, int maxResults) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        return super.queryForList(statementName, parameterObject, skipResults, maxResults);
    }

    @Override
    public void queryWithRowHandler(String statementName, RowHandler rowHandler) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        super.queryWithRowHandler(statementName, rowHandler);
    }

    @Override
    public void queryWithRowHandler(String statementName, Object parameterObject, RowHandler rowHandler) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        super.queryWithRowHandler(statementName, parameterObject, rowHandler);
    }


    @Override
    public Map queryForMap(String statementName, Object parameterObject, String keyProperty) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        return super.queryForMap(statementName, parameterObject, keyProperty);
    }

    @Override
    public Map queryForMap(String statementName, Object parameterObject, String keyProperty, String valueProperty) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.READ);
        return super.queryForMap(statementName, parameterObject, keyProperty, valueProperty);
    }

    @Override
    public Object insert(String statementName) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.WRITE);
        return super.insert(statementName);
    }

    @Override
    public Object insert(String statementName, Object parameterObject) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.WRITE);
        return super.insert(statementName, parameterObject);
    }

    @Override
    public int update(String statementName) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.WRITE);
        return super.update(statementName);
    }

    @Override
    public int update(String statementName, Object parameterObject) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.WRITE);
        return super.update(statementName, parameterObject);
    }

    @Override
    public void update(String statementName, Object parameterObject, int requiredRowsAffected) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.WRITE);
        super.update(statementName, parameterObject, requiredRowsAffected);
    }

    @Override
    public int delete(String statementName) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.WRITE);
        return super.delete(statementName);
    }

    @Override
    public int delete(String statementName, Object parameterObject) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.WRITE);
        return super.delete(statementName, parameterObject);
    }

    @Override
    public void delete(String statementName, Object parameterObject, int requiredRowsAffected) throws DataAccessException {
        DataSourceContextHolder.setCustomerType(DataSourceContextHolder.WRITE);
        super.delete(statementName, parameterObject, requiredRowsAffected);
    }


}
