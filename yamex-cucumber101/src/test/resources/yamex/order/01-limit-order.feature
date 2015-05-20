Feature: Limit Order

  Scenario: Pass a Buy limit order

    Given an empty order book
     When a buy limit order is passed for 150 FFLY at 10.4€
     Then the order book should be updated with this new order

  Scenario: Pass a Sell limit order

    Given an empty order book
     When a sell limit order is passed for 150 FFLY at 10.4€
     Then the order book should be updated with this new order