package com.hadoop.travis.FilesPredicted;

import com.hadoop.travis.Utils.ParamsClass;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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
 * @createTime : [2022/10/9 17:30]
 */
public class FilesPredictedDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 获取配置信息以及获取job对象
        Configuration configuration = new Configuration();
        FileSystem hdfs = FileSystem.get(configuration);
        Job job4 = Job.getInstance(configuration, "FilesPredicted");
        // 检查输出路径
        Path resultPath4 = new Path(ParamsClass.resultPath4);
        if (hdfs.exists(resultPath4)){
            hdfs.delete(resultPath4,true);
        }
        // 关联本Driver程序的jar
        job4.setJarByClass(FilesPredictedDriver.class);
        // 关联Mapper和Reducer的jar
        job4.setMapperClass(FilesPredictedMapper.class);
        job4.setReducerClass(FilesPredictedReducer.class);
        // 配置输入
        FileInputFormat.setInputDirRecursive(job4, true);

        // 设置Mapper输出的kv类型
        job4.setMapOutputKeyClass(Text.class);
        job4.setMapOutputValueClass(Text.class);
        // 设置Reducer输入
        job4.setCombinerClass(FilesPredictedReducer.class);
        // 设置最终输出kv类型
        job4.setOutputKeyClass(Text.class);
        job4.setOutputValueClass(Text.class);
        // 设置输入和输出路径
        FileInputFormat.addInputPath(job4, new Path(ParamsClass.resultPath3));
        FileOutputFormat.setOutputPath(job4, new Path(ParamsClass.resultPath4));
        // 提交job4
        boolean result = job4.waitForCompletion(true);
        System.exit(result ? 0 :1);
    }
}