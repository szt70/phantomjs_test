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
     * ���s�R�[�h
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
            
	public static boolean writeTextImage () throws IOException {
		
        // �o�͂��镶����
        StringBuilder text = new StringBuilder();

        String dateStr = DateTimeUtils.getDateTimeFormat("yyyy-MM-dd-HHmm");
        //�e���v���[�g�摜
        String filePath = "c:/tmp/template.png";
        String outputPath = "c:/tmp/image" + dateStr + ".png";
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        text.append(dateStr);
 
        // ����������������ʒu
        float y = 30;
        float x = 30;
 
        // ����������������̈�̕�
        float wrappingWidth = bufferedImage.getWidth() - (x * 2);
        AttributedString as = new AttributedString(text.toString());
        as.addAttribute(TextAttribute.FONT, new Font("MS �S�V�b�N", Font.BOLD, 15));
        as.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);
        as.addAttribute(TextAttribute.BACKGROUND, new Color(0, 0, 0, 0));
 
        Graphics2D g2 = bufferedImage.createGraphics();
 
        FontRenderContext context = g2.getFontRenderContext();
        LineBreakMeasurer measurer = new LineBreakMeasurer(as.getIterator(), context);
 
        int position;
 
        // ������̍Ō�܂�
        while ((position = measurer.getPosition()) < text.length()) {
            TextLayout layout;
            // ���s�`�F�b�N
            int indexOf = text.indexOf(LINE_SEPARATOR, position);
 
            // ���s���Ă�ꍇ
            if (position < indexOf) {
                // ���s�ʒu�̎�O�̕��܂Ń��C�A�E�g�����Ă���
                layout = measurer.nextLayout(wrappingWidth, indexOf, false);
            }
            else {
                // �����Ő܂�Ԃ��Ă�Ƃ��܂Ń��C�A�E�g�����Ă���
                layout = measurer.nextLayout(wrappingWidth);
            }
 
            // ���C�A�E�g�Ƃ�Ȃ�����
            if (layout == null) {
                break;
            }
 
            // Y���W���X�V�i�������̈ʒu���x�[�X���C���̏�Ƃ��āA�x�[�X���C���ɍ��킹��j
            y += layout.getAscent();
            float dx = layout.isLeftToRight() ? 0 : (wrappingWidth - layout
                    .getAdvance());
            // ���������������
            layout.draw(g2, x + dx, y);
            // Y���W���X�V�i�x�[�X���C���̉� + �s�ԁj
            y += layout.getDescent() + layout.getLeading();
        }
 
        // �����o��
        ImageIO.write(bufferedImage, "png", new File(outputPath));
        return true;
	}
}
