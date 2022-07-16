package com.kraken.tests;

import org.testng.annotations.*;


public class TickerTests extends BaseTest {

    @Parameters({ "apiUrl" })
    public TickerTests(String apiUrl) {
        super(apiUrl);
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

}
