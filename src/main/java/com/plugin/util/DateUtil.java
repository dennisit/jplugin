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

import clojure.main;

import java.util.Calendar;
import java.util.Date;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class DateUtil {

    public static void main(String [] args){
        System.out.println(DateUtil.getNowTime());
    }

    /**
     * 获取当前时间的字符串【单数前加0】
     *
     */
    public static String getNowTime() {
        Calendar calendar = Calendar.getInstance();
        int tmpdate = calendar.get(Calendar.DATE);
        int tmpmonth = calendar.get(Calendar.MONTH) + 1;
        int tmpyear = calendar.get(Calendar.YEAR);
        StringBuffer strdate = new StringBuffer("");
        strdate.append("" + tmpyear);
        if (tmpmonth < 10) {
            strdate.append("-");
            strdate.append("0" + tmpmonth);
        } else {
            strdate.append("-");
            strdate.append("" + tmpmonth);
        }
        if (tmpdate < 10) {
            strdate.append("-");
            strdate.append("0" + tmpdate);
        } else {
            strdate.append("-");
            strdate.append("" + tmpdate);
        }
        return strdate.toString().trim();
    }

    /**
     * 计算两个时期之间相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDistDates(Date startDate, Date endDate) {
        long totalDate = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        long timestart = calendar.getTimeInMillis();
        calendar.setTime(endDate);
        long timeend = calendar.getTimeInMillis();
        totalDate = Math.abs((timeend - timestart)) / (1000 * 60 * 60 * 24);
        return totalDate;
    }

    /**
     * 将星期数字装换为字符
     *
     * @return
     */
    private static String NumToWeek(int num) {
        if (num == 7)
            return "六";
        if (num == 1)
            return "日";
        if (num == 2)
            return "一";
        if (num == 3)
            return "二";
        if (num == 4)
            return "三";
        if (num == 5)
            return "四";
        if (num == 6)
            return "五";
        return "";
    }

}
