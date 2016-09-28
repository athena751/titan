package com.hp.titan.common.util;

import java.io.*;


public class Encode{
	
    public static String getEncode(String pw){
    	int nch[]=new int[20];     //密文中间变量    
    	String cryp;             //存放密文
        char ch[]=new char[20];   //存放密码的每个字符
        ch=pw.toCharArray(); 
    	for(int i=0;i<ch.length;i++){
    		nch[i]=transf(ch[i]);
    	    nch[i]=transf(ch[i]);        // 获得密文钥匙
    	}
        cryp=cryptograph(nch,ch.length);
        return cryp;    
    }
    public static int transf(char c){         //演算密文钥匙
    	if(c>='0'&&c<='9'){
    		return c+1;
    	}
    	else if(c>='a'&&c<='z'){
    		return c+2;
    	}
    	else if(c>='A'&&c<='Z'){
    		return c+3;
    	}
    	else return c;
    }
    public static String cryptograph(int[] n,int i){          //演算密文
    	int j;                     //用于循环
    	int numch=0;              //数组ch[]中圆熟的实际最大元素个数
    	int modnumber=0;
    	char[] ch=new char[i*3];
    	String s="";
    	String sch;
    	for(j=0;j<i;j++){
    		for(int k=1;k<i;k++){
    			modnumber+=n[(i-(j-k))%i];
    		}
    		int rightn=0;
    		if(modnumber%2==0){
    			rightn=modnumber+(n[j]>>>4);
    			ch[numch++]=(char)(rightn%25+97);
    			rightn=rightn/10;
    			ch[numch++]=(char)(rightn%25+97);
    			ch[numch++]=(char)(modnumber%25+97);
    		}
    		else{
    			rightn=modnumber+(n[j]>>>4);
    			ch[numch++]=(char)(rightn%9+48);
    			rightn=rightn/10;
    			ch[numch++]=(char)(rightn%9+48);
    			ch[numch++]=(char)(modnumber%9+48);
    		}
    	}
    	for(j=0;j<numch;j++){
    		s=s+ch[j];
    	}
    	return s;
    }
}
