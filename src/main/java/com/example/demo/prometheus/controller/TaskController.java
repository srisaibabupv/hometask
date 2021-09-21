package com.example.demo.prometheus.controller;

import com.example.demo.prometheus.bean.NotionalValuePrice;
import com.example.demo.prometheus.bean.Symbol;
import com.example.demo.prometheus.service.DataService;
import com.example.demo.prometheus.service.PriceSpreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    DataService service;

    @Autowired
    PriceSpreadService priceSpreadService;

    @GetMapping("Q1")
    @ResponseBody
    public List<Symbol> Q1(){
        return service.generateQ1Response();
    }

    @GetMapping("Q2")
    @ResponseBody
    public List<Symbol> Q2(){
        return service.generateQ2Response();
    }

    @GetMapping("Q3")
    @ResponseBody
    public List<NotionalValuePrice> Q3(){
        return service.generateQ3Response();
    }

    @GetMapping("Q4")
    @ResponseBody
    public String Q4(){
        return priceSpreadService.generateQ4Response().toString();
    }

}
