/*
 * Aipo is a groupware program developed by TOWN, Inc.
 * Copyright (C) 2004-2015 TOWN, Inc.
 * http://www.aipo.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aimluck.eip.modules.actions.controllers;

// Jetspeed stuff
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.jetspeed.modules.actions.controllers.VelocityControllerAction;
import org.apache.jetspeed.om.profile.Entry;
import org.apache.jetspeed.om.profile.IdentityElement;
import org.apache.jetspeed.om.profile.Layout;
import org.apache.jetspeed.om.profile.MetaInfo;
import org.apache.jetspeed.om.profile.Parameter;
import org.apache.jetspeed.om.profile.Portlets;
import org.apache.jetspeed.om.profile.Reference;
import org.apache.jetspeed.om.profile.psml.PsmlControl;
import org.apache.jetspeed.om.profile.psml.PsmlLayout;
import org.apache.jetspeed.om.profile.psml.PsmlParameter;
import org.apache.jetspeed.om.registry.PortletEntry;
import org.apache.jetspeed.portal.Portlet;
import org.apache.jetspeed.portal.PortletController;
import org.apache.jetspeed.portal.PortletSet;
import org.apache.jetspeed.services.PsmlManager;
import org.apache.jetspeed.services.Registry;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.services.rundata.JetspeedRunData;
import org.apache.jetspeed.services.statemanager.SessionState;
import org.apache.turbine.modules.ActionLoader;
import org.apache.turbine.services.localization.Localization;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;

import com.aimluck.eip.services.accessctl.ALAccessControlConstants;
import com.aimluck.eip.util.ALCommonUtils;
import com.aimluck.eip.util.ALEipUtils;
import com.aimluck.eip.util.ALLocalizationUtils;
import com.aimluck.eip.util.CustomizeUtils;

/**
 * This action builds a context suitable for controllers handling grid
 * positioned layout using PortletSet.Constraints
 *
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta </a>
 * @author <a href="mailto:paulsp@apache.org">Paul Spencer </a>
 */
public class ALMultiColumnControllerAction extends VelocityControllerAction {

  private static final String REFERENCES_REMOVED = "references-removed";

  // private static final String FS = File.separator;

  /**
   * Static initialization of the logger for this class
   */
  private static final JetspeedLogger logger = JetspeedLogFactoryService
    .getLogger(ALMultiColumnControllerAction.class.getName());

  // protected ALLicenseFactoryService licenseService = null;

