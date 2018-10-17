package uo.ri.amp.service.util;

import java.util.Arrays;
import java.util.List;

import alb.util.date.DateUtil;
import alb.util.random.Random;
import uo.ri.business.repository.RepositoryFactory;
import uo.ri.conf.Factory;
import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Bono;
import uo.ri.model.Cargo;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Intervencion;
import uo.ri.model.Mecanico;
import uo.ri.model.Metalico;
import uo.ri.model.Recomendacion;
import uo.ri.model.TarjetaCredito;
import uo.ri.model.TipoVehiculo;
import uo.ri.model.Vehiculo;
import uo.ri.model.types.Address;
import uo.ri.util.exception.BusinessException;

public class FixtureRepository {

	public static Bono findVoucherByCode(String code) {
		return repository().forMedioPago().findVoucherByCode( code );
	}

	public static TarjetaCredito findCardByNumber(String cardNumber) {
		return repository().forMedioPago().findCreditCardByNumber( cardNumber );
	}

	public static Metalico findCashByClientId(Long id) {
		return repository().forMedioPago().findByClientId(id).stream()
				.filter( mp -> mp instanceof Metalico)
				.map( mp -> (Metalico) mp)
				.findFirst()
				.orElse(  null );
	}

	public static List<Bono> findVouchersByClientId(Long id) {
		return repository().forMedioPago().findVouchersByClientId( id );
	}
	
	public static Cliente findClientByDni(String dni) {
		return repository().forCliente().findByDni(dni);
	}

	public static Cliente registerNewClient() {
		Cliente c = new Cliente(Random.string(4), "n", "a");
		c.setEmail( "email@client" );
		c.setPhone("123-456-789");
		c.setAddress( new Address("street", "city", "zipcode"));
		repository().forCliente().add( c );
		return c;
	}

	public static Cliente registerNewRecommendedClient(Cliente recommender) {
		Cliente c = registerNewClient();
		new Recomendacion(recommender, c);
		return c;
	}

	public static Cliente registerNewClientWithCash() {
		Cliente c = registerNewClient();
		Metalico m = new Metalico( c );
		repository().forMedioPago().add( m );
		return c;
	}

	public static Cliente registerNewClientWithVehicle() {
		Cliente c = registerNewClient();
		registerNewVehicleFor(c);
		return c;
	}

	public static Vehiculo registerNewVehicle() {
		Vehiculo v = new Vehiculo("mat-" + Random.integer(1000, 9999));
		v.setMarca( "seat" );
		v.setModelo( "ibiza" );
		repository().forVehiculo().add( v );
		return v;
	}
	
	public static Vehiculo registerNewVehicleFor(Cliente c) {
		Vehiculo v = registerNewVehicle();
		Association.Poseer.link(c,  v);
		return v;
	}
	
	public static TarjetaCredito registerNewCreditCard() {
		String number = Random.string('0', '9', 16);
		TarjetaCredito tc = new TarjetaCredito(number, "TC", DateUtil.tomorrow());
		repository().forMedioPago().add(tc);
		return tc;
	}

	public static Bono registerNewVoucherWithAvailable(double available) {
		String code = "B-" + Random.string(4);
		Bono b = new Bono(code, available);
		repository().forMedioPago().add( b );
		return b;
	}

	public static TarjetaCredito registerNewCreditCardForClient(Cliente c) {
		TarjetaCredito tc = registerNewCreditCard();
		Association.Pagar.link(c, tc);
		return tc;
	}

	public static Factura registerNewInvoiceWithChargesToCard(TarjetaCredito tc) 
			throws BusinessException {
		
		Long number = Random.longInteger(1000, 10000);
		Factura f = new Factura( number );
		new Cargo(f, tc, f.getImporte());
		return f;
	}

	public static Factura registerNewInvoiceForAmount(double amount) 
			throws BusinessException {
		
		Factura f = createInvoiceForAmount(amount);
		repository().forFactura().add( f );
		return f;
	}

	public static Factura registerNewInvoiceForClientWithAmount(
				Cliente c, double amount) 
				throws BusinessException {
		Factura f = createInvoiceForAmount(amount);
		Vehiculo v = f.getAverias().stream().findFirst().orElse(null).getVehiculo();
		Association.Poseer.link(c,  v);
		repository().forFactura().add( f );
		return f;
	}

	private static Factura createInvoiceForAmount(double amount) 
			throws BusinessException {
		
		double IVA = 0.21; // 21% current IVA, not 18%
		double EUROS_PER_HOUR = 50.0;
		TipoVehiculo tv = new TipoVehiculo("TV", EUROS_PER_HOUR /*€/hour*/);
		Vehiculo v = new Vehiculo("1234-ABC");
		Association.Clasificar.link(tv, v);
		Averia a = newAveria(v);
		Association.Averiar.link(v, a);
		Mecanico m = new Mecanico("123a");
		a.assignTo( m );
		
		amount /= 1 + IVA;
		int minutes = (int)(amount /*€*/ 
				* 60 /* mins/hour */ 
				/ EUROS_PER_HOUR /*€/hour*/);
		new Intervencion(m, a, minutes);
		a.markAsFinished();
		
		Factura f = new Factura( 123L, Arrays.asList( a ) );
		return f;
	}

	private static RepositoryFactory repository() {
		return Factory.repository;
	}

	public static Factura registerNewSettledInvoiceForAmount(Cliente c, double amount) 
			throws BusinessException {
		
		Factura f = createInvoiceForAmount(amount);
		Vehiculo v = f.getAverias().stream().findFirst().orElse( null).getVehiculo();
		Association.Poseer.link(c,  v);

		TarjetaCredito tc = FixtureRepository.registerNewCreditCardForClient(c);
		new Cargo(f, tc, f.getImporte());
		f.settle();
		
		repository().forFactura().add( f );
		return f;
	}

	public static Averia registerNewBreakdownFor(Vehiculo v) {
		Averia a = newAveria(v);
		Association.Averiar.link(v,  a);
		repository().forAveria().add( a );
		return a;
	}

	private static Averia newAveria(Vehiculo v) {
		sleep(5 /* ms */ );
		Averia a = new Averia( v );
		return a;
	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// don't care
		}
	}

	public static Averia registerNewInvoicedBreakdownFor(Vehiculo v) 
			throws BusinessException {
		
		Averia a = newAveria(v);
		Association.Averiar.link(v,  a);
		Mecanico m = new Mecanico("dni");
		a.assignTo( m );
		a.markAsFinished();
		Factura f = new Factura( 123L );
		f.addAveria( a );
		TarjetaCredito tc = registerNewCreditCardForClient( v.getCliente() );
		new Cargo(f, tc, f.getImporte());
		f.settle();
		
		repository().forAveria().add( a );
		return a;
	}

	public static Cliente registerNewClientWithBreakDown() {
		Cliente c = registerNewClient();
		Vehiculo v = registerNewVehicleFor(c);
		registerNewBreakdownFor( v );
		return c;
	}

	public static Recomendacion 
			registerNewClientWithBreakdownRecommendedBy(Cliente recommender) {
		Cliente c = registerNewClientWithBreakDown();
		Recomendacion r = new Recomendacion(recommender, c);
		repository().forRecomendacion().add( r );
		return r;
	}

	public static Recomendacion registerNewClientRecommendedBy(Cliente recommender) {
		Cliente c = registerNewClient();
		Recomendacion r = new Recomendacion(recommender, c);
		repository().forRecomendacion().add( r );
		return r;
	}

}
