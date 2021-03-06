# Crypto-Exchange-Simulator-BE

Simulates Crypto Trading Exchange System(backend server).

Used Java, Spring, Spring Boot, MySQL, Cassandra.

The frontend server is at https://github.com/heesungkim010/Crypto-Exchange-Simulator-FE

[Index]

- [A. System Design](#a-system-design)
- [B. Matching Engine Implementation](#b-matching-engine-implementation)
- [C. Database Design](#c-database-design)
- [D. API Design](#d-api-design)

# A. System Design

![crypto_exchange_system_design](https://user-images.githubusercontent.com/63962555/158497586-695edab1-d7a5-4fc1-952f-179a42cff68d.jpg)

As I used a single back-end server for this project, the “router” in the diagram above is a thread-to-thread router. The router I used is at https://github.com/heesungkim010/producer-consumer-router.
I've got a lot of idea from https://medium.com/@narengowda/stock-exchange-system-design-answered-ad4be1345851 and changed a bit for this project.

# B. Matching Engine Implementation
A matching engine is a system that matches the order of two sides(buy and sell) and makes a deal for both sides. Matching engines in exchanges use orderbooks to fill the order. However, this exchange simulator project does not use orderbook to match the orders. Because the exchange simulator aims to use the price information in the real world, not the orderbook in a small simulator which lacks of volume.

Therefore, I need to get the price information from the external exchanges in the real world, and use that information to decide if the orders are filled or not. I used Web Socket to get the price information.

 Each ticker has 2 Matching Engines(One for buy, cancel_buy orders, and the other for sell, cancel_sell orders). So if there are 3 tickers, there are 6 Matching Engines in total.
 
 The detailed system design around the matching engines per ticker is as follows:
 
 ![ME_detail](https://user-images.githubusercontent.com/63962555/164009941-7ecf5d07-efde-4793-aa78-ad689508812b.jpg)

 The Matching Engine has 3 functions.

1. open a new order(buy, sell) 
2. cancel an order(cancel_buy, cancel_sell)
3. fill the reserved the orders and update the price of trading system simulator. (fill and set price)

Function3 runs continuously, while function1 and function2 are called when there is an transmitted order.

The data structures of the matching engine are as follows:

![image](https://user-images.githubusercontent.com/63962555/159199570-d67d147d-f8bd-44f5-adc9-6b646cdfacc3.png)

The index of the static array on the left corresponds to a price. The element of the array is the pointer of a hash table.
The hash tables on the right store the orders at a certain price. The orders of the same price are stored in the same hash table.
Now, the three functions I mentioned above work as follows:
1. open a new order(buy, sell)   
1_1. Check the current price of simulator.    
1_2. Reserve the order, or fill the order at market price according to the price of the order.    
1_3. When reserving an order, get the index of the array by the price and access the hash table to add the order.

2. cancel an order(cancel_buy, cancel_sell)   
2_1. Get the index of the array by the price and access the hash table    
2_2. If the order id to cancel is in the hash table, delete the order from the hash table. If not, the order is already filled.    

3. fill the reserved the orders and update the price of trading system simulator. (fill and set price)     
3_1. Check the current price of the external exchange which is already received by web-socket and stored in an object.    
3_2. Now there are two kinds of prices. One is the current price(the very last updated price), and the other is previous price(the second last updated price)    
3_3. Fill the orders of prices between the current price and the previous price. Get the indexes of the array and fill all the orders in the hash tables.   
3-4. Update the current price of trading system simulator which will be transmitted to a front-end server.
 
The two biggest problems encountered when implementing the matching engine are 1. Time complexity and 2. Synchronization. 
 
### 1. Time complexity
 I’ve also considered some other options using dynamic array, linked list, trees(red-black tree, b+ tree). However, the data structure above was best in time complexity. 
 
 Let's say there are M distinct reserved prices and N orders reserved at a certain price.
1. open a new order(buy, sell) 

   As the index of the static array can be directly calculated by the price of the order, I can get the index of the array in O(1).
   
   As the element of the array stores the pointer of the hash table, I can open new order(add in hash table) in O(1).
   
   --> O(1) + O(1) = O(1)
2. cancel an order(cancel_buy, cancel_sell)

   As the index of the static array can be directly calculated by the price of the order, I can get the index of the array in O(1).
   
   As the element of the array stores the pointer of the hash table, I can cancel an order(delete in hash table) in O(1).
   
   --> O(1) + O(1) = O(1)
3. fill the reserved the orders and update the price of trading system simulator. (fill and set price)

    As the index of the static array can be directly calculated by the price of the order, I can get the index of the array in O(1).
 
    As there are N orders reserved at a certain price in a hash table, the time complexity of filling all N orders is O(N).
 
   --> O(1) + O(N) = O(N)


### 2. Synchronization problem
 The other issue than the time complexity is the synchronization problem. This is because the functions above share the same data structures and the current price of the simulator is determined at function3(3_4). At first I thought providing mutual exclusions on the unit of each index of the array would be enough. However as the current price is determined at function3(3_4), I had to provide mutual exclusions on the unit of each functions(1,2,3).
 
### 3. Performance
 I performed an experiment by using POSTMAN. 
 
 host : localhost
 
 number of requests per minute : 500
 
 order type : BUY for reserved price

 --> Result : 10 microseconds for one order to be processed in Matching Engine.
 
# C. Database Design

# D. API Design
