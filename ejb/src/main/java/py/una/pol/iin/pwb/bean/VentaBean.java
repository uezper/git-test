package py.una.pol.iin.pwb.bean;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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
import py.una.pol.iin.pwb.mybatis.DetalleVentaMapper;
import py.una.pol.iin.pwb.mybatis.MyBatisUtil;
import py.una.pol.iin.pwb.mybatis.VentaMapper;
import py.una.pol.iin.pwb.repository.VentaRepository;
import py.una.pol.iin.pwb.util.FileVentaParser;
import py.una.pol.iin.pwb.validator.CustomValidator;

@Stateless
@TransactionManagement(value=TransactionManagementType.BEAN)
public class VentaBean implements IVentaBean {

	@Inject IProductoBean productoBean;
	@Inject IClienteBean clienteBean;
	@Inject VentaMapper ventaMapper;
	@Inject DetalleVentaMapper detalleVentaMapper;
	
	@Resource
	UserTransaction userTransaction;

	
	@Override
	public Venta getVenta(Long id) throws Exception {
		Venta venta = ventaMapper.findVentaById(id);		
		if (venta == null) {
			throw new DataNotFoundException("La venta con id " + id + " no existe");
		}					
		
		venta.setDetalles(venta.getDetalleVentas().toArray(new DetalleVenta[venta.getDetalleVentas().size()]));
		venta.setClienteId(venta.getCliente().getId());
		return venta;
	}
	
	@Override
	public Venta addVenta(Venta venta) throws InvalidFormatException, InvalidArgumentException, Exception {
		
		boolean commit = false;
		
		try {
			
			
			if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION)
			{
				userTransaction.begin();
				commit = true;
			}
						
			CustomValidator.validateAndThrow(venta);
			Cliente cliente = clienteBean.getCliente(venta.getClienteId());
			
			
			double total = 0;
			for (DetalleVenta detalleVenta : venta.getDetalles())
			{			
				detalleVenta.setVenta(venta);							
				CustomValidator.validateAndThrow(detalleVenta);				
							
				Producto producto = productoBean.getProducto(detalleVenta.getProductoId());		
				if (detalleVenta.getCantidad() > producto.getCantidad()) {
					throw new InvalidArgumentException("La cantidad vendida no puede superar a la cantidad en stock");
				}																
			}
			
		
			
			venta.setCliente(cliente);
			ventaMapper.insertVenta(venta);
			
			for (DetalleVenta detalleVenta : venta.getDetalles())
			{
				Producto producto = productoBean.getProducto(detalleVenta.getProductoId());
				
				detalleVenta.setVenta(venta);
				detalleVenta.setProducto(producto);
				detalleVentaMapper.insertDetalleVenta(detalleVenta);				
				
				total += detalleVenta.getPrecioUnitario() * detalleVenta.getCantidad();
																
				producto.setCantidad(producto.getCantidad() - detalleVenta.getCantidad());
				productoBean.updateProducto(producto);				
			}
			
			
			cliente.setDeuda(cliente.getDeuda() + total);
			venta.setMontoTotal(total);
			
			clienteBean.updateCliente(cliente);
			ventaMapper.updateVenta(venta);
			
			if (commit)
			{
				userTransaction.commit();
			}
			
			
			
			return venta;
			
		} catch (Exception e) {			
			if (commit) { userTransaction.rollback(); }
			throw new InvalidArgumentException(e.getMessage());
		}	
		
	}
		
	@Override
	public boolean addVentasFromFile(String path) throws InvalidFormatException, InvalidArgumentException, Exception {			
		
		try {
			
			userTransaction.begin();
			
			FileVentaParser fp = new FileVentaParser(path);
			Object o;
			
			Venta venta = null;
			DetalleVenta detalleVenta = null;
			ArrayList<DetalleVenta> detalles = null;
			
			while((o = fp.nextObject()) != null)
			{
				if (o instanceof Venta)
				{
					if (venta == null)
					{
						venta = (Venta)o;		
						detalles = new ArrayList<>();
					} else {						
						DetalleVenta[] detalles_array = new DetalleVenta[detalles.size()];
						detalles_array = detalles.toArray(detalles_array);
						venta.setDetalles(detalles_array);
						addVenta(venta);
						
						venta = (Venta)o;
						detalles = new ArrayList<>();
					}
					
				}
				else if (o instanceof DetalleVenta)
				{
					detalleVenta = (DetalleVenta)o;
					
					if (venta != null)
					{
						detalles.add(detalleVenta);
					}
				}
			}
			if (venta != null)
			{
				DetalleVenta[] detalles_array = new DetalleVenta[detalles.size()];
				detalles_array = detalles.toArray(detalles_array);
				venta.setDetalles(detalles_array);
				addVenta(venta);
				venta = null;
			}
			
		}		
		catch (Exception e) {
			userTransaction.rollback();
			throw new InvalidFormatException(e.getMessage());
		}
		
		if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
		{
			userTransaction.commit();
		}
	
		
		return true;
	}



	@Override
	public Entry<ArrayList<Venta>, Long> getAllVentas(Long offset, int num_ventas) throws Exception {
		
		ArrayList<Venta> ventas = new ArrayList<>(ventaMapper.findAllVentas(offset + 1, num_ventas));
		
		long max_id = offset;
		for (Venta venta : ventas) {
			venta.setDetalleVentas(detalleVentaMapper.findDetalleVentaFromVenta(venta.getId()));
			venta.setDetalles(venta.getDetalleVentas().toArray(new DetalleVenta[venta.getDetalleVentas().size()]));
			
			if (venta.getId() > max_id) { max_id = venta.getId(); }
		}
		
		return new SimpleEntry(ventas, max_id);
		
	}	
		
}
