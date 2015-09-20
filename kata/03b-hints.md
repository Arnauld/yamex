## Signatures hints

```java
public interface PriorityStrategy {
  Comparator<? extends BookEntry> sorter();
}
```

```java
public interface OrderBook {
  PriorityStrategy priorityStrategy();
  Option<BookEntry> topEntry(Side side);
}
```

## Gherkin hints

`02-orderBook.feature`

```gherkin

Scenario: Price / Time Priority for Limit order - Sell by ascending price order

  Given An order book initialized with the "Price / Time" priority and sequential timestamp
   When the following orders are added to the order book:
    | Broker | Qty | Price | Side |
    | A      | 100 | 10.6  | Sell |
    | B      | 100 | 10.7  | Sell |
    | C      | 100 | 10.5  | Sell |
    | D      | 100 | 10.7  | Sell |
   Then the order book - for the Sell orders - should looks like:
    | Broker | Qty | Price | Timestamp |
    | C      | 100 | 10.5  | 3         |
    | A      | 100 | 10.6  | 1         |
    | B      | 100 | 10.7  | 2         |
    | D      | 100 | 10.7  | 4         |
   And the top Sell order should correspond to the order from broker C


Scenario: Price / Time Priority for Limit order - Buy by descending price order

  Given An order book initialized with the "Price / Time" priority and sequential timestamp
   When the following orders are added to the order book:
    | Broker | Qty | Price | Side |
    | A      | 100 | 10.6  | Buy  |
    | B      | 100 | 10.7  | Buy  |
    | C      | 100 | 10.5  | Buy  |
    | D      | 100 | 10.7  | Buy  |
   Then the order book - for the Buy orders - should looks like:
    | Broker | Qty | Price | Timestamp |
    | B      | 100 | 10.7  | 2         |
    | D      | 100 | 10.7  | 4         |
    | A      | 100 | 10.6  | 1         |
    | C      | 100 | 10.5  | 3         |
    And the top Buy order should correspond to the order from broker B

```


```gherkin
Scenario: Unless specified an order book is initialized with the "Price / Time priority" strategy

  Given an default order book
   Then the order book's priority strategy should be "Price / Time priority"
    And the order book's timestamp strategy should be sequential based
```

```gherkin
Scenario: Aggregated view of quantities per price - Sell order book

  Given an default order book
   When the following orders are added to the order book:
    | Broker | Qty | Price | Side |
    | A      | 100 | 10.6  | Buy  |
    | B      | 150 | 10.7  | Buy  |
    | C      |  50 | 10.5  | Buy  |
    | D      | 200 | 10.7  | Buy  |
   Then the order book aggregated view - for the Buy orders - should looks like:
    | Cumulative Qty | Qty | Price |
    |            350 | 350 | 10.7  |
    |            450 | 100 | 10.6  |
    |            500 |  50 | 10.5  |
```

```gherkin

Scenario: Order book - standard view

  Given an default order book initialized with the "Price / Time priority" strategy
   When the following orders are added to the order book (in a random order):
    | Broker | Qty | Price | Side |
    | A1     | 100 | 10.9  | Buy  |
    | A2     |  75 | 10.9  | Buy  |
    | B      | 100 | 10.8  | Buy  |
    | C1     |  50 | 10.7  | Buy  |
    | C2     |  80 | 10.7  | Buy  |
    | E1     | 150 | 11.1  | Sell |
    | E2     |  50 | 11.1  | Sell |
    | F      |  20 | 11.3  | Sell |
  Then the standard view of the order book should look like:
    | cumSize | bidSize | bidPrice | askPrice |  askSize | cumSize |
    |    175  |    175  |    10.9  |    11.1  |    200   |   200   |
    |    275  |    100  |    10.8  |    11.3  |     20   |   220   |
    |    405  |    130  |    10.7  |          |          |         |

```
