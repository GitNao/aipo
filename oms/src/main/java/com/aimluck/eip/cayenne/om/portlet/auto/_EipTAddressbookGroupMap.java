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

/** Class _EipTAddressbookGroupMap was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _EipTAddressbookGroupMap extends org.apache.cayenne.CayenneDataObject {

    public static final String ADDRESS_ID_PROPERTY = "addressId";
    public static final String EIP_MADDRESSBOOK_PROPERTY = "eipMAddressbook";
    public static final String EIP_TADDRESS_GROUP_PROPERTY = "eipTAddressGroup";

    public static final String ID_PK_COLUMN = "ID";

    public void setAddressId(Integer addressId) {
        writeProperty("addressId", addressId);
    }
    public Integer getAddressId() {
        return (Integer)readProperty("addressId");
    }
    
    
    public void setEipMAddressbook(com.aimluck.eip.cayenne.om.portlet.EipMAddressbook eipMAddressbook) {
        setToOneTarget("eipMAddressbook", eipMAddressbook, true);
    }

    public com.aimluck.eip.cayenne.om.portlet.EipMAddressbook getEipMAddressbook() {
        return (com.aimluck.eip.cayenne.om.portlet.EipMAddressbook)readProperty("eipMAddressbook");
    } 
    
    
    public void setEipTAddressGroup(com.aimluck.eip.cayenne.om.portlet.EipMAddressGroup eipTAddressGroup) {
        setToOneTarget("eipTAddressGroup", eipTAddressGroup, true);
    }

    public com.aimluck.eip.cayenne.om.portlet.EipMAddressGroup getEipTAddressGroup() {
        return (com.aimluck.eip.cayenne.om.portlet.EipMAddressGroup)readProperty("eipTAddressGroup");
    } 
    
    
}
