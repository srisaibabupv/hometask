package com.example.demo.prometheus.utils;

import com.example.demo.prometheus.bean.PriceSpreadBean;
import com.example.demo.prometheus.service.PriceSpreadService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
public class Scheduler {

    static Map<String, PriceSpreadBean> previousPriceSpreadMap = new HashMap<>();

    @Autowired
    PriceSpreadService spreadService;

    @Autowired
    MeterRegistry meterRegistry;

    @Scheduled(fixedDelay = 2000)
    public void scheduleFixedDelayTask() {
        Map<String, PriceSpreadBean> currentPriceSpreadMap = spreadService.generateQ4Response();
        if(previousPriceSpreadMap.isEmpty()){
            previousPriceSpreadMap.putAll(currentPriceSpreadMap);
        } else {
            previousPriceSpreadMap.forEach((s, priceSpreadBean) -> {
                priceSpreadBean.setPriceChange(Double.parseDouble(priceSpreadBean.getPriceChange()) - Double.parseDouble(currentPriceSpreadMap.get(s).getPriceChange())+"");
            });
        }
        previousPriceSpreadMap.forEach((symbol, priceBean) -> {meterRegistry.counter("custom.symbol.details", symbol, priceBean.getPriceChange());});
    }

}
