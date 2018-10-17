package uo.ri.amp.service.cash;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddCardPaymentMeanTests.class, AddVoucherPaymentMeanTests.class, DeletePaymentMeanTests.class,
		FindInvoiceByNumberTests.class, SettleInvoiceTests.class })

public class AllTests {

}
