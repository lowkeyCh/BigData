package com.example.Service;

import com.example.pojo.Student;
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
import java.util.Scanner;

public class Find {


    public static void main(String[] args) throws Exception {
//        args[0] = "/user/hadoop/MapReduce/input";
//        args[1] = "/user/hadoop/MapReduce/output1";
        if (args.length<2) {
            System.out.printf("Usage:%s <input> <output>\n");
        }
        Configuration conf = new Configuration();
        System.out.println("请输入需要查询的学生姓名：");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        conf.set("studentName",str);
        Job job = Job.getInstance(conf);

        job.setJarByClass(Find.class);
        job.setMapperClass(FCMapper.class);
        job.setReducerClass(FCReducer.class);

        job.setMapOutputKeyClass(Student.class);
        job.setMapOutputValueClass(NullWritable.class);

//        job.setOutputKeyClass(Student.class);
//        job.setOutputValueClass(NullWritable.class);
        Path inputPath = new Path(args[0]);
        Path outPutPath = new Path(args[1]);
//        FileSystem fs = FileSystem.get(conf);
        FileSystem fs = outPutPath.getFileSystem(conf);
        if (fs.exists(outPutPath)) fs.delete(outPutPath,true);
        FileInputFormat.setInputPaths(job,inputPath);
        FileOutputFormat.setOutputPath(job,outPutPath);

        boolean waitForCompletion = job.waitForCompletion(true);
        // System.exit(waitForCompletion ? 0 : 1);
        System.out.println("here");
        System.out.println(job.waitForCompletion(true)?1:0);
    }


    static class FCMapper extends Mapper<LongWritable,Text, Student, NullWritable> {

        @Override
        protected void map(LongWritable key, Text line, Context context) throws IOException, InterruptedException {
            String[] fields = line.toString().split(" ");
            context.write(new Student(new String(fields[0]),fields[1],new String(fields[2]),new String(fields[3])),NullWritable.get());
        }
    }

    static class FCReducer extends Reducer<Student,NullWritable,Student,NullWritable> {
        private static String studentName = "";
        private static Student stu = null;
        private static String id="";
        private static String Mathscore = "";
        private static String Englishscore = "";
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //setup是在map执行前加载的方法,读取数据
            studentName = context.getConfiguration().get("studentName");
            stu = new Student();
            stu.setStudentName(studentName);
        }
        @Override
        protected void reduce(Student key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            if (studentName.equals(key.getStudentName())){
//                for (NullWritable value : values) {
                id=key.getId();
                Mathscore=key.getMathScore();
                Englishscore=key.getEnglishScore();

                stu.setEnglishScore(Englishscore);
                stu.setId(id);
                stu.setMathScore(Mathscore);
                context.write(stu,NullWritable.get());
            }
        }
    }
}
