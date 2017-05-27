package py.una.pol.iin.pwb.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@XmlRootElement
@Table(name = "Compra")
public class Compra implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
	private Long id;
    
    @JsonIgnore
    @ManyToOne    
    private Proveedor proveedor;    
	private double montoTotal;
	
	@JsonIgnore
	@OneToMany(targetEntity=DetalleCompra.class, mappedBy="compra", cascade=CascadeType.REMOVE)	
	private Set<DetalleCompra> detalleCompras;

	
	public Compra()
	{		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	

	public Proveedor getProveedor() {
		return proveedor;
	}


	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}


	public double getMontoTotal() {
		return montoTotal;
	}


	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}


	public Set<DetalleCompra> getDetalleCompras() {
		return detalleCompras;
	}


	public void setDetalleCompras(Set<DetalleCompra> detalleCompras) {
		this.detalleCompras = detalleCompras;
	}
	
	
	// Json only fields and methods (for transfering data)
	@Transient
	@NotNull(message="El campo 'proveedorId' no puede ser nulo")	
    private Long proveedorId;
	@Transient
	@NotNull(message="El campo 'detalles' no puede ser nulo")
	@Size(min=1, message="El campo 'detalles' debe tener al menos un elemento")
	private DetalleCompra[] detalles;
	

	public Long getProveedorId() {
		return proveedorId;
	}


	public void setProveedorId(Long proveedorId) {
		this.proveedorId = proveedorId;
	}


	public DetalleCompra[] getDetalles() {
		return detalles;
	}


	public void setDetalles(DetalleCompra[] detalles) {
		this.detalles = detalles;
	}	

	
}
