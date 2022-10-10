package com.hadoop.travis.DealTestData;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/9 11:34]
 */
public class DealTestDateMapper extends Mapper<LongWritable, Text, Text, Text> {
    private Text k = new Text();
    private Text v = new Text();
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        InputSplit inputSplit = context.getInputSplit();
        String className = ((FileSplit) inputSplit).getPath().getParent().getName();
        String fileName = ((FileSplit) inputSplit).getPath().getName();
        k.set(className + '\t' + fileName);
        v.set(value.toString());
        context.write(k, v);
    }
    
}