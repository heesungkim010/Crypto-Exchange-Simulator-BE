# Crypto-Exchange-Simulator

Simulates Crypto Trading Exchange System.

This is a prototype. This does not use multiple servers, security(authentification, verification).

# A. System Design Overview

# B. Database Design

# C. API Design

# D. Domain/Service/Repository Design

# E. Challenges And My Solution

The followings are the challenges I got in this project and my solutions.

## Index

[Challenge #1] How to implement Matching Engine and get the price information.

[Challenge #2] How to safely cancel the open orders.(Matching engine)

[Challenge #3] How to quickly open/cancel the orders. (Matching engine)

[Challenge #4] How to send price information from the back-end server to the front-end server.

[Challenge #5] Overall system design, DB design, and Spring framework

### [Challenge #1] How to implement Matching Engine and get the price information.

  A Matching engine is a system that matches the order of two sides(buy and sell) and makes a deal for both sides. Matching engines in exchanges use orderbooks.
However, this exchange simulator project does not use orderbook to match the orders. Because the exchage simulator aims to use the price information in the real world, not the orderbook in a small simulator which lacks of volume.

  Therefore, I need to get the price information from the external exchanges in the real world, and use that information for the matching engines directly.


### [Solution]

  I can get the price information from the external exchanges by 2 methods. One is HTTP and the other is Web Socket. As the price information needs a real-time communication, I used Web Socket to get the price information.

  Once I get the real-time price and the open orders of users, matching engine can decide whether the orders would be filled or not. I used a hash table shown as below(key-price, value-a list of orders at the price). Once the price reaches at a certain value, the matching engine can fill the orders in the lists.

![image](https://user-images.githubusercontent.com/63962555/156721278-33ecb70c-6d4c-48b6-a9ab-fadd4bcb409e.png)

### [Challenge #2] How to safely cancel the open orders.(Matching engine)
As the price can change very quickly, there could be an unsafe situation as follows:  

(1) User A opened an order ( Buy 1 BTC at $40,000)   

(2) The matching engine reaches the price at $40,000 and tries to fill the order. At the same time, the user A canceled the order.   
   
In this situation, there exists a mutual exclusion problem. Because the matching engine tries to do two different things(fill and cancel) for the same order.   

### [Solution]
The simplest way to solve this problem is to lock and unlock on the lists of orders at the given prices. For example, in the situation above, the matching engine can lock on the lists at certain prices when the matching engine tries to fill or cancel the order, and unlock after filling or canceling the order. 

pros) Safe to order/cancel the orders. 

cons) Overhead and delay caused by locking/unlocking.

The cons seem critical but I could not find any other methods yet. One alternative I was thinking of is that once the matching engine gets the “cancel the order” request, the request waits at the router which is right after the matching engine. And then cancel the order if the incoming order corresponds to the “cancel the order” request. But this cannot be implemented when using the orderbooks as real world exchanges do and had some other issues. So I used the locking/unlocking method.

![image](https://user-images.githubusercontent.com/63962555/156720577-9a8de899-9016-456c-8a25-2e4182898259.png)

### [Challenge #3] How to quickly open/cancel the orders. (Matching engine)
 As in <Matching Engine – version 2> above, the orders of the certain price are in a list. If I think of one list, there are 3 situations as below.
 
(Situation 1) : An user opens an order.

(Situation 2] : The matching engine reaches the price of the list.

(Situation 3]) : An user cancels an order.

The matching engine can work as follows in each situation. I omitted the locking/unlocking procedure.

(Situation 1) : An user opens an order.

The matching engine append the order to the list.  --> O(1)

(Situation 2) : The matching engine reaches the price of the list

The matching engine sends the orders in the list to the router.  

-> O(N) when N = length of the list

(Situation 3) : An user cancels an order.

The matching engine searches the orders in the list and check if there is a corresponding open order to the canceling request and delete the order if exists.

-> O(N) when N = length of the list

The time complexity seems not that satisfying. I need to optimize the matching engine with some other data structures if possible.

### [Solution]
Let’s check some other data structures than a list. I checked (1) linked list (2) hash table (3) red-black tree. The time complexity is as follows:

![image](https://user-images.githubusercontent.com/63962555/156720794-2de8349c-5c36-45e0-97a0-586edaded7b1.png)

As seen above, a hash table could be the best choice in time complexity. Now the matching engine has two kinds of hash tables as follows:

![image](https://user-images.githubusercontent.com/63962555/156721462-eeb55331-1cd5-4154-9821-025c4983b3d8.png)
