package com.kraken.engine;

import com.kraken.api.WebsocketClient;
import com.kraken.utils.JsonHelper;
import com.kraken.utils.MessageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.Iterator;
import java.util.List;

public class TestEngine {
	private static final Logger LOGGER = LogManager.getLogger(TestEngine.class);
	private static final int TICKER_FIELD_INDEX = 3;
	private static final int TICKER_PRICE_OBJECT_INDEX = 1;
	private static final int OHLC_PRICE_OBJECT_INDEX = 1;
	private static final int TRADE_INFO_OBJECT_INDEX = 1;
	private static final int TRADE_INFO_PRICE_INDEX = 0;
	private static final int TRADE_INFO_VOLUME_INDEX = 1;
	private static final int TRADE_INFO_SIDE_INDEX = 3;
	private static final int TRADE_INFO_ORDER_TYPE_INDEX = 4;
	private static final int OHLC_BEGIN_TIME_INDEX = 0;
	private static final int OHLC_END_TIME_INDEX = 1;
	private static final int OHLC_OPEN_PRICE_INDEX = 2;
	private static final int OHLC_HIGH_PRICE_INDEX = 3;
	private static final int OHLC_LOW_PRICE_INDEX = 4;
	private static final int OHLC_CLOSE_PRICE_INDEX = 5;
	private static final int BOOK_SYMBOL_INDEX = 3;
	private static final int BOOK_ORDERS_OBJECT_INDEX = 1;
	WebsocketClient websocketClient;

	public TestEngine(String apiUrl) {
		this.websocketClient = new WebsocketClient(apiUrl);
	}

	public void subscribeTicker(String ticker) {
		LOGGER.info("subscribeTicker ticker = " + ticker);
		websocketClient.sendMessage(MessageHelper.getTickerSubscriptionPayload(ticker));
		validateSubscriptionStatusMessageReceived(ticker);
	}

	public void unSubscribeTicker(String ticker) {
		LOGGER.info("unSubscribeTicker ticker = " + ticker);
		websocketClient.sendMessage(MessageHelper.getTickerUnSubscriptionPayload(ticker));
		validateUnSubscriptionStatusMessageReceived(ticker);
	}

	public void subscribeOhlc(String ticker) {
		LOGGER.info("subscribeOhlc for ticker = " + ticker);
		websocketClient.sendMessage(MessageHelper.getOhlcSubscriptionPayload(ticker));
		validateSubscriptionStatusMessageReceived(ticker);
	}

	public void unSubscribeOhlc(String ticker) {
		LOGGER.info("unSubscribeOhlc ticker = " + ticker);
		websocketClient.sendMessage(MessageHelper.getOhlcUnSubscriptionPayload(ticker));
		validateUnSubscriptionStatusMessageReceived(ticker);
	}

	public void subscribeTrade(String ticker) {
		LOGGER.info("subscribeTrade ticker = " + ticker);
		websocketClient.sendMessage(MessageHelper.getTradeSubscriptionPayload(ticker));
		validateSubscriptionStatusMessageReceived(ticker);
	}

	public void unSubscribeTrade(String ticker) {
		LOGGER.info("unSubscribeTrade ticker = " + ticker);
		websocketClient.sendMessage(MessageHelper.getTradeUnSubscriptionPayload(ticker));
		validateUnSubscriptionStatusMessageReceived(ticker);
	}

	public void subscribeSpread(String ticker) {
		LOGGER.info("subscribeSpread ticker = " + ticker);
		websocketClient.sendMessage(MessageHelper.getSpreadSubscriptionPayload(ticker));
		validateSubscriptionStatusMessageReceived(ticker);
	}

	public void subscribeOrderBook(String ticker) {
		LOGGER.info("subscribeOrderBook ticker = " + ticker);
		websocketClient.sendMessage(MessageHelper.getBookSubscriptionPayload(ticker));
		validateSubscriptionStatusMessageReceived(ticker);
	}

	public void unSubscribeOrderBook(String ticker) {
		LOGGER.info("unSubscribeOrderBook ticker = " + ticker);
		websocketClient.sendMessage(MessageHelper.getBookUnSubscriptionPayload(ticker));
		validateUnSubscriptionStatusMessageReceived(ticker);
	}

