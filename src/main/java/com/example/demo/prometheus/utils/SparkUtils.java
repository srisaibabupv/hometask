package com.example.demo.prometheus.utils;

import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkUtils {
    public static SparkSession createSparkSession(){
        SparkSession sparkSession = SparkSession.builder()
                .appName("JsonDirectoryStreaming")
                .master("local")
                .getOrCreate();
        sparkSession.sparkContext().setLogLevel("ERROR");
        return sparkSession;
    }
}
