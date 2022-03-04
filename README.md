# Crypto-Exchange-Simulator

Simulates Crypto Trading Exchange System.

This is a prototype. This does not use multiple servers, security(authentification, verification).

[Challenges And My Solutions]

The followings are the challenges I got and my solutions in this project.
[Index]
[Challenge #1] How to implement Matching Engine and get the price information.
[Challenge #2] How to safely cancel the open orders.(Matching engine)
[Challenge #3] How to quickly open/cancel the orders. (Matching engine)
[Challenge #4] How to send price information from the back-end server to the front-end server.
[Challenge #5] Overall system design, DB design, and Spring framework

[Challenge #1] How to implement Matching Engine and get the price information.
  A Matching engine is a system that matches the order of two sides(buy and sell) and makes a deal for both sides. Matching engines in exchanges use orderbooks.
However, this exchange simulator project does not use orderbook to match the orders. Because the exchage simulator aims to use the price information in the real world, not the orderbook in a small simulator which lacks of volume.
Therefore, I need to get the price information from the external exchanges in the real world, and use that information for the matching engines directly.
[Solution]
I can get the price information from the external exchanges by 2 methods. One is HTTP and the other is Web Socket. As the price information needs a real-time communication, I used Web Socket to get the price information.
Once I get the real-time price and the open orders of users, matching engine can decide whether the orders would be filled or not. I used a hash table shown as below(key-price, value-a list of orders at the price). Once the price reaches at a certain value, the matching engine can fill the orders in the lists
<Hash table in the matching engine – version 1>
key	value
$40.001.34	{ order10, order11, … }
$40,000.2	{ order1, order2, … }
$40,000	
$39,999.12	{order3, order4, … }
$39,999.02	

