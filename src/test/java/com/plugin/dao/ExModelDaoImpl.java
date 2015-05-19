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
package com.plugin.dao;

import com.plugin.ibatis.datasource.IbatisRoutingBaseDao;
import com.plugin.model.ExModel;

import java.util.List;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class ExModelDaoImpl extends IbatisRoutingBaseDao implements ExModelDao{

    @Override
    public void insert(ExModel exModel) {
        insert("ExModelDao.insert", exModel);
    }

    @Override
    public int delete(int id) {
       return delete("ExModelDao.delete", id);
    }

    @Override
    public int update(ExModel exModel) {
        return update("ExModelDao.update", exModel);
    }

    @Override
    public ExModel selectById(int id){
        return (ExModel)queryForObject("ExModelDao.selectById", id);
    }

    @Override
    public ExModel select(ExModel exModel) {
        List<ExModel> lists = selects(exModel);
        if(lists.isEmpty()){
            return null;
        }
        return lists.get(0);
    }

    @Override
    public List<ExModel> selects(ExModel exModel) {
        return (List<ExModel>)queryForList("ExModelDao.selects", exModel);
    }

    @Override
    public int selectCount(ExModel exModel) {
        return (Integer) queryForObject("ExModelDao.selectCount", exModel);
    }

}
