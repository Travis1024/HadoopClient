package com.hadoop.travis.DealTestData;


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
 * @createTime : [2022/10/9 11:34]
 */
public class DealTestDataDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 获取配置信息以及获取job对象
        Configuration configuration = new Configuration();
        FileSystem hdfs = FileSystem.get(configuration);
        Job job3 = Job.getInstance(configuration, "DealTestData");
        // 检查输出路径
        Path resultPath3 = new Path(ParamsClass.resultPath3);
        if (hdfs.exists(resultPath3)){
            hdfs.delete(resultPath3,true);
        }
        // 关联本Driver程序的jar
        job3.setJarByClass(DealTestDataDriver.class);
        // 关联Mapper和Reducer的jar
        job3.setMapperClass(DealTestDateMapper.class);
        job3.setReducerClass(DealTestDataReducer.class);
        // 配置输入
        FileInputFormat.setInputDirRecursive(job3, true);

        // 设置Mapper输出的kv类型
        job3.setMapOutputKeyClass(Text.class);
        job3.setMapOutputValueClass(Text.class);
        // 设置Reducer输入
        job3.setCombinerClass(DealTestDataReducer.class);
        // 设置最终输出kv类型
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(Text.class);
        // 设置输入和输出路径
        FileInputFormat.addInputPath(job3, new Path(ParamsClass.inputPath2));
        FileOutputFormat.setOutputPath(job3, new Path(ParamsClass.resultPath3));
        // 提交job3
        boolean result = job3.waitForCompletion(true);
        System.exit(result ? 0 :1);
    }
}