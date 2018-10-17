package uo.ri.amp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	uo.ri.amp.domain.AllTests.class,
	
	uo.ri.amp.service.admin.GenerateVouchersByNumberOfBreakdowns.class,
	uo.ri.amp.service.admin.GenerateVouchersByInvoiceOver500Tests.class,
	uo.ri.amp.service.admin.GenerateVoucherSummaryTests.class,
	
	uo.ri.amp.service.cash.FindInvoiceByNumberTests.class,
	uo.ri.amp.service.cash.SettleInvoiceTests.class,
})

public class TestsForUoMod_2 {

}
