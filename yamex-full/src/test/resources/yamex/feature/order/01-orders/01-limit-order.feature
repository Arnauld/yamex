Feature: Limit Order

#  A limit order is an order to buy or sell a contract at a specific price or better.
#
#  A **buy** limit order can only be executed at the **limit price or lower**, and
#  a **sell** limit order can only be executed at the **limit price or higher**.
#
#  Use of a Limit order helps ensure that the customer will not receive an execution
#  at a price less favorable than the limit price. Use of a Limit order, however,
#  does not guarantee an execution.
#
#  * A buy limit order for AAPL at $125 will buy shares of IBM at $125 or less.
#  * A sell limit order for AAPL at $125 will sell shares of IBM for $125 or more.
#  * A limit order must have a Time in Force (TIF) value
#
#  In our system we support Limit Orders with various time in force parameters.
#  Fill-or-Kill (FoK), Immediate-or-Cancel (IoC) works the same way as for Market Orders.
#  Good-Till Date or Good-Till-Cancel and Day Orders are valid until specified time
#  requested by investor and cancelled after that.

  Scenario: Place a Buy Limit Order

    Given an empty order book
    When a limit order is placed to buy 150 FFLY at 10.4€
    Then the order book should be updated with this new order

  Scenario: Place a Sell Limit Order

    Given an empty order book
    When a limit order is placed to sell 150 FFLY at 10.4€
    Then the order book should be updated with this new order

  @default
  Scenario: Default Sell Limit Order

    Given a default limit order to sell
    Then the order should have the following properties:
      | order type  | way  | instrument | qty | price | time in force |
      | Limit Order | Sell | FFLY       | 100 | 10.1  | none          |

  @notImplemented
  Scenario Outline: Time in Force - Accepted parameter

    When one try to place a default limit order with the following specifics:
      | way  | time in force   |
      | sell | <time-in-force> |
    Then the order book should be updated with this new order

  Examples:
    | time-in-force |
    | fok           |
    | iok           |
    | gtd           |
    | gtc           |

