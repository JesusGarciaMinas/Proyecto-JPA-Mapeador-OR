package uo.ri.amp.service.cash;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.amp.service.util.Fixture;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.CashService;
import uo.ri.business.dto.CardDto;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.TarjetaCredito;
import uo.ri.util.exception.BusinessException;

public class AddCardPaymentMeanTests extends BaseServiceTests {

	private Cliente c;
	
	@Before
	public void setUp() throws Exception {		
		c = FixtureRepository.registerNewClient();
	}

	/**
	 * Se añade una tarjeta no registrada previamente a un cliente
	 * @throws BusinessException 
	 */
	@Test
	public void testAddNewCard() throws BusinessException {
		CardDto card = Fixture.newCardDto( c.getId() );
		
		CashService svc = Factory.service.forCash();
		svc.addCardPaymentMean(card);
		
		TarjetaCredito expected = FixtureRepository.findCardByNumber( card.cardNumber );
		
		assertTrue( expected.getCliente().getId().equals( c.getId() ) );
		assertTrue( expected.getCargos().size() == 0);
		assertTrue( expected.getAcumulado() == 0.0 );
		assertTrue( expected.getNumero().equals( card.cardNumber ) );
		assertTrue( expected.getTipo().equals( card.cardType ) );
		assertTrue( expected.getValidez().equals( card.cardExpiration ) );
	}
	
	/**
	 * El valor de acumulado se ignora al añadir nueva tarjeta
	 */
	@Test
	public void testAddNewCardWithAccumulated() throws BusinessException {
		CardDto card = Fixture.newCardDto( c.getId() );
		card.accumulated = 1000.0; /* € */
		
		CashService svc = Factory.service.forCash();
		svc.addCardPaymentMean(card);
		
		TarjetaCredito expected = FixtureRepository.findCardByNumber( card.cardNumber );
		
		assertTrue( expected.getAcumulado() == 0.0 );
	}
	
	/**
	 * No se puede añadir una tarjeta a un cliente que no existe
	 */
	@Test(expected = BusinessException.class) 
	public void testAddNewCardNonExisitngClient() throws BusinessException {
		CardDto card = Fixture.newCardDto( -1000L /*does not exist*/ );
		
		CashService svc = Factory.service.forCash();
		svc.addCardPaymentMean(card);  // must raise exception
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
		
		CardDto cardWithRepeatedNumber = Fixture.newCardDto( c.getId() );
		cardWithRepeatedNumber.cardNumber = card.cardNumber;
		svc.addCardPaymentMean( cardWithRepeatedNumber );   // must raise exception
	}

	/**
	 * No se puede añadir una tarjeta cuyo número ya existe en el sistema 
	 * incluso si es de otro cliente 
	 */
	@Test(expected = BusinessException.class) 
	public void testAddNewCardRepeatedNumberOtherClient() throws BusinessException {
		Cliente otherClient = FixtureRepository.registerNewClient();
		CardDto card = Fixture.newCardDto( c.getId() );
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
