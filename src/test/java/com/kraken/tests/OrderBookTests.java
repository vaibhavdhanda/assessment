package com.kraken.tests;

import com.kraken.engine.TestEngine;
import org.testng.annotations.*;


public class OrderBookTests {
    private final static String URL = "wss://ws.kraken.com";
    TestEngine testEngine;

    @DataProvider(name = "ticker")
    public Object[][] dataProviderMethod() {
        return new Object[][] { { "XBT/USD" }, { "XBT/EUR" } };
    }

    @BeforeClass
    private void beforeClass() throws Exception {
        testEngine = new TestEngine(URL);
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

    @AfterMethod(alwaysRun = true)
    public void cleanupAllMessages(){
        testEngine.cleanupAllMessages();
    }

    @AfterClass(alwaysRun = true)
    private void afterClass() {
        testEngine.logout();
    }
}
