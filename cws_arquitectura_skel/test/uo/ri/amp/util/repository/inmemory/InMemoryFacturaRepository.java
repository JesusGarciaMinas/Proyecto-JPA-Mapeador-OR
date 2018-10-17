package uo.ri.amp.util.repository.inmemory;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.business.repository.FacturaRepository;
import uo.ri.model.Factura;

public class InMemoryFacturaRepository 
				extends BaseMemoryRepository<Factura>
				implements FacturaRepository {

	@Override
	public Factura findByNumber(Long numero) {
		return entities.values().stream()
				.filter(f -> f.getNumero() == numero )
				.findFirst()
				.orElse(null);
	}

	@Override
	public Long getNextInvoiceNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Factura> findUnusedWithBono500() {
		return entities.values().stream()
				.filter(f -> f.getImporte() >= 500)
				.filter(f -> ! f.isBono500Used())
				.collect( Collectors.toList());
	}

}
