package com.kraken.tests;

import com.kraken.engine.TestEngine;
import org.testng.annotations.*;


public class TradeTests {
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
        testEngine.subscribeTrade(ticker);
        testEngine.validateSymbolInTradeMessages(ticker);
        testEngine.unSubscribeTrade(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validatePriceAndVolumeAreNotZero(String ticker) {
        testEngine.subscribeTrade(ticker);
        testEngine.validatePriceAndVolumeAreNotZero();
        testEngine.unSubscribeTrade(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validateTriggeringOrderSideIsCorrect(String ticker) {
        testEngine.subscribeTrade(ticker);
        testEngine.validateTriggeringOrderSideIsCorrect();
        testEngine.unSubscribeTrade(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validateTriggeringOrderTypeIsCorrect(String ticker) {
        testEngine.subscribeTrade(ticker);
        testEngine.validateTriggeringOrderTypeIsCorrect();
        testEngine.unSubscribeTrade(ticker);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupAllMessages() {
        testEngine.cleanupAllMessages();
    }

    @AfterClass(alwaysRun = true)
    private void afterClass() {
        testEngine.logout();
    }
}
