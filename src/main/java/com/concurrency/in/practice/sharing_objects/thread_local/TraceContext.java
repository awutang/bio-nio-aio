/**
 * Copyright 2014-2016 www.fangdd.com All Rights Reserved.
 * Author: Tang Yuqian <tangyuqian-sz@fangdd.com>
 * Date: 2018/7/30
 */
package com.concurrency.in.practice.sharing_objects.thread_local;

public class TraceContext {

    /**
     * qq用户openid
     */
    private String openid;

    /**
     * 公众号标识
     */
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
