package com.hadoop.travis.WordsCount;

import com.hadoop.travis.FilesCount.FilesCountDriver;
import com.hadoop.travis.FilesCount.FilesCountMapper;
import com.hadoop.travis.FilesCount.FilesCountReducer;
import com.hadoop.travis.FilesCount.WholeFileInputFormat;
import com.hadoop.travis.Utils.ParamsClass;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
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
 * @createTime : [2022/10/8 20:17]
 */
public class WordsCountDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 获取配置信息以及获取job对象
        Configuration configuration = new Configuration();
        FileSystem hdfs = FileSystem.get(configuration);
        Job job2 = Job.getInstance(configuration, "WordsCount");
        // 检查输出路径
        Path resultPath2 = new Path(ParamsClass.resultPath2);
        if (hdfs.exists(resultPath2)){
            hdfs.delete(resultPath2,true);
        }
        // 关联本Driver程序的jar
        job2.setJarByClass(WordsCountDriver.class);
        // 关联Mapper和Reducer的jar
        job2.setMapperClass(WordsCountMapper.class);
        job2.setReducerClass(WordsCountReducer.class);
        // 配置输入
        FileInputFormat.setInputDirRecursive(job2, true);

        // 设置Mapper输出的kv类型
        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(IntWritable.class);
        // 设置Reducer输入
        job2.setCombinerClass(WordsCountReducer.class);
        // 设置最终输出kv类型
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(IntWritable.class);
        // 设置输入和输出路径
        FileInputFormat.addInputPath(job2, new Path(ParamsClass.inputPath1));
        FileOutputFormat.setOutputPath(job2, new Path(ParamsClass.resultPath2));
        // 提交job2
        boolean result = job2.waitForCompletion(true);
        System.exit(result ? 0 :1);
    }
}