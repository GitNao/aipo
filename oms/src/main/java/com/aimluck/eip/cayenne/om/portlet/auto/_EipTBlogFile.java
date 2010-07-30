/*
 * Aipo is a groupware program developed by Aimluck,Inc.
 * Copyright (C) 2004-2008 Aimluck,Inc.
 * http://aipostyle.com/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aimluck.eip.cayenne.om.portlet.auto;

/** Class _EipTBlogFile was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _EipTBlogFile extends org.apache.cayenne.CayenneDataObject {

    public static final String CREATE_DATE_PROPERTY = "createDate";
    public static final String FILE_PATH_PROPERTY = "filePath";
    public static final String FILE_THUMBNAIL_PROPERTY = "fileThumbnail";
    public static final String OWNER_ID_PROPERTY = "ownerId";
    public static final String TITLE_PROPERTY = "title";
    public static final String UPDATE_DATE_PROPERTY = "updateDate";
    public static final String EIP_TBLOG_ENTRY_PROPERTY = "eipTBlogEntry";

    public static final String FILE_ID_PK_COLUMN = "FILE_ID";

    public void setCreateDate(java.util.Date createDate) {
        writeProperty("createDate", createDate);
    }
    public java.util.Date getCreateDate() {
        return (java.util.Date)readProperty("createDate");
    }
    
    
    public void setFilePath(String filePath) {
        writeProperty("filePath", filePath);
    }
    public String getFilePath() {
        return (String)readProperty("filePath");
    }
    
    
    public void setFileThumbnail(byte[] fileThumbnail) {
        writeProperty("fileThumbnail", fileThumbnail);
    }
    public byte[] getFileThumbnail() {
        return (byte[])readProperty("fileThumbnail");
    }
    
    
    public void setOwnerId(Integer ownerId) {
        writeProperty("ownerId", ownerId);
    }
    public Integer getOwnerId() {
        return (Integer)readProperty("ownerId");
    }
    
    
    public void setTitle(String title) {
        writeProperty("title", title);
    }
    public String getTitle() {
        return (String)readProperty("title");
    }
    
    
    public void setUpdateDate(java.util.Date updateDate) {
        writeProperty("updateDate", updateDate);
    }
    public java.util.Date getUpdateDate() {
        return (java.util.Date)readProperty("updateDate");
    }
    
    
    public void setEipTBlogEntry(com.aimluck.eip.cayenne.om.portlet.EipTBlogEntry eipTBlogEntry) {
        setToOneTarget("eipTBlogEntry", eipTBlogEntry, true);
    }

    public com.aimluck.eip.cayenne.om.portlet.EipTBlogEntry getEipTBlogEntry() {
        return (com.aimluck.eip.cayenne.om.portlet.EipTBlogEntry)readProperty("eipTBlogEntry");
    } 
    
    
}
