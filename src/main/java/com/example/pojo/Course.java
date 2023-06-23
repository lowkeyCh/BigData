package com.example.pojo;



import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Course implements WritableComparable<Course> {
    private String course;
    private double score;

    public Course(String course, double score) {
        super();
        this.course = course;
        this.score = score;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        // TODO Auto-generated method stub
        out.writeUTF(course);
        out.writeDouble(score);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        // TODO Auto-generated method stub
        this.course = in.readUTF();
        this.score = in.readDouble();
    }
    // 排序规则
    // compareTo方法充当排序用
    @Override
    public int compareTo(Course o) {
        // 倒序排列，从大到小
        return this.score > o.getScore() ? -1 : 1;
    }
    @Override
    public String toString() {
        return course + " " + score;
    }
}