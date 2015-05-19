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
package com.plugin.storm.dm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class SimpleSpout extends BaseRichSpout {

    /**
     * 用来发射数据的工具类
     */
    private SpoutOutputCollector collector;


    private static String[] info = new String[]{
            "comaple\t,12424,44w46,654,12424,44w46,654,",
            "lisi\t,435435,6537,12424,44w46,654,",
            "lipeng\t,45735,6757,12424,44w46,654,",
            "hujintao\t,45735,6757,12424,44w46,654,",
            "jiangmin\t,23545,6457,2455,7576,qr44453",
            "beijing\t,435435,6537,12424,44w46,654,",
            "xiaoming\t,46654,8579,w3675,85877,077998,",
            "xiaozhang\t,9789,788,97978,656,345235,09889,",
            "ceo\t,46654,8579,w3675,85877,077998,",
            "cto\t,46654,8579,w3675,85877,077998,",
            "zhansan\t,46654,8579,w3675,85877,077998,"};

    Random rd = new Random();

    /**
     * 这里初始化collector
     * @param conf
     * @param context
     * @param collector
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;

    }

    /**
     * 该方法会在SpoutTracker类中被调用每调用一次就可以向storm集群中发射一条数据（一个tuple元组）
     * 该方法会被不停的调用
     */
    @Override
    public void nextTuple() {
        try {
            String msg = info[rd.nextInt(10)];
            //调用发射方法
            collector.emit(new Values(msg));
            //模拟等待100ms
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这里定义字段id，该id在简单模式下没有用处，但在按照字段分组的模式下有很大的用处。
     * 该declarer变量有很大作用，我们还可以调用  declarer.declareStream();  来定义stramId，该id可以用来定义
     * 更加复杂的流拓扑结构
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("source"));

    }
}
