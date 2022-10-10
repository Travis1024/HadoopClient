package com.hadoop.travis.WordsCount;

import org.apache.hadoop.io.IntWritable;
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
 * @createTime : [2022/10/8 20:17]
 */
public class WordsCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private Text k = new Text();
    private IntWritable oneNumber = new IntWritable(1);
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        String line = value.toString();
        InputSplit inputSplit = context.getInputSplit();
        String className = ((FileSplit) inputSplit).getPath().getParent().getName();
        k.set(className + '\t' + line);
        context.write(k, oneNumber);
    }
}