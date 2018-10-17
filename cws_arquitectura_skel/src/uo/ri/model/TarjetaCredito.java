package uo.ri.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import alb.util.date.DateUtil;
import uo.ri.util.exception.BusinessException;

@Entity
@Table(name = "TTarjetasCredito")
@DiscriminatorValue("TTarjetasCredito")
public class TarjetaCredito extends MedioPago {

	@Column(unique = true)
	protected String numero;
	
	@Temporal(TemporalType.TIMESTAMP)
	protected Date validez;
	
	protected String tipo;

	TarjetaCredito() {
	}

	public TarjetaCredito(String numero) {
		this.numero = numero;
		validez = DateUtil.tomorrow();
		tipo = "UNKNOWN";
	}

	public TarjetaCredito(String numero, String tipo, Date validez) {
		this(numero);
		this.tipo = tipo;
		this.validez = validez;
	}

	@Override
	public String toString() {
		return "TarjetaCredito [numero=" + numero + ", tipo=" + tipo + ", validez=" + validez + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TarjetaCredito other = (TarjetaCredito) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	public Date getValidez() {
		return validez;
	}

	Date _getValidez() {
		return validez;
	}

	public String getTipo() {
		return tipo;
	}

	String _getTipo() {
		return tipo;
	}

	public double getAcumulado() {
		return acumulado;
	}

	double _getAcumulado() {
		return acumulado;
	}

	public String getNumero() {
		return numero;
	}

	String _getNumero() {
		return numero;
	}

	public boolean isValidNow() {
		if (new Date().getTime() > validez.getTime())
			return false;
		return true;
	}

	public void setValidez(Date validez) {
		this.validez = validez;
	}

	void _setValidez(Date validez) {
		this.validez = validez;
	}

	public void pagar(int acumulado) throws BusinessException {
		if (isValidNow())
			this.acumulado += acumulado;
		else
			throw new BusinessException("La tarjeta de credito expiró");
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}