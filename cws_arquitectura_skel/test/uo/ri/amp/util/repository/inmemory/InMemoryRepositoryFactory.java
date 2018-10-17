package uo.ri.amp.util.repository.inmemory;

import uo.ri.business.repository.AveriaRepository;
import uo.ri.business.repository.CargoRepository;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.FacturaRepository;
import uo.ri.business.repository.MecanicoRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.business.repository.RecomendacionRepository;
import uo.ri.business.repository.RepositoryFactory;
import uo.ri.business.repository.RepuestoRepository;
import uo.ri.business.repository.VehiculoRepository;

public class InMemoryRepositoryFactory implements RepositoryFactory {

	private ClienteRepository clientes = new InMemoryClienteRepository();
	private MedioPagoRepository mediosPago = new InMemoryMediosPagoRepository();
	private FacturaRepository facturas = new InMemoryFacturaRepository();
	private CargoRepository cargos = new InMemoryCargoRepository();
	private RecomendacionRepository recomendaciones = new InMemoryRecomendacionRepository();
	private VehiculoRepository vehiculos = new InMemoryVehiculoRepository();
	private AveriaRepository averias = new InMemoryAveriaRepository();

	@Override
	public MecanicoRepository forMechanic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AveriaRepository forAveria() {
		return averias;
	}

	@Override
	public MedioPagoRepository forMedioPago() {
		return mediosPago;
	}

	@Override
	public FacturaRepository forFactura() {
		return facturas;
	}

	@Override
	public ClienteRepository forCliente() {
		return clientes;
	}

	@Override
	public RepuestoRepository forRepuesto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecomendacionRepository forRecomendacion() {
		return recomendaciones;
	}

	@Override
	public CargoRepository forCargo() {
		return cargos;
	}

	@Override
	public VehiculoRepository forVehiculo() {
		return vehiculos;
	}

}
