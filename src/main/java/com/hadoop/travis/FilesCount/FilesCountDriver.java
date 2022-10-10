package com.hadoop.travis.FilesCount;

import com.hadoop.travis.Utils.ParamsClass;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/7 20:39]
 */
public class FilesCountDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 获取配置信息以及获取job对象
        Configuration configuration = new Configuration();
        FileSystem hdfs = FileSystem.get(configuration);
        Job job1 = Job.getInstance(configuration, "FilesCount");
        // 检查输出路径
        Path resultPath1 = new Path(ParamsClass.resultPath1);
        if (hdfs.exists(resultPath1)){
            hdfs.delete(resultPath1,true);
        }
        // 关联本Driver程序的jar
        job1.setJarByClass(FilesCountDriver.class);
        // 关联Mapper和Reducer的jar
        job1.setMapperClass(FilesCountMapper.class);
        job1.setReducerClass(FilesCountReducer.class);
        // 配置输入
        FileInputFormat.setInputDirRecursive(job1, true);
        // 设置Mapper输入的类型
        job1.setInputFormatClass(WholeFileInputFormat.class);
        // 设置Mapper输出的kv类型
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(IntWritable.class);
        // 设置Reducer输入
        job1.setCombinerClass(FilesCountReducer.class);
        // 设置最终输出kv类型
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);
        // 设置输入和输出路径
        FileInputFormat.addInputPath(job1, new Path(ParamsClass.inputPath1));
        FileOutputFormat.setOutputPath(job1, new Path(ParamsClass.resultPath1));
        // 提交job1
        boolean result = job1.waitForCompletion(true);
        System.exit(result ? 0 :1);

    }
}