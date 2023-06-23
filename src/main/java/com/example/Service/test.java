package com.example.Service;

import com.example.pojo.Course1;
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

public class test {
    public static void main(String[] args) throws Exception {
        String[] path = new String[20];
        path[0]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/output1/part-r-00000";
        path[1]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/output2";

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://0.0.0.0:9000");
        try {
            Job job = Job.getInstance(conf, "Course1 AveMaxMin");

            job.setJarByClass(test.class);

            job.setMapperClass(test.sortMapper.class);
            job.setReducerClass(test.sortReducer.class);

            job.setMapOutputKeyClass(Course1.class);
            job.setMapOutputValueClass(Text.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Course1.class);

            Path inputPath = new Path(path[0]);
            Path outPutPath = new Path(path[1]);
            FileSystem fs = FileSystem.get(conf);
            if (fs.exists(outPutPath)) fs.delete(outPutPath,true);

            FileInputFormat.setInputPaths(job,inputPath);
            FileOutputFormat.setOutputPath(job,outPutPath);
            System.out.println(job.waitForCompletion(true)?1:0);
        }catch (Exception e){
            System.out.println("失败");
        }
    }
    private static class sortMapper extends Mapper<LongWritable, Text, Course1, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split("\\s");
            String name = splits[0];
            double max = Double.parseDouble(splits[1]);
            double min = Double.parseDouble(splits[2]);
            double ave = Double.parseDouble(splits[3]);
            Course1 course1 = new Course1(max,min,ave);
            System.out.println(course1);
            context.write(course1,new Text(name));
        }
    }
    private static class sortReducer extends Reducer<Course1, Text, Text, Course1> {
        @Override
        protected void reduce(Course1 course1, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String name = values.iterator().next().toString();
            context.write(new Text(name),course1);
        }
    }
}
