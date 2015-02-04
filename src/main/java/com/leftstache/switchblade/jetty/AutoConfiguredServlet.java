package com.leftstache.switchblade.jetty;

import com.google.inject.*;
import com.leftstache.switchblade.core.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
@Component
public class AutoConfiguredServlet extends HttpServlet {
	private List<RegisteredEndpoint> endpoints = new ArrayList<>();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean found = false;
		for (RegisteredEndpoint endpoint : endpoints) {
			boolean invoked = endpoint.invokeOrPass(request, response);
			if(invoked) {
				found = invoked;
				break;
			}
		}

		if(!found) {
			response.setStatus(404);
		}

		response.flushBuffer();
	}

	public void registerEndpoint(Object o) {
		if(o != null) {
			endpoints.addAll(RegisteredEndpoint.registerBean(o));
		}
	}
}
