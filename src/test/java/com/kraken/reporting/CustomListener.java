package com.kraken.reporting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.util.List;
import java.util.Set;

public class CustomListener extends TestListenerAdapter implements ITestListener {
    private static final Logger LOGGER = LogManager.getLogger(CustomListener.class);

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        tr.setTestName(tr.getMethod().getQualifiedName() + "(" + getParamName(tr) + ")");
    }

    @Override
    public void onFinish(ITestContext context) {
        report(context);
    }

    void report(ITestContext context) {
        LOGGER.info("*************************Custom Test Summary Report (Console)*************************");
        LOGGER.info("Test reports for suite <" + context.getName() + ">");
        LOGGER.info("**************************************************************************************");
        report(context.getPassedTests().getAllResults(), "passed");
        report(context.getFailedTests().getAllResults(), "failed");
        report(context.getSkippedTests().getAllResults(), "skipped");
        LOGGER.info("**************************************************************************************");
    }

    private void report(Set<ITestResult> results, String status) {
        if (results.isEmpty()) {
            return;
        }
        LOGGER.info("Test methods that " + status + " are as below:");
        results.stream().sorted().forEach(this::report);
    }

    private void report(ITestResult result) {
        logTestRunDetails(result);
        List<String> output = Reporter.getOutput(result);
        if (!output.isEmpty()) {
            LOGGER.info("Test Method logs ");
            output.forEach(LOGGER::error);
        }
        if (result.getStatus() == ITestResult.SUCCESS) {
            return;
        }
        if (result.getStatus() == ITestResult.FAILURE) {
            LOGGER.info("Test Method failed due to ");
            result.getThrowable().printStackTrace();
            if (result.getStatus() == ITestResult.SKIP) {
                LOGGER.info("Test Method skipped due to problems in the following methods");
                result
                        .getSkipCausedBy()
                        .forEach(iTestNGMethod -> LOGGER.info(iTestNGMethod.getQualifiedName()));
            }
        }
    }

    private String getParamName(ITestResult tr) {
        Object[] params = tr.getParameters();
        return (String) params[0];
    }

    private void logTestRunDetails(ITestResult result) {
        LOGGER.info(result.getName() + "[" + String.valueOf(result.getEndMillis() - result.getStartMillis()) + " (ms)]");
    }
}
