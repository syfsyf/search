package org.syfsyf.search.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class ImmiediateJSServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/javascript");
		String pathInfo = req.getPathInfo();
		//System.out.println("path info:"+pathInfo);
		
		//System.out.println("abs:"+ new File(".").getAbsolutePath());
		File js = new File("d:/mat/eclipse.org.syfsyf/search/src/main/webapp/js/app"+pathInfo);
		System.out.println("js:"+js);
		ServletOutputStream ous = resp.getOutputStream();
		
		IOUtils.copy(new FileInputStream(js), ous);		
		ous.close();
		
	}
	
	
}
