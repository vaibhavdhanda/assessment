package com.kraken.tests;

import com.kraken.engine.TestEngine;
import org.testng.annotations.*;


public class OhlcTests {
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

    @Test(dataProvider = "ticker", enabled = true)
    public void validateSymbolIsCorrect(String ticker) {
        testEngine.subscribeOhlc(ticker);
        testEngine.validateSymbolInOhlcMessages(ticker);
        testEngine.unSubscribeOhlc(ticker);
    }

    @Test(dataProvider = "ticker", enabled = true)
    public void validateEndTimeIsAfterBeginTime(String ticker) {
        testEngine.subscribeOhlc(ticker);
        testEngine.validateEndTimeIsAfterBeginTime();
        testEngine.unSubscribeOhlc(ticker);
    }

    @Test(dataProvider = "ticker", enabled = true)
    public void validateOpenAndClosePricesAreWithInDailyHighAndLow(String ticker) {
        testEngine.subscribeOhlc(ticker);
        testEngine.validateOhlcOpenAndClosePricesAreWithInDailyHighAndLow();
        testEngine.unSubscribeOhlc(ticker);
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
