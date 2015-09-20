```gherkin
Feature: Market Order

#  A market order is an order to buy or sell a contract at the best available price.  Generally,
#  this type of order will be executed immediately.  However, the price at which a market order
#  will be executed is not guaranteed.  It is important for traders to remember that the last-traded
#  price is not necessarily the price at which a market order will be executed.  In fast-moving
#  markets, the price at which a market order will execute often deviates from the last-traded
#  price or “real time” quote.
#
#  Market Orders can have Fill-or-Kill (FoK) and Immediate or Cancel (IoC) parameters.
#  The first one means that orders either will be filled completely or cancelled in case of
#  insufficient liquidity. IoC order by contrast will be filled up to the existing level of
#  liquidity and the resting part will be cancelled.

  @marketOrder @placeOrder
  Scenario: Place a Sell Market order

    Given an empty order book
    When a market order is placed to sell 150 FFLY
    Then the order book should be updated with this new order

  @marketOrder @placeOrder
  Scenario: Place a Buy Market order

    Given an empty order book
    When a market order is placed to buy 150 FFLY
    Then the order book should be updated with this new order
```

```gherkin
Feature: Matching Principles for market orders

  @orderBook @matchingPrinciple @marketOrder
  Scenario: Matching a new Market Order to Buy against existing multiple sell limit orders

    Given an empty order book
    And the following orders have been placed:
      | Broker | order type  | way  | qty | price |
      | B1     | Limit Order | Sell | 15  | 10.4  |
      | B2     | Limit Order | Sell | 150 | 11.9  |
    When a market order is placed to buy 20 FFLY by "Broker-A"

    Then the order book should be composed of the following orders:
      | Broker | order type  | way  | qty | price |
      | B2     | Limit Order | Sell | 145 | 11.9  |
    And the following execution should have been triggered:
      | seller broker | buyer broker | qty | price |
      | B1            | Broker-A     | 15  | 10.4  |
      | B2            | Broker-A     | 5   | 11.9  |

  @orderBook @matchingPrinciple @marketOrder
  Scenario: Matching a new Limit Order to Buy against existing multiple sell orders market and limit

    Given an empty order book
    And the following orders have been placed:
      | Broker | order type   | way  | qty | price |
      | B1     | Limit Order  | Sell | 150 | 11.9  |
      | B2     | Market Order | Sell | 15  |       |
    When a market order is placed to buy 20 FFLY by "Broker-A"

    Then the order book should be composed of the following orders:
      | Broker | order type  | way  | qty | price |
      | B1     | Limit Order | Sell | 145 | 11.9  |
    And the following execution should have been triggered:
      | seller broker | buyer broker | qty | price |
      | B2            | Broker-A     | 15  | 11.9  |
      | B1            | Broker-A     | 5   | 11.9  |
```
