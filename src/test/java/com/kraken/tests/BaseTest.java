package com.kraken.tests;

import com.kraken.engine.TestEngine;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

public class BaseTest {
    private String apiUrl = "undefined";
    TestEngine testEngine;

    public BaseTest(String apiUrl){
        this.apiUrl = apiUrl;
        testEngine = new TestEngine(apiUrl);
    }

    @DataProvider(name = "ticker")
    public Object[][] dataProviderMethod() {
        return new Object[][] { { "XBT/USD" }, { "XBT/EUR" } };
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
