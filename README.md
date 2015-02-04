# switchblade-jetty
An auto-configured library for switchblade-core providing an embedded Jetty instance.

- `mvn install`
- create a project, import dependency. Ensure it is being compiled in Java 8 using the '-parameters' compiler flag.
- Create an application, start it.
- Include classes in the same package (or subpackage) as the application class, that is annotated with `@Endpoint` and has methods annotated with `@Endpoint`.
- `curl http://localhost:8080/{endpoint-path}`
- Example endpoint:

  ```java
  @Endpoint(path="/root")
  public class TestEndpoint {
	  @Endpoint(path="/:id")
	  public String id(String id) {
  		return "found " + id;
	  }
	}
	```
	
	```bash
	curl http://localhost:8080/root/10
	```
	
TODO
----

 - Currently path variables can only be strings. `ServletRequest` and `ServletResponse` can also be method parameters. Need to support primitives by default and allow applications to add their own handlers for various types.
 - Headers are not set, content types cannot be specified, request methods cannot be specified, etc.
 - Should support writing any response object as json. Allow the application to specify response handlers
