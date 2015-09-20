Second Iteration: Order Book and timestamp
================================================================================


Then, there’s the order book, which keeps track of buyers’ and sellers’ interests. At every price level, this book records open 'buy' and 'sell' orders, including their cumulative sizes.

When an order is entered into the order book, it is assigned a timestamp. This timestamp can be used to prioritize orders, when other criteria are identical, the order entered earliest gets executed first.

## Questions?

* one book for sell, one for buy?
