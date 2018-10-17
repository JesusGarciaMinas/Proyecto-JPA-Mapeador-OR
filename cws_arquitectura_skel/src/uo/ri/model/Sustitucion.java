package uo.ri.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TSustituciones")
public class Sustitucion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Repuesto repuesto;

	@ManyToOne
	private Intervencion intervencion;

	private int cantidad;

	Sustitucion() {
	}

	public Sustitucion(Repuesto repuesto, Intervencion intervencion) {
		Association.Sustituir.link(repuesto, intervencion, this);
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Intervencion getIntervencion() {
		return intervencion;
	}

	Intervencion _getIntervencion() {
		return intervencion;
	}

	void _setRepuesto(Repuesto repuesto) {
		this.repuesto = repuesto;
	}

	@Override
	public String toString() {
		return "Sustitucion [repuesto=" + repuesto + ", intervencion=" + intervencion + ", cantidad=" + cantidad + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((intervencion == null) ? 0 : intervencion.hashCode());
		result = prime * result + ((repuesto == null) ? 0 : repuesto.hashCode());
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
		Sustitucion other = (Sustitucion) obj;
		if (intervencion == null) {
			if (other.intervencion != null)
				return false;
		} else if (!intervencion.equals(other.intervencion))
			return false;
		if (repuesto == null) {
			if (other.repuesto != null)
				return false;
		} else if (!repuesto.equals(other.repuesto))
			return false;
		return true;
	}

	public Repuesto getRepuesto() {
		return repuesto;
	}

	Repuesto _getRepuesto() {
		return repuesto;
	}

	void _setIntervencion(Intervencion intervencion) {
		this.intervencion = intervencion;
	}

	public int getCantidad() {
		return cantidad;
	}

	int _getCantidad() {
		return cantidad;
	}

	public double getImporte() {
		return repuesto.getPrecio() * cantidad;
	}
}