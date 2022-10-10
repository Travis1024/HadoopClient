package com.hadoop.travis.FilesPredicted;

import com.hadoop.travis.Utils.ParamsClass;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/9 17:32]
 */
public class GetProbability {
    private static Map<String, Double> classPriorProb = new HashMap<>();
    private static Map<String, Double> wordConditionalProb = new HashMap<>();

    public static Map<String, Double> getClassPriorProb() throws IOException {
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(ParamsClass.resultPath1 + "part-r-00000"), configuration);
        FSDataInputStream fsDataInputStream = fs.open(new Path( ParamsClass.resultPath1 + "/part-r-00000"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fsDataInputStream));
        String line = null;
        // <类名，文档数量>
        Map<String, Integer> tempResult = new HashMap<>();
        int totalFilesNum = 0;
        while ((line = bufferedReader.readLine()) != null) {
            String[] words = line.split("\\s");
            for (String word : words) {
                word.trim();
            }
            String className = words[0];
            int filesNum = Integer.parseInt(words[1]);
            tempResult.put(className, filesNum);
            totalFilesNum += filesNum;
        }
        bufferedReader.close();
        for (Map.Entry<String, Integer> entry : tempResult.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue() * 1.0 / totalFilesNum;
            classPriorProb.put(key, value);
        }

        // <类名，该类中文档数量占所有四个类所有文档数量的比例>
        return classPriorProb;
    }

    public static Map<String, Double> getWordConditionalProb() throws IOException {
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(ParamsClass.resultPath2 + "part-r-00000"), configuration);
        FSDataInputStream fsDataInputStream = fs.open(new Path(ParamsClass.resultPath2 + "/part-r-00000"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fsDataInputStream));
        String line = null;
        // 训练集中<类名， 单词总数>
        Map<String, Integer> wordsSum = new HashMap<>();

        // 1、统计训练集四个类中所包含的单词总数
        while ((line = bufferedReader.readLine()) != null){
            String[] words = line.split("\\s");
            for (String word : words) {
                word.trim();
            }
            String className = words[0];
            String word = words[1];
            int wordNum = Integer.parseInt(words[2]);
            if (wordsSum.containsKey(className)) {
                wordsSum.put(className, wordsSum.get(className) + wordNum);
            }else {
                wordsSum.put(className, wordNum);
            }
        }
        bufferedReader = null;
        fsDataInputStream = null;

        // 2、计算条件概率
        fs = FileSystem.get(URI.create(ParamsClass.resultPath2 + "part-r-00000"), configuration);
        fsDataInputStream = fs.open(new Path(ParamsClass.resultPath2 + "/part-r-00000"));
        bufferedReader = new BufferedReader(new InputStreamReader(fsDataInputStream));
        while((line = bufferedReader.readLine()) != null){
            String[] words = line.split("\\s");
            for (String word : words) {
                word.trim();
            }
            String className = words[0];
            String word = words[1];
            int wordNum = Integer.parseInt(words[2]);
            String key = className + "\t" + word;
            double value = wordNum * 1.0 / wordsSum.get(className);
            // <<类名+单词>， 出现次数>
            wordConditionalProb.put(key, value);
        }

        // 3、定义四个类中出现新单词的概率，即1/类单词总数
        for (Map.Entry<String, Integer> entry : wordsSum.entrySet()) {
            String className = entry.getKey();
            double value = 1.0 / entry.getValue();
            wordConditionalProb.put(className, value);
        }
        bufferedReader.close();
        fsDataInputStream.close();
        // <<类名+单词>，单词出现次数>
        return wordConditionalProb;
    }

}