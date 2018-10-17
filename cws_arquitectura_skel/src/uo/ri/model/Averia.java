package uo.ri.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import alb.util.date.DateUtil;
import uo.ri.model.types.AveriaStatus;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;

@Entity
@Table(name = "TAverias")
public class Averia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Enumerated(EnumType.STRING)
	private AveriaStatus status = AveriaStatus.ABIERTA;

	@OneToMany(mappedBy = "averia")
	private Set<Intervencion> intervenciones = new HashSet<Intervencion>();

	@ManyToOne
	private Factura factura;
	@ManyToOne
	private Mecanico mecanico;
	@ManyToOne
	private Vehiculo vehiculo;

	private double importe = 0.0;
	private String descripcion;
	private boolean usada;

	Averia() {
	}

	public Averia(Vehiculo vehiculo) {
		fecha = DateUtil.now();
		Association.Averiar.link(vehiculo, this);
	}

	public Averia(Vehiculo vehiculo, String descripcion) {
		this(vehiculo);
		this.descripcion = descripcion;
	}

	/**
	 * Asigna la averia al mecanico
	 * 
	 * @param mecanico
	 * @throws BusinessException
	 */
	public void assignTo(Mecanico mecanico) throws BusinessException {
		if (status != AveriaStatus.ABIERTA) {
			throw new BusinessException("La avería no está abierta");
		}

		Association.Asignar.link(mecanico, this);
		status = AveriaStatus.ASIGNADA;
	}

	/**
	 * El mecánico da por finalizada esta avería, entonces se calcula el importe
	 * 
	 * @throws BusinessException
	 * 
	 */
	public void markAsFinished() throws BusinessException {
		if (status != AveriaStatus.ASIGNADA) {
			throw new BusinessException("La avería no está asignada");
		}
		calcularImporte();
		Association.Asignar.unlink(mecanico, this);
		status = AveriaStatus.TERMINADA;
	}

	private void calcularImporte() {
		this.importe = 0;
		for (Intervencion i : intervenciones) {
			importe += i.getImporte();
		}
	}

	/**
	 * Una averia en estado TERMINADA se puede asignar a otro mecánico (el primero
	 * no ha podido terminar la reparación), pero debe ser pasada a ABIERTA primero
	 * 
	 * @throws BusinessException
	 */
	public void reopen() throws BusinessException {
		if (status == AveriaStatus.TERMINADA) {
			status = AveriaStatus.ABIERTA;
		} else {
			throw new BusinessException("La avería no está terminada");
		}
	}

	/**
	 * Una avería ya facturada se elimina de la factura
	 * 
	 * @throws BusinessException
	 */
	public void markBackToFinished() throws BusinessException {
		if (status != AveriaStatus.FACTURADA) {
			throw new BusinessException("La avería no está facturada");
		}
		status = AveriaStatus.TERMINADA;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	@Override
	public String toString() {
		return "Averia [descripcion= " + descripcion + ", fecha= " + fecha + ", importe= " + importe + ", status= "
				+ status + ", vehiculo= " + vehiculo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((vehiculo == null) ? 0 : vehiculo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Averia other = (Averia) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (vehiculo == null) {
			if (other.vehiculo != null)
				return false;
		} else if (!vehiculo.equals(other.vehiculo))
			return false;
		return true;
	}

	Vehiculo _getVehiculo() {
		return vehiculo;
	}

	void _setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	Set<Intervencion> _getIntervenciones() {
		return intervenciones;
	}

	public Set<Intervencion> getIntervenciones() {
		return new HashSet<Intervencion>(intervenciones);
	}

	public Factura getFactura() {
		return factura;
	}

	Factura _getFactura() {
		return factura;
	}

	public AveriaStatus getStatus() {
		return status;
	}

	AveriaStatus _getStatus() {
		return status;
	}

	void _setMecanico(Mecanico mecanico) {
		this.mecanico = mecanico;
	}

	public void setMecanico(Mecanico mecanico) {
		this.mecanico = mecanico;
	}

	public Mecanico getMecanico() {
		return mecanico;
	}

	Mecanico _getMecanico() {
		return mecanico;
	}

	void _setFactura(Factura factura) {
		this.factura = factura;
	}

	public void SetFactura(Factura factura) {
		this.factura = factura;
	}

	/**
	 * La avería pasa a estar facturada en el caso de que esta esté en una factura
	 * 
	 * @throws BusinessException
	 */
	public void markAsInvoiced() throws BusinessException {
		if (factura == null) {
			throw new BusinessException("No hay una factura asignada");
		} else {
			status = AveriaStatus.FACTURADA;
		}
	}

	public double getImporte() {
		calcularImporte();
		return importe;
	}

	double _getImporte() {
		calcularImporte();
		return importe;
	}

	/**
	 * El mecánico deja de arreglar esta avería y vuelve a estar abierta
	 */
	public void desassign() {
		Association.Asignar.unlink(mecanico, this);
		status = AveriaStatus.ABIERTA;
	}

	public long getId() {
		return id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Date getFecha() {
		return new Date(fecha.getTime());
	}

	/**
	 * Una avería facturada cuya factura esté abonada puede utilizarse para
	 * conseguir un bono. (Si esta todavía no había sido utilizada para conseguirlo)
	 * 
	 * @return si es elegible o no para conseguir un bono
	 */
	public boolean esElegibleParaBono3() {
		if (status != AveriaStatus.FACTURADA)
			return false;
		else if (factura._getStatus() != FacturaStatus.ABONADA) {
			return false;
		}
		return !usada;
	}

	public void markAsBono3Used() {
		usada = true;
	}

	public boolean isUsadaBono3() {
		return usada;
	}

	public boolean isInvoiced() {
		return status == AveriaStatus.FACTURADA;
	}
}