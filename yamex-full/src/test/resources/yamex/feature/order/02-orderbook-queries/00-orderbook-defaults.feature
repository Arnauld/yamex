Feature: Default order placed


  @default
  Scenario: Default Order

    Given an empty order book
    And the following orders have been placed:
      | way  | qty | price |
      | Buy  | 150 | 8.4   |
      | Buy  | 15  | 9.9   |
      | Sell |     | 12.4  |
      | Sell |     |       |

    Then the order book should be composed of the following orders:
      | order type  | way  | qty | price |
      | Limit Order | Buy  | 150 | 8.4   |
      | Limit Order | Buy  | 15  | 9.9   |
      | Limit Order | Sell | 100 | 12.4  |
      | Limit Order | Sell | 100 | 10.1  |
