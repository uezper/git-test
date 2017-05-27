package py.una.pol.iin.pwb.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement
@Table(name = "Producto")
public class Producto implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;    
    @NotNull(message="El campo 'descripcion' no puede ser nulo")
	@Size(min=1, message="El campo 'descripcion' no puede estar vacio")
	private String descripcion;
	@Min(value=0, message="El campo 'cantidad' no puede tener valores negativo")
	private int cantidad;
	
	@JsonIgnore
	@OneToMany(targetEntity=DetalleVenta.class, mappedBy="producto")	
	private Set<DetalleVenta> detalleVentas;
	
	@PreRemove
	private void removeAssociationsWithDetalleVentas()
	{
		for (DetalleVenta dv : detalleVentas)
		{
			dv.setProducto(null);
		}
	}
	
	public Producto()
	{		
	}
	
	public Producto(Long id, String descripcion, int cantidad)
	{
		this.id = id;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Set<DetalleVenta> getDetalleVentas() {
		return detalleVentas;
	}

	public void setDetalleVentas(Set<DetalleVenta> detalleVentas) {
		this.detalleVentas = detalleVentas;
	}
	
	
	
}
