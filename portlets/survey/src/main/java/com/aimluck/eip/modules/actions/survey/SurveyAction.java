/*
 * Aipo is a groupware program developed by Aimluck,Inc.
 * Copyright (C) 2004-2011 Aimluck,Inc.
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

package com.aimluck.eip.modules.actions.survey;

import org.apache.jetspeed.portal.portlets.VelocityPortlet;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;

import com.aimluck.eip.modules.actions.common.ALBaseAction;
import com.aimluck.eip.survey.SurveySelectData;
import com.aimluck.eip.survey.util.SurveyUtils;

public class SurveyAction extends ALBaseAction {
  @Override
  protected void buildNormalContext(VelocityPortlet portlet, Context context,
      RunData rundata) throws Exception {
    // セッション情報をクリア
    SurveyUtils.clearSession(rundata, context);
    SurveyUtils.refreshFlagsByDate();
    rundata.getParameters().setString("listmode", SurveyUtils.LIST_UNRESPONDED);
    SurveySelectData listData = new SurveySelectData();
    listData.initField();
    if (listData.doViewList(this, rundata, context)) {
      setTemplate(rundata, "survey");
    }
  }

  /**
   * 最大化表示の際の処理を記述します。 <BR>
   * 
   * @param portlet
   * @param context
   * @param rundata
   */
  @Override
  protected void buildMaximizedContext(VelocityPortlet portlet,
      Context context, RunData rundata) {
    // セッション情報をクリア
    SurveyUtils.clearSession(rundata, context);
    SurveyUtils.refreshFlagsByDate();
    rundata.getParameters().setString("maximized", "true");

    SurveySelectData listData = new SurveySelectData();
    listData.initField();
    if (listData.doViewList(this, rundata, context)) {
      setTemplate(rundata, "survey-list");
    }
  }

}