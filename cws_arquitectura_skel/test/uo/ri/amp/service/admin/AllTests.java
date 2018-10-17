package uo.ri.amp.service.admin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GenerateVouchersByInvoiceOver500Tests.class, GenerateVouchersByNumberOfBreakdowns.class,
		GenerateVouchersByNumberOfRecommendations.class, GenerateVoucherSummaryTests.class })

public class AllTests {

}
