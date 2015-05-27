Feature: Stop Order

#  A stop order, also referred to as a stop-loss order, is an order to buy or sell a stock once
#  the price of the stock reaches a specified price, known as the stop price. When the stop price
#  is reached, a stop order becomes a market order. A Stop order is not guaranteed a specific
#  execution price and may execute significantly away from its stop price.
#
#  A buy stop order is entered at a stop price above the current market price.
#  Investors generally use a buy stop order to limit a loss or to protect a profit on a stock
#  that they have sold short.
#
#  A sell stop order is entered at a stop price below the current market price.
#  Investors generally use a sell stop order to limit a loss or to protect a profit on a stock
#  that they own.

  @stopOrder @placeOrder
  Scenario: Place a Buy stop order

    Given an empty order book
    When a stop order is placed to buy 150 FFLY at 10.4€
    Then the order book should be updated with this new order

  @stopOrder @placeOrder
  Scenario: Place a Sell stop order

    Given an empty order book
    When a stop order is placed to sell 150 FFLY at 10.4€
    Then the order book should be updated with this new order

  @stopOrder @placeOrder
  @notImplemented
  Scenario: Buy stop order becomes a Market Order

    # **When the market price point is reached for a stop order, it is converted to a market order
    # and submitted to the market order queue**.

    Given an empty order book
    And  a stop order has been placed to buy 150 FFLY at 10.4€ by "Broker-B"
    When a limit order is placed to sell 100 FFLY at 10.3€ by "Broker-S"
    Then the order book should be composed of the following orders:
      | instrument | order type   | way | qty | price |
      | FFLY       | Market Order | Buy | 50  |       |
    And the following execution should have been triggered:
      | seller broker id | buyer broker id | qty |

