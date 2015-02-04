package testpackage;

import com.google.inject.*;
import com.leftstache.switchblade.core.*;
import com.leftstache.switchblade.jetty.*;

/**
 * @author Joel Johnson
 */
@Component
@Endpoint(path = "/test")
public class TestEndpoint {
	@Endpoint(path="/id/:id")
	public String id(String id) {
		return "found " + id;
	}

	@Endpoint(path="/barf/:barf")
	public String barf(String barf) {
		TestApplication.application.close();
		return "found " + barf;
	}

	@Endpoint(path="/barf/:derp")
	public String darf(String barf) {
		return barf;
	}
}
