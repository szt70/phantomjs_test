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
	 * �e�X�g�N���X��static�C�j�V�����C�U�̌�ɌĂ΂��B
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		phantomJs = new PhantomJsUtils();
		driver = phantomJs.getDriver();
		String cookieJsonFilePath = "c:/tmp/mypage-cookie.json";
		phantomJs.setCookie(driver, cookieJsonFilePath);
	}
 
	/**
	 * �e�X�g�N���X���s��Ɏ��s���������\�b�h
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		phantomJs.close(driver);
	}
 
	/**
	 * �e�X�g���\�b�h�O�ɌĂ΂��
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
 
	/**
	 * �e�X�g���\�b�h���s��Ɏ��s���������\�b�h
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
 
	
	/**
	 * �}�C�y�[�W�̓����|�C���g���`�F�b�N
	 * <pre>
	 * ������IN�|�C���g�̒l�m�F�A0�|�C���g�Ŗ������OK
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
            if (StringUtils.contains(els.text(), "IN�|�C���g")) {
                element = els;
            }
        }
        
        if (element != null) {
            String str = element.select("td").get(0).text();
            assertNotEquals("������IN�|�C���g��0�ł��B", str, "0");
            
        } else {
            fail("�^�O��������܂���ł����B");
        }
        phantomJs.close(driver);
	}
	
	/**
	 * �o�^���ύX
	 * <pre>
	 * ���R����ύX���ēo�^���ύX
	 * ���s�O��ŉ摜���������������OK
	 * </pre>
	 * @throws Exception
	 */
	@Test
	public void testUserInfo() throws Exception {
        String url = "https://mypage.blogmura.com/join_input";
        driver.get(url);
        phantomJs.waitForLoad(driver);

        StringBuilder desc = new StringBuilder();
        desc.append(new Date() + " �e�X�g�p�̕���");
        //���R������͂��ăT�u�~�b�g�{�^������
        WebElement eleFreeText = driver.findElement(By.name("freetext"));
        eleFreeText.clear();
        eleFreeText.sendKeys(desc);
        driver.findElement(By.name("submit1")).click();

        Document documentConfirm = Jsoup.parse(driver.getPageSource());
        if (!StringUtils.contains(documentConfirm.text(), "���e������ł�낵����΁u�o�^�v�{�^���������Ċm�肵�Ă��������B")) {
        	fail("�o�^�m�F��ʂŃG���[���������܂����B");
        }

        //�m�F��ʂŁu�o�^�v����
        driver.findElement(By.name("submit1")).click();
        
        //�������
        Document document = Jsoup.parse(driver.getPageSource());
        if (!StringUtils.contains(document.text(), "�����L���O�ɂ��Q�������������߂ɁA�܂��͓\���Ă݂܂��傤")) {
        	fail("�o�^���s�ŃG���[���������܂����B");
        }
	}
	
	/**
	 * �v���t�B�[���摜���A�b�v���[�h
	 * <pre>
	 * ����3���o�^����Ă���ꍇ�͈�Ԍ��̉摜���폜���A�b�v���[�h����
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
        //�v���t�B�[���摜��Max�����̏ꍇ�͍Ō�̉摜���폜
        if (beforeCount == 3) {

        	List<WebElement> del = driver.findElements(By.linkText("�폜"));
        	del.get(2).click();
        	//�m�F��ʂ֑J��
            phantomJs.waitForLoad(driver);
            driver.findElement(By.name("submit1")).click();
        }
        
        //�A�b�v���[�h
        driver.findElement(By.id("filename")).sendKeys("c:/tmp/template.png");
        driver.findElement(By.id("sb")).click();
        phantomJs.waitForLoad(driver);

        img = documentBefore.select("[src^=http://image.blogmura.com]");
        int afterCount = img.size();
        
        assertThat(beforeCount, is(afterCount));
	}
	

	/**
	 * �}�C�o�i�[�̃����N�搳�퐶�m�F
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

        //�J�e�S�����w�肳��ĂȂ��}�C�o�i�[��ΏۂɃ`�F�b�N
        int index = 0;
        Elements titles = document.select("th.title");
        for (Element ele: titles) {
        	if (ele.text().contains("�ɂق�u���O��")) {
        		break;
        	}
        	index++;
        }

        //IN�����L���O�@�̃`�F�b�N��Ԏ擾
        boolean checkNewWindow = driver.findElement(By.id("check_inrank")).isSelected();
        //�܂���IN�����L���O�`�F�b�N���
        if (!checkNewWindow) {
        	driver.findElement(By.id("check_inrank")).click();
        	phantomJs.waitForLoad(driver);	
        }
        Element area = document.select("textarea.text").get(index);
        String link = Jsoup.parse(area.val()).select("a").get(index).attr("href");
        assertThat(link, is("//www.blogmura.com/ranking.html"));
        
        //OUT�����L���O
        driver.findElement(By.id("check_outrank")).click();
    	phantomJs.waitForLoad(driver);
    	document = Jsoup.parse(driver.getPageSource());
        area = document.select("textarea.text").get(index);
        link = Jsoup.parse(area.val()).select("a").get(index).attr("href");	
        assertThat(link, is("//www.blogmura.com/ranking_out.html"));
        
        //PV�����L���O
        driver.findElement(By.id("check_pvrank")).click();
    	phantomJs.waitForLoad(driver);
    	document = Jsoup.parse(driver.getPageSource());
        area = document.select("textarea.text").get(index);
        link = Jsoup.parse(area.val()).select("a").get(index).attr("href");	
        assertThat(link, is("//www.blogmura.com/ranking_pv.html"));
	}
	
}
