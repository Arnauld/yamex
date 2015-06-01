# Welcome to Yamex!

![Yamex Logo](${imageDir}/Yamex-header.png)

Yamex is the next-gen market exchange engine that will probably drives all EURO, AMER and ASIA trading platform.

A central part of a market exchange engine is the matching engine.
A matching engine is a program that accepts orders from buyers and sellers. The other matching module matches buy and sell orders, creates transactions to record the process, and updates the customers account balances.

The matching (or trade allocation) algorithm is an important part of an exchange trading mechanism, since it is responsible for resolving the buy/sell association in a fast and efficient way.

Exchanges set the institutional rules that govern trading and information flows about that trading. They are closely linked to the clearing facilities through which post-trade activities could be completed. An exchange centralizes the communication of bid and offer prices to all direct market participants, who can respond by selling or buying at one of the quotes or by replying with a different quote.

{icon=warning,icon-color=dark-red}
G> This specification describes the matching engine expected behaviors.

# Terminology

* **Order** - An order is an instruction to buy or sell a stock at a specific price or better
* **Bid** - the price in a buy order
* **Ask** - the price in a sell order
* **Spread** - the difference between the bid and the ask
* **Time In Force** - indicates how long an order will remain active (see Appendix B)


## Design Considerations

```ditaa
                                                             +-------------+
                                                     /------ | Market Book |
/--------------\     submit order  /-------------\   |       +-------------+
| Electronic   | ----------------> | Transaction |---/              .
| Trading      |                   | Router      |------ ...        .
| Network cBLK | <---------------- |        cRED |---\              .
\--------------/   order status    \-------------/   |       +-------------+
                                                     \-------| Market Book |
                                                             +-------------+
```

### Price/Time Algorithm

Under the Price/Time algorithm, an incoming trade is matched with the first orders (at the best price), which is the so called FIFO method.
More precisely: suppose `n` limit orders `Q1` ... `Qn` of a cumulative volume `Q` lots, and an incoming trade of `N ≤ Q` lots.

Then there exists an index `1 ≤ j ≤ n` such that:

```formula
\sum\limits_{i=1}^j Q_i \leq N
```

and

```formula
\sum\limits_{i=1}^{min(j+1,n)} Q_i \geq N
```

Under the Price/Time algorithm, the first `j` limit orders are filled in full, and the remaining lots are assigned to the `(j+1)`-th limit order (if `j < n`)