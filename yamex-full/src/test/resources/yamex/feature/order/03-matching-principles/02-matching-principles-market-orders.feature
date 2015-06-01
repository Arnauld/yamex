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
