package uo.ri.amp.util.repository.inmemory;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.business.repository.ClienteRepository;
import uo.ri.model.Cliente;

public class InMemoryClienteRepository 
		extends BaseMemoryRepository<Cliente> 
		implements ClienteRepository {

	@Override
	public Cliente findByDni(String dni) {
		return entities.values().stream()
				.filter( c -> c.getDni().equals( dni ))
				.findFirst()
				.orElse( null );
	}

	@Override
	public List<Cliente> findWithRecomendations() {
		return entities.values().stream()
				.filter( c -> c.getRecomendacionesHechas().size() >= 3)
				.flatMap( c -> c.getRecomendacionesHechas().stream() )
				.filter(r -> ! r.isUsada())
				.map(r -> r.getRecomendador())
				.distinct()
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cliente> findWithThreeUnusedBreakdowns() {
		return entities.values().stream()
				.flatMap(c -> c.getVehiculos().stream())
				.flatMap(v -> v.getAverias().stream())
				.filter( a -> a.isInvoiced() && ! a.isUsadaBono3() )
				.map(a -> a.getVehiculo().getCliente())
				.distinct()
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cliente> findRecomendedBy(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
