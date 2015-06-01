Feature: Control invariants

  Scenario: High volume of orders

    Given an empty order book
    When 1500 of (limit/market) orders are placed to (sell/buy) <any> qty at <any>€
    Then no sell market order should remain if there are any buy limit orders remaining
    And no buy market order should remain if there are any sell limit orders remaining
    And (∑ quantities in order book) + (∑ quantities in executions) should be identical to (∑ quantities in placed orders)

