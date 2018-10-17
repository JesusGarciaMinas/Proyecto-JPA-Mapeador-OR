package uo.ri.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import alb.util.date.DateUtil;
import alb.util.math.Round;
import uo.ri.model.types.AveriaStatus;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;

@Entity
@Table(name = "TFacturas")
public class Factura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private Long numero;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Enumerated(EnumType.STRING)
	private FacturaStatus status = FacturaStatus.SIN_ABONAR;

	@OneToMany(mappedBy = "factura")
	private Set<Averia> averias = new HashSet<Averia>();
	@OneToMany(mappedBy = "factura")
	private Set<Cargo> cargos = new HashSet<Cargo>();

	private double importe;
	private double iva;
	private boolean usada;

	Factura() {
	}

	public Factura(long numero) {
		this.numero = numero;
		this.fecha = DateUtil.now();
	}

	public Factura(long numero, Date today) {
		this(numero);
		fecha = today;
	}

	public Factura(long numero, List<Averia> averias) throws BusinessException {
		this(numero);
		for (Averia a : averias) {
			addAveria(a);
		}
	}

	public Factura(long l, Date f, List<Averia> averias) throws BusinessException {
		this(l, averias);
		fecha = f;
	}

	@Override
	public String toString() {
		return "Factura [numero=" + numero + ", fecha=" + fecha + ", importe=" + importe + ", iva=" + iva + ", status="
				+ status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
		Factura other = (Factura) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	/**
	 * Añade la averia a la factura
	 * 
	 * @param averia
	 * @throws BusinessException
	 */
	public void addAveria(Averia averia) throws BusinessException {
		if (this.status != FacturaStatus.SIN_ABONAR) {
			throw new BusinessException("La avería no está sin abonar");
		} else if (averia._getStatus() != AveriaStatus.TERMINADA) {
			throw new BusinessException("La avería no está terminada");
		} else {
			Association.Facturar.link(this, averia);
			averia.markAsInvoiced();
		}
	}

	/**
	 * Calcula el importe de la avería y su IVA, teniendo en cuenta la fecha de
	 * factura
	 */
	void calcularImporte() {
		this.importe = 0.0;
		if (DateUtil.isAfter(fecha, DateUtil.fromString("1/7/2012"))) {
			iva = 21.0;
		} else {
			iva = 18.0;
		}
		for (Averia averia : averias) {
			this.importe += (averia.getImporte() * (1 + this.iva / 100));
		}
		importe = Round.twoCents(importe);
	}

	/**
	 * Elimina una averia de la factura, solo si está SIN_ABONAR y recalcula el
	 * importe
	 * 
	 * @param averia
	 * @throws BusinessException
	 */
	public void removeAveria(Averia averia) throws BusinessException {
		if (status == FacturaStatus.ABONADA) {
			throw new BusinessException("La factura ya está abonada");
		}
		Association.Facturar.unlink(this, averia);
		averia.markBackToFinished();
		calcularImporte();
	}

	public Set<Averia> getAverias() {
		return new HashSet<Averia>(averias);
	}

	Set<Averia> _getAverias() {
		return averias;
	}

	public double getImporte() {
		calcularImporte();
		return importe;
	}

	double _getImporte() {
		calcularImporte();
		return importe;
	}

	Set<Cargo> _getCargos() {
		return cargos;
	}

	public Set<Cargo> getCargos() {
		return new HashSet<Cargo>(cargos);
	}

	public FacturaStatus getStatus() {
		return status;
	}

	FacturaStatus _getStatus() {
		return status;
	}

	/**
	 * Método creado para PersistenceTest
	 * @param fecha, el atributo fecha a modificar
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Long getId() {
		return id;
	}

	public long getNumero() {
		return numero;
	}

	public Date getFecha() {
		return fecha;
	}

	public double getIva() {
		return iva;
	}

	/**
	 * Una factura pasa a abonada si tiene al menos 1 avería y si el importe de
	 * todos los cargos tiene como límite 1 céntimo por arriba o por abajo como
	 * máximo
	 * 
	 * @throws BusinessException
	 */
	public void settle() throws BusinessException {
		if (averias.size() == 0) {
			throw new BusinessException("No se puede liquidar una factura sin averías");
		} else if (getImporteCargos() - this.importe > 0.01 || getImporteCargos() - this.importe < -0.01) {
			throw new BusinessException(
					"No se puede liquidar una factura con un pago distinto al importe de los cargos");
		} else {
			status = FacturaStatus.ABONADA;
		}
	}

	/**
	 * Se suma el importe de todos los cargos
	 * 
	 * @return el importe total de todos los cargos
	 */
	private double getImporteCargos() {
		double i = 0.0;
		for (Cargo c : cargos) {
			i += c._getImporte();
		}
		return i;
	}

	public boolean isSettled() {
		return status == FacturaStatus.ABONADA;
	}

	/**
	 * Se puede generar un bono si la factura está abonada y su importe supera los
	 * 500 euros y todavía no había sido utilizada para generar un bono
	 * 
	 * @return si es posible generar un bono o no
	 */
	public boolean puedeGenerarBono500() {
		if (isSettled() && this.importe > 500 && !usada) {
			return true;
		}
		return false;
	}

	public void markAsBono500Used() throws BusinessException {
		if (puedeGenerarBono500()) {
			usada = true;
		} else {
			throw new BusinessException("No se puede marcar como usada una factura no válida para generar un bono");
		}
	}

	public boolean isBono500Used() {
		return usada;
	}
}