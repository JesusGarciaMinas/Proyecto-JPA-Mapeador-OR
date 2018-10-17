package uo.ri.business.impl.admin;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.MecanicoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Mecanico;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

public class AddMechanic implements Command<Void> {

	private MechanicDto dto;
	MecanicoRepository r = Factory.repository.forMechanic();

	public AddMechanic(MechanicDto mecanico) {
		this.dto = mecanico;
	}

	/**
	 * Método execute que comprueba si el dni no existe en la base de datos. En caso
	 * negativo se añade a esta
	 */
	public Void execute() throws BusinessException {
		assertNotRepeatedDni(dto.dni);
		Mecanico m = DtoAssembler.toEntity(dto);
		r.add(m);
		return null;
	}


	private void assertNotRepeatedDni(String dni) throws BusinessException {
		Mecanico m = r.findByDni(dni);
		Check.isNull(m, "El dni está repetido");
	}
}