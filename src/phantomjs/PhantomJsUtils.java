package phantomjs;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import browser.BrowserPerformance;

public class PhantomJsUtils {

	Logger LOG = Logger.getLogger(PhantomJsUtils.class.getSimpleName());
    private static final String PHANTOMJS_PATH = "c:/phantomjs-2.1.1/bin/phantomjs.exe";
    
    /**
     * デフォルトのUA
     */
    private static final String USERAGENT_DEFAULT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:16.0) Gecko/20121026 Firefox/16.0";
    private String userAgent;

    public String getUserAgent() {
        if (StringUtils.isBlank(userAgent)) {
            return USERAGENT_DEFAULT;
        }
        return userAgent;
    }

    /**
     * getDriver()の実行前に呼び出す
     * 
     * @param userAgent
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public static void main(String[] args) {

        if (ArrayUtils.isEmpty(args)) {
            System.err.format("Usage : <URL>");
            return;
        }
        String url = args[0];
        System.out.format("URL   : %s\n", url);
        PhantomJsUtils test = new PhantomJsUtils();
        //test.checkSitePerformance(url);
        test.output(url);
    }

    public PhantomJsUtils() {
    }

    /**
     * PhantomJSDriverの初期設定
     */
    public PhantomJSDriver getDriver() {
            DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
            //PhantomJSパスの設定
            capabilities.setCapability("phantomjs.binary.path", PHANTOMJS_PATH);
            //PhantomJSのオプション設定
            //PhantomJSログ出力しない、PhantomJsログ出力先
            String[] phantomJsArgs = {"--webdriver-loglevel=NONE",
                                        "--webdriver-logfile=/tmp/phantomjsdriver.log"};
            capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomJsArgs);
            //JavaScriptの有効/無効
            capabilities.setJavascriptEnabled(true);
            //タイムアウト（ms）設定
            capabilities.setCapability("phantomjs.page.settings.resourceTimeout", 10 * 1000);
            //ユーザーエージェント設定
            capabilities.setCapability("phantomjs.page.settings.userAgent", getUserAgent());
            //BASIC認証アカウント
            capabilities.setCapability("phantomjs.page.settings.userName", "username");
            capabilities.setCapability("phantomjs.page.settings.password", "password");
            //PhantomJS のオプションパラメータを設定可能。
            PhantomJSDriver driver = new PhantomJSDriver(capabilities);
            return driver;
    }

    /**
     * cookieをセットする
     * @param driver
     * @param cookieJsonFilePath	JSONファイルからcookie読み込み（ChromeのアドオンEditThisCookieから出力されるフォーマットが利用可）
     * @throws IOException
     */
    public void setCookie(PhantomJSDriver driver, String cookieJsonFilePath) throws JsonSyntaxException, IOException {

        try{
            //現在のCookie確認
            Set<Cookie> cookieSet = driver.manage().getCookies();
            for (Cookie c : cookieSet) {
            	LOG.debug(System.out.format("[Before Cookie] %s", c.toString() + "\n"));
            }
            String cookieJson = FileUtils.readFileToString(new File(cookieJsonFilePath));
            JsonArray jsonArray = new Gson().fromJson(cookieJson, JsonArray.class);
            for(int i = 0; i < jsonArray.size() ; i++){
                JsonElement jsonElement = jsonArray.get(i);
                String name = jsonElement.getAsJsonObject().get("name").getAsString();
                String value = jsonElement.getAsJsonObject().get("value").getAsString();
                //domainは先頭が.で無いとエラー(org.openqa.selenium.InvalidCookieDomainException)発生するので対策
                String domain = jsonElement.getAsJsonObject().get("domain").getAsString();
                if (!StringUtils.startsWith(domain, ".")) {
                    domain = "." + domain;
                }
                String path = jsonElement.getAsJsonObject().get("path").getAsString();
                Cookie c = new Cookie.Builder(name, value).domain(domain).path(path).build();
                driver.manage().addCookie(c);
                LOG.debug(System.out.format("[Add Cookie] %s", c.toString() + "\n"));
            }
        } finally {
        }
    }

    /**
     * サイト情報を出力します
     * @param url
     */
    private void output(String url) {

        PhantomJSDriver driver = null;

        try {
            driver = getDriver();
            driver.get(url);    // access to specified URL
            waitForLoad(driver);
            Document document = Jsoup.parse(driver.getPageSource());
            System.out.println(document.getElementsByTag("title"));
            System.out.println(document.getElementsByTag("body").text());

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(driver);
        }
    }

    /**
     * サイトパフォーマンスを計測
     * @param url
     */
    public BrowserPerformance checkSitePerformance(String url) {

    	BrowserPerformance performance = new BrowserPerformance();
        PhantomJSDriver driver = null;

        try {
            driver = getDriver();
            driver.get(url);    // access to specified URL
            waitForLoad(driver);
            long startTime = (Long) driver.executeScript(
                    "return window.performance.timing.navigationStart");
            long loadEndTime = (Long) driver.executeScript(
                    "return window.performance.timing.loadEventEnd");
            long domLoadStartTime = (Long) driver.executeScript(
                    "return window.performance.timing.domLoading");
            long domLoadEndTime = (Long) driver.executeScript(
                    "return window.performance.timing.domComplete");
            long responseEndTime = (Long) driver.executeScript(
                    "return window.performance.timing.responseEnd");

            performance.setResponseTime(responseEndTime - startTime);
            performance.setDomLoadingTime(domLoadEndTime - domLoadStartTime);
            performance.setRenderingTime(loadEndTime - startTime);
        } finally {
            close(driver);
        }
        return performance;
    }


    /**
     * ページロード完了まで待つ
     * @param driver
     */
    public void waitForLoad(PhantomJSDriver driver) {
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