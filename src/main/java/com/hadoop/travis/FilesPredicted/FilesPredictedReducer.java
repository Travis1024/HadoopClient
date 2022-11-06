package com.hadoop.travis.FilesPredicted;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/9 17:30]
 */
public class FilesPredictedReducer extends Reducer<Text, Text, Text, Text> {
    private Text v = new Text();
    @Override
    protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        // 存放最大概率对应的类别名
        String classFromMaxProb = null;
        // 存放最大类别
        double MaxProb = 0.0;
        // 标记是否被处理过
        boolean flag = false;
        for (Text value : values) {
            String[] valueResult = value.toString().split("\\s");
            if(flag){
                // 被处理过，与之前的最大概率值进行比较，判断是否需要更改最大概率值与类别名
                if(Double.parseDouble(valueResult[1]) > MaxProb){
                    classFromMaxProb = valueResult[0];
                    MaxProb = Double.parseDouble(valueResult[1]);
                }
            }else {
                // 没有被处理过，标记为已被处理过
                flag = true;
                // 因为没有被处理过，无需进行概率值比较，直接赋值
                classFromMaxProb = valueResult[0];
                MaxProb = Double.parseDouble(valueResult[1]);
            }
        }
        v.set(classFromMaxProb + "\t" + MaxProb);
        context.write(key, v);
    }
}