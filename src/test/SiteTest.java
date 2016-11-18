package test;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import phantomjs.PhantomJsUtils;

public class SiteTest {

    public static void main(String[] args) {
        SiteTest test = new SiteTest();
        test.checkSite();
    }
    
    /**
     * ブログ村のマイページにログインしたページのHTMLソースを出力
     * @return
     */
    public boolean checkSite () {
        String url = "https://mypage.blogmura.com/login";
        String user = "hoge";
        String pass = "hoge";
        PhantomJsUtils pjs = new PhantomJsUtils();
        PhantomJSDriver driver = pjs.getDriver();
        try {
            driver.get(url);
            pjs.waitForLoad(driver);
            driver.findElement(By.name("mail")).sendKeys(user);
            driver.findElement(By.name("pass")).sendKeys(pass);
            WebElement sublit = driver.findElement(By.name("submit1"));
            sublit.click();
            Document document = Jsoup.parse(driver.getPageSource());
            Elements eles = document.select("tr .rank_point_data");
            Element element = null;
            for (Element els : eles) {
                if (StringUtils.contains(els.text(), "INポイント")) {
                    element = els;
                }
            }
            if (element != null) {
                System.out.println(element.select("td").get(0).text());
                
            } else {
                System.out.println("not found");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pjs.close(driver);
        }
        return true;
    }

}
