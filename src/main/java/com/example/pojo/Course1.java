package com.example.pojo;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Course1 implements WritableComparable<Course1> {
    private double max;
    private double min;
    private double ave;

    public Course1(double max, double min, double ave) {
        this.max = max;
        this.min = min;
        this.ave = ave;
    }

    public Course1() {
    }

    @Override
    public int compareTo(Course1 o) {
        if (this.ave - o.ave < -0.0001)
            return 1;
        else
            return -1;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(max);
        dataOutput.writeDouble(min);
        dataOutput.writeDouble(ave);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        max = dataInput.readDouble();
        min = dataInput.readDouble();
        ave = dataInput.readDouble();
    }

    /**
     * 获取
     * @return max
     */
    public double getMax() {
        return max;
    }

    /**
     * 设置
     * @param max
     */
    public void setMax(double max) {
        this.max = max;
    }

    /**
     * 获取
     * @return min
     */
    public double getMin() {
        return min;
    }

    /**
     * 设置
     * @param min
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * 获取
     * @return ave
     */
    public double getAve() {
        return ave;
    }

    /**
     * 设置
     * @param ave
     */
    public void setAve(double ave) {
        this.ave = ave;
    }

    public String toString() {
        return "Course1{max = " + max + ", min = " + min + ", ave = " + ave + "}";
    }
}