	public void validateSubscriptionStatusMessageReceived(String ticker) {
		List<String> messages = websocketClient.getMessageHandler().getAllSubscriptionStatusMessages();
		long count = messages.stream()
				.filter(m -> "subscribed".equals(JsonHelper.getJsonObjectFromString(m).getString("status"))
						&& ticker.equals(JsonHelper.getJsonObjectFromString(m).getString("pair")))
				.count();
		Assert.assertTrue(count > 0, "Subscription Status message not received!");
	}

	public void validateUnSubscriptionStatusMessageReceived(String ticker) {
		List<String> messages = websocketClient.getMessageHandler().getAllSubscriptionStatusMessages();
		long count = messages.stream()
				.filter(m -> "unsubscribed".equals(JsonHelper.getJsonObjectFromString(m).getString("status"))
						&& ticker.equals(JsonHelper.getJsonObjectFromString(m).getString("pair")))
				.count();
		Assert.assertTrue(count > 0, "Subscription Status message not received!");
	}

	public void validateBidAndAskPricesAreWithInDailyHighAndLow() {
		LOGGER.info("validateBidAndAskPricesAreWithInDailyHighAndLow");
		List<String> messages = websocketClient.getMessageHandler().getAllTickerMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonObject priceObject = jsonArray.getJsonObject(TICKER_PRICE_OBJECT_INDEX);
			double bestAskPrice = getDoubleFromString(priceObject.getJsonArray("a").getString(0));
			double bestBidPrice = getDoubleFromString(priceObject.getJsonArray("b").getString(0));
			double dailyHigh = getDoubleFromString(priceObject.getJsonArray("h").getString(0));
			double dailyLow = getDoubleFromString(priceObject.getJsonArray("l").getString(0));
			Assert.assertTrue(bestAskPrice <= dailyHigh && bestAskPrice >= dailyLow, "BestAskPrice is not between daily high and low: " + message);
			Assert.assertTrue(bestBidPrice <= dailyHigh && bestBidPrice >= dailyLow, "BestBidPrice is not between daily high and low: " + message);
		}
	}

	public void validateOpenAndClosePricesAreWithInDailyHighAndLow() {
		LOGGER.info("validateOpenAndClosePricesAreWithInDailyHighAndLow");
		List<String> messages = websocketClient.getMessageHandler().getAllTickerMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonObject priceObject = jsonArray.getJsonObject(TICKER_PRICE_OBJECT_INDEX);
			double dailyOpenPrice = getDoubleFromString(priceObject.getJsonArray("o").getString(0));
			double dailyClosePrice = getDoubleFromString(priceObject.getJsonArray("c").getString(0));
			double dailyHigh = getDoubleFromString(priceObject.getJsonArray("h").getString(0));
			double dailyLow = getDoubleFromString(priceObject.getJsonArray("l").getString(0));
			Assert.assertTrue(dailyOpenPrice <= dailyHigh && dailyOpenPrice >= dailyLow, "BestAskPrice is not between daily high and low: " + message);
			Assert.assertTrue(dailyClosePrice <= dailyHigh && dailyClosePrice >= dailyLow, "BestBidPrice is not between daily high and low: " + message);
		}
	}

	public void validateAskPriceIsGreaterThanBidPrice() {
		LOGGER.info("validateAskPriceIsGreaterThanBidPrice");
		List<String> messages = websocketClient.getMessageHandler().getAllTickerMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonObject priceObject = jsonArray.getJsonObject(TICKER_PRICE_OBJECT_INDEX);
			String bestAskPrice = priceObject.getJsonArray("a").getString(0);
			String bestBidPrice = priceObject.getJsonArray("b").getString(0);
			Assert.assertTrue(getDoubleFromString(bestAskPrice) > getDoubleFromString(bestBidPrice),
					"BestAskPrice is not greater than BestBidPrice in message: " + message);
		}
	}

	public void validateSymbolInTickerMessages(String ticker) {
		LOGGER.info("validateSymbolInTickerMessages");
		List<String> messages = websocketClient.getMessageHandler().getAllTickerMessages();
		for (String message : messages) {
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			String actualValue = jsonArray.getString(TICKER_FIELD_INDEX);
			assertEquals("ticker", actualValue, ticker);
		}
	}

	public void validateEndTimeIsAfterBeginTime() {
		LOGGER.info("validateEndTimeIsAfterBeginTime");
		List<String> messages = websocketClient.getMessageHandler().getAllOhlcMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonArray priceArray = jsonArray.getJsonArray(OHLC_PRICE_OBJECT_INDEX);
			String beginTime = priceArray.getString(OHLC_BEGIN_TIME_INDEX);
			String endTime = priceArray.getString(OHLC_END_TIME_INDEX);
			Assert.assertTrue(getDoubleFromString(endTime) > getDoubleFromString(beginTime),
					"OHLC: End Time is not greater than Begin Time in message: " + message);
		}
	}

	public void validateOhlcOpenAndClosePricesAreWithInDailyHighAndLow() {
		LOGGER.info("validateOhlcOpenAndClosePricesAreWithInDailyHighAndLow");
		List<String> messages = websocketClient.getMessageHandler().getAllOhlcMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonArray priceArray = jsonArray.getJsonArray(OHLC_PRICE_OBJECT_INDEX);
			double openPrice = getDoubleFromString(priceArray.getString(OHLC_OPEN_PRICE_INDEX));
			double closePrice = getDoubleFromString(priceArray.getString(OHLC_CLOSE_PRICE_INDEX));
			double lowPrice = getDoubleFromString(priceArray.getString(OHLC_LOW_PRICE_INDEX));
			double highPrice = getDoubleFromString(priceArray.getString(OHLC_HIGH_PRICE_INDEX));

			Assert.assertTrue(openPrice <= highPrice && openPrice >= lowPrice, "OHLC: Open Price is not within daily high and low prices message: " + message);
			Assert.assertTrue(closePrice <= highPrice && closePrice >= lowPrice,
					"OHLC: Close Price is not within daily high and low prices message: " + message);
		}
	}

	public void validateSymbolInOhlcMessages(String ticker) {
		LOGGER.info("validateSymbolInOhlcMessages");
		List<String> messages = websocketClient.getMessageHandler().getAllOhlcMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			String actualValue = jsonArray.getString(TICKER_FIELD_INDEX);
			assertEquals("ticker", actualValue, ticker);
		}
	}

	public void validateSymbolInTradeMessages(String ticker) {
		LOGGER.info("validateSymbolInTradeMessages");
		List<String> messages = websocketClient.getMessageHandler().getAllTradeMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			String actualValue = jsonArray.getString(TICKER_FIELD_INDEX);
			assertEquals("ticker", actualValue, ticker);
		}
	}

	public void validatePriceAndVolumeAreNotZero() {
		LOGGER.info("validatePriceAndVolumeAreNotZero");
		List<String> messages = websocketClient.getMessageHandler().getAllTradeMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonArray tradesArray = jsonArray.getJsonArray(TRADE_INFO_OBJECT_INDEX);
			int tradeCount = tradesArray.size();
			Assert.assertTrue(tradeCount > 0, "There are no trade present in TRADE message. Message = " + message);

			for (int i = 0; i < tradeCount; i++) {
				JsonArray singleTrade = tradesArray.getJsonArray(i);
				double price = getDoubleFromString(singleTrade.getString(TRADE_INFO_PRICE_INDEX));
				double volume = getDoubleFromString(singleTrade.getString(TRADE_INFO_VOLUME_INDEX));
				Assert.assertTrue(price > 0, "Trade: Trade price is less than or equal to zero. Message = " + message);
				Assert.assertTrue(volume > 0, "Trade: Volume is less than or equal to zero. Message = " + message);
			}
		}
	}

	public void validateTriggeringOrderSideIsCorrect() {
		LOGGER.info("validateTriggeringOrderSideIsCorrect");
		List<String> messages = websocketClient.getMessageHandler().getAllTradeMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonArray tradesArray = jsonArray.getJsonArray(TRADE_INFO_OBJECT_INDEX);
			int tradeCount = tradesArray.size();
			Assert.assertTrue(tradeCount > 0, "There are no trades present in TRADE message. Message = " + message);

			for (int i = 0; i < tradeCount; i++) {
				JsonArray singleTrade = tradesArray.getJsonArray(i);
				String tradeSide = singleTrade.getString(TRADE_INFO_SIDE_INDEX);
				Assert.assertTrue(tradeSide != null && ("s".equals(tradeSide) || "b".equals(tradeSide)),
						"Trade: Side does not match 'b' or 's'. Message = " + message);
			}
		}

	}

	public void validateTriggeringOrderTypeIsCorrect() {
		LOGGER.info("validateTriggeringOrderTypeIsCorrect");
		List<String> messages = websocketClient.getMessageHandler().getAllTradeMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonArray tradesArray = jsonArray.getJsonArray(TRADE_INFO_OBJECT_INDEX);
			int tradeCount = tradesArray.size();
			Assert.assertTrue(tradeCount > 0, "There are no trades present in TRADE message. Message = " + message);
			for (int i = 0; i < tradeCount; i++) {
				JsonArray singleTrade = tradesArray.getJsonArray(i);
				String orderType = singleTrade.getString(TRADE_INFO_ORDER_TYPE_INDEX);
				Assert.assertTrue(orderType != null && ("m".equals(orderType) || "l".equals(orderType)),
						"Trade: Order Type does not match 'm' or 'l'. Message = " + message);
			}
		}

	}

	public void validateSymbolInOrderBookMessages(String ticker) {
		LOGGER.info("validateSymbolInOrderBookMessages ticker = " + ticker);
		List<String> messages = websocketClient.getMessageHandler().getAllOrderBookMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(iter.next());
			String actualValue = jsonArray.getString(BOOK_SYMBOL_INDEX);
			assertEquals("ticker", actualValue, ticker);
		}
	}

	public void validateSortingOfOrdersInBookSnapshot() {
		LOGGER.info("validateSortingOfOrdersInBookSnapshot");
		List<String> messages = websocketClient.getMessageHandler().getAllOrderBookMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonObject ordersObject = jsonArray.getJsonObject(BOOK_ORDERS_OBJECT_INDEX);

			if (!ordersObject.containsKey("as") && !ordersObject.containsKey("bs")) {
				continue;
			}

			JsonArray askOrderArray = ordersObject.getJsonArray("as");
			int askOrdersCount = askOrderArray.size();
			// Validate Ask orders array is sorted Ascending
			for (int i = 0; i < askOrdersCount - 1; i++) {
				double askPrice = getDoubleFromString(askOrderArray.getJsonArray(i).getString(0));
				double nextBestAskPrice = getDoubleFromString(askOrderArray.getJsonArray(i + 1).getString(0));
				Assert.assertTrue(askPrice < nextBestAskPrice, "Book: Sorting of Ask order is incorrect in the book message. Message = " + message);
			}

			// Validate Bid orders array is sorted Descending
			JsonArray bidOrderArray = ordersObject.getJsonArray("bs");
			int bidOrdersCount = bidOrderArray.size();
			for (int i = 0; i < bidOrdersCount - 1; i++) {
				double bidPrice = getDoubleFromString(bidOrderArray.getJsonArray(i).getString(0));
				double nextBestBidPrice = getDoubleFromString(bidOrderArray.getJsonArray(i + 1).getString(0));
				Assert.assertTrue(bidPrice > nextBestBidPrice, "Book: Sorting of Bid order is incorrect in the book message. Message = " + message);
			}

		}
	}

	public void validateBestAskIsGreaterThanBestBidInBookSnapshot() {
		LOGGER.info("validateBestAskIsGreaterThanBestBidInBookSnapshot");
		List<String> messages = websocketClient.getMessageHandler().getAllOrderBookMessages();
		Iterator<String> iter = messages.iterator();
		while (iter.hasNext()) {
			String message = iter.next();
			JsonArray jsonArray = JsonHelper.getJsonArrayFromString(message);
			JsonObject ordersObject = jsonArray.getJsonObject(BOOK_ORDERS_OBJECT_INDEX);

			// skipping validation for book update messages
			if (!ordersObject.containsKey("as") && !ordersObject.containsKey("bs")) {
				continue;
			}

			JsonArray askOrderArray = ordersObject.getJsonArray("as");
			JsonArray bidOrderArray = ordersObject.getJsonArray("bs");

			// As per the documentation index 0 is the order with the best price on both sides
			double bestAskPrice = getDoubleFromString(askOrderArray.getJsonArray(0).getString(0));
			double bestBidPrice = getDoubleFromString(bidOrderArray.getJsonArray(0).getString(0));
			Assert.assertTrue(bestAskPrice > bestBidPrice, "Book: Best Ask price lesser than or equal to best Bid price. Message = " + message);
		}
	}

	private void assertEquals(String fieldName, String actualValue, String expectedValue) {
		LOGGER.info("assertEquals");
		Assert.assertEquals(actualValue, expectedValue,
				String.format("Actual %s doesn't match expected ticker. Actual=%s and Expected=%s", fieldName, actualValue, expectedValue));

	}

	public void cleanupAllMessages() {
		LOGGER.info("cleanupAllMessages");
		websocketClient.getMessageHandler().cleanupAllMessages();
	}

	private double getDoubleFromString(String value) {
		return Double.parseDouble(value);
	}

	public void logout() {
		LOGGER.info("logout");
		this.websocketClient.logOut();
	}
}
