package com.hp.titan.common.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SVNDiff {
	
	
	// /* */ comments lines
	private int commentLine = 0;
	
	// <!-- --> and // comments lines
	private int commentLine1 = 0;	
	
	// add null lines
	private int commentLine2 = 0;
	
	// temp record three comments 
	private int commentLineTemp = 0;	 
	private int commentLineTemp1 = 0;
	private int commentLineTemp2 = 0;
	
	// statistics comments switch
	private boolean comment = false;
	private boolean comment1 = false;
	
	// add all lines
	private int addLines = 0;
	
	// keep filepathname and change lines
	private Map<String,Integer> map = new HashMap<String,Integer>();
	
	// keep filepathname
	private List<String> updatechangefilename=new ArrayList<String>();
	
	// record which files is executing
	private int count = 0;
	
	// record all comments per file
	private int allCommentsPerfile = 0;
	
	// temp parameters
	private int temp = 0;
	private String tempstr = "";
	private String strTemp = "";
	
//	public static void main(String[] args) {
//		Date date = new Date();
//		File file = new File("C:\\Users\\chunjian\\Desktop\\changecode.txt");
//		getChild(file);  
//		for(String ss1 : updatechangefilename){
//			System.out.println("Filename and change lines: " + ss1 + "--- "+map.get(ss1));  
//		}
//		System.out.println("------------------------------------");
//        System.out.println("/**/ and // comment lines: " + commentLine);  
//        System.out.println("<!-- -->comment lines: " + commentLine1);  
//        System.out.println("add null lines: " + commentLine2);
//        Date date1 = new Date();
//        System.out.println(date1.getTime()-date.getTime()+"ms");
//	}
	
	public void getChild(File child){
		try {  
            BufferedReader br =  new BufferedReader(new FileReader(child));  
            String line = "";  
            while ((line = br.readLine()) != null) {
                parse(line);
            } 
            if(updatechangefilename.size()==count){
            	mapLastUpdateFile();
            }
        } catch (FileNotFoundException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	public int getAllCommitLines(BufferedReader br){
		int i = 0;
		String line = "";
        try {
			while ((line = br.readLine()) != null) {
			    parse(line);
			}
			if(updatechangefilename.size()==count){
				mapLastUpdateFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(updatechangefilename.size()>0){
			for(String ss : updatechangefilename){
				i = i + map.get(ss);
			}
		}
		return i;
	}
	private void parse(String line) {  
	    line = line.trim();
	    if(line.startsWith("Index:")){
	    	String[] s = line.split(" ");
	    	strTemp = s[1];
	    }
	    if(line.startsWith("+")&&!line.startsWith("+++")){
	    	addLines++;
	    }
	    if (comment) {
	    	if(line.startsWith("+")&&!line.startsWith("+++")){
	    		commentLine++; 
	    	}
	    	if (line.endsWith("*/")) {  
	    		comment = false;  
	    	} else if (line.matches(".*\\*/.+")) {
	    		commentLine--;
	    		comment = false;  
	    	}
	    } else if (line.startsWith("+//")) {  
	    	commentLine++;  
	    } else if (line.matches("\\+.*http://.*") || line.matches("\\+.*//SPRING//DTD BEAN//EN.*")){
	    	
	    } else if (line.matches("\\+.+//.*")) {  
	    	commentLine++;  
	    } else if(line.matches("\\+\\s*chmod.*") && line.endsWith("/*")){
	    	comment = false;
	    } else if (line.matches("\\+\\s*/\\*.*") && !line.endsWith("*/")) {
	    	commentLine++;
	    	if (findPair(line)) {
	    		commentLine--;
	    		comment = false;  
	    	} else {  
	    		comment = true;  
	    	}  
	    } else if (line.matches("\\+\\s*/\\*.*") && line.endsWith("*/")) {
	    	commentLine++;
	    	comment = false;
	    } else if (line.matches("\\+.+/\\*.*") && !line.endsWith("*/")) {  
	    	if (findPair(line)) {  
	    		comment = false;  
	    	} else {  
	    		comment = true;  
	    	}  
	    } else if (line.matches("\\+.+/\\*.*") && line.endsWith("*/")) {  
	    	comment = false;  
	    }
	    
	   if (comment1) {
			if(line.startsWith("+")&&!line.startsWith("+++")){
	            commentLine1++;
			}
            if (line.endsWith("-->")) {  
                comment1 = false;  
            } else if (line.matches(".*-->.+")) {
            	commentLine1--;
                comment1 = false;  
            }  
        }else if (line.matches("\\+\\s*<!--.*") && line.endsWith("-->")) {
	    	commentLine1++;
	    	comment1 = false;
	    }else if (line.matches("\\+.+<!--.*") && !line.endsWith("-->")) {  
            if (findPair1(line)) {  
                comment1 = false;  
            } else {  
                comment1 = true;  
            }  
        } else if (line.matches("\\+.+<!--.*") && line.endsWith("-->")) {  
            comment1= false;  
        }
	   
	   // add all null
	   if(line.startsWith("+") && !line.startsWith("+++") && "".equals(line.substring(1))){
		   commentLine2++;	
	   }
	   
	   if(line.startsWith("+++")){
	    	count++;
	    	updatechangefilename.add(strTemp);
	    	allCommentsPerfile = commentLine-commentLineTemp + commentLine1-commentLineTemp1 + commentLine2-commentLineTemp2; 
	    	if(!"".equals(tempstr)){
	    		map.put(tempstr, addLines-temp-allCommentsPerfile);
	    	}
	    	temp = addLines;
	    	tempstr = strTemp;
	    	commentLineTemp = commentLine;
	    	commentLineTemp1 = commentLine1;
	    	commentLineTemp2 = commentLine2;
	    }
    }  
	private void mapLastUpdateFile(){
			map.put(tempstr, addLines-temp-(commentLine-commentLineTemp+commentLine1-commentLineTemp1+commentLine2-commentLineTemp2));
	}
	
	// to find if /* */ is in the same line or not
	private boolean findPair(String line) {   
		int count1 = 0;  
		int count2 = 0;  
		Pattern p = Pattern.compile("/\\*");  
		Matcher m = p.matcher(line);  
		while (m.find()) {  
			count1++;  
		}  
		p = Pattern.compile("\\*/");  
		m = p.matcher(line);  
		while (m.find()) {  
			count2++;  
		}  
		return (count1 == count2);  
	}  
	
	// to find if <!-- --> is in the same line or not 
	private boolean findPair1(String line) {   
		int count1 = 0;  
		int count2 = 0;  
		Pattern p = Pattern.compile("<!--");  
		Matcher m = p.matcher(line);  
		while (m.find()) {  
			count1++;  
		}  
		p = Pattern.compile("-->");  
		m = p.matcher(line);  
		while (m.find()) {  
			count2++;  
		}  
		return (count1 == count2);  
	}

	public int getCommentLine() {
		return commentLine;
	}

	public void setCommentLine(int commentLine) {
		this.commentLine = commentLine;
	}

	public int getCommentLine1() {
		return commentLine1;
	}

	public void setCommentLine1(int commentLine1) {
		this.commentLine1 = commentLine1;
	}

	public int getCommentLine2() {
		return commentLine2;
	}

	public void setCommentLine2(int commentLine2) {
		this.commentLine2 = commentLine2;
	}

	public int getCommentLineTemp() {
		return commentLineTemp;
	}

	public void setCommentLineTemp(int commentLineTemp) {
		this.commentLineTemp = commentLineTemp;
	}

	public int getCommentLineTemp1() {
		return commentLineTemp1;
	}

	public void setCommentLineTemp1(int commentLineTemp1) {
		this.commentLineTemp1 = commentLineTemp1;
	}

	public int getCommentLineTemp2() {
		return commentLineTemp2;
	}

	public void setCommentLineTemp2(int commentLineTemp2) {
		this.commentLineTemp2 = commentLineTemp2;
	}

	public boolean isComment() {
		return comment;
	}

	public void setComment(boolean comment) {
		this.comment = comment;
	}

	public boolean isComment1() {
		return comment1;
	}

	public void setComment1(boolean comment1) {
		this.comment1 = comment1;
	}

	public int getAddLines() {
		return addLines;
	}

	public void setAddLines(int addLines) {
		this.addLines = addLines;
	}

	public Map<String, Integer> getMap() {
		return map;
	}

	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}

	public List<String> getUpdatechangefilename() {
		return updatechangefilename;
	}

	public void setUpdatechangefilename(List<String> updatechangefilename) {
		this.updatechangefilename = updatechangefilename;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAllCommentsPerfile() {
		return allCommentsPerfile;
	}

	public void setAllCommentsPerfile(int allCommentsPerfile) {
		this.allCommentsPerfile = allCommentsPerfile;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public String getTempstr() {
		return tempstr;
	}

	public void setTempstr(String tempstr) {
		this.tempstr = tempstr;
	}

	public String getStrTemp() {
		return strTemp;
	}

	public void setStrTemp(String strTemp) {
		this.strTemp = strTemp;
	}  
	
	
}
