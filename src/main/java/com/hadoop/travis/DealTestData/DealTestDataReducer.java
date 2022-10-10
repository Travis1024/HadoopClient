package com.hadoop.travis.DealTestData;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/9 11:34]
 */
public class DealTestDataReducer extends Reducer<Text, Text, Text, Text> {
    private Text result = new Text();
    private StringBuffer stringBuffer;
    @Override
    protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        stringBuffer = new StringBuffer();
        for (Text value : values) {
            stringBuffer = stringBuffer.append(value.toString() + " ");
        }
        result.set(stringBuffer.toString());
        context.write(key, result);
    }
}