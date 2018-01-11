package com.junranhuigu.simpleNote.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.junranhuigu.simpleNote.Start;
import com.junranhuigu.simpleNote.vo.Note;

public class NoteUtil {
	/**
	 * 获取所有的注释
	 * */
	public static List<Note> allNotes(){
		String noteFilePath = Start.packPath + File.separator + "info" + File.separator + "note.out";
		File file = new File(noteFilePath);
		if(!file.exists()){
			return Collections.emptyList();
		}
		List<Note> notelist = new ArrayList<>();
		try(	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"))){
			while(reader.ready()){
				String line = reader.readLine();
				String[] splits = line.split("=");
				notelist.add(new Note(splits[0], splits[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notelist;
	}
	
	/**
	 * 获取单个注释
	 * */
	public static String note(String photoName){
		List<Note> notes = new ArrayList<>();
		for(Note note : notes){
			if(note.getPhotoName().equals(photoName)){
				return note.getNote();
			}
		}
		return null;
	}
}
