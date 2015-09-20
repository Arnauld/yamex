## Signatures hints

```java
public interface BookEntry {
  Order order();
  Timestamp timestamp();
}
```

```java
public interface OrderBook {
  void add(Order order);
  Stream<BookEntry> entries();
}
```


## Gherkin hints

```gherkin
@technical @orderBook @timestamp
Scenario: Timestamp are automatically assigned on order

  Given An order book initialized with a sequential timestamp starting at 6
   When the following standard orders are added to the order book:
    | Broker | Side |
    | A      | Sell |
    | B      | Buy  |
    | C      | Sell |
    | D      | Sell |
   Then the order book should looks like (whatever the actual order):
    | Broker | Side | Timestamp |
    | C      | Sell | 8         |
    | A      | Sell | 6         |
    | B      | Buy  | 7         |
    | D      | Sell | 9         |
```
