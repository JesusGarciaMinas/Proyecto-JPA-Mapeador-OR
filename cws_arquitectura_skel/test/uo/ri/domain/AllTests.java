package uo.ri.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	AveriaTest.class, 
	FacturaTest.class, 
	IntervencionTest.class,
	SustitucionTest.class 
})
public class AllTests {

}
