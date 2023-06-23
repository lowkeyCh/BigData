package com.example.Service;


import com.example.pojo.Course;
import com.example.pojo.Course1;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


public class AvgSort1 {
    public static void main(String[] args) throws Exception {
        if (args.length<2) {
            System.out.printf("Usage:%s <input> <output>\n");
        }
        //创建一个Configuration实体类对象

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "course Course");

        job.setJarByClass(AvgSort1.class);
        job.setMapperClass(AvgSort1.sortMapper.class);
        job.setReducerClass(AvgSort1.sortReducer.class);
        job.setOutputKeyClass(Course1.class);
        job.setOutputValueClass(Course1.class);
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

    private static class sortMapper extends Mapper<LongWritable, Text, Course, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            System.out.println("mapper here");
            String[] splits = value.toString().split(",");
            System.out.println("mapper1 here");
            String name = splits[0];
            System.out.println("mapper2 here" + name);
            double max = Double.parseDouble(splits[1]);
            System.out.println("mapper3 here" + max);
            double min = Double.parseDouble(splits[2]);
            System.out.println("mapper4 here" + min);
            double ave = Double.parseDouble(splits[3]);
            System.out.println("mapper5 here" + ave);
            Course course = new Course(name,ave);
            System.out.println("mapper6 here");
            System.out.println(course);
            context.write(course, NullWritable.get());
        }
    }
    private static class sortReducer extends Reducer<Course, Text, Text, Course> {
        @Override
        protected void reduce(Course course, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            System.out.println("reduce here");
            String name = values.iterator().next().toString();
            context.write(new Text(name),course);
        }
    }
}
