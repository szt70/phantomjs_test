package com.szt70.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日付・時間関連処理
 * @author 
 *
 */
public class DateTimeUtils {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * 指定したフォーマットで日時を取得
	 * @param format	フォーマット文字列
	 * @param locale
	 * @return
	 */
	public static String getDateTimeFormat (String format) {
		return getDateTimeFormat(format, null);
	}
	
	/**
	 * 指定したフォーマットで日時を取得
	 * @param format	フォーマット文字列
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
	 * 日付文字列をDate型に変換します
	 * @param dateStr	日付文字列
	 * @param format	フォーマット文字列
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStrToDate(String dateStr, String format) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date formatDate = sdf.parse(dateStr);
        return formatDate;
	}
}
