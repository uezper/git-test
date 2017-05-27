package py.una.pol.iin.pwb.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@XmlRootElement
@Table(name = "Proveedor")
public class Proveedor implements Serializable {
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
	
	@JsonIgnore
	@OneToMany(targetEntity=Compra.class, mappedBy="proveedor")	
	protected Set<Compra> compras;
	
	
	public Proveedor()
	{	
	}
	
	public Proveedor(long id, String nombre, String telefono)
	{
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
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

	public Set<Compra> getCompras() {
		return compras;
	}

	public void setCompras(Set<Compra> compras) {
		this.compras = compras;
	}
	
	
}
