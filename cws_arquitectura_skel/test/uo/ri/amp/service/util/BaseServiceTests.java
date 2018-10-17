package uo.ri.amp.service.util;

import static org.junit.Assert.assertTrue;

import java.util.List;

import static uo.ri.amp.service.util.FixtureRepository.findVouchersByClientId;

import uo.ri.amp.util.repository.inmemory.InMemoryCommandExecutorFactory;
import uo.ri.amp.util.repository.inmemory.InMemoryRepositoryFactory;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.BusinessFactory;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.model.MedioPago;
import uo.ri.util.exception.BusinessException;

public abstract class BaseServiceTests {

	/**
	 * Does the dependency injection for all Business layer tests
	 */
	public BaseServiceTests() {
		Factory.service = new BusinessFactory();
		Factory.executor = new InMemoryCommandExecutorFactory();
		Factory.repository = new InMemoryRepositoryFactory();
	}

	/**
	 * Asserts whether the exception message is the expected
	 * @param be
	 * @param msg
	 */
	protected void assertMsg(BusinessException be, String msg) {
		assertTrue( be.getMessage().equals( msg ) );
	}

	protected void assertSameData(ClientDto dto, Cliente entity) {
		assertTrue( entity.getAddress().getStreet().equals( dto.addressStreet) );
		assertTrue( entity.getAddress().getCity().equals( dto.addressCity) );
		assertTrue( entity.getAddress().getZipCode().equals( dto.addressZipcode) );
		assertTrue( entity.getEmail().equals( dto.email ));
		assertTrue( entity.getPhone().equals( dto.phone ));
		assertTrue( entity.getNombre().equals( dto.name ));
		assertTrue( entity.getApellidos().equals( dto.surname ));
	}

	protected MedioPago getMetalico(Cliente c) {
		return c.getMediosPago().stream()
			.findFirst()
			.orElse( null );
	}

	protected Bono getFirstVoucher(Cliente c) {
		List<Bono> bns = findVouchersByClientId( c.getId() );
		assertTrue( bns.size() == 1 );
		Bono expected = bns.get( 0 );
		return expected;
	}

}
