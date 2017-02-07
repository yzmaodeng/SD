package com.sd.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;

import com.sd.util.SmsUtil;

public class SysAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	
	private File myfile;
      
    // myFileContentType属性用来封装上传文件的类型  
    private String myfileContentType;  
  
    // myFileFileName属性用来封装上传文件的文件名  
    private String myfileFileName; 
    
	public void sendSms4Excel() {
		Pattern pattern = Pattern.compile("^1\\d{10}$");
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(getMyfile());

			Workbook book = Workbook.getWorkbook(fis);
			Sheet sheet = book.getSheet(0);
			String model = sheet.getName();
			for (int i = 1; i < sheet.getRows(); i++) {
				// 获取每一行的单元格
				String tele = sheet.getCell(0, i).getContents();// （列，行）

				Matcher matcher = pattern.matcher(tele);
				if (matcher.matches()) {
					SmsUtil.tplSendSms(Long.parseLong(model), "", tele);
				}
			}
		} catch(Exception e)  {
        	e.printStackTrace();
        } 
	}
	public File getMyfile() {
		return myfile;
	}
	public void setMyfile(File myfile) {
		this.myfile = myfile;
	}
	public String getMyfileContentType() {
		return myfileContentType;
	}
	public void setMyfileContentType(String myfileContentType) {
		this.myfileContentType = myfileContentType;
	}
	public String getMyfileFileName() {
		return myfileFileName;
	}
	public void setMyfileFileName(String myfileFileName) {
		this.myfileFileName = myfileFileName;
	}

}
