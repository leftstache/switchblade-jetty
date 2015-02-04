package com.leftstache.switchblade.jetty;

import com.google.inject.*;
import com.leftstache.switchblade.core.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;

import java.util.*;

/**
 * @author Joel Johnson
 */
@Component
public class JettyApplicationListener implements ApplicationListener, BeanListener {
	private Server jettyServer;

	@Inject
	private AutoConfiguredServlet autoConfiguredServlet;

	@Override
	public void started(SwitchbladeApplication<?> switchbladeApplication) throws Exception {
		jettyServer = new Server(8080);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		ServletHolder servletHolder = new ServletHolder(autoConfiguredServlet);
		context.addServlet(servletHolder, "/*");

		jettyServer.setHandler(context);
		jettyServer.start();
	}

	@Override
	public void ended(SwitchbladeApplication<?> switchbladeApplication) throws Exception {
		jettyServer.stop();
		jettyServer.destroy();
	}

	@Override
	public void postConstruct(Object o) throws Exception {
		autoConfiguredServlet.registerEndpoint(o);
	}
}
