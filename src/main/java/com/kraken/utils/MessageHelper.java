package com.kraken.utils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;


public class MessageHelper {
    public static String getTickerSubscriptionPayload(String ticker){
        return Json.createObjectBuilder()
                .add("event", "subscribe")
                .add("pair", getTickerArray(ticker))
                .add("subscription", getTickerMessageObj())
                .build()
                .toString();
    }

    public static String getTickerUnSubscriptionPayload(String ticker){
        return Json.createObjectBuilder()
                .add("event", "unsubscribe")
                .add("pair", getTickerArray(ticker))
                .add("subscription", getTickerMessageObj())
                .build()
                .toString();
    }

    public static String getOhlcSubscriptionPayload(String ticker){
        return Json.createObjectBuilder()
                .add("event", "subscribe")
                .add("pair", getTickerArray(ticker))
                .add("subscription", getOhlcMessageObj())
                .build()
                .toString();
    }

    public static String getOhlcUnSubscriptionPayload(String ticker){
        return Json.createObjectBuilder()
                .add("event", "unsubscribe")
                .add("pair", getTickerArray(ticker))
                .add("subscription", getOhlcMessageObj())
                .build()
                .toString();
    }

    public static String getTradeSubscriptionPayload(String ticker){
        return Json.createObjectBuilder()
                .add("event", "subscribe")
                .add("pair", getTickerArray(ticker))
                .add("subscription", getTradeMessageObj())
                .build()
                .toString();
    }

    public static String getTradeUnSubscriptionPayload(String ticker){
        return Json.createObjectBuilder()
                .add("event", "unsubscribe")
                .add("pair", getTickerArray(ticker))
                .add("subscription", getTradeMessageObj())
                .build()
                .toString();
    }

    public static String getSpreadSubscriptionPayload(String ticker){
        return Json.createObjectBuilder()
                .add("event", "subscribe")
                .add("pair", getTickerArray(ticker))
                .add("subscription", getSpreadMessageObj())
                .build()
                .toString();
    }

    public static String getBookSubscriptionPayload(String ticker){
        return Json.createObjectBuilder()
                .add("event", "subscribe")
                .add("pair", getTickerArray(ticker))
                .add("subscription", getBookMessageObj())
                .build()
                .toString();
    }

    public static String getBookUnSubscriptionPayload(String ticker){
        return Json.createObjectBuilder()
                .add("event", "unsubscribe")
                .add("pair", getTickerArray(ticker))
                .add("subscription", getBookMessageObj())
                .build()
                .toString();
    }


    private static JsonArray getTickerArray(String ticker){
        return Json.createArrayBuilder()
                .add(ticker)
                .build();
    }

    private static JsonObject getTickerMessageObj(){
        return Json.createObjectBuilder()
                .add("name", "ticker")
                .build();
    }

    private static JsonObject getOhlcMessageObj(){
        return  Json.createObjectBuilder()
                .add("name", "ohlc")
                .add("interval", 1)
                .build();
    }

    private static JsonObject getTradeMessageObj(){
        return  Json.createObjectBuilder()
                .add("name", "trade")
                .build();
    }

    private static JsonObject getSpreadMessageObj(){
        return  Json.createObjectBuilder()
                .add("name", "spread")
                .build();
    }

    private static JsonObject getBookMessageObj(){
        return  Json.createObjectBuilder()
                .add("name", "book")
                .build();
    }
}
