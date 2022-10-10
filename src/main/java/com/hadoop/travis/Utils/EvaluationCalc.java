package com.hadoop.travis.Utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * description: ()
 *
 * @author : [travis-wei]
 * @version : [v1.0]
 * @createTime : [2022/10/10 09:24]
 */
public class EvaluationCalc {
    private static final String[] classList = {"I13000", "I33020", "I75000", "I81402"};
    private static int[] FP = new int[4];
    private static int[] TP = new int[4];
    private static int[] FN = new int[4];
    private static int[] TN = new int[4];
    private static int totalNumber = 0;
    private static int[] realClassNums = new int[4];
    private static int[] predictClassNums = new int[4];
    private static Map<String, Double> resultIndex = new HashMap<>();

    /**
     * 计算各个类别的FP、TP、FN、TN等指标
     * @author travis-wei
     * @createTime 2022/10/10
     * @param
     * @return void
     **/
    public static void dealPredictResult() throws IOException, URISyntaxException, InterruptedException {
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop02:8020"), configuration, "travis01");
        FSDataInputStream fsDataInputStream = fs.open(new Path(ParamsClass.resultPath4 + "/part-r-00000"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fsDataInputStream));
        String line = null;
        boolean flag = false;

        while ((line = bufferedReader.readLine()) != null){
            String[] results = line.split("\\s");
            for (String result : results) {
                result.trim();
            }
            String trueClass = results[0];
            String predictClass = results[2];
            flag = trueClass.equals(predictClass) ? true : false;
            if (flag){
                for (int i = 0; i < classList.length; i++){
                    if (trueClass.equals(classList[i])){
                        realClassNums[i]++;
                        predictClassNums[i]++;
                        TP[i]++;
                    }
                }
            }else {
                for (int i = 0; i < classList.length; i++){
                    if (trueClass.equals(classList[i])){
                        realClassNums[i]++;
                    }
                    if (predictClass.equals(classList[i])){
                        predictClassNums[i]++;
                    }
                }
            }
        }
        bufferedReader.close();
        // 所有文件数量
        for (int i = 0; i < classList.length; i++) {
            totalNumber += realClassNums[i];
        }
        // 间接计算FP、FN、TN
        for (int i = 0; i < classList.length; i++) {
            FP[i] = predictClassNums[i] - TP[i];
            FN[i] = realClassNums[i] - TP[i];
            TN[i] = totalNumber - realClassNums[i] - predictClassNums[i] + TP[i];
        }
    }

    /**
     * 计算微平均指标（microRecall、microPrecision、microF1）
     * @author travis-wei
     * @createTime 2022/10/10
     * @param
     * @return void
     **/
    public static void calcMicroIndex(){
        int top = 0, down1 = 0, down2 = 0;
        for (int i = 0; i < classList.length; i++) {
            top += TP[i];
            down1 += FN[i];
            down2 += FP[i];
        }
        double microRecall = top * 1.0 / (top + down1);
        double microPrecision = top * 1.0 / (top + down2);
        double microF1 = 2 * microRecall * microPrecision / (microPrecision + microRecall);
        resultIndex.put("microRecall", microRecall);
        resultIndex.put("microPrecision", microPrecision);
        resultIndex.put("microF1", microF1);
    }

    /**
     * 计算宏平均指标（macroRecall、macroPrecision、macroF1Score、macroF1）
     * @author travis-wei
     * @createTime 2022/10/10
     * @param
     * @return void
     **/
    public static void calcMacroIndex(){
        double top = 0.0;
        for (int i = 0; i < classList.length; i++) {
            resultIndex.put("macroRecall" + i, TP[i] * 1.0 / (TP[i] + FN[i]));
            resultIndex.put("macroPrecision" + i, TP[i] * 1.0 / (TP[i] + FP[i]));
            resultIndex.put("macroF1Score" + i, TP[i] * 2.0 / (2.0 * TP[i] + FP[i] + FN[i]));
            top += resultIndex.get("macroF1Score" + i);
        }
        resultIndex.put("macroF1", top / classList.length);
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        dealPredictResult();
        calcMicroIndex();
        calcMacroIndex();
        System.out.println("microRecall" + "\t\t-----> " + resultIndex.get("microRecall"));
        System.out.println("microPrecision" + "\t-----> " + resultIndex.get("microPrecision"));
        System.out.println("microF1" + "\t\t\t-----> " + resultIndex.get("microF1"));
        System.out.println("macroRecall0" + "\t-----> " + resultIndex.get("macroRecall0"));
        System.out.println("macroRecall1" + "\t-----> " + resultIndex.get("macroRecall1"));
        System.out.println("macroRecall2" + "\t-----> " + resultIndex.get("macroRecall2"));
        System.out.println("macroRecall3" + "\t-----> " + resultIndex.get("macroRecall3"));
        System.out.println("macroPrecision0" + "\t-----> " + resultIndex.get("macroPrecision0"));
        System.out.println("macroPrecision1" + "\t-----> " + resultIndex.get("macroPrecision1"));
        System.out.println("macroPrecision2" + "\t-----> " + resultIndex.get("macroPrecision2"));
        System.out.println("macroPrecision3" + "\t-----> " + resultIndex.get("macroPrecision3"));
        System.out.println("macroF1Score0" + "\t-----> " + resultIndex.get("macroF1Score0"));
        System.out.println("macroF1Score1" + "\t-----> " + resultIndex.get("macroF1Score1"));
        System.out.println("macroF1Score2" + "\t-----> " + resultIndex.get("macroF1Score2"));
        System.out.println("macroF1Score3" + "\t-----> " + resultIndex.get("macroF1Score3"));
        System.out.println("macroF1" + "\t\t\t-----> " + resultIndex.get("macroF1"));
    }
}
