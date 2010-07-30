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
package com.aimluck.eip.cayenne.om.security.auto;

import java.util.List;

/** Class _TurbineRole was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _TurbineRole extends org.apache.cayenne.CayenneDataObject {

    public static final String OBJECTDATA_PROPERTY = "objectdata";
    public static final String ROLE_NAME_PROPERTY = "roleName";
    public static final String TURBINE_ROLE_PERMISSION_PROPERTY = "turbineRolePermission";
    public static final String TURBINE_USER_GROUP_ROLE_PROPERTY = "turbineUserGroupRole";

    public static final String ROLE_ID_PK_COLUMN = "ROLE_ID";

    public void setObjectdata(byte[] objectdata) {
        writeProperty("objectdata", objectdata);
    }
    public byte[] getObjectdata() {
        return (byte[])readProperty("objectdata");
    }
    
    
    public void setRoleName(String roleName) {
        writeProperty("roleName", roleName);
    }
    public String getRoleName() {
        return (String)readProperty("roleName");
    }
    
    
    public void addToTurbineRolePermission(com.aimluck.eip.cayenne.om.security.TurbineRolePermission obj) {
        addToManyTarget("turbineRolePermission", obj, true);
    }
    public void removeFromTurbineRolePermission(com.aimluck.eip.cayenne.om.security.TurbineRolePermission obj) {
        removeToManyTarget("turbineRolePermission", obj, true);
    }
    public List getTurbineRolePermission() {
        return (List)readProperty("turbineRolePermission");
    }
    
    
    public void addToTurbineUserGroupRole(com.aimluck.eip.cayenne.om.security.TurbineUserGroupRole obj) {
        addToManyTarget("turbineUserGroupRole", obj, true);
    }
    public void removeFromTurbineUserGroupRole(com.aimluck.eip.cayenne.om.security.TurbineUserGroupRole obj) {
        removeToManyTarget("turbineUserGroupRole", obj, true);
    }
    public List getTurbineUserGroupRole() {
        return (List)readProperty("turbineUserGroupRole");
    }
    
    
}
