package uo.ri.business.repository;

import java.util.List;

import uo.ri.model.Averia;

public interface AveriaRepository extends Repository<Averia> {

	List<Averia> findByIds(List<Long> idsAveria);

	List<Averia> findNoFacturadasByDni(String dni);

	List<Averia> findWithUnusedBono3ByClienteId(Long id);

}