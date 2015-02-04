package com.leftstache.switchblade.jetty;

import com.google.common.collect.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;

/**
 * @author Joel Johnson
 */
public class RegisteredEndpoint {
	private final Endpoint classAnnotation;
	private final Endpoint methodAnnotation;
	private final Object instance;
	private final Method method;
	private final List<Parameter> parameters;
	private final Pattern pathPattern;

	public static List<RegisteredEndpoint> registerBean(Object bean) {
		List<RegisteredEndpoint> discoveredEndpoints = new ArrayList<>();

		Class<?> beanClass = bean.getClass();
		Endpoint classAnnotation = beanClass.getAnnotation(Endpoint.class);

		for (Method method : beanClass.getMethods()) {
			Endpoint methodAnnotation = method.getAnnotation(Endpoint.class);
			if(methodAnnotation != null) {
				method.setAccessible(true);
				discoveredEndpoints.add(new RegisteredEndpoint(classAnnotation, methodAnnotation, bean, method));
			}
		}

		return discoveredEndpoints;
	}

	private RegisteredEndpoint(Endpoint classAnnotation, Endpoint methodAnnotation, Object instance, Method method) {
		this.classAnnotation = classAnnotation;
		this.methodAnnotation = methodAnnotation;
		this.instance = instance;
		this.method = method;
		this.pathPattern = Pattern.compile(createPattern(classAnnotation.path() + methodAnnotation.path()));
		this.parameters = ImmutableList.copyOf(method.getParameters());
	}

	private static String createPattern(String pattern) {
		String[] split = pattern.split("/");
		StringBuilder sb = new StringBuilder();

		sb.append("^");

		for (String pathChunk : split) {
			if(pathChunk.isEmpty()) {
				continue;
			}

			sb.append("/");
			if(pathChunk.startsWith(":")) {
				sb.append("(?<").append(pathChunk.substring(1)).append(">[^/]*?)");
			} else {
				sb.append(pathChunk);
			}
		}

		sb.append("$");

		return sb.toString();
	}

	public boolean invokeOrPass(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getPathInfo();
		Matcher matcher = pathPattern.matcher(path);

		if(matcher.find()) {
			int count = 0;

			Object[] parameterValues = new Object[this.parameters.size()];
			for (Parameter parameter : this.parameters) {
				if(ServletRequest.class.isAssignableFrom(parameter.getType())) {
					parameterValues[count] = request;
				} else if(ServletResponse.class.isAssignableFrom(parameter.getType())) {
					parameterValues[count] = response;
				} else {
					String parameterName = parameter.getName();
					String parameterValue;
					try {
						parameterValue = matcher.group(parameterName);
					} catch (IllegalArgumentException e) {
						return false;
					}

					parameterValues[count] = parameterValue;
				}

				count++;
			}

			try {
				method.invoke(instance, parameterValues);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
			return true;
		}

		return false;
	}
}
