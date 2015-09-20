First Iteration: "The Law and Order"
================================================================================


The market exchange platform must allow broker to take order either buy or sell.
All orders are created by brokers, are either to sell or to buy a given instrument, and have a known size and quantity.

> A customer enters the quantity and prices of the orders and clicks “buy” or “sell”

![Take Order](images/Order-controls.png)

![My Order list](images/All-orders.png)


* buy/bid
* sell/offer


Limit orders, in addition, define a limit price, and will not execute if market conditions are inferior to the limit price.
The motivation of market makers to place limit orders is to “buy low and sell high”. Thus, a market maker would prefer as large spread between his bid and offer as possible.


> *Limit Orders*
> A limit order stipulates a maximum purchase price or minimum selling price.
> A limit order can be placed during the order accumulation period and during the main trading session. A limit order entered during the trading session is executed either fully or partially, as market conditions permit. Failing this, it is logged in the order book in descending buy-price order or ascending sell-price order (the price-priority principle) and joins the queue of orders having the same price (the time-priority principle).
>
> -- Order Types, NYSE Euronext
