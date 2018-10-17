package uo.ri.amp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	uo.ri.amp.domain.AllTests.class,
	
	uo.ri.amp.service.admin.GenerateVouchersByNumberOfBreakdowns.class,
	uo.ri.amp.service.foreman.AllTests.class
})

public class TestsForUoMod_1 {

}