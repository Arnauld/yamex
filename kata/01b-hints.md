
## Signatures hints

```java
public enum Side {
  Buy,
  Sell
}
```

```java
public interface Broker {
  String name();
}
```

```java
public interface Order {
  Side side();
  Broker broker();
  double quantity();
}
```

```java
public interface OrderId {
}
```

```java
public interface MarketExchange {
  OrderId takeOrder(Order order);
  Option<Order> findById(OrderId id);
}
```

```java
public interface LimitOrder extends Order {
  double limit();
}
```

## Gherkin hints

`01-order-acquisition.feature`

```gherkin
Feature: Basic Order Processing

  Scenario: Take a sell order

    When broker "A" take a sell order of 150 XBZ-01 for 10.5€
    Then the market exchange should have created the following record:
      | Broker | Side | Qty | Price |
      | A      | Sell | 150 | 10.5  |
```

`00-standard/01-limit-order.feature`

```gherkin

Feature: Standard limit order

# In order to ease the scenario's writing and reading, standard are defined with
# default and pertinent values.
#```ditaa
#
# /----------\ 1       /--------------\          1 /----------\
# | Broker   |<--------|  Limit Order |----------->| <<enum>> |
# +----------+         +--------------+            |   Side   |
# | + name   |         | + quantity   |            +----------+
# \----------/         | + limit  cGRE|            | buy      |
#                      \--------------/            | sell     |
#                                                  \----------/
#```

@standard @limitOrder
Scenario: Standard sell limit order

  Given a standard sell limit order from broker A
   Then the order should have the following specifics:
     | Broker | Qty | Price | Side |
     | A      | 100 | 10.0  | Sell |

@standard @limitOrder
Scenario: Standard sell limit order

  Given a standard buy limit order from broker A
   Then the order should have the following specifics:
     | Broker | Qty | Price | Side |
     | A      | 100 | 10.0  | Buy  |

```

