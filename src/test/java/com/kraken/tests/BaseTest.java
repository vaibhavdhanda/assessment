package com.kraken.tests;

import com.kraken.engine.TestEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class BaseTest {
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);
    TestEngine testEngine;

    public BaseTest(String apiUrl){
        testEngine = new TestEngine(apiUrl);
    }

    @DataProvider(name = "ticker")
    public Object[][] dataProviderMethod() {
        return new Object[][] { { "XBT/USD" }, { "XBT/EUR" } };
    }

    @BeforeMethod
    public void beforeTestCase(Method m) {
        LOGGER.info("Test Started: " + m.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupAllMessages(Method m){
        testEngine.cleanupAllMessages();
        LOGGER.info("Test Completed: " + m.getName());
    }

    @AfterClass(alwaysRun = true)
    private void afterClass() {
        testEngine.logout();
    }

}
