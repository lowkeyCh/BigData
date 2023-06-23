package com.example.pojo;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Student implements WritableComparable<Student> {
    private String id;
    private String studentName;//学生姓名
    private String Mathscore;
    private String Englishscore;

    public Student() {

    }

    public Student(String id, String studentName, String Mathscore,String Englishscore) {
        this.id = id;
        this.studentName = studentName;
        this.Mathscore = Mathscore;
        this.Englishscore=Englishscore;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getMathScore() {
        return Mathscore;
    }
    public void setMathScore(String Mathscore) {
        this.Mathscore = Mathscore;
    }
    public String getEnglishScore() {
        return Englishscore;
    }
    public void setEnglishScore(String Englishscore) {
        this.Englishscore = Englishscore;
    }
    //    public void setCourseName(StringBuilder courseName) {
//    	this.courseName=courseName;
//    }
//    public StringBuilder getCourseName() {
//    	return courseName;
//    }
    @Override
    public int compareTo(Student o) {
        return this.studentName.compareTo(o.studentName);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(id);
        out.writeUTF(studentName);
        out.writeUTF(Mathscore);
        out.writeUTF(Englishscore);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = new String(in.readUTF());
        this.studentName = in.readUTF();
        this.Mathscore = in.readUTF();
        this.Englishscore = in.readUTF();
    }
    @Override

    public String toString() {
        return id+" "+studentName + ":" +"Math "+Mathscore+"  English "+Englishscore;
    }

}
