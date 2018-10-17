package uo.ri.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import uo.ri.util.exception.BusinessException;

@Entity
@Table(name = "TBonos")
@DiscriminatorValue("TBonos")
public class Bono extends MedioPago {

	@Column(unique = true)
	protected String codigo;

	protected double disponible = 0.0;
	private String descripcion;

	Bono() {
	}

	public Bono(String codigo) {
		this.codigo = codigo;
	}

	public Bono(String codigo, double d) {
		this(codigo);
		disponible = d;
		descripcion = "";
	}

	public Bono(String codigo, String descripcion, double disponible) {
		this(codigo, disponible);
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Bono other = (Bono) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Bono [disponible=" + disponible + ", codigo=" + codigo + "]";
	}

	public double getDisponible() {
		return disponible;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	/**
	 * Si se paga con un bono, si este tiene un saldo mayor al precio a pagar se
	 * reducirá el saldo de este y se realizará el pago correctamente. En el otro
	 * caso saltará una excepción por intentar pagar con dinero insuficiente.
	 */
	public void pagar(double d) throws BusinessException {
		if (d > disponible) {
			throw new BusinessException(
					"No puedes pagar con un bono cuyo dinero disponible sea menor al importe a pagar");
		} else {
			super.pagar(d);
			disponible -= d;
		}
	}
}