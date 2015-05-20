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

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class ListUtil {

    /**
     * 分割List
     * @author huangweihong
     * @date 2013.11.13
     * @param list 待分割的list
     * @param pageSize 每段list的大小
     * @return List<<List<T>>
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {

        int listSize = list.size();                                          //list的大小
        int page = (listSize + (pageSize-1))/ pageSize;                      //页数

        List<List<T>> listArray = new ArrayList<List<T>>();                        //创建list数组 ,用来保存分割后的list
        for(int i=0;i<page;i++) {                                            //按照数组大小遍历
            List<T> subList = new ArrayList<T>();                            //数组每一位放入一个分割后的list
            for(int j=0;j<listSize;j++) {                                    //遍历待分割的list
                int pageIndex = ( (j + 1) + (pageSize-1) ) / pageSize;       //当前记录的页码(第几页)
                if(pageIndex == (i + 1)) {                                   //当前记录的页码等于要放入的页码时
                    subList.add(list.get(j));                                //放入list中的元素到分割后的list(subList)
                }

                if( (j + 1) == ((j + 1) * pageSize) ) {                      //当放满一页时退出当前循环
                    break;
                }
            }
            listArray.add(subList);                                          //将分割后的list放入对应的数组的位中
        }
        return listArray;
    }


}
