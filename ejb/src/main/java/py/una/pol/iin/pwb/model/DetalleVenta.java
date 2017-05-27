package py.una.pol.iin.pwb.model;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement
@Table(name = "DetalleVenta")
public class DetalleVenta implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonIgnore
	private Long id;
    @JsonIgnore
    @ManyToOne       
    private Venta venta;
    @JsonIgnore
	@ManyToOne		
	private Producto producto;
	@Min(value=1, message="El campo 'precioUnitario' debe ser positivo")
	private double precioUnitario;
	@Min(value=1, message="El campo 'cantidad' debe tener un valor mayor o igual a 1")
	private int cantidad;

	
	public DetalleVenta()
	{		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Venta getVenta() {
		return venta;
	}


	public void setVenta(Venta venta) {
		this.venta = venta;
	}


	public Producto getProducto() {
		return producto;
	}


	public void setProducto(Producto producto) {
		this.producto = producto;
	}


	public double getPrecioUnitario() {
		return precioUnitario;
	}


	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}


	public int getCantidad() {
		return cantidad;
	}


	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	

	// Json only fields and methods (for transfering data)
	@Transient
	@NotNull(message="El campo 'productoId' no puede ser nulo")	
	private Long productoId;	

	public Long getProductoId() {
		return productoId;
	}


	public void setProductoId(Long productoId) {		
		this.productoId = productoId;
	}
	
	
	
}
