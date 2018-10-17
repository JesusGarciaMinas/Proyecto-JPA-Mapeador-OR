package uo.ri.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TMetalicos")
@DiscriminatorValue("TMetalicos")
public class Metalico extends MedioPago {

	Metalico() {
	}

	public Metalico(Cliente cliente) {
		Association.Pagar.link(cliente, this);
	}
}