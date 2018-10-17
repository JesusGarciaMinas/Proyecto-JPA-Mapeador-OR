package uo.ri.amp.service.admin;

import static org.junit.Assert.assertTrue;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClient;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClientRecommendedBy;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClientWithBreakDown;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClientWithBreakdownRecommendedBy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.AdminService;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.model.Recomendacion;
import uo.ri.util.exception.BusinessException;

public class GenerateVouchersByNumberOfRecommendations extends BaseServiceTests {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Un cliente con averias, que hace tres recomendaciones, no usadas, a tres 
	 * clientes que tienen averias registradas tiene derecho a un bono 
	 * por recomendaciones y las tres recomendaciones quedan marcadas commo 
	 * usadas para bono3Recomendaciones
	 * @throws BusinessException 
	 */
	@Test
	public void testOneVoucherGenerated() throws BusinessException {
		Cliente c = registerNewClientWithBreakDown();
		Recomendacion r1 = registerNewClientWithBreakdownRecommendedBy( c );
		Recomendacion r2 = registerNewClientWithBreakdownRecommendedBy( c );
		Recomendacion r3 = registerNewClientWithBreakdownRecommendedBy( c );
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 1 );
		assertTrue( r1.isUsada() );
		assertTrue( r2.isUsada() );
		assertTrue( r3.isUsada() );
		
		Bono expected = getFirstVoucher( c );
		assertRightVoucher(c, expected);
	}
	
	/**
	 * A un cliente con 2 recomendaciones no se le generará bono
	 */
	@Test
	public void testWithTwoRecommends() throws BusinessException {
		Cliente c = registerNewClientWithBreakDown();
		Recomendacion r1 = registerNewClientWithBreakdownRecommendedBy( c );
		Recomendacion r2 = registerNewClientWithBreakdownRecommendedBy( c );
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0 );
		assertTrue( ! r1.isUsada() );
		assertTrue( ! r2.isUsada() );
	}
		
	/**
	 * Un cliente que no ha hecho reparaciones pero que ha recomendado a otros 
	 * tres que sí han hecho reparaciones, no recibirá bono 
	 */
	@Test
	public void testWithoutBreakdown() throws BusinessException {
		Cliente c = registerNewClient(); // <-- sin reparacion
		Recomendacion r1 = registerNewClientWithBreakdownRecommendedBy( c );
		Recomendacion r2 = registerNewClientWithBreakdownRecommendedBy( c );
		Recomendacion r3 = registerNewClientWithBreakdownRecommendedBy( c );
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0 );
		assertTrue( ! r1.isUsada() );
		assertTrue( ! r2.isUsada() );
		assertTrue( ! r3.isUsada() );
	}
			
	/**
	 * Un cliente que ha hecho reparaciones, que ha recomendado a otros 
	 * tres que sí han hecho reparaciones, pero con una recomnedación ya
	 * usada para generar otro bono no recibirá bono 
	 */
	@Test
	public void testWithRecommendationUsed() throws BusinessException {
		Cliente c = registerNewClientWithBreakDown();
		Recomendacion r1 = registerNewClientWithBreakdownRecommendedBy( c );
		Recomendacion r2 = registerNewClientWithBreakdownRecommendedBy( c );
		Recomendacion r3 = registerNewClientWithBreakdownRecommendedBy( c );
		
		r3.markAsUsadaBono();
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0 );
		assertTrue( ! r1.isUsada() );
		assertTrue( ! r2.isUsada() );
		assertTrue( r3.isUsada() );
	}
			
	/**
	 * Un cliente con tres recomendaciones hechas a sendos otros clientes, 
	 * pero uno de ellos aún no ha reparado en el taller hace que el recomendador
	 * no tenga derecho a bono
	 */
	@Test
	public void testRecommendedWithoutBreakdown() throws BusinessException {
		Cliente c = registerNewClientWithBreakDown();
		Recomendacion r1 = registerNewClientWithBreakdownRecommendedBy( c );
		Recomendacion r2 = registerNewClientWithBreakdownRecommendedBy( c );
		Recomendacion r3 = registerNewClientRecommendedBy( c ); // <-- sin
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0 );
		assertTrue( ! r1.isUsada() );
		assertTrue( ! r2.isUsada() );
		assertTrue( ! r3.isUsada() );
	}
				
	/**
	 * Un cliente con (n*3)+1 recomendaciones elegibles para bono3 recibirá
	 * n bonos y tendrá n*3 recomendaciones marcadas
	 */
	@Test
	public void testGeneralizedVoucherGeneration() throws BusinessException {
		int N = 10;
		List<Recomendacion> rs = generateRecommnedations( N * 3 + 1 );
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == N );
		assertAreMarkedAsUsed( rs, N * 3 );
	}
		
	/**
	 * De tres clientes, con tres recomendaciones cada uno, pero uno de ellos
	 * con una recomendación que no ha hecho reparación, recibirán bono3R solo 
	 * dos ellos  
	 */
	@Test
	public void testThreeClientVoucherGeneration() throws BusinessException {
		List<Recomendacion> rs1 = generateRecommnedations( 3 );
		List<Recomendacion> rs2 = generateRecommnedations( 3 );
		List<Recomendacion> rs3 = generateRecommnedations( 3 );
		
		rs3.get(0).markAsUsadaBono();  // <-- tercer cliente no generará bono
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 2 );
		assertAreMarkedAsUsed( rs1, 3 );
		assertAreMarkedAsUsed( rs2, 3 );
		assertAreMarkedAsUsed( rs3, 1 ); // <-- solo una, la que se marcó
	}	
	
	private void assertRightVoucher(Cliente c, Bono expected) {
		assertTrue( expected.getAcumulado() == 0.0 );
		assertTrue( expected.getCargos().size() == 0.0 );
		assertTrue( expected.getCliente().equals( c ) );
		assertTrue( expected.getDescripcion().equals("Por recomendación") );
		assertTrue( expected.getDisponible() == 25.0 /*€*/ );
	}	

	private void assertAreMarkedAsUsed(List<Recomendacion> rs, int counter) {
		assertTrue( 
			rs.stream()
				.filter( r -> r.isUsada() )
				.count() == counter
		);
	}

	private List<Recomendacion> generateRecommnedations(int counter) {
		Cliente c = registerNewClientWithBreakDown();
		List<Recomendacion> res = new ArrayList<>();
		for(int i = 0; i < counter; i++) {
			res.add( registerNewClientWithBreakdownRecommendedBy( c ) );
		}
		return res;
	}

}
