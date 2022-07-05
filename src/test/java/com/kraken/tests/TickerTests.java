package com.kraken.tests;

import com.kraken.engine.TestEngine;
import org.testng.annotations.*;


public class TickerTests {
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
        testEngine.subscribeTicker(ticker);
        testEngine.validateSymbolInTickerMessages(ticker);
        testEngine.unSubscribeTicker(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validateBestAskIsGreaterThanBestBid(String ticker) {
        testEngine.subscribeTicker(ticker);
        testEngine.validateAskPriceIsGreaterThanBidPrice();
        testEngine.unSubscribeTicker(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validateBidAndAskPricesAreWithInDailyHighAndLow(String ticker) {
        testEngine.subscribeTicker(ticker);
        testEngine.validateBidAndAskPricesAreWithInDailyHighAndLow();
        testEngine.unSubscribeTicker(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validateOpenAndClosePricesAreWithInDailyHighAndLow(String ticker) {
        testEngine.subscribeTicker(ticker);
        testEngine.validateOpenAndClosePricesAreWithInDailyHighAndLow();
        testEngine.unSubscribeTicker(ticker);
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
