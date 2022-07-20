## Test Cases

### Ticker:

* 1 - Validate symbol is correct in incoming Ticker messages i.e. same as the one user subscribed with.
* 2 - Validate Best Ask price is greater than best Bid price
* 3 - Validate Bid and Ask prices are between Daily High and Low.
* 4 - Validate Open and close prices are between Daily High and Low.

### Trade:

* 5 - Validate symbol is correct in incoming Trade messages i.e. same as the one user subscribed with.
* 6 - Validate price and volume are not zero in incoming messages.
* 7 - Validate the side of triggering order is always 'b' (Buy) or 's' (Sell).
* 8 - Validate the type of triggering order is always 'l' (Limit) or 'm' (Market).

### Book (Only Snapshot messages):

* 9 - Validate symbol is correct in incoming Book messages i.e. same as the one user subscribed with.
* 10 - Validate sorting of orders based on price is correct for both Bid and Ask sides.
* 11 - Validate that best Ask price is greater than Best Bid price in book snapshot message.

### OHLC:

* 12 - Validate symbol is correct in incoming OHLC messages i.e. same as the one user subscribed with.
* 13 - Validate End time is always after Begin time in incoming messages.
* 14 - Validate Open and close prices are between Daily High and Low.

Please note: Currently the data provider for all tests class has two currency pairs i.e. "XBT/USD", "XBT/EUR". That's
why all the above listed 14 test methods run twice i.e. once for each pair. That is the reason why we also see 28 (14 *
2) tests executed in tests results.

## Automation approach:

### Data driven test

Each test class has its own Data provider which is a list of symbols to be used for running these tests. So, without
making any other changes these tests could be run with collections of symbols. The tests have been kept in separate
tests classes each for individual API/Subscription topic.

### Typical flow:

* 1 - Connect to websocket (Once per test class)
* 2 - Subscribe to a topic (Trade/Ticker/OHLC etc.)
* 3 - Validate Subscription status message
* 4 - Wait for the messages to arrive (Currently only waiting for one message but this could be changed easily)
* 5 - Collect different types of messages in different collections in Message Handler
* 6 - Perform validation
* 7 - Un-subscribe from the topic
* 8 - Validate Un-Subscription status message
* 9 - Clear all existing messages
* Repeat steps 2-9 for all individual tests
* 10 - Close websocket connection.

## Executing Automated Tests

### Through docker:

* Unzip zip file to local directory
* Open Command prompt and navigate to project root and run following commands
    * `docker build --no-cache -t my-image:1 -f ./Dockerfile .`
    * `docker run -it --rm my-image:1 /bin/sh`
    * After hitting enter on second command type/paste:
        * For PROD: `mvn clean test -B -P prod-websocket-api-regression-tests -DskipTests=false`
        * For BETA: `mvn clean test -B -P beta-websocket-api-regression-tests -DskipTests=false`

Execution Timings

* After the test run you type in these commands to view results:
    * `cd /target/surefire-reports && less TEST-TestSuite.xml | grep "testcase name"`
* this will show time taken to tun each individual case

### Directly from IDE as TestNG suite:

The tests could also be run directly from IDE by running TestNG suite file which can be found here:

* For PROD: src/test/resources/regression-suite-prod.xml
* For BETA: src/test/resources/regression-suite-beta.xml

### Using maven:

* Go to project root directory and run one of the following mvn commands:
    * For running test against PROD:`mvn clean test -B -P prod-websocket-api-regression-tests -DskipTests=false`
    * For running test against BETA:`mvn clean test -B -P beta-websocket-api-regression-tests -DskipTests=false`

### Prerequisites

* Java 1.8 or above
* Maven (3.6 +)

### Dependencies can be found under project pom.xml

* testng (version 7.4.0)
* javax.websocket-api (version 1.1)
* javax.json-api (version 1.1)
* javax.json (version 1.1)
* tyrus-standalone-client (version 1.9)
* awaitility (version 4.0.3)
* surefire-testng (version 3.0.0-M5)

## Improvement areas

### Logging:

Embarrassingly that was the area where I spent most of my time. But just could not get the simple log4j logging working.
Things which is usual day-to-day life work out of the box but for some reason did not work for me yesterday. Current
programmatic logging is horrible should be replaced with something more robust instead of polluting all the supporting
classes with the same logging related code.

### Environment:

Currently, the environment URL is hardcoded in the individual test classes and should ideally come from a config file
without the tester having to manually update it in all the test classes.

### Test summary:

Currently, you have to do an extra step to grep the test reports file, but it should ideally be printed out of the box
after the executions has finished.

## Functional challenges

With trades API we get the messages as the trades are happening in the environment. Sometimes when there's very few
trades happening in the environment (around 3 AM CEST for me) the framework just keeps waiting until a timeout of 20
secs (configurable). This makes the overall test execution seem to be awfully slow although the tests are just waiting
for the message to arrive before validation. This can also cause intermittent failures when there are no trades messages
received within the configured time (20 secs currently)

## What more could be done?

### Additional tests for book 'update' messages.

Collect all book update messages in between two snapshot messages and then validate the second snapshot messages

### Tests with combination of topics

Subscribe to Trades API and as a new trade is received - validate the changes should also be reflected in Book
messages (Change in spread, Volume for one of the sides, spread etc.)

### Validate messages on topics along with placing trades from another interface API or UI

Currently, different messages are triggered by actual/test users trading in the environment. It would be interesting to
have a controlled environment (to make updates deterministic) and then perform trades/order cancellation/Amendments and
validating the corresponding messages are received on each of the topics.