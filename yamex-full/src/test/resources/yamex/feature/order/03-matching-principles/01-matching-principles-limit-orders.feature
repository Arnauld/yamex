Feature: Matching Principles for limit orders

# When a new order (or quote) is entered, the Yamex system first checks the limits of al
# orders contained in the central order book. If the incoming order is immediately executable,
# meaning it is capable of being matched against an existing order or orders, one or more
# transactions are generated.
#
# To be immediately executable, the order must be:
#
#* A market order, where opposite already exist in the central order book;
#* an order to buy at a price at or above the lowest offer in the central order book;
#* an order to sell at a price at or below the highest bid in the book.
#
# The orders already present in the order book are always executed at their specified limit price.
# Price improvements for orders in the order book are only possible during an auction process -
# opening or closing auction. Orders going into the order book are always matched at the appropriate
# prices available in the order book, up to the specified limit price.

  @orderBook @matchingPrinciple @limitOrder
  Scenario: Matching a Buy order partially - exact same price

    #
    # Order book contains already two sell orders.
    #
    # The buy order triggered is not fully fulfilled, thus remains in the order book but with only the missing quantity.
    #
    # But an execution is triggered for the partial fulfillment
    #
    # **Alternate scenario?**
    #
    #{width:100%}
    #```cucumber
    #  Given an order book containing two sell orders: 15@10.4 by B1 and 150@11.9 by B2
    #  When a buy order is placed 20@10.4 by BBuyer
    #  Then the buy order should remain in the order book with the remaining quantity of 5@10.4
    #  But an execution should have been triggered: [B1->BBuyer 15@10.4]
    #```

    Given an empty order book
    And the following orders have been placed:
      | Broker | order type  | way  | qty | price |
      | B1     | Limit Order | Sell | 15  | 10.4  |
      | B2     | Limit Order | Sell | 150 | 11.9  |
    When a limit order is placed to buy 20 FFLY at 10.4€ by "Broker-A"

    Then the order book should be composed of the following orders:
      | Broker   | order type  | way  | qty | price |
      | Broker-A | Limit Order | Buy  | 5   | 10.4  |
      | B2       | Limit Order | Sell | 150 | 11.9  |
    And the following execution should have been triggered:
      | seller broker | buyer broker | qty | price |
      | B1            | Broker-A     | 15  | 10.4  |


  @orderBook @matchingPrinciple @limitOrder
  Scenario: Matching a Buy order partially - higher buy price

    # In the case of limit orders, orders with the best possible prices:
    #
    #* highest price limit for buy orders
    #* lowest price limit for sell orders
    #
    # always take precedence in the matching process over other orders
    # with worse prices. Again, if the limit orders have the same price limit,
    # the criterion used for establishing matching priority is the order timestamp.
    #
    #  If not executed upon entry, an order is held in the central order book.

    Given an empty order book
    And the following orders have been placed:
      | Broker | order type  | way  | qty | price |
      | B1     | Limit Order | Sell | 15  | 10.4  |
    When a limit order is placed to buy 20 FFLY at 11.4€ by "Broker-A"

    Then the order book should be composed of the following orders:
      | Broker   | order type  | way | qty | price |
      | Broker-A | Limit Order | Buy | 5   | 11.4  |
    And the following execution should have been triggered:
      | seller broker | buyer broker | qty | price |
      | B1            | Broker-A     | 15  | 11.4  |


  @orderBook @matchingPrinciple @limitOrder
  Scenario: Matching a Sell order partially - exact same price

    Given an empty order book
    And the following orders have been placed:
      | Broker | order type  | way | qty | price |
      | B1     | Limit Order | Buy | 150 | 10.4  |
      | B2     | Limit Order | Buy | 15  | 11.9  |
    When a limit order is placed to sell 20 FFLY at 11.9€ by "Broker-A"

    Then the order book should be composed of the following orders:
      | Broker   | order type  | way  | qty | price |
      | Broker-A | Limit Order | Sell | 5   | 11.9  |
      | B1       | Limit Order | Buy  | 150 | 10.4  |
    And the following execution should have been triggered:
      | seller broker | buyer broker | qty | price |
      | Broker-A      | B2           | 15  | 11.9  |


  @orderBook @matchingPrinciple @limitOrder
  Scenario: Matching a Sell order against multiple Buy orders - higher buy price fulfilled first

    # Orders may not necessarily be executed at a single price, but may generate several
    # partial transactions at different prices. When a large order executes against the
    # total available quantity at a given price level, the next best price level becomes
    # best. This process continues as long as the incoming order remains executable.
    #
    # Also, it is possible for a single order to generate multiple executions at different
    # points in time. For example, an order may generate a partial execution upon entry, while
    # the remaining open order remains in the order book. The open portion may get executed a
    # minute later, an hour later, or even a day later, if its validity extends beyond the
    # current trading day.

    Given an empty order book
    And the following orders have been placed:
      | Broker | order type  | way | qty | price |
      | B1     | Limit Order | Buy | 150 | 10.4  |
      | B2     | Limit Order | Buy | 15  | 11.9  |
      | B3     | Limit Order | Buy | 15  | 12.3  |
    When a limit order is placed to sell 20 FFLY at 11.9€ by "Broker-A"

    Then the order book should be composed of the following orders:
      | Broker | order type  | way | qty | price |
      | B1     | Limit Order | Buy | 150 | 10.4  |
      | B2     | Limit Order | Buy | 10  | 11.9  |
    And the following executions should have been triggered:
      | seller broker | buyer broker | qty | price |
      | Broker-A      | B3           | 15  | 12.3  |
      | Broker-A      | B2           | 5   | 11.9  |


  @orderBook @matchingPrinciple @limitOrder
  Scenario: Matching a Sell order against multiple Buy orders - buys fulfilled in fifo

    # When an order (or quote) is entered into the order book, it is assigned a timestamp.
    # This timestamp is used to prioritize orders in the book with the same price - the order
    # entered earliest at a given price limit gets executed first (FIFO).

    Given an empty order book
    And the following orders have been placed in order:
      | Broker | order type  | way | qty | price |
      | B1     | Limit Order | Buy | 150 | 10.4  |
      | B2     | Limit Order | Buy | 15  | 12.3  |
      | B3     | Limit Order | Buy | 15  | 11.9  |
      | B4     | Limit Order | Buy | 15  | 12.3  |
    When a limit order is placed to sell 20 FFLY at 11.9€ by "Broker-A"

    Then the order book should be composed of the following orders:
      | Broker | order type  | way | qty | price |
      | B1     | Limit Order | Buy | 150 | 10.4  |
      | B3     | Limit Order | Buy | 15  | 11.9  |
      | B4     | Limit Order | Buy | 10  | 12.3  |
    And the following executions should have been triggered:
      | seller broker | buyer broker | qty | price |
      | Broker-A      | B2           | 15  | 12.3  |
      | Broker-A      | B4           | 5   | 12.3  |
