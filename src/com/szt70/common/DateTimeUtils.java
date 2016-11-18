package com.szt70.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * ���t�E���Ԋ֘A����
 * @author 
 *
 */
public class DateTimeUtils {

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	/**
	 * �w�肵���t�H�[�}�b�g�œ������擾
	 * @param format	�t�H�[�}�b�g������
	 * @param locale
	 * @return
	 */
	public static String getDateTimeFormat (String format) {
		return getDateTimeFormat(format, null);
	}
	
	/**
	 * �w�肵���t�H�[�}�b�g�œ������擾
	 * @param format	�t�H�[�}�b�g������
	 * @param locale
	 * @return
	 */
	public static String getDateTimeFormat (String format, Locale locale) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = null;
        if (locale != null) {
        	sdf = new SimpleDateFormat(format, locale);
        } else {
        	sdf = new SimpleDateFormat(format);
        }
        return sdf.format(c.getTime());
	}
	
	/**
	 * ���t�������Date�^�ɕϊ����܂�
	 * @param dateStr	���t������
	 * @param format	�t�H�[�}�b�g������
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStrToDate(String dateStr, String format) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date formatDate = sdf.parse(dateStr);
        return formatDate;
	}
}
