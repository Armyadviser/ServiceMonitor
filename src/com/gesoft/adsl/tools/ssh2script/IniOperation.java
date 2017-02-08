package com.gesoft.adsl.tools.ssh2script;

import java.util.Vector;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IniOperation {

  public IniOperation() {
    vecIniList = new Vector();
  }

  private class Session{
    String strMean;
    String strKey;
    Vector vecNoteValue;
  }

  private class Note{
    String strMean;
    String strKey;
    String strValue;
  }

  private String strFileName = null;
  private Vector vecIniList;
  private Session session = null;
  private String strMean = null;
  private boolean bChanged = false;

  //回写信息
  public synchronized boolean saveIni()throws Exception{
      if(!bChanged){
          return true;
      }
      String strLineGe = System.getProperty ("line.separator");

      File fIniFile = new File(strFileName);
      long nLastTime = fIniFile.lastModified();
      try{
          if(vecIniList == null) {
              return false;
          }
          FileWriter fw;
          fw = new FileWriter(fIniFile, false);

          Object[] objSession = vecIniList.toArray();
          int nSessionNum = vecIniList.size();

          for (int i = 0; i < nSessionNum; i++) {
              Session sesRoot = (Session) objSession[i];
              if (sesRoot.strMean != null) {
                  fw.write(sesRoot.strMean);
                  fw.write(strLineGe);
              }
              if (sesRoot.strKey != null) {
                  fw.write("[");
                  fw.write(sesRoot.strKey);
                  fw.write("]");
                  fw.write(strLineGe);
              }
              Object[] objNote = sesRoot.vecNoteValue.toArray();
              int nNoteNum = sesRoot.vecNoteValue.size();
              for (int j = 0; j < nNoteNum; j++) {
                  Note noteValue = (Note) objNote[j];
                  if (noteValue.strMean != null) {
                      fw.write(noteValue.strMean);
                      fw.write(strLineGe);
                  }
                  if (noteValue.strKey != null) {
                      fw.write(noteValue.strKey);
                      fw.write("=");
                  }
                  if (noteValue.strValue != null) {
                      fw.write(noteValue.strValue);
                  }
                  fw.write(strLineGe);
              }
         }
         fw.close();
         fIniFile.setLastModified(nLastTime);
    }
    catch(IOException ex){
      throw new IOException();
    }
    return true;
  }

  //清空
  public synchronized boolean cleanIni(){
     if (vecIniList == null) {
         return false;
     }
     if(vecIniList.isEmpty()){
         return true;
     }
     vecIniList.clear();
     bChanged = true;
     return true;
  }

  //重置文本值
  public boolean setKeyValue(String strSection, String strKey,
                                    String strNewValue){
      if(vecIniList == null || strSection == null || strKey == null || strNewValue == null){
          return false;
      }

      Object[] objSession = vecIniList.toArray();
      int nSessionNum = vecIniList.size();
      for (int i = 0; i < nSessionNum; i++) {
          Session sesRoot = (Session) objSession[i];
          if(sesRoot.strKey.compareTo(strSection) != 0){
              continue;
          }
          Object[] objNote = sesRoot.vecNoteValue.toArray();
          int nNoteNum = sesRoot.vecNoteValue.size();

          for (int j = 0; j < nNoteNum; j++) {
              Note noteValue = (Note) objNote[j];
              if (noteValue.strKey.compareTo(strKey) == 0) {
                 noteValue.strValue = strNewValue;
                 bChanged = true;
              }
              sesRoot.vecNoteValue.set(j, noteValue);
              vecIniList.set(i, sesRoot);
          }
       }
      return true;
  }

  //重置long型值
  public boolean setKeyValue(String strSection, String strKey,
                                      long lNewValue) {
      return !(vecIniList == null || strSection == null || strKey == null || lNewValue < 0)
              && (setKeyValue(strSection, strKey, Long.toString(lNewValue)));
  }

  //重置bool型值
  public boolean setKeyValue(String strSection, String strKey,
                                    boolean bNewValue){
      if(vecIniList == null || strSection == null || strKey == null){
          return false;
      }
      String strBNewValue = Long.toString(0);
      if(bNewValue){
        strBNewValue = Long.toString(1);
      }
      return (setKeyValue(strSection, strKey, strBNewValue));
  }

  //重置字符型值
  public boolean setKeyValue(String strSection, String strKey,
                                    char chNewValue){
      if(vecIniList == null || strSection == null || strKey == null){
         return false;
      }
      char[] chValue = new char[1];
      chValue[0] = chNewValue;
      String strChNewValue = new String(chValue);
      return (setKeyValue(strSection, strKey, strChNewValue));
  }

  //取文本型值
  public String getKeyValue(String strSection, String strKey) {
     if(vecIniList == null || strSection == null || strKey == null){
        return null;
     }
     Object[] objSession = vecIniList.toArray();
     int nSessionNum = vecIniList.size();
     for (int i = 0; i < nSessionNum; i++) {
        Session sesRoot = (Session) objSession[i];
        if (sesRoot.strKey.compareTo(strSection) != 0) {
            continue;
        }
        Object[] objNote = sesRoot.vecNoteValue.toArray();
        int nNoteNum = sesRoot.vecNoteValue.size();

        for (int j = 0; j < nNoteNum; j++) {
            Note noteValue = (Note) objNote[j];
            if (noteValue.strKey.compareTo(strKey) == 0) {
//              return JTools.strChange(noteValue.strValue);
            	return noteValue.strValue;
            }
        }
      }
      return null;
  }

  //取long型值
  public long getKeyValueInt(String strSection, String strKey){
    if(vecIniList == null || strSection == null || strKey == null){
       return -1;
    }
    String strReValue = getKeyValue(strSection, strKey);
    if(strReValue == null){
        return -1;
    }
    if(strReValue.length() == 0){
        return -1;
    }
    return new Long(strReValue);
  }

  //取bool型值
  public boolean getKeyValueBool(String strSection, String strKey) {
      if (vecIniList == null || strSection == null || strKey == null) {
          return false;
      }
      String strReValue = getKeyValue(strSection, strKey);
      return strReValue != null
              && strReValue.length() != 0
              && strReValue.compareTo("0") != 0;
  }

  //取字符型值
  public char getKeyValueChar(String strSection, String strKey) {
      if(vecIniList == null || strSection == null || strKey == null){
          return '0';
          }
      String strReValue = getKeyValue(strSection, strKey);
      if(strReValue == null){
          return '0';
      }
      if (strReValue.length() == 0) {
          return '0';
      }
      char[] chReValue = strReValue.toCharArray();
      return chReValue[0];
  }

  //取段中的关键字上方的注示
  public String getNoteValue(String strSection, String strKey){
      if(strSection == null || strKey == null){
          return null;
      }
      Object[] objSession = vecIniList.toArray();
      int nSessionNum = vecIniList.size();
      for (int i = 0; i < nSessionNum; i++) {
          Session sesRoot = (Session) objSession[i];
          if (sesRoot.strKey.compareTo(strSection) != 0) {
              continue;
          }
          Object[] objNote = sesRoot.vecNoteValue.toArray();
          int nNoteNum = sesRoot.vecNoteValue.size();

          for (int j = 0; j < nNoteNum; j++) {
              Note noteValue = (Note) objNote[j];
              if (noteValue.strKey.compareTo(strKey) == 0) {
                return noteValue.strMean;
              }
          }
      }
      return null;
  }

  //取段上方的注示
  public String getSessionValue(String strSection){
      if(strSection == null){
          return null;
      }
      Object[] objSession = vecIniList.toArray();
      int nSessionNum = vecIniList.size();
      for (int i = 0; i < nSessionNum; i++) {
          Session sesRoot = (Session) objSession[i];
          if (sesRoot.strKey.compareTo(strSection) == 0) {
              return sesRoot.strMean;
          }
      }
      return null;
  }

  //载ＩＮＩ文件
  public boolean loadIni(String strIniFileName) {
      try{
        strFileName = strIniFileName;
        FileReader br = new FileReader(); //建立FileReader对象，并实例化为fr
        br.open(strFileName);
        String strLine; //从文件读取一行字符串
        //判断读取到的字符串是否不为空
        while (br.hasNext()) {
        	strLine = br.getLine();
            LoadLine(strLine);
        }
        if (session != null) {
            vecIniList.add(session);
            session = null;
        }
        br.close();
      }
      catch (IOException ex) {
        return false;
      }

      return true;
  }

//载一行
  private void LoadLine(String strLine) {
	  if (strLine.length() == 0) {
		  return;
	  }

      String strLineGe = System.getProperty ("line.separator");
      int nMeanPos = strLine.indexOf("#");
      if (nMeanPos == 0) {
          if (strMean != null) {
            strMean = strMean.concat(strLineGe).concat(strLine);
          }
          else {
            strMean = strLine;
          }
          return;
      }

      int pos = strLine.indexOf("=", 0);
      if (pos != -1) {
          if(session == null){
            return;
          }
          Note note = new Note();
          note.strKey = strLine.substring(0, pos).trim();
          note.strValue = strLine.substring(pos + 1, strLine.length()).trim();
          note.strMean = strMean;
          strMean = null;
          session.vecNoteValue.add(note);
      }
      else {
          if (session != null) {
              vecIniList.add(session);
              session = null;
          }
          int posSt = strLine.indexOf("[", 0);
          if (posSt != -1) {
            int posEn = strLine.indexOf("]", posSt + 1);
            if (posEn > posSt) {
                session = new Session();
                session.strKey = strLine.substring(posSt + 1, posEn);
                session.vecNoteValue = new Vector();
                session.strMean = strMean;
                strMean = null;
            }
           }
        }
    }

}
