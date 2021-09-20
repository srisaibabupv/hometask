package com.example.demo.prometheus.service;

import com.example.demo.prometheus.bean.PriceSpreadBean;
import com.example.demo.prometheus.bean.Symbol;
import com.example.demo.prometheus.utils.SparkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

@Service
public class PriceSpreadService {

    @Autowired
    DataService service;

    public Map<String, PriceSpreadBean> generateQ4Response() {
        SparkSession sparkSession = SparkUtils.createSparkSession();
        List<Symbol> inputData = service.getData();
        Dataset<Symbol> dataSetQ2 = sparkSession.createDataset(inputData, Encoders.bean(Symbol.class));
        dataSetQ2 = dataSetQ2.filter(col("symbol").endsWith("USDT"))
                .orderBy(col("count").desc())
                .limit(5);
        List<PriceSpreadBean> priceSpreadBeanList = getSymbolPrice(dataSetQ2.collectAsList());
        priceSpreadBeanList.forEach(priceSpreadBean -> {
            priceSpreadBean.setPriceChange(Double.parseDouble(priceSpreadBean.getBidPrice()) - Double.parseDouble(priceSpreadBean.getAskPrice())+"");
        });
        return priceSpreadBeanList.stream().collect(Collectors.toMap(PriceSpreadBean::getSymbol, Function.identity()));
    }

    public List<PriceSpreadBean> getSymbolPrice(List<Symbol> symbolLst){
        List<PriceSpreadBean> priceSpreadBean = new ArrayList<>();
        for (Symbol symbol : symbolLst) {
            // request url
            String url = "https://api.binance.com/api/v3/ticker/bookTicker?symbol=" + symbol.getSymbol();
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
            try {
                priceSpreadBean.add(objectMapper.reader().forType(new TypeReference<PriceSpreadBean>() {
                }).readValue(response.getBody()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return priceSpreadBean;
    }
}
