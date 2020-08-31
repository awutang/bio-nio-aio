/**
 * Copyright 2014-2016 www.fangdd.com All Rights Reserved.
 * Author: Tang Yuqian <tangyuqian-sz@fangdd.com>
 * Date: 2018/6/22
 */
package com.utils;

public class DBTableNumUtil {
    public static int dbLocNum(long policy_id, TddlEnv env) {
        return tabLocNum(policy_id, env) / (env.getTabNum() / env.getDbNum());
    }

    public static int tabLocNum(long policy_id, TddlEnv env) {
//		return Math.abs(String.valueOf(policy_id).hashCode()) % env.getTabNum();
        return (int) (policy_id % env.getTabNum());
    }

    public static int dbLocNum(String policy_id, TddlEnv env) {
        return tabLocNum(policy_id, env) / (env.getTabNum() / env.getDbNum());
    }

    public static int tabLocNum(String policy_id, TddlEnv env) {
        return Math.abs(String.valueOf(policy_id).hashCode()) % env.getTabNum();
//        return (int) (policy_id % env.getTabNum());
//        return (int) (Long.parseLong(policy_id) % env.getTabNum());
    }

    public static void main(String[] args) {
        String policyId = "50741832082307367";
        int policyDBNum = DBTableNumUtil.dbLocNum(policyId, TddlEnv.ITest);
        int billTableNum = DBTableNumUtil.tabLocNum(policyId, TddlEnv.ITest);
        System.out.println(String.format("policy_%02d.policy_%04d", policyDBNum, billTableNum));
    }
}
