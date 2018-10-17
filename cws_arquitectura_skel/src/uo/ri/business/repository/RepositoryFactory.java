package uo.ri.business.repository;

public interface RepositoryFactory {

	MecanicoRepository forMechanic();

	AveriaRepository forAveria();

	MedioPagoRepository forMedioPago();

	FacturaRepository forFactura();

	ClienteRepository forCliente();

	RepuestoRepository forRepuesto();

	RecomendacionRepository forRecomendacion();

	CargoRepository forCargo();

	VehiculoRepository forVehiculo();

}