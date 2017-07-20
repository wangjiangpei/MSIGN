package com.dscs.sign.utils;

/**
 *
 */

public class MTextUrils{
    //把一个字符串中的大写转为小写，小写转换为大写：思路2
    public static String exChange(String str){
        StringBuilder sb = new StringBuilder();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append(Character.toLowerCase(c));
                }else if(Character.isLowerCase(c)){
                    sb.append(Character.toUpperCase(c));
                }else{
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
    public static String maohao(String str){
        StringBuilder sb = new StringBuilder();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                sb.append(c);
                if ((i+4)%2==1)
                    if(i!=str.length()-1)
                    sb.append(":");
            }
        }
        return sb.toString();
    }
}
