package py.una.pol.iin.pwb.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@XmlRootElement
@Table(name = "Cliente")
public class Cliente implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@NotNull(message="El campo 'nombre' no puede ser nulo")
	@Size(min=1, message="El campo 'nombre' no puede estar vacio")
	private String nombre;
	@NotNull(message="El campo 'telefono' no puede ser nulo")
	@Size(min=1, message="El campo 'telefono' no puede estar vacio")
	@Pattern(regexp="[0-9-+]+", message="El campo 'telefono' solo puede contener numeros y los signos - y +")
	private String telefono;	
	@Min(value=0, message="El campo 'deuda' no puede tener valores ser negativos")
	private double deuda;
	
	@JsonIgnore
	@OneToMany(targetEntity=Venta.class, mappedBy="cliente")	
	protected Set<Venta> ventas;
	
	
	public Cliente()
	{	
	}
	
	public Cliente(long id, String nombre, String telefono, double deuda)
	{
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.deuda = deuda;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public double getDeuda() {
		return deuda;
	}
	public void setDeuda(double deuda) {
		this.deuda = deuda;
	}

	public Set<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(Set<Venta> ventas) {
		this.ventas = ventas;
	}
	
	
}
