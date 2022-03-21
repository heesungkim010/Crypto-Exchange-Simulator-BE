# Crypto-Exchange-Simulator

Simulates Crypto Trading Exchange System.

[Index]

- [A. System Design](#a-system-design)
- [B. Matching Engine Implementation](#b-matching-engine-implementation)
- [C. Database Design](#c-database-design)
- [D. API Design](#d-api-design)

# A. System Design

![crypto_exchange_system_design](https://user-images.githubusercontent.com/63962555/158497586-695edab1-d7a5-4fc1-952f-179a42cff68d.jpg)

As I used a single back-end server for this project, the “router” in the digram above is a thread-to-thread router. The router I used is at https://github.com/heesungkim010/producer-consumer-router.


# B. Matching Engine Implementation

A matching engine is a system that matches the order of two sides(buy and sell) and makes a deal for both sides. Matching engines in exchanges use orderbooks to fill the order. An orderbook is the list of order that trading systems use to record the interest of buyers and sellers in a particular financial instrument. An orderbook is as follows :

![image](https://user-images.githubusercontent.com/63962555/159198328-78765a52-8a3d-425d-8f98-6ed627f6fee9.png)
https://en.wikipedia.org/wiki/Order_book

The x-axis is the unit price, the y-axis is cumulative order depth. Bids (buyers) on the left, asks (sellers) on the right.


However, this exchange simulator project does not use orderbook to match the orders. Because the exchage simulator aims to use the price information in the real world, not the orderbook in a small simulator which lacks of volume.


Therefore, I need to get the price information from the external exchanges in the real world, and use that information to decide if the orders are filled or not. I used Web Socket to get the price information.

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

 Each ticker has 2 Matching Engines(One for buy, cancel_buy orders, and the other for sell, cancel_sell orders). So if there are 3 tickers, there are 6 Matching Engines in total.
 I’ve also considered some other options using dynamic array, linked list, trees(red-black tree, b+ tree). However, the data structure above was best in time complexity. 

# C. Database Design

# D. API Design
