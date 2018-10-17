package uo.ri.amp.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AveriaTests.class, BonoPor3AveriasTests.class, BonoPor3RecomendacionesTests.class,
		BonoPorFactura500Tests.class, BonoTests.class, CargoTests.class, LiquidarFacturaTests.class,
		MetalicoTests.class, RecomendarTests.class, TarjetaCreditoTests.class })

public class AllTests {

}
