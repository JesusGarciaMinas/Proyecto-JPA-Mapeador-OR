package uo.ri.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TIntervenciones")
public class Intervencion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Averia averia;
	@ManyToOne
	private Mecanico mecanico;

	@OneToMany(mappedBy = "intervencion")
	private Set<Sustitucion> sustituciones = new HashSet<Sustitucion>();

	private int minutos;

	Intervencion() {
	}

	public Intervencion(Mecanico mecanico, Averia averia) {
		super();
		Association.Intervenir.link(mecanico, this, averia);
	}

	public Intervencion(Mecanico m, Averia a, int minutes) {
		this(m, a);
		minutos = minutes;
	}

	@Override
	public String toString() {
		return "Intervencion [averia=" + averia + ", mecanico=" + mecanico + ", minutos=" + minutos + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((averia == null) ? 0 : averia.hashCode());
		result = prime * result + ((mecanico == null) ? 0 : mecanico.hashCode());
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
		Intervencion other = (Intervencion) obj;
		if (averia == null) {
			if (other.averia != null)
				return false;
		} else if (!averia.equals(other.averia))
			return false;
		if (mecanico == null) {
			if (other.mecanico != null)
				return false;
		} else if (!mecanico.equals(other.mecanico))
			return false;
		return true;
	}

	void _setMecanico(Mecanico mecanico) {
		this.mecanico = mecanico;
	}

	public Mecanico getMecanico() {
		return mecanico;
	}

	Mecanico _getMecanico() {
		return mecanico;
	}

	void _setAveria(Averia averia) {
		this.averia = averia;
	}

	Averia _getAveria() {
		return averia;
	}

	public Averia getAveria() {
		return averia;
	}

	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}

	Set<Sustitucion> _getSustituciones() {
		return sustituciones;
	}

	public Set<Sustitucion> getSustituciones() {
		return new HashSet<Sustitucion>(sustituciones);
	}

	public Long getId() {
		return id;
	}

	/**
	 * El importe de la intervención se calcula con la suma de todos los importes
	 * sumado al precio/hora del vehiculo por todos los mínutos que ha durado
	 * 
	 * @return el importe total
	 */
	public double getImporte() {
		double importe = ((double) minutos / 60.0) * averia.getVehiculo().getTipo().getPrecioHora();
		for (Sustitucion sustitucion : sustituciones)
			importe += sustitucion.getImporte();
		return importe;
	}
}