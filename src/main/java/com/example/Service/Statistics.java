package com.example.Service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class Statistics {
    public static void main(String[] args) throws Exception {
        if (args.length<2) {
            System.out.printf("Usage:%s <input> <output>\n");
        }
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        // 指定我这个job所在的jar包

        job.setJarByClass(Statistics.class);
        job.setMapperClass(Statistics.MyMapper.class);
        job.setReducerClass(Statistics.MyReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

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

    private static class MyMapper extends Mapper<LongWritable, Text,Text,IntWritable> {
        Text k = new Text();
        IntWritable v = new IntWritable();
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            String string = value.toString();
            System.out.println("here");
            //按行分割
            String[] strings = string.split("\n");
            for (int i = 0; i < strings.length; i++) {
                //以空格分割
                String[] data = strings[i].split(" ");
                String scores=data[3];
                if(scores.equals("English")) {
                    continue;
                }else {
                    double score = Double.parseDouble(data[3]);
                    //以成绩进行分割
                    if (score < 60) {
                        k.set("差");
                        v.set(1);
                    } else if (score >= 60 && score < 85) {
                        k.set("中等");
                        v.set(1);
                    } else if (score >= 85) {
                        k.set("优秀");
                        v.set(1);
                    }
                    //生成key value键值对
                    context.write(k, v);
                    //数组第5个元素是成绩
                }
            }
        }
    }

    private static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        //重写reduce方法
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            int count = 0;

            //遍历values
            for (IntWritable v : values) {
                int value = v.get();
                System.out.println("value"  + value);
                count += value;
            }

            //汇总每个分数段的人数
            context.write(key, new IntWritable(count));
        }
    }
}
