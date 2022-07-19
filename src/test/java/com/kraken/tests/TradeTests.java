package com.kraken.tests;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class TradeTests extends BaseTest {

    @Parameters({"apiUrl"})
    public TradeTests(String apiUrl) {
        super(apiUrl);
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

}
