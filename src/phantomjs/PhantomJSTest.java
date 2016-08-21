package phantomjs;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

public class PhantomJSTest {

    private static final String PHANTOMJS_PATH = "c:/phantomjs-2.1.1/bin/phantomjs.exe";

    public static void main(String[] args) {

    	if (ArrayUtils.isEmpty(args)) {
    		System.err.format("Usage : <URL>");
    		return;
    	}
        String url = args[0];
        System.out.format("URL   : %s\n", url);
        PhantomJSTest test = new PhantomJSTest();
        //test.checkSitePerformance(url);
        test.output(url);
    }

    public PhantomJSTest() {
    }

    /**
     * PhantomJSDriver�̏����ݒ�
     */
    private PhantomJSDriver initDriver() {
            DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
            //PhantomJS�p�X�̐ݒ�
            capabilities.setCapability("phantomjs.binary.path", PHANTOMJS_PATH);
            //PhantomJS�̃I�v�V�����ݒ�
            //PhantomJS���O�o�͂��Ȃ��APhantomJs���O�o�͐�
            String[] phantomJsArgs = {"--webdriver-loglevel=NONE",
            							"--webdriver-logfile=/tmp/phantomjsdriver.log"};
            capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomJsArgs);
            //JavaScript�̗L��/����
            capabilities.setJavascriptEnabled(true);
            //�^�C���A�E�g�ims�j�ݒ�
            capabilities.setCapability("phantomjs.page.settings.resourceTimeout", 10 * 1000);
            //���[�U�[�G�[�W�F���g�ݒ�
            capabilities.setCapability("phantomjs.page.settings.userAgent", "Original UserAgnet");
            //BASIC�F�؃A�J�E���g
            capabilities.setCapability("phantomjs.page.settings.userName", "username");
            capabilities.setCapability("phantomjs.page.settings.password", "password");
            //PhantomJS �̃I�v�V�����p�����[�^��ݒ�\�B
            PhantomJSDriver driver = new PhantomJSDriver(capabilities);
            return driver;
    }

    /**
     * cookie���Z�b�g����
     * @param driver
     * @throws IOException
     */
    private void setCookie(String url, PhantomJSDriver driver) throws JsonSyntaxException, IOException {

    	try{
    		//���݂�Cookie�m�F
    		Set<Cookie> cookieSet = driver.manage().getCookies();
    		for (Cookie c : cookieSet) {
    			System.out.format("[Before Cookie] %s", c.toString() + "\n");
    		}
    		//JSON�t�@�C������cookie�ǂݍ��݁iChrome����o�́j
        	String cookieJsonFile = "c:/tmp/cookie-chrome.txt";
        	String cookieJson = FileUtils.readFileToString(new File(cookieJsonFile));
        	JsonArray jsonArray = new Gson().fromJson(cookieJson, JsonArray.class);
        	for(int i = 0; i < jsonArray.size() ; i++){
        		JsonElement jsonElement = jsonArray.get(i);
            	String name = jsonElement.getAsJsonObject().get("name").getAsString();
            	String value = jsonElement.getAsJsonObject().get("value").getAsString();
            	//domain��.�擪��.�Ŗ����ƃG���[(org.openqa.selenium.InvalidCookieDomainException)
            	String domain = jsonElement.getAsJsonObject().get("domain").getAsString();
            	if (!StringUtils.startsWith(domain, ".")) {
            		domain = "." + domain;
            	}
            	String path = jsonElement.getAsJsonObject().get("path").getAsString();
            	Cookie c = new Cookie.Builder(name, value).domain(domain).path(path).build();
                driver.manage().addCookie(c);
    			System.out.format("[Add Cookie] %s", c.toString() + "\n");
        	}
        } finally {
    	}
    }

    /**
     * �T�C�g�����o�͂��܂�
     * @param url
     */
    private void output(String url) {

        PhantomJSDriver driver = null;

        try {
            driver = initDriver();
            setCookie(url, driver);
            driver.get(url);    // access to specified URL
            waitForLoad(driver);
            Document document = Jsoup.parse(driver.getPageSource());
            System.out.println(document.getElementsByTag("title"));
            System.out.println(document.getElementsByTag("body").text());

        } catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
        	close(driver);
        }
    }

    /**
     * �T�C�g�p�t�H�[�}���X���v��
     * @param url
     */
    private void checkSitePerformance(String url) {

        PhantomJSDriver driver = null;

        try {
            driver = initDriver();
            driver.get(url);    // access to specified URL
            waitForLoad(driver);
            long startTime = (Long) driver.executeScript(
                    "return window.performance.timing.navigationStart");
            long loadEndTime = (Long) driver.executeScript(
                    "return window.performance.timing.loadEventEnd");
            long responseEndTime = (Long) driver.executeScript(
                    "return window.performance.timing.responseEnd");

            System.out.format("Response Time : %d\n", responseEndTime - startTime);
            System.out.format("PageLoad Time : %d\n", loadEndTime - startTime);
        } finally {
        	close(driver);
        }
    }


    /**
     * �y�[�W���[�h�����܂ő҂�
     * @param driver
     */
    void waitForLoad(PhantomJSDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
            ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                }
            };
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(pageLoadCondition);
    }

    /**
     * PhantomJSDriver close
     * @param driver
     */
    public void close (PhantomJSDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}