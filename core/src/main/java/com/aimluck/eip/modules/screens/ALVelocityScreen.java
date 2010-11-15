/*
 * Aipo is a groupware program developed by Aimluck,Inc.
 * Copyright (C) 2004-2010 Aimluck,Inc.
 * http://aipostyle.com/
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

package com.aimluck.eip.modules.screens;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.services.resources.JetspeedResources;
import org.apache.turbine.modules.screens.RawScreen;
import org.apache.turbine.util.RunData;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.modules.actions.common.ALAction;
import com.aimluck.eip.orm.DatabaseOrmService;
import com.aimluck.eip.util.ALCommonUtils;
import com.aimluck.eip.util.ALEipUtils;
import com.aimluck.eip.util.orgutils.ALOrgUtilsFactoryService;
import com.aimluck.eip.util.orgutils.ALOrgUtilsHandler;

/**
 * ブラウザにHTML（Velocity）を返すクラスです。 <br />
 * 
 */
public abstract class ALVelocityScreen extends RawScreen implements ALAction {

  /** logger */
  private static final JetspeedLogger logger = JetspeedLogFactoryService
    .getLogger(ALVelocityScreen.class.getName());

  /** コンテントタイプ */
  private static final String CONTENT_TYPE = "text/json;charset="
    + ALEipConstants.DEF_CONTENT_ENCODING;

  /** 表示モード */
  private String mode;

  /** result */
  private Object result;

  /** 検索結果を格納するリスト */
  private List<Object> resultList;

  /** 正常系のメッセージを格納するリスト */
  private List<String> msgList;

  /** 異常系のメッセージを格納するリスト */
  private List<String> errmsgList;

  protected void setTemplate(RunData rundata, Context context, String template) {
    BufferedWriter writer;
    try {
      ServletOutputStream out = null;
      HttpServletResponse response = rundata.getResponse();
      out = response.getOutputStream();

      writer =
        new BufferedWriter(new OutputStreamWriter(
          out,
          ALEipConstants.DEF_CONTENT_ENCODING));
      Template templete = Velocity.getTemplate(template);
      templete.merge(context, writer);
      writer.flush();
      writer.close();
    } catch (Exception e) {
      logger.error("[ALVelocityScreen]", e);
    }
  }

  /**
   *
   *
   */
  @Override
  protected void doOutput(RunData rundata) throws Exception {

    VelocityContext context = new VelocityContext();

    // MODEを取得
    mode = rundata.getParameters().getString(ALEipConstants.MODE);
    try {
      ALEipUtils.setupContext(rundata, context);

      this.doOutput(rundata, context);

    } catch (Exception ex) {
      logger.error("[ALVelocityScreen] Exception.", ex);
      ALEipUtils.redirectDBError(rundata);
    }
  }

  protected abstract void doOutput(RunData rundata, Context context)
      throws Exception;

  /**
   * 
   * @param obj
   */
  public void setResultData(Object obj) {
    result = obj;
  }

  /**
   * 
   * @param obj
   */
  public void addResultData(Object obj) {
    if (resultList == null) {
      resultList = new ArrayList<Object>();
    }
    resultList.add(obj);
  }

  /**
   * 
   * @param objList
   */
  public void setResultDataList(List<Object> objList) {
    resultList = objList;
  }

  /**
   * 
   * @param msg
   */
  public void addErrorMessage(String msg) {
    if (errmsgList == null) {
      errmsgList = new ArrayList<String>();
    }
    errmsgList.add(msg);
  }

  /**
   * 
   * @param msg
   */
  public void addErrorMessages(List<String> msgs) {
    if (errmsgList == null) {
      errmsgList = new ArrayList<String>();
    }
    errmsgList.addAll(msgs);
  }

  /**
   * 
   * @param msgs
   */
  public void setErrorMessages(List<String> msgs) {
    errmsgList = msgs;
  }

  /**
   * 
   * @param mode
   */
  public void setMode(String mode) {
    this.mode = mode;
  }

  /**
   * 
   * @return
   */
  public String getMode() {
    return mode;
  }

  /**
   * 
   * @param context
   */
  public void putData(RunData rundata, Context context) {
    context.put(ALEipConstants.MODE, mode);
    context.put(ALEipConstants.RESULT, result);
    context.put(ALEipConstants.RESULT_LIST, resultList);
    context.put(ALEipConstants.MESSAGE_LIST, msgList);
    context.put(ALEipConstants.ERROR_MESSAGE_LIST, errmsgList);
    context.put(ALEipConstants.ENTITY_ID, ALEipUtils.getTemp(
      rundata,
      context,
      ALEipConstants.ENTITY_ID));
    context.put("config", new JetspeedResources());
    context.put("utils", new ALCommonUtils());

    // For security
    context.put(ALEipConstants.SECURE_ID, rundata.getUser().getTemp(
      ALEipConstants.SECURE_ID));

    ALOrgUtilsHandler handler =
      ALOrgUtilsFactoryService.getInstance().getOrgUtilsHandler();
    Map<String, String> attribute =
      handler.getParameters(DatabaseOrmService.getInstance().getOrgId(rundata));
    for (Map.Entry<String, String> e : attribute.entrySet()) {
      context.put(e.getKey(), e.getValue());
    }
  }

  /**
   * 
   * @param rundata
   * @return
   */
  @Override
  protected String getContentType(RunData rundata) {
    return CONTENT_TYPE;
  }

}
