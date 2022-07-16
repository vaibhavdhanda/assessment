package com.kraken.tests;

import org.testng.annotations.*;


public class OhlcTests extends BaseTest {

    @Parameters({ "apiUrl" })
    public OhlcTests(String apiUrl) {
        super(apiUrl);
    }

    @Test(dataProvider = "ticker")
    public void validateSymbolIsCorrect(String ticker) {
        testEngine.subscribeOhlc(ticker);
        testEngine.validateSymbolInOhlcMessages(ticker);
        testEngine.unSubscribeOhlc(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validateEndTimeIsAfterBeginTime(String ticker) {
        testEngine.subscribeOhlc(ticker);
        testEngine.validateEndTimeIsAfterBeginTime();
        testEngine.unSubscribeOhlc(ticker);
    }

    @Test(dataProvider = "ticker")
    public void validateOpenAndClosePricesAreWithInDailyHighAndLow(String ticker) {
        testEngine.subscribeOhlc(ticker);
        testEngine.validateOhlcOpenAndClosePricesAreWithInDailyHighAndLow();
        testEngine.unSubscribeOhlc(ticker);
    }

}
