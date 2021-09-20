package com.example.demo.prometheus.service;

import com.example.demo.prometheus.bean.*;
import com.example.demo.prometheus.utils.SparkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.spark.sql.functions.col;

@Service
public class DataService {
    public List<Symbol> generateQ1Response(){
        SparkSession sparkSession = getSession();
        List<Symbol> inputData = getData();
        Dataset<Symbol> dataSetQ1 = sparkSession.createDataset(inputData, Encoders.bean(Symbol.class));
        dataSetQ1 = dataSetQ1.filter(col("symbol").endsWith("BTC"))
                .orderBy(col("quoteVolume").desc())
                .limit(5);
        dataSetQ1.select(col("symbol").as("Symbol"),col("quoteVolume").as("Volume"))
                .show();
        return dataSetQ1.collectAsList();
    }

    public List<Symbol> generateQ2Response(){
        SparkSession sparkSession = getSession();
        List<Symbol> inputData = getData();
        Dataset<Symbol> dataSetQ2 = sparkSession.createDataset(inputData, Encoders.bean(Symbol.class));
        dataSetQ2 = dataSetQ2.filter(col("symbol").endsWith("USDT"))
                .orderBy(col("count").desc())
                .limit(5);
        dataSetQ2.select(col("symbol").as("Symbol"),col("count").as("Trades")).show();
        return dataSetQ2.collectAsList();
    }

    public List<TradeBean> generateQ3Response(){
        List<TradeBean> tradeBeanData = new ArrayList<>();
        List<Symbol> inputData = generateQ1Response();
        inputData.forEach(symbol -> {
            Dataset<TradeBean> dataSetQ3 = getSession().createDataset(getSymbolData(symbol), Encoders.bean(TradeBean.class));
            dataSetQ3.select(col("price"), col("quantity"))
                    .withColumn("NotionalValue", col("price").multiply(col("quantity")))
                    .orderBy(col("NotionalValue").desc())
                    .limit(200)
                    .show();
            tradeBeanData.add(dataSetQ3.collectAsList().get(0));
        });
        return tradeBeanData;
    }

    public List<Symbol> getData(){
        {
            String url = "https://api.binance.com/api/v3/ticker/24hr";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );
            if (response.getStatusCode() != HttpStatus.OK) {
                System.out.println("Request Failed");
                System.out.println(response.getStatusCode());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<Symbol> listSymbol = null;
            try {
                listSymbol = objectMapper.readValue(response.getBody(), new TypeReference<List<Symbol>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
            return listSymbol;
        }
    }

    private SparkSession getSession(){
        return SparkUtils.createSparkSession();
    }

    public static List<TradeBean> getSymbolData(Symbol symbol){
        System.out.println("----------------------------Symbol : "+symbol.getSymbol()+"------------------------------------");
        String url = "https://api.binance.com/api/v3/depth?symbol="+symbol.getSymbol()+"&limit=5000";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class,
                1
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Request Failed");
            System.out.println(response.getStatusCode());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SymbolNotionalValue symbolNotionalValue = null;
        NotionalValue notionalValue = new NotionalValue();
        notionalValue.setName(symbol.getSymbol());
        notionalValue.setBids(new ArrayList<>());
        try {
            symbolNotionalValue = objectMapper.reader().forType(new TypeReference<SymbolNotionalValue>(){}).readValue(response.getBody());
            Arrays.stream(symbolNotionalValue.getBids()).forEach((i) -> {
                notionalValue.getBids().add(new TradeBean(Double.parseDouble(i[0].getValue()), Double.parseDouble(i[1].getValue())));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return notionalValue.getBids();
    }
}
