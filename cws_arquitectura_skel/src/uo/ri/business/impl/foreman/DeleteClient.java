package uo.ri.business.impl.foreman;

import uo.ri.business.impl.Command;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.MedioPago;
import uo.ri.model.Recomendacion;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

public class DeleteClient implements Command<Void> {

	private Long idCliente;

	public DeleteClient(Long idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * Se comprueba si el cliente existe, en caso positivo se borran sus
	 * recomendaciones y el cliente
	 */
	public Void execute() throws BusinessException {
		ClienteRepository r = Factory.repository.forCliente();
		MedioPagoRepository mpr = Factory.repository.forMedioPago();
		Cliente c = r.findById(idCliente);
		checks(c);
		eliminaRecomendaciones(c);
		for (MedioPago m : c.getMediosPago()) {
			mpr.remove(m);
		}
		r.remove(c);
		return null;
	}

	/**
	 * Se comprueba si el cliente no existe o si ya tiene vehículos en la base de
	 * datos. En esos casos, fallará
	 * 
	 * @param c,
	 *            el cliente a buscar en la base de datos
	 * @throws BusinessException
	 */
	private void checks(Cliente c) throws BusinessException {
		Check.isNotNull(c, "El cliente no existe");
		Check.isTrue(c.getVehiculos().isEmpty(), "El cliente no puede ser eliminado al tener vehículos registrados");
	}

	/**
	 * Si el cliente tenía una recomendación recibida, se borrara esta además de
	 * todas las recomendaciones hechas de este cliente
	 * 
	 * @param c,
	 *            el cliente a eliminar sus recomendaciones
	 */
	private void eliminaRecomendaciones(Cliente c) {
		if (c.getRecomendacionRecibida() != null) {
			c.getRecomendacionRecibida().unlink();
		}

		for (Recomendacion rec : c.getRecomendacionesHechas()) {
			rec.unlink();
		}
	}
}