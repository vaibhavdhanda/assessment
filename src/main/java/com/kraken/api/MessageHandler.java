package com.kraken.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.testng.internal.collections.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kraken.utils.JsonHelper.getEventFromMessage;

public class MessageHandler {
    private static final Logger LOGGER = LogManager.getLogger(MessageHandler.class);
    private static final Pair<Integer, TimeUnit> DEFAULT_MESSAGE_TIMEOUT = Pair.of(20, TimeUnit.SECONDS);
    private static final Pair<Integer, TimeUnit> TRADE_MESSAGE_TIMEOUT = Pair.of(45, TimeUnit.SECONDS);
    private List<String> allMessages = new ArrayList<>();
    private List<String> allSystemStatusMessages = new ArrayList<>();
    private List<String> allSubscriptionStatusMessages = new ArrayList<>();
    private List<String> allTickerMessages = new ArrayList<>();
    private List<String> allTradeMessages = new ArrayList<>();
    private List<String> allOrderBookMessages = new ArrayList<>();
    private List<String> allOhlcMessages = new ArrayList<>();

    public void handleMessage(String message) {
        String event = getEventFromMessage(message);
        allMessages.add(message);
        switch (event) {
            case "systemStatus":
                LOGGER.info("systemStatus: " + message);
                allSystemStatusMessages.add(message);
                break;
            case "subscriptionStatus":
                LOGGER.info("subscriptionStatus: " + message);
                allSubscriptionStatusMessages.add(message);
                break;
            case "ticker":
                LOGGER.info("ticker: " + message);
                allTickerMessages.add(message);
                break;
            case "ohlc":
                LOGGER.info("ohlc: " + message);
                allOhlcMessages.add(message);
                break;
            case "trade":
                LOGGER.info("trade: " + message);
                allTradeMessages.add(message);
                break;
            case "book":
                LOGGER.info("book: " + message);
                allOrderBookMessages.add(message);
                break;
            case "heartbeat":
                break;
            default:
                LOGGER.error("Invalid event in message" + event + ". Message =  " + message);
                throw new IllegalArgumentException("Invalid event in message" + event + ". Message =  " + message);
        }

    }

    public List<String> getAllTickerMessages() {
        Awaitility.with()
                .alias("Wait for TICKER message.")
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .atMost(DEFAULT_MESSAGE_TIMEOUT.first(), DEFAULT_MESSAGE_TIMEOUT.second())
                .until(() -> allTickerMessages.size(),
                        Matchers.greaterThan(0));
        return new ArrayList<>(allTickerMessages);
    }

    public List<String> getAllSubscriptionStatusMessages() {
        Awaitility.with()
                .alias("Wait for Subscription Status message.")
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .atMost(DEFAULT_MESSAGE_TIMEOUT.first(), DEFAULT_MESSAGE_TIMEOUT.second())
                .until(() -> allSubscriptionStatusMessages.size(),
                        Matchers.greaterThan(0));
        return new ArrayList<>(allSubscriptionStatusMessages);
    }

    public List<String> getAllOhlcMessages() {
        Awaitility.with()
                .alias("Wait for OHLC messages.")
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .atMost(DEFAULT_MESSAGE_TIMEOUT.first(), DEFAULT_MESSAGE_TIMEOUT.second())
                .until(() -> allOhlcMessages.size(),
                        Matchers.greaterThan(0));
        return new ArrayList<>(allOhlcMessages);
    }

    public List<String> getAllTradeMessages() {
        Awaitility.with()
                .alias("Wait for TRADE messages.")
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .atMost(TRADE_MESSAGE_TIMEOUT.first(), TRADE_MESSAGE_TIMEOUT.second())
                .until(() -> allTradeMessages.size(),
                        Matchers.greaterThan(0));
        return new ArrayList<>(allTradeMessages);
    }

    public List<String> getAllOrderBookMessages() {
        Awaitility.with()
                .alias("Wait for BOOK messages.")
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .atMost(DEFAULT_MESSAGE_TIMEOUT.first(), DEFAULT_MESSAGE_TIMEOUT.second())
                .until(() -> allOrderBookMessages.size(),
                        Matchers.greaterThan(0));
        return new ArrayList<>(allOrderBookMessages);
    }

    public void cleanupAllMessages() {
        allMessages.clear();
        allSystemStatusMessages.clear();
        allSubscriptionStatusMessages.clear();
        allTickerMessages.clear();
        allOhlcMessages.clear();
        allTradeMessages.clear();
        allOrderBookMessages.clear();
    }
}
