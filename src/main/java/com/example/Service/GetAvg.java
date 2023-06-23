package com.example.Service;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAvg {

    AllFileSystem allFileSystem;
    public List<Map<String,Object>> getAve(int flag) throws IOException {

        FileSystem fs = allFileSystem.fs;
        //读文件
        Path file;
        file = new Path("hdfs://0.0.0.0:9000/user/hadoop/MapReduce/output1/part-r-00000");

        FSDataInputStream getIt = fs.open(file);
        InputStreamReader reader = new InputStreamReader(getIt);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        List<Map<String,Object>> maps = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null)
        {
            String[] splits = line.split("\\s");
            Map<String,Object> map = new HashMap<>();
            map.put("name",splits[0]);
            map.put("max",splits[1]);
            map.put("min",splits[2]);
            map.put("ave",splits[3]);
            maps.add(map);
        }
        return maps;
    }
}