  /**
   * Subclasses must override this method to provide default behavior for the
   * portlet action
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected void buildNormalContext(PortletController controller,
      Context context, RunData rundata) {
    try {
      // retrieve the number of columns
      String cols = controller.getConfig().getInitParameter("cols");
      int colNum = 0;
      int rowNum = 0;
      try {
        colNum = Integer.parseInt(cols);
      } catch (Exception e) {
        // not an integer or null, default to standarrd value
        colNum = 3;
      }
      context.put("colNum", String.valueOf(colNum));

      // retrieve the size for each of the columns
      String sizes = controller.getConfig().getInitParameter("sizes");
      context.put("sizes", getCellSizes(sizes));

      // retrieve the class for each of the columns
      String columnClasses =
        controller.getConfig().getInitParameter("col_classes");
      context.put("col_classes", getCellClasses(columnClasses));

      PortletSet set = controller.getPortlets();

      // normalize the constraints and calculate max num of rows needed
      Enumeration<?> en = set.getPortlets();
      int row = 0;
      int col = 0;
      while (en.hasMoreElements()) {
        Portlet p = (Portlet) en.nextElement();

        PortletSet.Constraints constraints =
          p.getPortletConfig().getConstraints();

        if ((constraints != null)
          && (constraints.getColumn() != null)
          && (constraints.getRow() != null)) {
          col = constraints.getColumn().intValue();
          row = constraints.getRow().intValue();
          if (row > rowNum) {
            rowNum = row;
          }
        }
      }
      row = (int) Math.ceil((double) set.size() / colNum);
      if (row > rowNum) {
        rowNum = row;
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Controller calculated setSize "
          + set.size()
          + " row "
          + row
          + " colNum: "
          + colNum
          + " rowNum: "
          + rowNum);
      }
      // initialize the result position table and the work list
      List[] table = new List[colNum];
      List filler = Collections.nCopies(rowNum + 2, null);
      for (int i = 0; i < colNum; i++) {
        table[i] = new ArrayList<Object>();
        table[i].addAll(filler);
      }

      List<Portlet> work = new ArrayList<Portlet>();

      // position the constrained elements and keep a reference to the
      // others
      for (int i = 0; i < set.size(); i++) {
        Portlet p = set.getPortletAt(i);
        // String name = p.getName();

        PortletSet.Constraints constraints =
          p.getPortletConfig().getConstraints();

        if ((constraints != null)
          && (constraints.getColumn() != null)
          && (constraints.getRow() != null)
          && (constraints.getColumn().intValue() < colNum)) {
          row = constraints.getRow().intValue();
          col = constraints.getColumn().intValue();

          table[col].set(row, p);
        } else {
          work.add(p);
        }
      }
      // insert the unconstrained elements in the table
      Iterator<Portlet> i = work.iterator();
      for (row = 0; row < rowNum; row++) {
        for (col = 0; i.hasNext() && (col < colNum); col++) {
          if (table[col].get(row) == null) {
            table[col].set(row, i.next());
          }
        }
      }

      // now cleanup any remaining null elements
      for (int j = 0; j < table.length; j++) {
        i = table[j].iterator();
        while (i.hasNext()) {
          Object obj = i.next();
          if (obj == null) {
            i.remove();
          }
        }
      }

      if (logger.isDebugEnabled()) {
        dumpColumns(table);
      }
      context.put("portlets", table);
    } catch (Exception e) {
      logger.error("ALMultiColumnControllerAction.buildNormalContext", e);
    }
  }

  /**
   * Subclasses must override this method to provide default behavior for the
   * portlet action
   */
  @SuppressWarnings("deprecation")
  @Override
  protected void buildCustomizeContext(PortletController controller,
      Context context, RunData rundata) {
    JetspeedRunData jdata = (JetspeedRunData) rundata;

    // アクセス権限「追加」のチェック
    int aclType = 0;
    aclType = ALAccessControlConstants.VALUE_ACL_INSERT;
    boolean hasAuthority =
      ALEipUtils.getHasAuthority(rundata, context, aclType);
    context.put("authority", hasAuthority);

    super.buildCustomizeContext(controller, context, rundata);

    List<?>[] columns = null;

    // retrieve the number of columns
    String cols = controller.getConfig().getInitParameter("cols");
    int colNum = 0;
    try {
      colNum = Integer.parseInt(cols);
    } catch (Exception e) {
      // not an integer or null, default to standarrd value
      colNum = 3;
    }
    context.put("colNum", String.valueOf(colNum));

    // retrieve the size for each of the columns
    String sizes = controller.getConfig().getInitParameter("sizes");
    context.put("sizes", getCellSizes(sizes));

    // retrieve the class for each of the columns
    String columnClasses =
      controller.getConfig().getInitParameter("col_classes");
    context.put("col_classes", getCellClasses(columnClasses));
    PortletSet customizedSet = (PortletSet) jdata.getCustomized();
    Portlets set =
      jdata.getCustomizedProfile().getDocument().getPortletsById(
        customizedSet.getID());
    columns = CustomizeUtils.buildCustomizeColumns(rundata, context, set);

    if (logger.isDebugEnabled()) {
      logger.debug("MultiCol: columns "
        + Arrays.toString(columns)
        + " set "
        + set);
    }

    if ((columns != null) && (columns.length == colNum)) {
      int eCount = 0;
      for (int i = 0; i < columns.length; i++) {
        eCount += columns[i].size();
      }

      if (logger.isDebugEnabled()) {
        logger.debug("MultiCol: eCount "
          + eCount
          + " setCount"
          + set.getEntryCount()
          + set.getPortletsCount());
      }
      if (eCount != set.getEntryCount() + set.getPortletsCount()) {
        if (logger.isDebugEnabled()) {
          logger.debug("MultiCol: rebuilding columns ");
        }
        columns = buildColumns(set, colNum);
      }

    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("MultiCol: rebuilding columns ");
      }
      columns = buildColumns(set, colNum);
    }

    context.put("portlets", columns);

