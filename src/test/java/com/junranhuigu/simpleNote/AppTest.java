package com.junranhuigu.simpleNote;

import com.junranhuigu.simpleNote.util.NoteUtil;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
   @org.junit.Test
   public void test(){
	  System.out.println(NoteUtil.allNotes().size());
   }
}
