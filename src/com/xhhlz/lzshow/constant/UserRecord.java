package com.xhhlz.lzshow.constant;

public class UserRecord {

	public static final String TABLE_NAME = "user_record";
	public static final String FONTLIB_NAME = "fontlib_name";
	public static final String UPDATE_INDEX = "update_index";
	public static final String UPDATE_TIME = "update_time";
	public static final String ID = "user_record_id";
	
	private int updateIndex;
	private String fontLibName;
	
	public int getUpdateIndex() {
		return updateIndex;
	}
	public void setUpdateIndex(int updateIndex) {
		this.updateIndex = updateIndex;
	}
	
	public String getFontLibName() {
		return fontLibName;
	}
	
	public void setFontLibName(String fontLibName) {
		this.fontLibName = fontLibName;
	}
	
	public UserRecord() {
		
	}
	
	public UserRecord(int updateIndex, String fontLibName) {
		this.updateIndex = updateIndex;
		this.fontLibName = fontLibName;
	}
	
}