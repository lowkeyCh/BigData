package com.example.Service;

import java.lang.reflect.Method;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        try {
            ClassLoader classLoader = Menu.class.getClassLoader();
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println("**********************基于MapReduce的学生成绩分析**********************");
                System.out.println("1、计算每门课程成绩平均成绩");
                System.out.println("2、计算每门课程学生的平均成绩，并将平均成绩从高到低输出");
                System.out.println("3、求课程的最高成绩");
                System.out.println("4、求课程的最低成绩");
                System.out.println("5、统计课程成绩的分布情况，如：某门课程多少人参加考试，各个分数段的人数等");
                System.out.println("6、查找。输入一个学生的姓名，输出该生姓名以及其参加考试的课程和成绩");
                System.out.println("7、求该成绩表每门课程当中出现了相同分数的分数，出现的次数，以及该相同分数的人数");
                System.out.println("0、退出");
                System.out.println("*******************************************************************");
                System.out.print("请选择：");


                Class<?> loadClass =null;

                String[] params = new String[20];

                int option = scanner.nextInt();
                Method method = null;
                switch(option){
                    case 1:
                        loadClass = classLoader.loadClass("com.example.Service.CourseAvg");

                        method = loadClass.getMethod("main", String[].class);

                        params[0]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/input";

                        params[1]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/output1";

                        params[2]="";

                        method.invoke(null, new Object[] {params});

                        break;
                    case 2:
                        loadClass = classLoader.loadClass("com.example.Service.AvgSort1");

                        method = loadClass.getMethod("main", String[].class);

                        params[0]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/output1/part-r-00000";

                        params[1]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/output2";

                        params[2]="";

                        method.invoke(null, new Object[] {params});

                        break;
//                    case 3:
//                        method = SiMain.class.getMethod("main", String[].class);
//                        method.invoke(null, (Object) new String[] {});
//                        break;
//                    case 4:
//                        method = CssMain.class.getMethod("main", String[].class);
//                        method.invoke(null, (Object) new String[] {});
//                        break;
                    case 5:
                        loadClass = classLoader.loadClass("com.example.Service.Statistics");

                        method = loadClass.getMethod("main", String[].class);

                        params[0]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/input";

                        params[1]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/output5";

                        params[2]="";

                        method.invoke(null, new Object[] { params });

                        break;

                    case 6:
                        method = Find.class.getMethod("main", String[].class);

                        params[0]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/input";

                        params[1]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/output6";

                        method.invoke(null, new Object[] {params});

                        break;
                    case 7:

                        loadClass = classLoader.loadClass("com.example.Service.FindSame");

                        method = loadClass.getMethod("main", String[].class);

                        params[0]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/input";

                        params[1]="hdfs://0.0.0.0:9000/user/hadoop/MapReduce/output7";

                        params[2]="";

                        method.invoke(null, new Object[] { params });

                        break;
                    case 0:
                        System.exit(1);
                        break;
                    default:
                        System.out.println("请输入正确的选项！！");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
