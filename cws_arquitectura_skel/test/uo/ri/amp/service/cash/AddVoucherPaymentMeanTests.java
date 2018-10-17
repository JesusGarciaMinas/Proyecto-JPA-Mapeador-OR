package uo.ri.amp.service.cash;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.amp.service.util.Fixture;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.CashService;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

public class AddVoucherPaymentMeanTests extends BaseServiceTests {

	private Cliente c;
	
	@Before
	public void setUp() throws Exception {
		c = FixtureRepository.registerNewClient();
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Se añade un bono no registrado previamente a un cliente
	 * @throws BusinessException 
	 */
	@Test
	public void testAddNewVoucher() throws BusinessException {
		VoucherDto voucher = Fixture.newVoucherDto( c.getId() );
		
		CashService svc = Factory.service.forCash();
		svc.addVoucherPaymentMean(voucher);
		
		List<Bono> bonos = FixtureRepository.findVouchersByClientId( c.getId() );
		Bono expected = bonos.get(0);
		
		assertTrue( expected.getCliente().getId().equals( c.getId() ) );
		assertTrue( expected.getCargos().size() == 0);
		assertTrue( expected.getAcumulado() == 0.0 );
		assertTrue( expected.getDisponible() == voucher.available );
		assertTrue( expected.getDescripcion().equals( voucher.description ) );
	}

	/**
	 * El valor de acumulado se ignora al añadir nuevo bono
	 */
	@Test
	public void testAddNewVoucherWithAccumulated() throws BusinessException {
		VoucherDto voucher = Fixture.newVoucherDto( c.getId() );
		voucher.accumulated = 1000.0; /* € */
		
		CashService svc = Factory.service.forCash();
		svc.addVoucherPaymentMean(voucher);
		
		List<Bono> bonos = FixtureRepository.findVouchersByClientId( c.getId() );
		Bono expected = bonos.get(0);
		
		assertTrue( expected.getAcumulado() == 0.0 );
	}
	
	/**
	 * No se puede añadir una tarjeta a un cliente que no existe
	 */
	@Test(expected = BusinessException.class) 
	public void testAddNewVoucherToNonExisitngClient() throws BusinessException {
		VoucherDto voucher = Fixture.newVoucherDto( -1000L /*does not exist*/ );
		
		CashService svc = Factory.service.forCash();
		svc.addVoucherPaymentMean(voucher);  // must raise exception
	}
	
	
	/**
	 * No se puede añadir una tarjeta cuyo número ya existe en el sistema
	 */
	@Test(expected = BusinessException.class) 
	public void testAddNewCardRepeatedNumber() throws BusinessException {
		CardDto card = Fixture.newCardDto( c.getId() );
		CashService svc = Factory.service.forCash();
		try {
			svc.addCardPaymentMean(card);
		} catch (BusinessException bex) {
			fail("Not here!");
		}

		CardDto cardWithRepeatedNumber = Fixture.newCardDto(c.getId());
		cardWithRepeatedNumber.cardNumber = card.cardNumber;
		svc.addCardPaymentMean(cardWithRepeatedNumber); // must raise exception
	}

	/**
	 * No se puede añadir una tarjeta cuyo número ya existe en el sistema incluso si
	 * es de otro cliente
	 */
	@Test(expected = BusinessException.class)
	public void testAddNewCardRepeatedNumberOtherClient() throws BusinessException {
		Cliente otherClient = FixtureRepository.registerNewClient();
		CardDto card = Fixture.newCardDto(c.getId());
		CashService svc = Factory.service.forCash();
		try {
			svc.addCardPaymentMean(card);
		} catch (BusinessException bex) {
			fail("Not here!");
		}
		
		CardDto cardWithRepeatedNumber = Fixture.newCardDto( otherClient.getId() );
		cardWithRepeatedNumber.cardNumber = card.cardNumber;
		svc.addCardPaymentMean( cardWithRepeatedNumber );   // must raise exception
	}

}
