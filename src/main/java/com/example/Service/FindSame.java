package com.example.Service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class FindSame {
    public static void main(String[] args) throws Exception {
        if (args.length<2) {
            System.out.printf("Usage:%s <input> <output>\n");
        }
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "course same");
        // 指定我这个job所在的jar包
        job.setJarByClass(FindSame.class);
        job.setMapperClass(FindSame.sameMapper.class);
        job.setReducerClass(FindSame.sameReducer.class);
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

    private static class sameMapper extends Mapper<LongWritable, Text, Text, Text> {

        Text keyOut = new Text();
        Text valueOut = new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//            System.out.println("heremap");
            String[] splits = value.toString().split(" ");
            String math = splits[2];
            String name = splits[1];
            keyOut.set("math" +"\t"+ math);
            String English = splits[3];
            String name1 = splits[1];
            context.write(keyOut, valueOut);
            keyOut.set("English" +"\t"+ English);
//            valueOut.set(name);
//            valueOut.set(name1);
            context.write(keyOut, valueOut);
        }
    }

    private static class sameReducer extends Reducer<Text, Text, Text, Text> {
        Text valueOut = new Text();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//            System.out.println("herereduce");
            StringBuilder sb = new StringBuilder();
            int number = 0;
            for(Text t: values){
                sb.append(t.toString()).append(",");//append方法是追加功能
                number++;
            }
            if(number > 1){
                String names = sb.toString().substring(0, sb.toString().length() - 1);//substring(参数)是java中截取字符串的一个方法
                valueOut.set(names +" " + "共有" + number + "人");
                context.write(key, valueOut);
            }
        }
    }
}
