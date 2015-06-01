Feature: Order Book Accumulative Quantities

  Spreading information : Share market collects every type of information in respect of
  the listed companies. Generally, this information is published or in case of need
  anybody can get it from the stock exchange free of any cost.
  In this way, the stock exchange guides the investors by providing various types of
  information. Consequently, the number of shareholders in companies is increasing
  continuously. Thus, the stock exchanges are playing a vital role in ensuring wider
  share ownership.

  @orderBook @cumulativeView @limitOrder
  Scenario: Cumulative Totals of Bids and Offers

    Given an empty order book
    And the following orders have been placed:
      | way  | qty | price |
      | Buy  | 150 | 47    |
      | Buy  | 70  | 46    |
      | Buy  | 100 | 45    |
      | Buy  | 1   | 44    |
      | Buy  | 30  | 43    |
      | Sell | 226 | 48    |
      | Sell | 1   | 49    |
      | Sell | 100 | 50    |
      | Sell | 30  | 51    |
    Then the order book's view should look like:
      | Sum of Bids | Bid Qty | Bid Price | Ask Price | Ask Qty | Sum of Asks |
      | 150         | 150     | 47        | 48        | 226     | 226         |
      | 220         | 70      | 46        | 49        | 1       | 227         |
      | 320         | 100     | 45        | 50        | 100     | 327         |
      | 321         | 1       | 44        | 51        | 30      | 357         |
      | 351         | 30      | 43        |           |         |             |
    And the order book's available quantities for sell under 50.5€ should be 327
    And the order book's available quantities for buy over 44.5€ should be 320
