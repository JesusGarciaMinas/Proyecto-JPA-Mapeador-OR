package uo.ri.amp.service.foreman;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddClientTests.class, DeleteClientTests.class, FindClientByIdTests.class, UpdateClientTests.class })
public class AllTests {

}