    Map<String, String> titles = new HashMap<String, String>();
    for (int col = 0; col < columns.length; col++) {
      for (int row = 0; row < columns[col].size(); row++) {
        IdentityElement identityElement =
          (IdentityElement) columns[col].get(row);
        MetaInfo metaInfo = identityElement.getMetaInfo();
        if ((metaInfo != null) && (metaInfo.getTitle() != null)) {
          titles.put(identityElement.getId(), metaInfo.getTitle());
          continue;
        }

        if (identityElement instanceof Entry) {
          Entry entry = (Entry) identityElement;
          PortletEntry pentry =
            (PortletEntry) Registry.getEntry(Registry.PORTLET, entry
              .getParent());
          if ((pentry != null) && (pentry.getTitle() != null)) {
            titles.put(entry.getId(), pentry.getTitle());
            continue;
          }

          titles.put(entry.getId(), entry.getParent());
          continue;
        }

        if (identityElement instanceof Reference) {
          titles.put(identityElement.getId(), Localization.getString(
            rundata,
            "CUSTOMIZER_REF_DEFAULTTITLE"));
          continue;
        }

        // Let's make sure their is a title
        titles.put(identityElement.getId(), Localization.getString(
          rundata,
          "CUSTOMIZER_NOTITLESET"));
      }
    }

    Map<String, String> descriptions = new HashMap<String, String>();
    for (int col = 0; col < columns.length; col++) {
      for (int row = 0; row < columns[col].size(); row++) {
        IdentityElement identityElement =
          (IdentityElement) columns[col].get(row);
        MetaInfo metaInfo = identityElement.getMetaInfo();
        if ((metaInfo != null) && (metaInfo.getDescription() != null)) {
          descriptions.put(identityElement.getId(), metaInfo.getDescription());
          continue;
        }

        if (identityElement instanceof Entry) {
          Entry entry = (Entry) identityElement;
          PortletEntry pentry =
            (PortletEntry) Registry.getEntry(Registry.PORTLET, entry
              .getParent());
          if ((pentry != null) && (pentry.getDescription() != null)) {
            descriptions.put(entry.getId(), pentry.getDescription());
            continue;
          }

          descriptions.put(entry.getId(), entry.getParent());
          continue;
        }

        // if (identityElement instanceof Reference)
        // {
        // description.put(identityElement.getId(),
        // Localization.getString(rundata,
        // "CUSTOMIZER_REF_DEFAULTTITLE"));
        // continue;
        // }

        // Let's make sure their is a title
        // description.put(identityElement.getId(),
        // Localization.getString(rundata, "CUSTOMIZER_NOTITLESET"));
        descriptions.put(identityElement.getId(), ALLocalizationUtils
          .getl10n("COMMON_NO_EXPLAINATION"));
      }
    }

