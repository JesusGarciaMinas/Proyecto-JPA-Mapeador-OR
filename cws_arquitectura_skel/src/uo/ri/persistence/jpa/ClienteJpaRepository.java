package uo.ri.persistence.jpa;

import java.util.List;

import uo.ri.business.repository.ClienteRepository;
import uo.ri.model.Cliente;
import uo.ri.persistence.jpa.util.BaseRepository;
import uo.ri.persistence.jpa.util.Jpa;

public class ClienteJpaRepository extends BaseRepository<Cliente> implements ClienteRepository {

	@Override
	public Cliente findByDni(String dni) {
		return Jpa.getManager().createNamedQuery("Cliente.findBydni", Cliente.class).setParameter(1, dni)
				.getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public List<Cliente> findAll() {
		return Jpa.getManager().createNamedQuery("Cliente.findAll", Cliente.class).getResultList();
	}

	@Override
	public List<Cliente> findWithRecomendations() {
		return null;
	}

	@Override
	public List<Cliente> findWithThreeUnusedBreakdowns() {
		return null;
	}

	@Override
	public List<Cliente> findRecomendedBy(Long id) {
		return null;
	}
}