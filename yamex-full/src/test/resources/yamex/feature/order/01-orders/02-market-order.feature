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
  Scenario: Pass a Sell Market order

    Given an empty order book
    When a market order is placed to sell 150 FFLY
    Then the order book should be updated with this new order

  @marketOrder @placeOrder
  Scenario: Pass a Buy Market order

    Given an empty order book
    When a market order is placed to buy 150 FFLY
    Then the order book should be updated with this new order
