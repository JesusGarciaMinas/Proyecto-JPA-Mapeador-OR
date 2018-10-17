package uo.ri.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import alb.util.random.Random;
import uo.ri.model.types.Address;

@Entity
@Table(name = "TClientes")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String dni;

	@OneToMany(mappedBy = "cliente")
	private Set<Vehiculo> vehiculos = new HashSet<Vehiculo>();
	@OneToMany(mappedBy = "cliente")
	private Set<MedioPago> mediosPago = new HashSet<MedioPago>();
	@OneToMany(mappedBy = "recomendador" , cascade = CascadeType.PERSIST)
	private Set<Recomendacion> recomendacionesHechas = new HashSet<Recomendacion>();

	@OneToOne(mappedBy = "recomendado")
	private Recomendacion recibida;

	private String nombre;
	private String apellidos;
	private Address address;
	private String email;
	private String phone;

	Cliente() {
	}

	public Cliente(String dni) {
		this.dni = dni;
	}

	public Cliente(String dni, String nombre, String apellidos) {
		this(dni);
		this.nombre = nombre;
		this.apellidos = apellidos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
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
		Cliente other = (Cliente) obj;
		if (dni == null) {
			if (other.dni != null)
				return false;
		} else if (!dni.equals(other.dni))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cliente [nombre=" + nombre + ", apellidos=" + apellidos + ", dni=" + dni + ", address=" + address + "]";
	}

	public Set<Vehiculo> getVehiculos() {
		return new HashSet<Vehiculo>(vehiculos);
	}

	Set<Vehiculo> _getVehiculos() {
		return vehiculos;
	}

	public Set<MedioPago> getMediosPago() {
		return new HashSet<MedioPago>(mediosPago);
	}

	Set<MedioPago> _getMediosPago() {
		return mediosPago;
	}

	public Long getId() {
		return id;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDni() {
		return dni;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<Recomendacion> getRecomendacionesHechas() {
		return new HashSet<Recomendacion>(recomendacionesHechas);
	}

	Set<Recomendacion> _getRecomendacionesHechas() {
		return recomendacionesHechas;
	}

	public Recomendacion getRecomendacionRecibida() {
		return recibida;
	}

	Recomendacion _getRecomendacionRecibida() {
		return recibida;
	}

	void _setRecomendacionRecibida(Recomendacion recomendacion) {
		this.recibida = recomendacion;
	}

	/**
	 * Se hace una lista de averías que no han sido utilizadas para conseguir bonos
	 * pero que si son elegibles para serlo
	 * 
	 * @return la lista con todas las averías con las que se pueden conseguir bonos
	 */
	public List<Averia> getAveriasBono3NoUsadas() {
		List<Averia> averias = new ArrayList<Averia>();
		for (Vehiculo v : vehiculos) {
			for (Averia a : v._getAverias()) {
				if (a.esElegibleParaBono3()) {
					averias.add(a);
				}
			}
		}
		return averias;
	}

	public int averiasParaBono3() {
		return getAveriasBono3NoUsadas().size();
	}

	/**
	 * Se hace una lista con todas las averías de un cliente
	 * 
	 * @return la lista con todas las averías
	 */
	private List<Averia> getAverias() {
		List<Averia> averias = new ArrayList<Averia>();
		for (Vehiculo v : vehiculos) {
			for (Averia a : v._getAverias()) {
				averias.add(a);
			}
		}
		return averias;
	}

	public Address getAddress() {
		return this.address;
	}

	/**
	 * Se cuentan todas las recomendaciones hechas y si esta no ha sido utilizada
	 * anteriormente
	 * 
	 * @return el número de recomendaciones realizadas con averías
	 */
	private int getRecomendadosAverias() {
		int contador = 0;
		for (Recomendacion r : this.recomendacionesHechas) {
			if (r._getRecomendado().getAverias().size() > 0 && !r.isUsada()) {
				contador++;
			}
		}
		return contador;
	}

	/**
	 * Si el cliente tiene un vehículo con alguna avería y con 3 o más
	 * recomendaciones hechas si es elegible este cliente para conseguir un bono por
	 * recomendaciones
	 * 
	 * @return si es elegible para conseguir un bono
	 */
	public boolean elegibleBonoPorRecomendaciones() {
		if (vehiculos.isEmpty()) {
			return false;
		} else if (getAverias().isEmpty()) {
			return false;
		} else if (this._getRecomendacionesHechas().size() < 3) {
			return false;
		} else if (getRecomendadosAverias() < 3) {
			return false;
		}
		return true;
	}

	public void setRecomendacionRecibida(Recomendacion rec) {
		this.recibida = rec;
	}

	public void setNombre(String name) {
		this.nombre = name;
	}

	public void setApellidos(String surname) {
		this.apellidos = surname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	/**
	 * Se genera un bono con un código aleatorio de tamaño 8 alfanumérico con valor
	 * de 20 euros
	 * 
	 * @return el bono generado
	 */
	public Bono generaBono() {
		Bono b = new Bono("B-" + Random.string(7), "Por tres averías", 20.0);
		b._setCliente(this);
		mediosPago.add(b);
		return b;
	}

	/**
	 * Se marca un número de averías según los bonos dados a este cliente
	 * 
	 * @param marcarAverias, número de bonos recibidos * 3
	 */
	public void marcarAverias(int marcarAverias) {
		for (Averia a : getAveriasBono3NoUsadas()) {
			if (marcarAverias > 0) {
				a.markAsBono3Used();
				marcarAverias--;
			} else
				break;
		}
	}
}