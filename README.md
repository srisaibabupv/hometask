# hometask
Please follow steps to view the answeres for the questions. 

Start HomeTaskApplication.java (Spring Boot Application)

1. Print the top 5 symbols with quote asset BTC and the highest volume over the last 24 hours in descending order.
Ans - http://localhost:8080/Q1 (access link)

2. Print the top 5 symbols with quote asset USDT and the highest number of trades over the last 24 hours in descending order.
Ans- http://localhost:8080/Q2 (access link)
 
3. Using the symbols from Q1, what is the total notional value of the top 200 bids and asks currently on each order book?
Ans - http://localhost:8080/Q3 (access link)

4. What is the price spread for each of the symbols from Q2?
Ans - http://localhost:8080/Q4 (access link)

5. Every 10 seconds print the result of Q4 and the absolute delta from the previous value for each symbol.
Ans - Scheduler is triggered every 10 seconds for computation

6. Make the output of Q5 accessible by querying http://localhost:8080/metrics using the Prometheus Metrics format.
Ans - http://localhost:8080/metrics (access link for prometheus metrics)

Notes

Project structure

pom.xml - Dependencies
main - HomeTaskApplication
Controller - TashController
Service - DataService, PriceSpreadService
Scheduler
SparkUtils
