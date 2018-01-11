package com.junranhuigu.simpleNote.vo;

public class Note {
	private String photoName;
	private String note;
	
	public Note() {
		// TODO Auto-generated constructor stub
	}
	
	public Note(String photoName, String note) {
		this.photoName = photoName;
		this.note = note;
	}

	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
