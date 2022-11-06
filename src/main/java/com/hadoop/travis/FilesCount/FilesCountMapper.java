package com.hadoop.travis.FilesCount;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/7 20:39]
 */
public class FilesCountMapper extends Mapper<Text, BytesWritable, Text, IntWritable> {
    private IntWritable oneNumber = new IntWritable(1);

    @Override
    protected void map(Text key, BytesWritable value, Mapper<Text, BytesWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        // Map的输出设定为  <类别名，1>
        context.write(key, oneNumber);
    }
}