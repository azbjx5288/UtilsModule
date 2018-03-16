package com.gcp.utilsmodule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class StringUtils.
 *
 * @author Bvin
 */
public class StringUtils {

	/**
	 * Gets the number array from string.
	 * 从字符串中获取数字构成数组
	 * @param str the str
	 * @return the number array from string
	 */
	public static String[] getNumberArrayFromString(String str){
		if (TextUtils.isEmpty(str)) {
			return null;
		}
		String regEx="[0-9]*";  
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(str); 
		List<String> s = new ArrayList<String>();
		while(m.find()){  
			String word = m.group().trim();
			if (!TextUtils.isEmpty(word)) {
				s.add(word);
			}
			
        }  
		String[] arrays = null;
		try {
			arrays = s.toArray(new String[s.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrays;
	}
	
	/**
	 * Gets the string from array.
	 *
	 * @param array the array
	 * @return the string from array
	 */
	public static String getStringFromArray(int[] array){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0,len=array.length;i<len; i++) {
			if(i==len-1){
				sb.append(array[i]+"}");
			}else
			sb.append(array[i]+",");
		}
		return sb.toString();
	}
	
	public static String getStringFromArray(CharSequence[] array){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0,len=array.length;i<len; i++) {
			if(i==len-1){
				sb.append(array[i]+"}");
			}else
			sb.append(array[i]+",");
		}
		return sb.toString();
	}
	
	/**
	 * Gets the string from array.
	 *
	 * @param array the array
	 * @return the string from array
	 */
	public static String getStringFromArray(float[] array){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0,len=array.length;i<len; i++) {
			if(i==len-1){
				sb.append(array[i]+"}");
			}else
			sb.append(array[i]+",");
		}
		return sb.toString();
	}
	
	/**
	 * Gets the string from array.
	 *
	 * @param array the array
	 * @return the string from array
	 */
	public static String getStringFromArray(double[] array){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0,len=array.length;i<len; i++) {
			if(i==len-1){
				sb.append(array[i]+"}");
			}else
			sb.append(array[i]+",");
		}
		return sb.toString();
	}
	
	/**
	 * Gets the string from array.
	 *
	 * @param array the array
	 * @return the string from array
	 */
	public static String getStringFromArray(Array array){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0,len = Array.getLength(array);i <len ; i++) {
			if(i==len-1){
				sb.append(Array.get(array,i)+"}");
			}else
			sb.append(Array.get(array, i)+",");
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @Title: getStringFromMap 
	 * @Description: 以a=1&b=2的形式返回map字符串
	 * @param map
	 * @return: String
	 */
	public static String getStringFromMap(Map<String, Object> map){
		return getStringFromMap(map,"utf-8");
	}
	
	public static String getStringFromMap(Map<String, Object> map,String paramsEncoding){
		StringBuilder encodedParams = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			try {
				if (StringUtils.hazAvailableContent(URLEncoder.encode(entry.getValue().toString(), paramsEncoding))) {

					encodedParams.append(URLEncoder.encode(entry.getKey(),paramsEncoding));

					encodedParams.append('=');

					encodedParams.append(URLEncoder.encode(entry.getValue().toString(), paramsEncoding));

					encodedParams.append('&');
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (encodedParams.toString().endsWith("&")) {
			encodedParams.deleteCharAt(encodedParams.length()-1);
		}
		return encodedParams.toString();
	}
	
	/**
	 * Gets the string from list.
	 *
	 * @param array the array
	 * @return the string from list
	 */
	public static String getStringFromList(List array){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0,len = array.size();i <len ; i++) {
			if(i==len-1){
				sb.append(array.get(i)+"}");
			}else
			sb.append(array.get(i)+",");
		}
		return sb.toString();
	}
	
	/**
	 * Gets the list from string array.
	 *
	 * @param array the array
	 * @return the list from string array
	 */
	public static List<String> getListFromStringArray(String array[]){
		if (array == null||array.length == 0) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		int length = array.length;
		for (int i = 0; i < length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	/**
	 * Gets the list from Integer array.
	 *
	 * @param array the array
	 * @return the list from Integer array
	 */
	public static List<Integer> getListFromIntArray(int array[]){
		if (array == null||array.length == 0) {
			return null;
		}
		List<Integer> list = new ArrayList<Integer>();
		int length = array.length;
		for (int i = 0; i < length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	/**
	 * InputStream 转 String
	 * 
	 * 
	 */
	public static String InputStringToStr(InputStream inputStream, String encode) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = 0;
		byte[] date = new byte[1024];
		String result = "";
		try {
			while ((len = inputStream.read(date)) != -1) {
				outputStream.write(date, 0, len);
			}
			result = new String(outputStream.toByteArray(), encode);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	
	/**
	 *  判断paramString是否全是数字
	 */
	public static boolean isNumberValid(String paramString) {
		return Pattern
				.compile(
						"^[0-9]*$")
				.matcher(paramString).matches();
	}
	/**
	 *  判断paramString第一个是英文字符
	 */
	public static boolean isChar1Valid(String paramString) {
		return Pattern
				.compile(
						"^[a-zA-Z]+$")
						.matcher(paramString).matches();
	}
	/**
	 *  判断paramString是否全是中文
	 */
	public static boolean isCNValid(String paramString) {
		return Pattern
				.compile(
						"^[\u4e00-\u9fa5]+$")
						.matcher(paramString).matches();
	}
	
	 /** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */  
    public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }
    /**
     * 时间加0
     */
    public static String AddZero(int i){  
    	String string = String.valueOf(i);
    	
    	
        return string.length()>1?string:"0"+string;  
    }
    
    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     * 
     * @param value
     *            指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 
     * @Title: hazAvailableContent 
     * @Description: TextUtils.isEmpty+trim
     * @param content
     * @return: boolean||return true if the content after trim not empty
     */
    public static boolean hazAvailableContent(String content){
    	if (!TextUtils.isEmpty(content)) {
			String trimText = content.trim();
			if (!TextUtils.isEmpty(trimText)) {
				return true;
			}
		}
		return false;
    }

    /**
     * 
     * @Title: getSimpleFloat 
     * @Description: 如100.000就显示100,100.001就显示100.001
     * @param f
     * @return: String
     */
    public static String getSimpleFloat(float f){
    	if (f<=(int)f){
    		return String.valueOf((int)f);
		}else{
			return String.valueOf(f);
		}
    }
    
    /**
	   * 
	   * @Title: getIntFromString 
	   * @Description: 从字符串参数转换为int型参数，缺省值为-1
	   * @param str
	   * @return: int
	   */
    public static int getIntFromString(String str) {
      try {
        return Integer.valueOf(str);
      } catch (Exception e) {
        return -1;
      }
    }
    
    /**
     * 生成url字符串
     * @param map 输入的参数
     * @return 返回类似a=1&b=2形式的字符串
     */
    public static String generateUrlString(Map<String, Object> map) {
        return getStringFromMap(map);
    }
}
