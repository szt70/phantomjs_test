package site;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import phantomjs.PhantomJsUtils;

public class MypageTest {
    
    private static PhantomJsUtils phantomJs = null;
    private static PhantomJSDriver driver = null;

    /**
     * テストクラスのstaticイニシャライザの後に呼ばれる。
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        phantomJs = new PhantomJsUtils();
        driver = phantomJs.getDriver();
        String cookieJsonFilePath = "/var/resource/mypage-cookie.json";
        phantomJs.setCookie(driver, cookieJsonFilePath);
    }
 
    /**
     * テストクラス実行後に実行したいメソッド
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        phantomJs.close(driver);
    }
 
    /**
     * テストメソッド前に呼ばれる
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }
 
    /**
     * テストメソッド実行後に実行したいメソッド
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }
 
    
    /**
     * マイページの当日ポイントをチェック
     * <pre>
     * 当日のINポイントの値確認、0ポイントで無ければOK
     * </pre>
     * @throws Exception
     */
    @Test
    public void testMypageInPoint() throws Exception {
        String url = "https://mypage.blogmura.com/";
        driver.get(url);
        phantomJs.waitForLoad(driver);

        Document document = Jsoup.parse(driver.getPageSource());
        Elements eles = document.select("tr .rank_point_data");
        Element element = null;
        for (Element els : eles) {
            if (StringUtils.contains(els.text(), "INポイント")) {
                element = els;
            }
        }
        
        if (element != null) {
            String str = element.select("td").get(0).text();
            assertNotEquals("当日のINポイントが0です。", str, "0");
            
        } else {
            fail("タグが見つかりませんでした。");
        }
        phantomJs.close(driver);
    }
    
    /**
     * 登録情報変更
     * <pre>
     * 自由文を変更して登録情報変更
     * 実行前後で画像件数が等しければOK
     * </pre>
     * @throws Exception
     */
    @Test
    public void testUserInfo() throws Exception {
        String url = "https://mypage.blogmura.com/join_input";
        driver.get(url);
        phantomJs.waitForLoad(driver);

        StringBuilder desc = new StringBuilder();
        desc.append(new Date() + " テスト用の文章");
        //自由文を入力してサブミットボタン押下
        WebElement eleFreeText = driver.findElement(By.name("freetext"));
        eleFreeText.clear();
        eleFreeText.sendKeys(desc);
        driver.findElement(By.name("submit1")).click();

        Document documentConfirm = Jsoup.parse(driver.getPageSource());
        if (!StringUtils.contains(documentConfirm.text(), "内容がこれでよろしければ「登録」ボタンを押して確定してください。")) {
            fail("登録確認画面でエラーが発生しました。");
        }

        //確認画面で「登録」押下
        driver.findElement(By.name("submit1")).click();
        
        //完了画面
        Document document = Jsoup.parse(driver.getPageSource());
        if (!StringUtils.contains(document.text(), "ランキングにご参加いただくために、まずは貼ってみましょう")) {
            fail("登録実行でエラーが発生しました。");
        }
    }
    
    /**
     * プロフィール画像をアップロード
     * <pre>
     * 既に3件登録されている場合は一番後ろの画像を削除しアップロードする
     * </pre>
     * @throws Exception
     */
    @Test
    public void testProfileImageUpload() throws Exception {

        String url = "https://mypage.blogmura.com/profpicup";
        driver.get(url);
        phantomJs.waitForLoad(driver);
        
        Document documentBefore = Jsoup.parse(driver.getPageSource());
        Elements img = documentBefore.select("[src^=http://image.blogmura.com]");
        int beforeCount = img.size();
        //プロフィール画像がMax件数の場合は最後の画像を削除
        if (beforeCount == 3) {

            List<WebElement> del = driver.findElements(By.linkText("削除"));
            del.get(2).click();
            //確認画面へ遷移
            phantomJs.waitForLoad(driver);
            driver.findElement(By.name("submit1")).click();
        }
        
        //アップロード
        driver.findElement(By.id("filename")).sendKeys("c:/tmp/template.png");
        driver.findElement(By.id("sb")).click();
        phantomJs.waitForLoad(driver);

        img = documentBefore.select("[src^=http://image.blogmura.com]");
        int afterCount = img.size();
        
        assertThat(beforeCount, is(afterCount));
    }
    

    /**
     * マイバナーのリンク先正常生確認
     * <pre>
     * </pre>
     * @throws Exception
     */
    @Test
    public void testMyBannerList() throws Exception {

        String url = "http://mypage.blogmura.com/join_comp";
        driver.get(url);
        phantomJs.waitForLoad(driver);
        
        Document document = Jsoup.parse(driver.getPageSource());

        //カテゴリが指定されてないマイバナーを対象にチェック
        int index = 0;
        Elements titles = document.select("th.title");
        for (Element ele: titles) {
            if (ele.text().contains("にほんブログ村")) {
                break;
            }
            index++;
        }

        //INランキング　のチェック状態取得
        boolean checkNewWindow = driver.findElement(By.id("check_inrank")).isSelected();
        //まずはINランキングチェック状態
        if (!checkNewWindow) {
            driver.findElement(By.id("check_inrank")).click();
            phantomJs.waitForLoad(driver);  
        }
        Element area = document.select("textarea.text").get(index);
        String link = Jsoup.parse(area.val()).select("a").get(index).attr("href");
        assertThat(link, is("//www.blogmura.com/ranking.html"));
        
        //OUTランキング
        driver.findElement(By.id("check_outrank")).click();
        phantomJs.waitForLoad(driver);
        document = Jsoup.parse(driver.getPageSource());
        area = document.select("textarea.text").get(index);
        link = Jsoup.parse(area.val()).select("a").get(index).attr("href"); 
        assertThat(link, is("//www.blogmura.com/ranking_out.html"));
        
        //PVランキング
        driver.findElement(By.id("check_pvrank")).click();
        phantomJs.waitForLoad(driver);
        document = Jsoup.parse(driver.getPageSource());
        area = document.select("textarea.text").get(index);
        link = Jsoup.parse(area.val()).select("a").get(index).attr("href"); 
        assertThat(link, is("//www.blogmura.com/ranking_pv.html"));
    }
    
}
