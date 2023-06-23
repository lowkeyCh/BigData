package com.example.Service;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Reducer;

public class CourseAvg {
    public static void main(String[] args) throws Exception {
        if (args.length<2) {
            System.out.printf("Usage:%s <input> <output>\n");
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "course Course");
        job.setJarByClass(CourseAvg.class);
        job.setMapperClass(CourseAvg.averageMapper.class);
        job.setReducerClass(CourseAvg.averageReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        Path inputPath = new Path(args[0]);
        Path outPutPath = new Path(args[1]);
        FileSystem fs = outPutPath.getFileSystem(conf);
        if (fs.exists(outPutPath)) fs.delete(outPutPath,true);
        FileInputFormat.setInputPaths(job,inputPath);
        FileOutputFormat.setOutputPath(job,outPutPath);
        boolean waitForCompletion = job.waitForCompletion(true);
        // System.exit(waitForCompletion ? 0 : 1);
        System.out.println(job.waitForCompletion(true)?1:0);

    }
    private static class averageMapper extends Mapper<LongWritable, Text, Text, Text> {
        List<String> courseNames = new ArrayList<>();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split("\\s");
            if (Objects.equals(key, new LongWritable(0))){
                courseNames.clear();
                courseNames.addAll(Arrays.asList(splits).subList(2, splits.length));
            }else {
                System.out.println(splits.length);
                for (int i = 2; i < splits.length; i++) {
                    System.out.println(courseNames.get(i - 2)+":"+Double.parseDouble(splits[i]));
                    context.write(new Text(courseNames.get(i-2)),new Text(splits[i]));
                }
            }
        }
    }
    private static class averageReducer extends Reducer<Text, Text, Text, Text> {
        Text valueOut = new Text();
        List<Double> scoreList = new ArrayList<>();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            scoreList.clear();
            //遍历
            for(Text t: values){
                scoreList.add(Double.valueOf(t.toString()));
            }
            // 求最高成绩和最低成绩
            Double maxScore = Collections.max(scoreList);//java.util.Collections类使用求集合最大值
            Double minScore = Collections.min(scoreList);//java.util.Collections类使用求集合最小值

            double sumScore = 0;
            for(double score: scoreList){
                sumScore += score;
            }
            // 求平均成绩
            double avgScore = sumScore / scoreList.size();//1D是后缀，是double类型，会自动类型转换
            valueOut.set(Math.round(maxScore * 10) / 10.0 + "," +Math.round(minScore * 10) / 10.0+ "," + Math.round(avgScore * 10) / 10.0);
            context.write(key, valueOut);//输出结果
        }
    }
}
