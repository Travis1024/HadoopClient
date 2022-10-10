package com.hadoop.travis.Utils;

import org.apache.commons.collections.map.HashedMap;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/8 17:49]
 */
public class PartitionDataSet {
    public static ArrayList<String> getAllFileName(String path) {
        ArrayList<String> fileNameList = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                fileNameList.add(tempList[i].getName());
            }
        }
        return fileNameList;
    }

    public static void coreFunc(String trainPath, String testPath, ArrayList<String> fileNameList, int maxSize) {
        int num = 0;
        Random random = new Random();
        Map<String, Integer> map = new HashMap<String, Integer>();
        while (num < maxSize){
            int temp = random.nextInt(fileNameList.size());
            if(!map.containsKey(fileNameList.get(temp))){
                map.put(fileNameList.get(temp), 1);
                num++;
                //做移动操作
                moveFile(trainPath, testPath, fileNameList.get(temp));
            }
        }
    }

    private static void moveFile(String trainPath, String testPath, String s) {
        File file = new File(trainPath + "\\" + s);
        file.renameTo(new File(testPath + "\\" +s));
    }


    public static void main(String[] args) {
        String trainPath = "E:\\ShareFiles\\CourseMapReduce\\IndustryData\\train\\I81402";
        String testPath = "E:\\ShareFiles\\CourseMapReduce\\IndustryData\\test\\I81402";
        ArrayList<String> fileNameList = getAllFileName(trainPath);
        coreFunc(trainPath, testPath, fileNameList, (int) (fileNameList.size() * 0.3));
    }
}
