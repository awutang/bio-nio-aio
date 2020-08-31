/**
 * Copyright 2014-2016 www.fangdd.com All Rights Reserved.
 * Author: Tang Yuqian <tangyuqian-sz@fangdd.com>
 * Date: 2018/6/22
 */
package com.utils;

public enum TddlEnv {
    ITest(2, 4),
    Prod(16, 1024),
    Prod_bill(8, 1024),
    Prod_bill_balance_bill(4, 4),
    Prod_bill_commission(8, 8),
    Prod_claim_reserve(4, 512),
    Prod_claim_settlement(4, 512);

    public int getDbNum() {
        return dbNum;
    }

    public void setDbNum(int dbNum) {
        this.dbNum = dbNum;
    }

    public int getTabNum() {
        return tabNum;
    }

    public void setTabNum(int tabNum) {
        this.tabNum = tabNum;
    }

    int dbNum;
    int tabNum;

    TddlEnv(int dbNum, int tabNum) {
        this.dbNum = dbNum;
        this.tabNum = tabNum;
    }
}
