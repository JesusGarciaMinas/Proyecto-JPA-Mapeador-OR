package uo.ri.amp.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	uo.ri.amp.service.cash.AllTests.class,
	uo.ri.amp.service.foreman.AllTests.class,
	uo.ri.amp.service.admin.AllTests.class
})

public class AllTests {

}
