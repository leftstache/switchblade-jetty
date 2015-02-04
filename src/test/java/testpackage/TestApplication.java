package testpackage;

import com.leftstache.switchblade.core.*;
import org.junit.*;

import java.io.*;

/**
 * @author Joel Johnson
 */
public class TestApplication {
	static SwitchbladeApplication<TestApplication> application;
	@Test
	public void test() throws IOException {
		SwitchbladeApplication<TestApplication> switchbladeApplication = SwitchbladeApplication.create(TestApplication.class);
		application = switchbladeApplication;
		switchbladeApplication.start();
	}
}
