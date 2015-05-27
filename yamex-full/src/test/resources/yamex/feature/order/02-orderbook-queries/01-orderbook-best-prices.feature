Feature: Order Book Best Prices

# In the case of limit orders, orders with the best possible prices:
#
# * highest price limit for buy orders
# * lowest price limit for sell orders
#
#* **Bid** - the price in a buy order
#* **Ask** - the price in a sell order

  @default @orderBook @bestPrices
  Scenario: Best bid price - limit orders only

    Given an empty order book
    And the following orders have been placed:
      | instrument | order type  | way  | qty | price |
      | FFLY       | Limit Order | Buy  | 150 | 10.4  |
      | FFLY       | Limit Order | Buy  | 15  | 11.9  |
      | FFLY       | Limit Order | Sell | 15  | 12.9  |
    Then the order book's best bid price should be 11.9€

  @orderBook @bestPrices
  Scenario: Best bid price - limit orders and market orders

    Given an empty order book
    And the following orders have been placed:
      | instrument | order type   | way  | qty | price |
      | FFLY       | Market Order | Buy  | 150 |       |
      | FFLY       | Limit Order  | Buy  | 150 | 10.4  |
      | FFLY       | Limit Order  | Buy  | 15  | 11.9  |
      | FFLY       | Limit Order  | Sell | 15  | 12.9  |
    Then the order book's best bid price should be 11.9€

  @orderBook @bestPrices @limitOrder
  Scenario: Best ask price - limit orders only

    Given an empty order book
    And the following orders have been placed:
      | instrument | order type  | way  | qty | price |
      | FFLY       | Limit Order | Sell | 150 | 11.9  |
      | FFLY       | Limit Order | Sell | 15  | 10.4  |
      | FFLY       | Limit Order | Buy  | 15  | 10.1  |
    Then the order book's best ask price should be 10.4€

  @orderBook @bestPrices @limitOrder
  Scenario: Best ask price - limit orders and market orders

    Given an empty order book
    And the following orders have been placed:
      | instrument | order type   | way  | qty | price |
      | FFLY       | Market Order | Sell | 150 |       |
      | FFLY       | Limit Order  | Sell | 150 | 11.9  |
      | FFLY       | Limit Order  | Sell | 15  | 10.4  |
      | FFLY       | Limit Order  | Buy  | 15  | 12.9  |
    Then the order book's best ask price should be 10.4€

  @orderBook @bestPrices @limitOrder @stopOrder
  Scenario: Best ask price - limit orders and stop orders

    #  Stop order should not be taken into account for best price.

    Given an empty order book
    And the following orders have been placed:
      | instrument | order type  | way  | qty | price |
      | FFLY       | Stop Order  | Sell | 150 | 10.1  |
      | FFLY       | Limit Order | Sell | 150 | 11.9  |
      | FFLY       | Limit Order | Sell | 15  | 10.4  |
      | FFLY       | Limit Order | Buy  | 15  | 10.0  |
    Then the order book's best ask price should be 10.4€