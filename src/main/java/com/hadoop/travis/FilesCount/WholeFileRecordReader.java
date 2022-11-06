package com.hadoop.travis.FilesCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/8 16:03]
 */
public class WholeFileRecordReader extends RecordReader<Text, BytesWritable> {

    // 保存输入的分片，将被转换成一条（key，value）记录
    private FileSplit fileSplit;
    // 配置对象
    private Configuration configuration;
    private Text key = new Text();
    // value对象，内容为空
    private BytesWritable value = new BytesWritable();
    // bool变量记录记录是否被处理过
    private boolean processed = false;

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        // 重新实现RecordReader的初始化方法，inputSplit为输入分片，taskAttemptContext为任务的上下文
        // 将输入分片强制转换成FileSplit
        this.fileSplit = (FileSplit) inputSplit;
        // 从taskAttemptContext获取配置信息
        this.configuration = taskAttemptContext.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        // 如果记录没有被处理过
        if (!processed) {
            // 从fileSplit对象获取split的字节数，创建byte数组contents
            byte[] contents = new byte[(int) fileSplit.getLength()];
            // 从fileSplit对象获取输入文件路径
            Path file = fileSplit.getPath();
            // 获取文件系统对象
            FileSystem fs = file.getFileSystem(configuration);
            // 定义文件输入流对象
            FSDataInputStream in = null;
            try {
                // 打开文件，返回文件输入流对象
                in = fs.open(file);
                // 从输入流读取所有字节到contents
                IOUtils.readFully(in, contents, 0, contents.length);
                // 将contents内容设置到value对象中
                value.set(contents, 0, contents.length);
                String className = fileSplit.getPath().getParent().getName();
                key.set(className);
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                // 关闭输入流
                IOUtils.closeStream(in);
            }
            // 将是否处理标志设为true，下次调用该方法会返回false
            processed = true;
            return true;
        }
        // 如果记录被处理过，返回false，表示split处理完毕
        return false;
    }

    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return processed ? 1.0f : 0.0f;
    }

    @Override
    public void close() throws IOException {

    }
}