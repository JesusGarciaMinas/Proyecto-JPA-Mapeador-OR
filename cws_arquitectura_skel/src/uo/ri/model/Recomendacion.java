package uo.ri.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TRecomendaciones")
public class Recomendacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Cliente recomendador;

	@OneToOne
	private Cliente recomendado;

	private boolean usada;

	Recomendacion() {
	}

	public Recomendacion(Cliente recomendador, Cliente recomendado) {
		this.recomendador = recomendador;
		this.recomendado = recomendado;
		Association.Recomendar.link(this);
	}

	public Cliente getRecomendado() {
		return recomendado;
	}

	public Cliente getRecomendador() {
		return recomendador;
	}

	@Override
	public String toString() {
		return "Recomendacion [recomendador=" + recomendador + ", recomendado=" + recomendado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((recomendado == null) ? 0 : recomendado.hashCode());
		result = prime * result + ((recomendador == null) ? 0 : recomendador.hashCode());
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
		Recomendacion other = (Recomendacion) obj;
		if (recomendado == null) {
			if (other.recomendado != null)
				return false;
		} else if (!recomendado.equals(other.recomendado))
			return false;
		if (recomendador == null) {
			if (other.recomendador != null)
				return false;
		} else if (!recomendador.equals(other.recomendador))
			return false;
		return true;
	}

	public void unlink() {
		Association.Recomendar.unlink(this);
		recomendador = null;
		recomendado = null;
	}

	Cliente _getRecomendador() {
		return recomendador;
	}

	Cliente _getRecomendado() {
		return recomendado;
	}

	public void markAsUsadaBono() {
		usada = true;
	}

	public boolean isUsada() {
		return usada;
	}
}