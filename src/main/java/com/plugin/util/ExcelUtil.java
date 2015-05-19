//--------------------------------------------------------------------------
// Copyright (c) 2010-2020, En.dennisit or Cn.苏若年
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// Neither the name of the dennisit nor the names of its contributors
// may be used to endorse or promote products derived from this software
// without specific prior written permission.
// Author: dennisit@163.com | dobby | 苏若年
//--------------------------------------------------------------------------
package com.plugin.util;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class ExcelUtil {

    private Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * @param request
     * @param response
     * @param exportName：生成下载的文件名
     * @param templateName：位于指定的路径下的模板文件名
     * @param dataMap：需要导出的数据
     * @throws Exception
     */
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String exportName, String templateName, Map<String, Object> dataMap) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + exportName + "\"");

        String templateFullPath = request.getSession().getServletContext().getRealPath("/") + "excel_template/" + templateName;
        XLSTransformer transformer = new XLSTransformer();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(templateFullPath));
            HSSFWorkbook workbook = transformer.transformXLS(is, dataMap);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

}
