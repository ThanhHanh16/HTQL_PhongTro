package common;

import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("*** Test Suite " + context.getName() + " started ***");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println(("*** Test Suite " + context.getName() + " ending ***"));
        ExtentTestManager.extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(("*** Running test method " + result.getMethod().getMethodName() + "..."));
        ExtentTestManager.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("*** Executed " + result.getMethod().getMethodName() + " test successfully...");
        ExtentTestManager.getTest().log(Status.PASS, "Test passed");
        takeScreenshotAndAttach(result, Status.PASS);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("*** Test execution " + result.getMethod().getMethodName() + " failed...");
        ExtentTestManager.getTest().log(Status.FAIL, "Test Failed");
        ExtentTestManager.getTest().fail(result.getThrowable());
        takeScreenshotAndAttach(result, Status.FAIL);
    }

    private void takeScreenshotAndAttach(ITestResult result, Status status) {
        if (Constant.WEBDRIVER != null) {
            try {
                String methodName = result.getName();
                String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());

                String reportDirectory = System.getProperty("user.dir")
                        + File.separator + "target"
                        + File.separator + "ExtentReports";

                String folderName = (status == Status.FAIL) ? "failure_screenshots" : "success_screenshots";
                File screenshotFolder = new File(reportDirectory + File.separator + folderName);

                if (!screenshotFolder.exists()) {
                    screenshotFolder.mkdirs();
                }

                File srcFile = ((TakesScreenshot) Constant.WEBDRIVER).getScreenshotAs(OutputType.FILE);

                String screenshotName = methodName + "_" + timeStamp + ".png";
                File destFile = new File(screenshotFolder, screenshotName);

                FileUtils.copyFile(srcFile, destFile);

                // Attach to ExtentReports
                ExtentTestManager.getTest().addScreenCaptureFromPath(folderName + "/" + screenshotName);

            } catch (IOException e) {
                ExtentTestManager.getTest().log(status, "Lỗi khi chụp screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("*** Test failed but within percentage % " + result.getMethod().getMethodName());
    }
}
