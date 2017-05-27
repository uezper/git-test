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
import py.una.pol.iin.pwb.model.Proveedor;
import py.una.pol.iin.pwb.model.DetalleCompra;
import py.una.pol.iin.pwb.model.Producto;
import py.una.pol.iin.pwb.model.Compra;
import py.una.pol.iin.pwb.mybatis.DetalleCompraMapper;
import py.una.pol.iin.pwb.mybatis.MyBatisUtil;
import py.una.pol.iin.pwb.mybatis.CompraMapper;
import py.una.pol.iin.pwb.repository.CompraRepository;
import py.una.pol.iin.pwb.util.FileCompraParser;
import py.una.pol.iin.pwb.validator.CustomValidator;

@Stateless
@TransactionManagement(value=TransactionManagementType.BEAN)
public class CompraBean implements ICompraBean {

	@Inject IProductoBean productoBean;
	@Inject IProveedorBean proveedorBean;
	@Inject CompraMapper compraMapper;
	@Inject DetalleCompraMapper detalleCompraMapper;
	
	@Resource
	UserTransaction userTransaction;

	
	@Override
	public Compra getCompra(Long id) throws Exception {
		Compra compra = compraMapper.findCompraById(id);		
		if (compra == null) {
			throw new DataNotFoundException("La compra con id " + id + " no existe");
		}					
		
		compra.setDetalles(compra.getDetalleCompras().toArray(new DetalleCompra[compra.getDetalleCompras().size()]));
		compra.setProveedorId(compra.getProveedor().getId());
		return compra;
	}
	
	@Override
	public Compra addCompra(Compra compra) throws InvalidFormatException, InvalidArgumentException, Exception {

		boolean commit = false;
		try {
			

			if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION)
			{
				userTransaction.begin();
				commit = true;
			}
						
			CustomValidator.validateAndThrow(compra);
			Proveedor proveedor = proveedorBean.getProveedor(compra.getProveedorId());
			
			
			double total = 0;
			for (DetalleCompra detalleCompra : compra.getDetalles())
			{			
				detalleCompra.setCompra(compra);							
				CustomValidator.validateAndThrow(detalleCompra);										
			}
			
		
			
			compra.setProveedor(proveedor);
			compraMapper.insertCompra(compra);
			
			for (DetalleCompra detalleCompra : compra.getDetalles())
			{
				Producto producto = productoBean.getProducto(detalleCompra.getProductoId());
				
				detalleCompra.setCompra(compra);
				detalleCompra.setProducto(producto);
				detalleCompraMapper.insertDetalleCompra(detalleCompra);				
				
				total += detalleCompra.getPrecioUnitario() * detalleCompra.getCantidad();
																
				producto.setCantidad(producto.getCantidad() + detalleCompra.getCantidad());
				productoBean.updateProducto(producto);				
			}
			
			
			compra.setMontoTotal(total);
			
			proveedorBean.updateProveedor(proveedor);
			compraMapper.updateCompra(compra);
			
			
			if (commit)
			{
				userTransaction.commit();
			}
			
			
			
			return compra;
			
		}  catch (Exception e) {			
			if (commit) { userTransaction.rollback(); }
			throw new InvalidArgumentException(e.getMessage());
		}	
		
	}
		
	@Override
	public boolean addComprasFromFile(String path) throws InvalidFormatException, InvalidArgumentException, Exception {			
		
		try {
			
			userTransaction.begin();
			
			FileCompraParser fp = new FileCompraParser(path);
			Object o;
			
			Compra compra = null;
			DetalleCompra detalleCompra = null;
			ArrayList<DetalleCompra> detalles = null;
			
			while((o = fp.nextObject()) != null)
			{
				if (o instanceof Compra)
				{
					if (compra == null)
					{
						compra = (Compra)o;		
						detalles = new ArrayList<>();
					} else {						
						DetalleCompra[] detalles_array = new DetalleCompra[detalles.size()];
						detalles_array = detalles.toArray(detalles_array);
						compra.setDetalles(detalles_array);
						addCompra(compra);
						
						compra = (Compra)o;
						detalles = new ArrayList<>();
					}
					
				}
				else if (o instanceof DetalleCompra)
				{
					detalleCompra = (DetalleCompra)o;
					
					if (compra != null)
					{
						detalles.add(detalleCompra);
					}
				}
			}
			if (compra != null)
			{
				DetalleCompra[] detalles_array = new DetalleCompra[detalles.size()];
				detalles_array = detalles.toArray(detalles_array);
				compra.setDetalles(detalles_array);
				addCompra(compra);
				compra = null;
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
	public Entry<ArrayList<Compra>, Long> getAllCompras(Long offset, int num_compras) throws Exception {
			
		ArrayList<Compra> compras = new ArrayList<>(compraMapper.findAllCompras(offset + 1, num_compras));
		
		long max_id = offset;
		for (Compra compra : compras) {
			compra.setDetalleCompras(detalleCompraMapper.findDetalleCompraFromCompra(compra.getId()));
			compra.setDetalles(compra.getDetalleCompras().toArray(new DetalleCompra[compra.getDetalleCompras().size()]));
			
			if (compra.getId() > max_id) { max_id = compra.getId(); }
		}
		
		return new SimpleEntry(compras, max_id);
		
	}	
		
}
