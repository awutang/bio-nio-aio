/**
 * Copyright 2014-2016 www.fangdd.com All Rights Reserved.
 * Author: Tang Yuqian <tangyuqian-sz@fangdd.com>
 * Date: 2018/6/22
 */
package com.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FileSupport {

    private static Map<String, String> provinceCodeMap = new HashMap<>();

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

    public static void process(String[] args) throws IOException {

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

    public static void withdrawDistribution(String path, String outPath) {
        Map<String, BigDecimal> certToAmount = new HashMap<>();
        Map<String, BigDecimal> addressToAmount = new HashMap<>();

        File file = new File(path);
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        int index = 1;
        String lineTxt = null;
        try {
            read = new InputStreamReader(new FileInputStream(file));
            bufferedReader = new BufferedReader(read);

            // 第一行略过
            bufferedReader.readLine();
            while (StringUtils.isNotBlank(lineTxt = bufferedReader.readLine())) {
                try {
                    String[] strArr = lineTxt.split(",");
                    BigDecimal withdrawAmount = new BigDecimal(strArr[0].trim());
                    String certNo = strArr[1].trim();
                    String address = strArr[2].trim();

                    // cert
                    String certKey = certNo.substring(0,2);
                    String provinceKey = provinceCodeMap.get(certKey);
                    if (certToAmount.containsKey(provinceKey)) {
                        certToAmount.put(provinceKey, certToAmount.get(provinceKey).add(withdrawAmount));
                    } else {
                        certToAmount.put(provinceKey, withdrawAmount);
                    }

                    // address
                    String addressKey = address.replace("\"","").substring(0,2);
                    if (addressToAmount.containsKey(addressKey)) {
                        addressToAmount.put(addressKey, addressToAmount.get(addressKey).add(withdrawAmount));
                    } else {
                        addressToAmount.put(addressKey, withdrawAmount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("index:"+index+", lineTxt:"+lineTxt);
                }
                index++;
                System.out.println("index:"+index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("index:"+index+", lineTxt:"+lineTxt);
        } finally {
            BufferedWriter bufferedWriter = null;
            try {
                File outFile = new File(outPath);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outFile));
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                for (Map.Entry<String, BigDecimal> entry :certToAmount.entrySet()) {
                    bufferedWriter.write(entry.getKey()+","+entry.getValue());
                    bufferedWriter.newLine();
                }

                for (Map.Entry<String, BigDecimal> entry :addressToAmount.entrySet()) {
                    bufferedWriter.write(entry.getKey()+","+entry.getValue());
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();

                System.out.println(certToAmount.toString());

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void processProvinceCode() {
        String str = "11 北京市 ,12 天津市 ,13 河北省 ,14 山西省 ,15 内蒙古自治区 ,21 辽宁省 ,22 吉林省 ,23 黑龙江省 ,31 上海市 ,32 江苏省 ,33 浙江省 ,34 安徽省 ,35 福建省 ,36 江西省 ,37 山东省 ,41 河南省 ,42 湖北省 ,43 湖南省 ,44 广东省 ,45 广西壮族自治区 ,46 海南省 ,50 重庆市 ,51 四川省 ,52 贵州省 ,53 云南省 ,54 西藏自治区 ,61 陕西省 ,62 甘肃省 ,63 青海省 ,64 宁夏回族自治区 ,65 新疆维吾尔自治区 ,71 台湾省 ,81 香港特别行政区 ,82 澳门特别行政区";
        String[] strArr = str.split(",");
        for (String item : strArr) {
            String[] itemArr = item.trim().split(" ");
            provinceCodeMap.put(itemArr[0].trim(), itemArr[1].trim());
        }
    }

    public static void main(String[] args) {
        processProvinceCode();

        String path = "/Users/ayutang/Downloads/20210223171937__finance_odps_dev_xiaoying20210223_4.csv";
        String outPath = "/Users/ayutang/Downloads/20210223171937__finance_odps_dev_xiaoying20210223_4_1.csv";

        withdrawDistribution(path, outPath);
    }




}

