package uo.ri.persistence.jpa;

import uo.ri.business.repository.MecanicoRepository;
import uo.ri.model.Mecanico;
import uo.ri.persistence.jpa.util.BaseRepository;
import uo.ri.persistence.jpa.util.Jpa;

public class MechanicJpaRepository extends BaseRepository<Mecanico> implements MecanicoRepository {

	@Override
	public Mecanico findByDni(String dni) {
		return Jpa.getManager().createNamedQuery("Mecanico.findBydni", Mecanico.class).setParameter(1, dni)
				.getResultList().stream().findFirst().orElse(null);
	}
}