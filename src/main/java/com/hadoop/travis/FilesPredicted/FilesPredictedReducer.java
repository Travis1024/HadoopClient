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
        String classFromMaxProb = null;
        double MaxProb = 0.0;
        boolean flag = false;
        for (Text value : values) {
            String[] valueResult = value.toString().split("\\s");
            if(flag){
                if(Double.parseDouble(valueResult[1]) > MaxProb){
                    classFromMaxProb = valueResult[0];
                    MaxProb = Double.parseDouble(valueResult[1]);
                }
            }else {
                flag = true;
                classFromMaxProb = valueResult[0];
                MaxProb = Double.parseDouble(valueResult[1]);
            }
        }
        v.set(classFromMaxProb + "\t" + MaxProb);
        context.write(key, v);
    }
}