package py.una.pol.iin.pwb.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.PostActivate;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.ibatis.session.SqlSession;

import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidArgumentException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Cliente;
import py.una.pol.iin.pwb.model.DetalleVenta;
import py.una.pol.iin.pwb.model.Producto;
import py.una.pol.iin.pwb.model.Venta;
import py.una.pol.iin.pwb.mybatis.ClienteMapper;
import py.una.pol.iin.pwb.mybatis.DetalleVentaMapper;
import py.una.pol.iin.pwb.mybatis.MyBatisUtil;
import py.una.pol.iin.pwb.mybatis.ProductoMapper;
import py.una.pol.iin.pwb.mybatis.VentaMapper;
import py.una.pol.iin.pwb.validator.CustomValidator;

@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class CarritoVentaBean implements ICarritoVentaBean {

	private String sessionKey;	

	@Resource
	private UserTransaction userTransaction;
	
	@Inject IProductoBean productoBean;
	@Inject IClienteBean clienteBean;
	@Inject VentaMapper ventaMapper;
	@Inject DetalleVentaMapper detalleVentaMapper;
	@Inject ProductoMapper productoMapper;
	@Inject ClienteMapper clienteMapper;
	
	Venta venta;
	
	
	@PostConstruct
	@PostActivate
	public void initValues()
	{
		sessionKey = null;
		venta = null;
	}
	
	@Override
	public String getSessionKey() {
		return sessionKey;
	}

	@Override
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
	@Override
	public Venta crearVenta(Venta venta) throws InvalidArgumentException, InvalidFormatException, DataNotFoundException, Exception {
				
		
		if (userTransaction.getStatus() == Status.STATUS_ACTIVE && this.venta == null)
		{
			throw new InvalidArgumentException("Ya existe una venta en curso. Debe finalizar o cancelar la operacion.");
		}
		
		try {
			
			
			
			// Para que el validador no tire el error de que la lista
			// no puede ser nula o estar vacia.
			venta.setDetalles(new DetalleVenta[2]);
			
			userTransaction.begin();
			
			CustomValidator.validateAndThrow(venta);
			Cliente cliente = clienteBean.getCliente(venta.getClienteId());
			
			this.venta = venta;
			this.venta.setCliente(cliente);
			
			ventaMapper.insertVenta(this.venta);
		} catch (DataNotFoundException e) {
			if (userTransaction.getStatus() == Status.STATUS_ACTIVE) userTransaction.rollback();			
			throw new InvalidArgumentException(e.getMessage());
		} catch (InvalidFormatException e) {
			if (userTransaction.getStatus() == Status.STATUS_ACTIVE) userTransaction.rollback();			
			throw new InvalidFormatException(e.getMessage());
		} catch (Exception e) {			
			if (userTransaction.getStatus() == Status.STATUS_ACTIVE) userTransaction.rollback();
			throw new Exception(e.getMessage());
		}
			
		return getVenta();
	}
	
	@Override
	public Venta finalizarVenta() throws InvalidArgumentException, Exception {
		
		
		verifyStatus();
		
		this.venta = getVenta();
		if (this.venta.getDetalleVentas().size() < 1)
		{
			throw new InvalidArgumentException("No se puede finalizar una venta sin productos asociados");
		}
		
		Cliente cliente = this.venta.getCliente();
		cliente.setDeuda(cliente.getDeuda() + venta.getMontoTotal());
		clienteMapper.updateCliente(cliente);
		
		Venta venta = this.venta;
		this.venta = null;
		userTransaction.commit();				
		return venta;
	}
	@Override
	public void cancelarVenta() throws InvalidArgumentException, Exception {
		
		verifyStatus();
		
		this.venta = null;
		userTransaction.rollback();		
	}
	
	
	@Override
	public Venta agregarProductos(List<DetalleVenta> detallesVentas)
			throws InvalidArgumentException, InvalidFormatException, Exception {
		
		verifyStatus();
		
		
		for (DetalleVenta detalleVenta : detallesVentas)
		{
			try {
				CustomValidator.validateAndThrow(detallesVentas);
				
				Producto producto = productoBean.getProducto(detalleVenta.getProductoId());							
								
				if (producto.getCantidad() < detalleVenta.getCantidad())
				{
					throw new InvalidArgumentException("La cantidad del producto " + producto.getId() + " a vender no puede ser mayor a la cantidad en stock");
				}
				
			} catch (DataNotFoundException e) {				
				throw new InvalidArgumentException(e.getMessage());				
			} 
		}		
			
		
		for (DetalleVenta detalleVenta : detallesVentas)
		{
				try {
					
					Producto producto = productoBean.getProducto(detalleVenta.getProductoId());							
					
					producto.setCantidad(producto.getCantidad() - detalleVenta.getCantidad());
					productoMapper.updateProducto(producto);
					
					boolean actualizado = false;
					for (DetalleVenta detalleVentaExistente : this.venta.getDetalleVentas())
					{
						if (detalleVentaExistente.getProducto().getId().equals(detalleVenta.getProductoId()))
						{
							detalleVentaExistente.setCantidad(detalleVentaExistente.getCantidad() + detalleVenta.getCantidad());
							detalleVentaMapper.updateDetalleVenta(detalleVentaExistente);
							actualizado = true;
							break;
						}
					}
					if (!actualizado)
					{					
						detalleVenta.setVenta(this.venta);
						detalleVenta.setProducto(producto);
						detalleVentaMapper.insertDetalleVenta(detalleVenta);
						
					}
					
				} catch (Exception e)
				{
					if (userTransaction.getStatus() == Status.STATUS_ACTIVE) userTransaction.rollback();
					throw new Exception(e.getMessage());
				}
			
			
		}
		return getVenta();
	}

	@Override
	public Venta quitarProductos(List<DetalleVenta> detallesVentas) throws InvalidArgumentException, InvalidFormatException, Exception {		
		
		verifyStatus();	
		
		
		for (DetalleVenta detalleVenta : detallesVentas)
		{
			try {
				CustomValidator.validateAndThrow(detallesVentas);
				
				Producto producto = productoBean.getProducto(detalleVenta.getProductoId());																			
				
				boolean encontrado = false;
				for (DetalleVenta detalleVentaExistente : this.venta.getDetalleVentas())
				{
					if (detalleVentaExistente.getProducto().getId().equals(detalleVenta.getProductoId()))
					{
						if (detalleVentaExistente.getCantidad() < detalleVenta.getCantidad())
							throw new InvalidArgumentException("La cantidad del producto " + producto.getId() + " que se quiere quitar es menor a la cantidad existente en el carrito");
						encontrado = true;
						break;
					}
				}
				if (!encontrado)
				{					
					throw new InvalidArgumentException("No existe el producto " + producto.getId() + " en el carrito.");
					
				}
				
			} catch (DataNotFoundException e) {				
				throw new InvalidArgumentException(e.getMessage());				
			} 
		}		
			
		
		for (DetalleVenta detalleVenta : detallesVentas)
		{
				try {
					
					Producto producto = productoBean.getProducto(detalleVenta.getProductoId());							
					
					producto.setCantidad(producto.getCantidad() + detalleVenta.getCantidad());
					productoMapper.updateProducto(producto);
					
					for (DetalleVenta detalleVentaExistente : this.venta.getDetalleVentas())
					{
						if (detalleVentaExistente.getProducto().getId().equals(detalleVenta.getProductoId()))
						{
							int cantidad = detalleVentaExistente.getCantidad() - detalleVenta.getCantidad();
							if (cantidad > 0) {
								detalleVentaExistente.setCantidad(cantidad);
								detalleVentaMapper.updateDetalleVenta(detalleVentaExistente);
							}							
							break;
						}
					}					
					
				} catch (Exception e)
				{
					if (userTransaction.getStatus() == Status.STATUS_ACTIVE) userTransaction.rollback();
					throw new Exception(e.getMessage());
				}
			
			
		}
		return getVenta();
	}

	@Override
	public Venta getVenta() throws InvalidArgumentException, Exception {
		
		verifyStatus();
		
		
		DetalleVenta[] detalles = new DetalleVenta[this.venta.getDetalleVentas().size()];
		double monto = 0f;
		int i = 0;
		for (DetalleVenta detalleVenta : this.venta.getDetalleVentas()) {
			monto += detalleVenta.getPrecioUnitario() * detalleVenta.getCantidad();
			detalles[i++] = detalleVenta;
		}
		
		this.venta.setMontoTotal(monto);
		this.venta.setDetalles(detalles);
		
		ventaMapper.updateVenta(this.venta);
		
		return this.venta;
	}

	
	private void verifyStatus() throws InvalidArgumentException, Exception {
		
		
		
		if (userTransaction.getStatus() == Status.STATUS_ROLLEDBACK || userTransaction.getStatus() == Status.STATUS_ROLLING_BACK || userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
		{
			throw new InvalidArgumentException("La transaccion ha sido abortada");
		}
		else if (userTransaction.getStatus() == Status.STATUS_COMMITTED || userTransaction.getStatus() == Status.STATUS_COMMITTING)
		{
			throw new InvalidArgumentException("La transaccion ha sido finalizada");
		}
				
		else if (userTransaction.getStatus() != Status.STATUS_ACTIVE || this.venta == null)
		{
			throw new InvalidArgumentException("No existe una transaccion en curso");
		}
	}
	
}
