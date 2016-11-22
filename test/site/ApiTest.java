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

import com.google.gson.Gson;

import phantomjs.PhantomJsUtils;

public class ApiTest {
    
    private static Gson gson = null;

    /**
     * テストクラスのstaticイニシャライザの後に呼ばれる。
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	gson = new Gson();
    }
 
    /**
     * テストクラス実行後に実行したいメソッド
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
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

        Document document = Jsoup.connect(url).get();
        ResponseApi model = gson.fromJson(document.toString(),ResponseApi.class);
        
            assertNotEquals("当日のINポイントが0です。", "", "0");
            
            fail("タグが見つかりませんでした。");
    }

    public class ResponseApi {
    	
    }

}
