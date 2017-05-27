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
@Table(name = "Venta")
public class Venta implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
	private Long id;
    
    @JsonIgnore
    @ManyToOne    
    private Cliente cliente;    
	private double montoTotal;
	
	@JsonIgnore
	@OneToMany(targetEntity=DetalleVenta.class, mappedBy="venta", cascade=CascadeType.REMOVE)	
	private Set<DetalleVenta> detalleVentas;

	
	public Venta()
	{		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	

	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public double getMontoTotal() {
		return montoTotal;
	}


	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}


	public Set<DetalleVenta> getDetalleVentas() {
		return detalleVentas;
	}


	public void setDetalleVentas(Set<DetalleVenta> detalleVentas) {
		this.detalleVentas = detalleVentas;
	}
	
	
	// Json only fields and methods (for transfering data)
	@Transient
	@NotNull(message="El campo 'clienteId' no puede ser nulo")	
    private Long clienteId;
	@Transient
	@NotNull(message="El campo 'detalles' no puede ser nulo")
	@Size(min=1, message="El campo 'detalles' debe tener al menos un elemento")
	private DetalleVenta[] detalles;
	

	public Long getClienteId() {
		return clienteId;
	}


	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}


	public DetalleVenta[] getDetalles() {
		return detalles;
	}


	public void setDetalles(DetalleVenta[] detalles) {
		this.detalles = detalles;
	}	

	
}
