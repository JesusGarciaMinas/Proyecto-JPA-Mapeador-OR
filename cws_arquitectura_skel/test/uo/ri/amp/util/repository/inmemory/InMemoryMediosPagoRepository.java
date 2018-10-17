package uo.ri.amp.util.repository.inmemory;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.model.Bono;
import uo.ri.model.MedioPago;
import uo.ri.model.TarjetaCredito;

public class InMemoryMediosPagoRepository 
		extends BaseMemoryRepository<MedioPago> 
		implements MedioPagoRepository {

	@Override
	public List<MedioPago> findPaymentMeansByClientId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MedioPago> findPaymentMeansByInvoiceId(Long idFactura) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MedioPago> findByClientId(Long id) {
		return entities.values().stream()
				.filter(m -> m.getCliente().getId().equals( id ))
				.collect( Collectors.toList() );
	}

	@Override
	public Object[] findAggregateVoucherDataByClientId(Long id) {
		List<Bono> bs = findVouchersByClientId(id);
		
		double available = bs.stream()
				.map(b -> b.getDisponible())
				.collect(Collectors.summingDouble( a -> a));
		
		double consumed = bs.stream()
				.map(b -> b.getAcumulado())
				.collect(Collectors.summingDouble( a -> a));
		
		Object[] res = new Object[]{
			bs.size(),
			available,
			consumed
		};
		return res;
	}

	@Override
	public TarjetaCredito findCreditCardByNumber(String pan) {
		return entities.values().stream()
				.filter( m -> m instanceof TarjetaCredito)
				.map( m -> (TarjetaCredito) m)
				.filter(tc -> tc.getNumero().equals( pan ))
				.findFirst()
				.orElse( null );
	}

	@Override
	public List<Bono> findVouchersByClientId(Long id) {
		return entities.values().stream()
				.filter( m -> m instanceof Bono)
				.map( m -> (Bono) m)
				.filter(b -> b.getCliente().getId().equals( id ))
				.collect( Collectors.toList() );
	}

	@Override
	public Bono findVoucherByCode(String code) {
		return entities.values().stream()
				.filter( m -> m instanceof Bono)
				.map( m -> (Bono) m)
				.filter(b -> b.getCodigo().equals( code ))
				.findFirst()
				.orElse( null );
	}

}
