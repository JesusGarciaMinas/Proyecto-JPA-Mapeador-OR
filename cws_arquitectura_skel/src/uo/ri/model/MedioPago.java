package uo.ri.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uo.ri.util.exception.BusinessException;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "TMediosPago")
public abstract class MedioPago {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	protected Cliente cliente;
	
	@OneToMany(mappedBy = "medioPago")
	protected Set<Cargo> cargos = new HashSet<Cargo>();
	
	protected double acumulado = 0.0;

	@Override
	public String toString() {
		return "MedioPago [acumulado=" + acumulado + ", cliente=" + cliente + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
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
		MedioPago other = (MedioPago) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		return true;
	}

	Set<Cargo> _getCargos() {
		return cargos;
	}

	public Set<Cargo> getCargos() {
		return new HashSet<Cargo>(cargos);
	}

	void _setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	Cliente _getCliente() {
		return this.cliente;
	}

	public void pagar(double acumulado) throws BusinessException {
		this.acumulado += acumulado;
	}

	public double getAcumulado() {
		return acumulado;
	}

	double _getAcumulado() {
		return acumulado;
	}

	public Long getId() {
		return id;
	}

	void _setAcumulado(double acumulado) {
		this.acumulado = acumulado;
	}

	public void setAcumulado(double acumulado) {
		this.acumulado = acumulado;
	}
}