package image;

import java.awt.Color;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.Date;

import javax.imageio.ImageIO;

import com.szt70.common.DateTimeUtils;

public class ImageGenerator {

	public static void main(String[] args) throws IOException {

		writeTextImage();
	}

    /**
     * 改行コード
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
            
	public static boolean writeTextImage () throws IOException {
		
        // 出力する文字列
        StringBuilder text = new StringBuilder();

        String dateStr = DateTimeUtils.getDateTimeFormat("yyyy-MM-dd-HHmm");
        //テンプレート画像
        String filePath = "c:/tmp/template.png";
        String outputPath = "c:/tmp/image" + dateStr + ".png";
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        text.append(dateStr);
 
        // 文字列を書きだす位置
        float y = 30;
        float x = 30;
 
        // 文字列を書きだす領域の幅
        float wrappingWidth = bufferedImage.getWidth() - (x * 2);
        AttributedString as = new AttributedString(text.toString());
        as.addAttribute(TextAttribute.FONT, new Font("MS ゴシック", Font.BOLD, 15));
        as.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);
        as.addAttribute(TextAttribute.BACKGROUND, new Color(0, 0, 0, 0));
 
        Graphics2D g2 = bufferedImage.createGraphics();
 
        FontRenderContext context = g2.getFontRenderContext();
        LineBreakMeasurer measurer = new LineBreakMeasurer(as.getIterator(), context);
 
        int position;
 
        // 文字列の最後まで
        while ((position = measurer.getPosition()) < text.length()) {
            TextLayout layout;
            // 改行チェック
            int indexOf = text.indexOf(LINE_SEPARATOR, position);
 
            // 改行してる場合
            if (position < indexOf) {
                // 改行位置の手前の分までレイアウトもってくる
                layout = measurer.nextLayout(wrappingWidth, indexOf, false);
            }
            else {
                // 自動で折り返してるとこまでレイアウト持ってくる
                layout = measurer.nextLayout(wrappingWidth);
            }
 
            // レイアウトとれなかった
            if (layout == null) {
                break;
            }
 
            // Y座標を更新（さっきの位置をベースラインの上として、ベースラインに合わせる）
            y += layout.getAscent();
            float dx = layout.isLeftToRight() ? 0 : (wrappingWidth - layout
                    .getAdvance());
            // 文字列を書きだす
            layout.draw(g2, x + dx, y);
            // Y座標を更新（ベースラインの下 + 行間）
            y += layout.getDescent() + layout.getLeading();
        }
 
        // 書き出し
        ImageIO.write(bufferedImage, "png", new File(outputPath));
        return true;
	}
}
