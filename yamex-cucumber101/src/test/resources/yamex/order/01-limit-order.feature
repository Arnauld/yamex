Feature: Limit Order

  Scenario: Pass a Buy limit order

    Given an empty order book
    When a buy limit order is placed for 150 FFLY at 10.4€
    Then the order book should be updated with this new order

  Scenario: Pass a Sell limit order

    Given an empty order book
    When a sell limit order is placed for 150 FFLY at 10.4€
    Then the order book should be updated with this new order

  @ignore @arnauld
  Scenario: Pass a Market order

    Given an empty order book
    When a market order is placed for 150 FFLY
    Then the order book should be updated with this new order

  @wip @arnauld
  Scenario: Best bid price

    Given an empty order book
    And the following orders have been placed:
      | instrument | order type  | way  | qty | price |
      | FFLY       | Limit Order | Buy  | 150 | 10.4  |
      | FFLY       | Limit Order | Buy  | 15  | 11.9  |
      | FFLY       | Limit Order | Sell | 15  | 12.9  |
    Then the order book's best bid price should be 11.9€