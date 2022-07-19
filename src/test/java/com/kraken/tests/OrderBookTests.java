package com.kraken.tests;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class OrderBookTests extends BaseTest {

    @Parameters({"apiUrl"})
    public OrderBookTests(String apiUrl) {
        super(apiUrl);
    }

    @Test(dataProvider = "ticker")
    public void validateSymbolIsCorrect(String ticker) {
        testEngine.subscribeOrderBook(ticker);
        testEngine.validateSymbolInOrderBookMessages(ticker);
        testEngine.unSubscribeOrderBook(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validateSortingOfOrdersInBookSnapshot(String ticker) {
        testEngine.subscribeOrderBook(ticker);
        testEngine.validateSortingOfOrdersInBookSnapshot();
        testEngine.unSubscribeOrderBook(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validateBestAskIsGreaterThanBestBidInBookSnapshot(String ticker) {
        testEngine.subscribeOrderBook(ticker);
        testEngine.validateBestAskIsGreaterThanBestBidInBookSnapshot();
        testEngine.unSubscribeOrderBook(ticker);
    }

}
