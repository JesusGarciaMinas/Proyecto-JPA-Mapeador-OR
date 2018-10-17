package uo.ri.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;

@Entity
@Table(name = "TCargos")
public class Cargo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Factura factura;
	@ManyToOne
	private MedioPago medioPago;

	private double importe = 0.0;

	Cargo() {
	}

	public Cargo(Factura factura, MedioPago medioPago, double importe) throws BusinessException {
		this.importe = importe;
		if (medioPago instanceof TarjetaCredito) {
			if (!((TarjetaCredito) medioPago).isValidNow()) {
				throw new BusinessException("Esta tarjeta de crédito está caducada");
			}
		}
		medioPago.pagar(importe);
		Association.Cargar.link(factura, this, medioPago);
	}

	/**
	 * Anula (retrocede) este cargo de la factura y el medio de pago Solo se puede
	 * hacer si la factura no está abonada. Decrementar el acumulado del medio de
	 * pago Desenlazar el cargo de la factura y el medio de pago
	 * 
	 * @throws BusinessException
	 */
	public void rewind() throws BusinessException {
		if (factura._getStatus() != FacturaStatus.ABONADA) {
			medioPago._setAcumulado(medioPago._getAcumulado() - importe);
			Association.Cargar.unlink(this);
		} else {
			throw new BusinessException("La factura está abonada");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((factura == null) ? 0 : factura.hashCode());
		result = prime * result + ((medioPago == null) ? 0 : medioPago.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Cargo [factura=" + factura + ", medioPago=" + medioPago + ", importe=" + importe + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cargo other = (Cargo) obj;
		if (factura == null) {
			if (other.factura != null)
				return false;
		} else if (!factura.equals(other.factura))
			return false;
		if (medioPago == null) {
			if (other.medioPago != null)
				return false;
		} else if (!medioPago.equals(other.medioPago))
			return false;
		return true;
	}

	public Factura getFactura() {
		return factura;
	}

	Factura _getFactura() {
		return factura;
	}

	public MedioPago getMedioPago() {
		return medioPago;
	}

	MedioPago _getMedioPago() {
		return medioPago;
	}

	void _setFactura(Factura factura) {
		this.factura = factura;
	}

	void _setMedioPago(MedioPago medioPago) {
		this.medioPago = medioPago;
	}

	public double getImporte() {
		return importe;
	}

	double _getImporte() {
		return importe;
	}
}