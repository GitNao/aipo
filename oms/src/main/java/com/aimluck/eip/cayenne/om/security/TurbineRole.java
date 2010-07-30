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
package com.aimluck.eip.cayenne.om.security;

import org.apache.cayenne.ObjectId;
import org.apache.jetspeed.om.security.Role;

import com.aimluck.eip.cayenne.om.security.auto._TurbineRole;

public class TurbineRole extends _TurbineRole implements Role {

  public String getId() {
    if (getObjectId() != null && !getObjectId().isTemporary()) {
      Object obj = getObjectId().getIdSnapshot().get(ROLE_ID_PK_COLUMN);
      if (obj instanceof Long) {
        Long value = (Long) obj;
        return value.toString();
      } else {
        return obj.toString();
      }
    } else {
      return null;
    }
  }

  public void setId(String id) {
    setObjectId(new ObjectId("TurbineRole", ROLE_ID_PK_COLUMN, Integer
        .valueOf(id)));
  }

  public String getName() {
    return getRoleName();
  }

  public void setName(String name) {
    setRoleName(name);
  }

}
