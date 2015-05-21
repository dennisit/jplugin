/*
 * Copyright (c) 2015, www.jd.com. All rights reserved.
 *
 * 警告：本计算机程序受著作权法和国际公约的保护，未经授权擅自复制或散布本程序的部分或全部、以及其他
 * 任何侵害著作权人权益的行为，将承受严厉的民事和刑事处罚，对已知的违反者将给予法律范围内的全面制裁。
 *
 */
package com.plugin.hash;

/**
 * Description:
 *
 * @author pudongping
 * @version 1.0.1
 */
/*
 * =========================== 维护日志 ===========================
 * 2015-04-16 17:49  pudongping 新建代码 
 * =========================== 维护日志 ===========================
 */
public abstract class ShardingInfo<T> {

    private int weight;

    public ShardingInfo() {
    }

    public ShardingInfo(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }

    protected abstract T createResource();

    public abstract String getName();
}