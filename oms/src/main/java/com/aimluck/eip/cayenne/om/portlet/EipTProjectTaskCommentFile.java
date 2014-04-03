package com.aimluck.eip.cayenne.om.portlet;

import org.apache.cayenne.ObjectId;

import com.aimluck.eip.cayenne.om.portlet.auto._EipTProjectTaskCommentFile;

public class EipTProjectTaskCommentFile extends _EipTProjectTaskCommentFile
    implements IProjectFile {

  @Override
  public Integer getFileId() {
    if (getObjectId() != null && !getObjectId().isTemporary()) {
      Object obj = getObjectId().getIdSnapshot().get(FILE_ID_PK_COLUMN);
      if (obj instanceof Long) {
        Long value = (Long) obj;
        return Integer.valueOf(value.intValue());
      } else {
        return (Integer) obj;
      }
    } else {
      return null;
    }
  }

  @Override
  public void setFileId(String id) {
    setObjectId(new ObjectId("EipTProjectTaskFile", FILE_ID_PK_COLUMN, Integer
      .valueOf(id)));
  }

  @Override
  public void setEipT(Object eipT) {
    setEipTProjectTaskComment((EipTProjectTaskComment) eipT);
  }

}
