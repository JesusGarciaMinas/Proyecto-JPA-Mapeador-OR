package uo.ri.amp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	uo.ri.amp.domain.AllTests.class,
	
	uo.ri.amp.service.admin.GenerateVouchersByNumberOfRecommendations.class,
	uo.ri.amp.service.admin.GenerateVoucherSummaryTests.class,
	
	uo.ri.amp.service.cash.AddCardPaymentMeanTests.class,
	uo.ri.amp.service.cash.AddVoucherPaymentMeanTests.class,
	uo.ri.amp.service.cash.DeletePaymentMeanTests.class,
})

public class TestsForUoMod_0 {

}
