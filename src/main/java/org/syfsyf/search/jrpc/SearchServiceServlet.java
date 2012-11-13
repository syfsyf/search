package org.syfsyf.search.jrpc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.jsonrpc4j.JsonRpcServer;

public class SearchServiceServlet extends HttpServlet{

	private SearchService searchService;
	private JsonRpcServer jsonRpcServer;

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//jsonRpcServer.
		jsonRpcServer.handle(req, resp);
		
	}
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.searchService=new SearchService();
		this.jsonRpcServer=new JsonRpcServer(this.searchService,SearchService.class);
		
		// TODO Auto-generated method stub
		//super.init(config);
	}
	
}
