package uo.ri.business.impl.admin;

import java.util.ArrayList;
import java.util.List;

import uo.ri.business.impl.Command;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

public class GenerateVouchers implements Command<Integer> {

	private List<Cliente> candidatos;

	/**
	 * Método que a partir de los clientes candidatos a conseguir bonos, se añaden
	 * los bonos al cliente y las averías pasar a ser utilizadas. Finalmente,
	 * devuelven el número de bonos dados
	 */
	@Override
	public Integer execute() throws BusinessException {
		int bonos = 0;
		int marcarAverias = 0;
		ClienteRepository r = Factory.repository.forCliente();
		MedioPagoRepository mpr = Factory.repository.forMedioPago();
		List<Cliente> lista = r.findAll();
		clientesCandidatos(lista);
		for (Cliente c : candidatos) {
			for (int i = 0; i < c.getAveriasBono3NoUsadas().size() / 3; i++) {
				Bono b = c.generaBono();
				mpr.add(b);
				bonos++;
			}
			marcarAverias = (c.getAveriasBono3NoUsadas().size() / 3) * 3;
			c.marcarAverias(marcarAverias);
		}
		return bonos;
	}

	/**
	 * Devuelve los clientes que tienen suficientes averías para darle bonos por
	 * avería
	 * 
	 * @param lista, la lista inicial con todos los clientes
	 * @return candidatos, los clientes que recibirán mínimo un bono
	 */
	private List<Cliente> clientesCandidatos(List<Cliente> lista) {
		candidatos = new ArrayList<Cliente>();
		for (Cliente l : lista) {
			if (l.averiasParaBono3() > 2) {
				candidatos.add(l);
			}
		}
		return candidatos;
	}
}