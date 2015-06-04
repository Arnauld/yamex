@wip @arnauld
Feature: Reading Order Single message

#  The new order message type is used by institutions wishing to electronically
#  submit securities and forex orders to a broker for execution.

  Background:
    Given the FIX message delimiter has been set to "|"

  @fix42
  Scenario: BUY 100 CVS MKT DAY - alternative 1

    Given the following FIX message:
    """
    8=FIX.4.2|9=145|35=D|34=4|49=ABC_DEFG01|52=20090323-15:40:29|56=CCG|
    115=XYZ|11=NF 0542/03232009|54=1|38=100|55=CVS|40=1|59=0|47=A|
    60=20090323-15:40:29|21=1|207=N|10=139|
    """
    When the FIX message is parsed
    Then the FIX message should match:
      | Tag | Value             | Tag Name         |
      | 8   | FIX.4.2           | BeginString      |
      | 9   | 145               | BodyLength       |
      | 10  | 139               | CheckSum         |
      | 11  | NF 0542/03232009  | ClOrdID          |
      | 21  | 1                 | HandlInst        |
      | 34  | 4                 | MsgSeqNum        |
      | 35  | D                 | MsgType          |
      | 38  | 100               | OrderQty         |
      | 40  | 1                 | OrdType          |
      | 47  | A                 | Rule80A          |
      | 49  | ABC_DEFG01        | SenderCompID     |
      | 52  | 20090323-15:40:29 | SendingTime      |
      | 54  | 1                 | Side             |
      | 55  | CVS               | Symbol           |
      | 56  | CCG               | TargetCompID     |
      | 59  | 0                 | TimeInForce      |
      | 60  | 20090323-15:40:29 | TransactTime     |
      | 115 | XYZ               | OnBehalfOfCompID |
      | 207 | N                 | SecurityExchange |

  @fix42
  Scenario: BUY 100 CVS MKT DAY - alternative 2

    When the following FIX message is parsed
    """
    8=FIX.4.2|9=145|35=D|34=4|49=ABC_DEFG01|52=20090323-15:40:29|56=CCG|
    115=XYZ|11=NF 0542/03232009|54=1|38=100|55=CVS|40=1|59=0|47=A|
    60=20090323-15:40:29|21=1|207=N|10=139|
    """
    Then the FIX message should match:
      | Tag | Value             | Tag Name         |
      | 8   | FIX.4.2           | BeginString      |
      | 9   | 145               | BodyLength       |
      | 10  | 139               | CheckSum         |
      | 11  | NF 0542/03232009  | ClOrdID          |
      | 21  | 1                 | HandlInst        |
      | 34  | 4                 | MsgSeqNum        |
      | 35  | D                 | MsgType          |
      | 38  | 100               | OrderQty         |
      | 40  | 1                 | OrdType          |
      | 47  | A                 | Rule80A          |
      | 49  | ABC_DEFG01        | SenderCompID     |
      | 52  | 20090323-15:40:29 | SendingTime      |
      | 54  | 1                 | Side             |
      | 55  | CVS               | Symbol           |
      | 56  | CCG               | TargetCompID     |
      | 59  | 0                 | TimeInForce      |
      | 60  | 20090323-15:40:29 | TransactTime     |
      | 115 | XYZ               | OnBehalfOfCompID |
      | 207 | N                 | SecurityExchange |

  @fix42
  Scenario: BUY 100 CVS MKT DAY - alternative 3

    When the following FIX message is received
    """
    8=FIX.4.2|9=145|35=D|34=4|49=ABC_DEFG01|52=20090323-15:40:29|56=CCG|
    115=XYZ|11=NF 0542/03232009|54=1|38=100|55=CVS|40=1|59=0|47=A|
    60=20090323-15:40:29|21=1|207=N|10=139|
    """
    Then a market order to buy 100 CVS should have been triggered
