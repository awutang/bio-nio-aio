/**
 * Copyright 2014-2016 www.fangdd.com All Rights Reserved.
 * Author: Tang Yuqian <tangyuqian-sz@fangdd.com>
 * Date: 2018/6/22
 */
package com.utils;

import java.io.*;

public class FileSupport {

    public static void appendToFile(String filePath, String context) throws IOException {

        BufferedWriter bufferWritter = null;
        FileWriter fileWritter = new FileWriter(filePath, true);
        bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(context);
        bufferWritter.flush();
        bufferWritter.close();
    }

    public static boolean hasAnyContent(String filePath) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        if (reader.readLine() != null)
            return true;
        return false;
    }

    /*public static List<String> getFileData(String file) throws IOException {
        if (!(new File(file).exists())) {
            return new ArrayList<String>();
        }
        List<String> array = new ArrayList<>();
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if(!line.contains("| id |")) {
                array.add(line);
            }
        }

        try {
            if (reader != null)
                reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }*/

    public static void main(String[] args) throws IOException {

        String file = "D:\\工作相关文档\\QQ钱包安全险\\query_wechat_bill_23_20180622-1.csv";
        if (!(new File(file).exists())) {
            // return new ArrayList<String>();
        }
        // List<String> array = new ArrayList<>();
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        int i = 1;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arr = line.split(",");
            String policyId = arr[1];
            String idStr = arr[0];
            // long id = Long.parseLong(idStr);
            int policyDBNum = DBTableNumUtil.dbLocNum(policyId, TddlEnv.Prod_bill);
            int billTableNum = DBTableNumUtil.tabLocNum(policyId, TddlEnv.Prod_bill);
            String dbName = String.format("bill_%02d.bill_bill_%04d", policyDBNum, billTableNum);
            String sql = "UPDATE " + dbName + " SET pay_object = policy_id,gmt_modified = NOW(),MODIFIER = 'tangyuqian' WHERE id =" + idStr + " AND policy_id = '" + policyId + "' AND campaign_def_id = '1000490003' AND (package_def_id = '51272404' OR package_def_id = '50630002') AND  pay_object = 'null';";
            System.out.println("sql:" + sql + "i:" + i++);
            appendToFile("D:\\工作相关文档\\QQ钱包安全险\\20180621微信银行卡安全险bill修复pay_object字段-2.sql", sql + "\n\n");
            /*if(!line.contains("| id |")) {
                array.add(line);
            }*/
        }

        try {
            if (reader != null)
                reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return array;

    }

    /**
     * @param row Excel第几列作为唯一字段
     * @param filename 文件名
     * @return
     * @throws Exception
     */
//    public static boolean filterRepeatRecord(int row, String filename) throws Exception {
//        Set<String> set = new HashSet<String>();
//        List<String> newList = new ArrayList<String>();
//        List<String> oldList = PolicyIdData.getPolicyNo(filename);
//        for (int i = 0; i < oldList.size(); i++) {
//            String[] info = oldList.get(i).split("\t");
//            if (set.contains(info[row])) {
//                // System.out.println(policyInfoList.get(i));
//                continue;
//            }
//            set.add(info[row]);
//            newList.add(oldList.get(i));
//        }
//        System.out.println("oldList.size()=" + oldList.size());
//        System.out.println("newList.size()=" + newList.size());
//        System.out.println("set.size()=" + set.size());
//        if (newList.size() != oldList.size()) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//            File file = new File(filename);
//            if (!file.renameTo(new File(filename + sdf.format(new Date())))) {
//                System.err.println("重命名失败");
//                System.exit(1);
//            }
//            for (String s : newList) {
//                FileSupport.appendToFile(filename, s + "\r\n");
//            }
//            return false;
//        }
//        return true;
//    }
}

