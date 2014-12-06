package com.bitlove.mw.pojo;
/**
 * 书籍
 * */
public class Book {
	private String bookName;
	private String bookShowName;
	private String isInit;
	private int bookImgId;
	public int getBookImgId() {
		return bookImgId;
	}
	public void setBookImgId(int bookImgId) {
		this.bookImgId = bookImgId;
	}
	public String getBookShowName() {
		return bookShowName;
	}
	public void setBookShowName(String bookShowName) {
		this.bookShowName = bookShowName;
	}
	public String getIsInit() {
		return isInit;
	}
	public void setIsInit(String isInit) {
		this.isInit = isInit;
	}
	private String bookPath;
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookPath() {
		return bookPath;
	}
	public void setBookPath(String bookPath) {
		this.bookPath = bookPath;
	}
}
