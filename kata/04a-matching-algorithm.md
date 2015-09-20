Fourth iteration: Matching Algorithm
================================================================================

The Order Matching Service tries to find a match to the buy or sell order. To find a match, we search all Active orders which match the specified price.

* If the order is a buy, we look for a price less than or equal.
* If the order is a sell, we look for a price greater than or equal.
* If it’s a market order, we find the highest (sell) or lowest (buy) price.

We sort the matches ascending for buy orders, and descending for sell orders. Then we sort by date if the price matches.
