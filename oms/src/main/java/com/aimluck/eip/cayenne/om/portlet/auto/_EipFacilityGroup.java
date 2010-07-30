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

/** Class _EipFacilityGroup was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _EipFacilityGroup extends org.apache.cayenne.CayenneDataObject {

    public static final String EIP_MFACILITY_PROPERTY = "eipMFacility";
    public static final String TURBINE_GROUP_PROPERTY = "turbineGroup";

    public static final String ID_PK_COLUMN = "ID";

    public void setEipMFacility(com.aimluck.eip.cayenne.om.portlet.EipMFacility eipMFacility) {
        setToOneTarget("eipMFacility", eipMFacility, true);
    }

    public com.aimluck.eip.cayenne.om.portlet.EipMFacility getEipMFacility() {
        return (com.aimluck.eip.cayenne.om.portlet.EipMFacility)readProperty("eipMFacility");
    } 
    
    
    public void setTurbineGroup(com.aimluck.eip.cayenne.om.security.TurbineGroup turbineGroup) {
        setToOneTarget("turbineGroup", turbineGroup, true);
    }

    public com.aimluck.eip.cayenne.om.security.TurbineGroup getTurbineGroup() {
        return (com.aimluck.eip.cayenne.om.security.TurbineGroup)readProperty("turbineGroup");
    } 
    
    
}
