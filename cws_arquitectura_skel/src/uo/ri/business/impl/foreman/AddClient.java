package uo.ri.business.impl.foreman;

import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.Metalico;
import uo.ri.model.Recomendacion;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

public class AddClient implements Command<Void> {

	private ClientDto dto;
	private Long recomendation;
	private ClienteRepository r = Factory.repository.forCliente();

	public AddClient(ClientDto cliente, Long recomendation) {
		this.dto = cliente;
		this.recomendation = recomendation;
	}

	/**
	 * Se comprueba el dni, el id del recomendador. Si todo sale bien, se crea un
	 * cliente junto su medio de pago en metálico
	 */
	public Void execute() throws BusinessException {
		MedioPagoRepository mpr = Factory.repository.forMedioPago();
		assertNotRepeatedDni(dto.dni);
		assertExistId(recomendation);
		Cliente c = DtoAssembler.toEntity(dto);
		añadirRecomendacion(recomendation, c);
		Metalico m = new Metalico(c);
		c.getMediosPago().add(m);
		r.add(c);
		mpr.add(m);
		return null;
	}

	/**
	 * Si el cliente tiene recomendador se creará una nueva recomendación entre el
	 * nuevo cliente y el recomendador
	 * 
	 * @param id, el id del recomendador
	 * @param recomendado, el nuevo cliente creado
	 */
	private void añadirRecomendacion(Long id, Cliente recomendado) {
		if (id != null) {
			Cliente recomendador = r.findById(id);
			new Recomendacion(recomendador, recomendado);
		}
	}

	/**
	 * Se comprueba si el dni ya existe en la base de datos
	 * 
	 * @param dni, el dni a comprobar su existencia
	 * @throws BusinessException
	 */
	private void assertNotRepeatedDni(String dni) throws BusinessException {
		Cliente c = r.findByDni(dni);
		Check.isNull(c, "Ya existe un cliente con ese dni");
	}

	/**
	 * Se comprueba si el recomendador existe en la base de datos
	 * 
	 * @param id, el id del recomendador a comprobar su existencia
	 * @throws BusinessException
	 */
	private void assertExistId(Long id) throws BusinessException {
		if (id != null) {
			Cliente c = r.findById(id);
			Check.isNotNull(c, "No existe el cliente recomendador");
		}
	}
}