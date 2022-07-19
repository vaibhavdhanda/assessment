package com.kraken.tests;

import com.kraken.engine.TestEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class BaseTest {
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);
    TestEngine testEngine;

    public BaseTest(String apiUrl) {
        testEngine = new TestEngine(apiUrl);
    }

    @DataProvider(name = "ticker")
    public Object[][] dataProviderMethod(ITestContext context) {
        return new Object[][]{{"XBT/USD"}, {"XBT/EUR"}};
    }

    @BeforeMethod
    public void beforeTestCase(Method method) {
        LOGGER.info("Test Started: " + method.getDeclaringClass().getName() + "." + method.getName());

    }

    @AfterMethod(alwaysRun = true)
    public void cleanupAllMessages(Method m) {
        testEngine.cleanupAllMessages();
        LOGGER.info("Test Completed: " + m.getDeclaringClass().getName() + "." + m.getName());
    }

    @AfterClass(alwaysRun = true)
    private void afterClass() {
        testEngine.logout();
    }

}
