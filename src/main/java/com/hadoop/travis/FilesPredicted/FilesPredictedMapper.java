package com.hadoop.travis.FilesPredicted;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/9 17:29]
 */
public class FilesPredictedMapper extends Mapper<LongWritable, Text, Text, Text> {
    private static Map<String, Double> classPriorProb = new HashMap<>();
    private static Map<String, Double> wordConditionProb = new HashMap<>();
    private Text k = new Text();
    private Text v = new Text();

    @Override
    protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        // <类名，该类中文档数量占所有四个类所有文档数量的比例>
        classPriorProb = GetProbability.getClassPriorProb();
        // <<类名+单词>，单词出现次数>
        wordConditionProb = GetProbability.getWordConditionalProb();
    }

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        String[] lineWords = value.toString().split("\\s");
        // 测试集处理之后的数据，result3
        String classNameTest = lineWords[0];
        String fileNameTest = lineWords[1];
        // 对于测试集的一个类中的一个文件
        // 循环四个类，分别获取四个概率值
        for (Map.Entry<String, Double> entry : classPriorProb.entrySet()) {
            String className = entry.getKey();
            k.set(classNameTest + "\t" + fileNameTest);
            double vTemp = Math.log(entry.getValue());
            for (int i = 2; i < lineWords.length; i++){
                String kTemp = className + "\t" + lineWords[i];
                if(wordConditionProb.containsKey(kTemp)){
                    vTemp += Math.log(wordConditionProb.get(kTemp));
                }else {
                    vTemp += Math.log(wordConditionProb.get(className));
                }
            }
            v.set(className + "\t" + vTemp);
            // <<测试集类名，测试集文件名>,<类名，属于该类的概率>>
            context.write(k, v);
        }
    }
}