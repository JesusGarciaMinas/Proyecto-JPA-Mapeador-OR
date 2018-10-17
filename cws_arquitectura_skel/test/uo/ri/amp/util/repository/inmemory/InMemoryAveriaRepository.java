package uo.ri.amp.util.repository.inmemory;

import java.util.List;

import uo.ri.business.repository.AveriaRepository;
import uo.ri.model.Averia;

public class InMemoryAveriaRepository 
	extends BaseMemoryRepository<Averia> 
	implements AveriaRepository {

	@Override
	public List<Averia> findByIds(List<Long> idsAveria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Averia> findNoFacturadasByDni(String dni) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Averia> findWithUnusedBono3ByClienteId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