    context.put("titles", titles);
    context.put("descriptions", descriptions);
    context.put("action", "controllers.ALMultiColumnControllerAction");
    context.put("utils", new ALCommonUtils());

  }

  /**
   * Cancel the current customizations. If this was the last customization on
   * the stack, then return the user to the home page.
   */
  @Override
  public void doCancel(RunData data, Context context) {
    // move one level back in customization
    ((JetspeedRunData) data).setCustomized(null);
    // if we are all done customization
    if (((JetspeedRunData) data).getCustomized() == null) {
      try {
        ActionLoader.getInstance().exec(data, "controls.EndCustomize");
      } catch (Exception e) {
        logger.error("Unable to load action controls.EndCustomize ", e);
      }
    }
  }

  @SuppressWarnings("rawtypes")
  public void doSave(RunData data, Context context) {

    String js_peid = data.getParameters().get("js_peid");
    // Profile set = (Profile) ((JetspeedRunData) data).getProfile();
    Portlets portlets =
      ((JetspeedRunData) data)
        .getCustomizedProfile()
        .getDocument()
        .getPortletsById(js_peid);

    JetspeedRunData jdata = (JetspeedRunData) data;
    jdata.setCustomized(CustomizeUtils.getCustomizePortlet(data, js_peid));

    // update the changes made here to the profile being edited
    List[] columns =
      CustomizeUtils.buildCustomizeColumns(data, context, portlets);
    for (int col = 0; col < columns.length; col++) {
      for (int row = 0; row < columns[col].size(); row++) {
        setPosition((IdentityElement) columns[col].get(row), col, row);
      }
    }

    // move one level back in customization
    // ((JetspeedRunData) data).setCustomized(null);

    // if we are all done customization
    // if (((JetspeedRunData) data).getCustomized() == null)
    // {
    // save the edit profile and make it current
    try {
      ((JetspeedRunData) data).getCustomizedProfile().store();

      // Because of the way references are stored in memory, we have to
      // completely refresh
      // the profile after a references is removed (otherwise it will
      // continue
      // being displayed)
      SessionState customizationState =
        ((JetspeedRunData) data).getPageSessionState();
      String referencesRemoved =
        (String) customizationState.getAttribute(REFERENCES_REMOVED);
      if (referencesRemoved != null && referencesRemoved.equals("true")) {
        PsmlManager.refresh(((JetspeedRunData) data).getCustomizedProfile());
      }
    } catch (Exception e) {
      logger.error("Unable to save profile ", e);
    }

    try {
      ActionLoader.getInstance().exec(data, "controls.EndCustomize");
    } catch (Exception e) {
      logger.error("Unable to load action controls.EndCustomize ", e);
    }
    // }
  }

  @SuppressWarnings("rawtypes")
  public void doDelete(RunData data, Context context) {
    JetspeedRunData jdata = (JetspeedRunData) data;

    // アクセス権限のチェック
    ALEipUtils.checkAclPermissionForCustomize(
      data,
      context,
      ALAccessControlConstants.VALUE_ACL_DELETE);

    String js_peid = data.getParameters().get("js_peid");
    // Profile set = (Profile) ((JetspeedRunData) data).getProfile();
    Portlets portlets =
      ((JetspeedRunData) data)
        .getCustomizedProfile()
        .getDocument()
        .getPortletsById(js_peid);

    jdata.setCustomized(CustomizeUtils.getCustomizePortlet(data, js_peid));

    SessionState customizationState = jdata.getPageSessionState();
    customizationState.setAttribute(REFERENCES_REMOVED, "false");

    int col = data.getParameters().getInt("col", -1);
    int row = data.getParameters().getInt("row", -1);
    if (portlets != null) {
      List[] columns =
        CustomizeUtils.buildCustomizeColumns(data, context, portlets);
      if (columns.length == 0) {
        return;
      }

      if ((col > -1) && (row > -1)) {
        try {
          IdentityElement identityElement =
            (IdentityElement) columns[col].get(row);
          columns[col].remove(row);

          if (identityElement instanceof Entry) {
            for (int i = 0; i < portlets.getEntryCount(); i++) {
              if (portlets.getEntry(i) == identityElement) {
                portlets.removeEntry(i);
              }
            }
          } else if (identityElement instanceof Reference) {
            for (int i = 0; i < portlets.getReferenceCount(); i++) {
              if (portlets.getReference(i) == identityElement) {
                customizationState.setAttribute(REFERENCES_REMOVED, "true");
                portlets.removeReference(i);
              }
            }
          }

        } catch (Exception e) {
          // probably got wrong coordinates
          logger.error(
            "MultiColumnControllerAction: Probably got wrong coordinates",
            e);
        }
      }
      for (col = 0; col < columns.length; col++) {
        for (row = 0; row < columns[col].size(); row++) {
          setPosition((IdentityElement) columns[col].get(row), col, row);
        }
      }
      try {
        ((JetspeedRunData) data).getCustomizedProfile().store();
        PsmlManager.refresh(((JetspeedRunData) data).getCustomizedProfile());
      } catch (Exception e) {
        logger.error("Unable to save profile ", e);
      }
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void doLeft(RunData data, Context context) {
    // アクセス権限のチェック
    ALEipUtils.checkAclPermissionForCustomize(
      data,
      context,
      ALAccessControlConstants.VALUE_ACL_UPDATE);
    String js_peid = data.getParameters().get("js_peid");
    // Profile set = (Profile) ((JetspeedRunData) data).getProfile();
    Portlets portlets =
      ((JetspeedRunData) data)
        .getCustomizedProfile()
        .getDocument()
        .getPortletsById(js_peid);

    JetspeedRunData jdata = (JetspeedRunData) data;
    jdata.setCustomized(CustomizeUtils.getCustomizePortlet(data, js_peid));

    List[] columns =
      CustomizeUtils.buildCustomizeColumns(data, context, portlets);
    int col = data.getParameters().getInt("col", -1);
    int row = data.getParameters().getInt("row", -1);
    if (columns.length == 0) {
      return;
    }

    if ((col > 0) && (row > -1)) {
      move(columns, col, row, col - 1, row);
    }
    for (col = 0; col < columns.length; col++) {
      for (row = 0; row < columns[col].size(); row++) {
        setPosition((IdentityElement) columns[col].get(row), col, row);
      }
    }
    try {
      ((JetspeedRunData) data).getCustomizedProfile().store();
      PsmlManager.refresh(((JetspeedRunData) data).getCustomizedProfile());
    } catch (Exception e) {
      logger.error("Unable to save profile ", e);
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void doRight(RunData data, Context context) {
    // アクセス権限のチェック
    ALEipUtils.checkAclPermissionForCustomize(
      data,
      context,
      ALAccessControlConstants.VALUE_ACL_UPDATE);

    String js_peid = data.getParameters().get("js_peid");
    // Profile set = (Profile) ((JetspeedRunData) data).getProfile();
    Portlets portlets =
      ((JetspeedRunData) data)
        .getCustomizedProfile()
        .getDocument()
        .getPortletsById(js_peid);

    JetspeedRunData jdata = (JetspeedRunData) data;
    jdata.setCustomized(CustomizeUtils.getCustomizePortlet(data, js_peid));

    List[] columns =
      CustomizeUtils.buildCustomizeColumns(data, context, portlets);
    int col = data.getParameters().getInt("col", -1);
    int row = data.getParameters().getInt("row", -1);
    if (columns.length == 0) {
      return;
    }

    if ((col > -1) && (row > -1) && (col < columns.length - 1)) {
      move(columns, col, row, col + 1, row);
    }
    for (col = 0; col < columns.length; col++) {
      for (row = 0; row < columns[col].size(); row++) {
        setPosition((IdentityElement) columns[col].get(row), col, row);
      }
    }
    try {
      ((JetspeedRunData) data).getCustomizedProfile().store();
      PsmlManager.refresh(((JetspeedRunData) data).getCustomizedProfile());
    } catch (Exception e) {
      logger.error("Unable to save profile ", e);
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void doUp(RunData data, Context context) {
    // アクセス権限のチェック
    ALEipUtils.checkAclPermissionForCustomize(
      data,
      context,
      ALAccessControlConstants.VALUE_ACL_UPDATE);

    String js_peid = data.getParameters().get("js_peid");
    // Profile set = (Profile) ((JetspeedRunData) data).getProfile();
    Portlets portlets =
      ((JetspeedRunData) data)
        .getCustomizedProfile()
        .getDocument()
        .getPortletsById(js_peid);

    JetspeedRunData jdata = (JetspeedRunData) data;
    jdata.setCustomized(CustomizeUtils.getCustomizePortlet(data, js_peid));

    List[] columns =
      CustomizeUtils.buildCustomizeColumns(data, context, portlets);
    int col = data.getParameters().getInt("col", -1);
    int row = data.getParameters().getInt("row", -1);
    if (columns.length == 0) {
      return;
    }

    if ((col > -1) && (row > 0)) {
      move(columns, col, row, col, row - 1);
    }
    for (col = 0; col < columns.length; col++) {
      for (row = 0; row < columns[col].size(); row++) {
        setPosition((IdentityElement) columns[col].get(row), col, row);
      }
    }
    try {
      ((JetspeedRunData) data).getCustomizedProfile().store();
      PsmlManager.refresh(((JetspeedRunData) data).getCustomizedProfile());
    } catch (Exception e) {
      logger.error("Unable to save profile ", e);
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void doDown(RunData data, Context context) {
    // アクセス権限のチェック
    ALEipUtils.checkAclPermissionForCustomize(
      data,
      context,
      ALAccessControlConstants.VALUE_ACL_UPDATE);

    String js_peid = data.getParameters().get("js_peid");
    // Profile set = (Profile) ((JetspeedRunData) data).getProfile();
    Portlets portlets =
      ((JetspeedRunData) data)
        .getCustomizedProfile()
        .getDocument()
        .getPortletsById(js_peid);

    JetspeedRunData jdata = (JetspeedRunData) data;
    jdata.setCustomized(CustomizeUtils.getCustomizePortlet(data, js_peid));

    List[] columns =
      CustomizeUtils.buildCustomizeColumns(data, context, portlets);

    int col = data.getParameters().getInt("col", -1);
    int row = data.getParameters().getInt("row", -1);
    if (columns.length == 0) {
      return;
    }

    if ((col > -1) && (row > -1) && (row < columns[col].size() - 1)) {
      move(columns, col, row, col, row + 1);
    }
    for (col = 0; col < columns.length; col++) {
      for (row = 0; row < columns[col].size(); row++) {
        setPosition((IdentityElement) columns[col].get(row), col, row);
      }
    }
    try {
      ((JetspeedRunData) data).getCustomizedProfile().store();
      PsmlManager.refresh(((JetspeedRunData) data).getCustomizedProfile());
    } catch (Exception e) {
      logger.error("Unable to save profile ", e);
    }
  }

  public void doControl(RunData data, Context context) {
    JetspeedRunData jdata = (JetspeedRunData) data;

    String controlName = data.getParameters().getString("control");
    String id = data.getParameters().getString("js_peid");

    try {
      Entry entry = jdata.getCustomizedProfile().getDocument().getEntryById(id);

      if (entry != null) {
        if (controlName != null && controlName.trim().length() > 0) {
          PsmlControl control = new PsmlControl();
          control.setName(controlName);
          if (control != null) {
            entry.setControl(control);
          }
        } else {
          entry.setControl(null);
        }
      }
    } catch (Exception e) {
      logger.error("ALMultiColumnControllerAction.doControl", e);
    }
  }

  protected static void setPosition(IdentityElement identityElement, int col,
      int row) {
    boolean colFound = false;
    boolean rowFound = false;

    if (identityElement != null) {
      Layout layout = identityElement.getLayout();

      if (layout == null) {
        layout = new PsmlLayout();
        identityElement.setLayout(layout);
      }

      for (int i = 0; i < layout.getParameterCount(); i++) {
        Parameter p = layout.getParameter(i);

        if (p.getName().equals("column")) {
          p.setValue(String.valueOf(col));
          colFound = true;
        } else if (p.getName().equals("row")) {
          p.setValue(String.valueOf(row));
          rowFound = true;
        }
      }

      if (!colFound) {
        Parameter p = new PsmlParameter();
        p.setName("column");
        p.setValue(String.valueOf(col));
        layout.addParameter(p);
      }

      if (!rowFound) {
        Parameter p = new PsmlParameter();
        p.setName("row");
        p.setValue(String.valueOf(row));
        layout.addParameter(p);
      }
    }
  }

  protected static void move(List<Object>[] cols, int oCol, int oRow, int nCol,
      int nRow) {
    Object obj = null;

    if ((oCol < cols.length) && (oRow < cols[oCol].size())) {
      obj = cols[oCol].get(oRow);
      if (obj != null) {
        cols[oCol].remove(oRow);
      }
    }

    if (obj != null) {
      if (nCol < cols.length) {
        if (nRow < cols[nCol].size()) {
          cols[nCol].add(nRow, obj);
        } else {
          cols[nCol].add(obj);
        }
      }
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected static List[] buildColumns(Portlets set, int colNum) {
    // normalize the constraints and calculate max num of rows needed
    Iterator<?> iterator = set.getEntriesIterator();
    int row = 0;
    int col = 0;
    int rowNum = 0;
    while (iterator.hasNext()) {
      IdentityElement identityElement = (IdentityElement) iterator.next();

      Layout layout = identityElement.getLayout();

      if (layout != null) {
        for (int p = 0; p < layout.getParameterCount(); p++) {
          Parameter prop = layout.getParameter(p);

          try {
            if (prop.getName().equals("row")) {
              row = Integer.parseInt(prop.getValue());
              if (row > rowNum) {
                rowNum = row;
              }
            } else if (prop.getName().equals("column")) {
              col = Integer.parseInt(prop.getValue());
            }
          } catch (Exception e) {
            // ignore any malformed layout properties
          }
        }
      }
    }

    int sCount = set.getEntryCount() + set.getPortletsCount();
    row = (sCount / colNum) + 1;
    if (row > rowNum) {
      rowNum = row;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Controller customize colNum: "
        + colNum
        + " rowNum: "
        + rowNum);
    }
    // initialize the result position table and the work list
    List[] table = new List[colNum];
    List filler = Collections.nCopies(rowNum + 1, null);
    for (int i = 0; i < colNum; i++) {
      table[i] = new ArrayList();
      table[i].addAll(filler);
    }

    List<IdentityElement> work = new ArrayList<IdentityElement>();

    // position the constrained elements and keep a reference to the
    // others
    for (int i = 0; i < set.getEntryCount(); i++) {
      addElement(set.getEntry(i), table, work, colNum);
    }

    // Add references
    for (int i = 0; i < set.getReferenceCount(); i++) {
      addElement(set.getReference(i), table, work, colNum);
    }

    // insert the unconstrained elements in the table
    Iterator<IdentityElement> i = work.iterator();
    for (row = 0; row < rowNum; row++) {
      for (col = 0; i.hasNext() && (col < colNum); col++) {
        if (table[col].get(row) == null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Set portlet at col " + col + " row " + row);
          }
          table[col].set(row, i.next());
        }
      }
    }

    // now cleanup any remaining null elements
    for (int j = 0; j < table.length; j++) {
      if (logger.isDebugEnabled()) {
        logger.debug("Column " + j);
      }
      i = table[j].iterator();
      while (i.hasNext()) {
        Object obj = i.next();
        if (logger.isDebugEnabled()) {
          logger.debug("Element " + obj);
        }
        if (obj == null) {
          i.remove();
        }

      }
    }

    return table;
  }

  /**
   * Parses the size config info and returns a list of size values for the
   * current set
   *
   * @param sizeList
   *          java.lang.String a comma separated string a values
   * @return a List of values
   */
  protected static List<String> getCellSizes(String sizeList) {
    List<String> list = new Vector<String>();

    if (sizeList != null) {
      StringTokenizer st = new StringTokenizer(sizeList, ",");
      while (st.hasMoreTokens()) {
        list.add(st.nextToken());
      }
    }

    return list;
  }

  protected static List<String> getCellClasses(String classlist) {
    List<String> list = new Vector<String>();

    if (classlist != null) {
      StringTokenizer st = new StringTokenizer(classlist, ",");
      while (st.hasMoreTokens()) {
        list.add(st.nextToken());
      }
    }

    return list;
  }

  /**
   * Add an element to the "table" or "work" objects. If the element is
   * unconstrained, and the position is within the number of columns, then the
   * element is added to "table". Othewise the element is added to "work"
   *
   * @param element
   *          to add
   * @param table
   *          of positioned elements
   * @param work
   *          list of un-positioned elements
   * @param columnCount
   *          Number of colum
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected static void addElement(IdentityElement element, List[] table,
      List<IdentityElement> work, int columnCount) {

    Layout layout = element.getLayout();
    int row = -1;
    int col = -1;

    if (layout != null) {
      try {
        for (int p = 0; p < layout.getParameterCount(); p++) {
          Parameter prop = layout.getParameter(p);

          if (prop.getName().equals("row")) {
            row = Integer.parseInt(prop.getValue());
          } else if (prop.getName().equals("column")) {
            col = Integer.parseInt(prop.getValue());
          }
        }
      } catch (Exception e) {
        // ignore any malformed layout properties
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Constraints col " + col + " row " + row);
    }

    if ((row >= 0)
      && (col >= 0)
      && (col < columnCount)
      && (table[col].get(row) == null)) {
      table[col].set(row, element);
    } else {
      work.add(element);
    }

  }

  @SuppressWarnings("rawtypes")
  protected void dumpColumns(List[] cols) {
    for (int i = 0; i < cols.length; i++) {
      logger.debug("Column " + i);
      for (int j = 0; j < cols[i].size(); j++) {
        logger.debug("Row " + j + " object: " + cols[i].get(j));
      }
    }
  }
}
