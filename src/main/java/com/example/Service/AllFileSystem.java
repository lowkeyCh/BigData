package com.example.Service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;

public class AllFileSystem {
    FileSystem fs;
    public AllFileSystem() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://0.0.0.0:9000");
        conf.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
        fs = FileSystem.newInstance(conf);
    }
}
