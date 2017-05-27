package py.una.pol.iin.pwb.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import py.una.pol.iin.pwb.model.DetalleCompra;
import py.una.pol.iin.pwb.model.Compra;

@Stateless
public class CompraRepository {

	@Inject
	EntityManager em;
	
	public List<Compra> findAllCompras(int offset_rows, int limit_rows)
	{
		
		
		String sql = "SELECT v.id, v.montototal, v.proveedor_id, d.producto_id, "
				+ "d.cantidad, d.preciounitario "
				+ "FROM compra v "
				+ "INNER JOIN detallecompra d "
				+ "ON v.id = d.compra_id "
				+ "ORDER BY v.id "				
				+ "OFFSET " + offset_rows + " LIMIT " + limit_rows +";";
		
		Query q = em.createNativeQuery(sql);
		List<Object[]> compras_result = q.getResultList();
		
		Compra compra = null;
		ArrayList<DetalleCompra> detalles = null;
		ArrayList<Compra> compras = new ArrayList<>();
		
		for (Object[] compra_result : compras_result)
		{
			Long compraId = ((BigInteger)compra_result[0]).longValue();
			Double montoTotal = (Double)compra_result[1];
			Long proveedorId = ((BigInteger)compra_result[2]).longValue();
			
			Long productoId = ((BigInteger)compra_result[3]).longValue();
			Integer cantidad = (Integer)compra_result[4];
			Double precioUnitario = (Double)compra_result[5];
			
			if (compra == null)
			{
				compra = new Compra();
				compra.setId(compraId);
				compra.setMontoTotal(montoTotal);
				compra.setProveedorId(proveedorId);	
				detalles = new ArrayList<>();
			}
			
			if (compraId.equals(compra.getId()))
			{
				DetalleCompra detalleCompra = new DetalleCompra();
				detalleCompra.setCantidad(cantidad);
				detalleCompra.setPrecioUnitario(precioUnitario);
				detalleCompra.setProductoId(productoId);
				detalles.add(detalleCompra);
				
			} else {								
				DetalleCompra[] detalles_array = new DetalleCompra[detalles.size()];
				detalles_array = detalles.toArray(detalles_array);
				compra.setDetalles(detalles_array);
				compras.add(compra);
				
				compra = new Compra();
				compra.setId(compraId);
				compra.setMontoTotal(montoTotal);
				compra.setProveedorId(proveedorId);	
				detalles = new ArrayList<>();
				
				DetalleCompra detalleCompra = new DetalleCompra();
				detalleCompra.setCantidad(cantidad);
				detalleCompra.setPrecioUnitario(precioUnitario);
				detalleCompra.setProductoId(productoId);
				detalles.add(detalleCompra);
				
			}
			
		}
		
		
		if (compra != null)
		{
			DetalleCompra[] detalles_array = new DetalleCompra[detalles.size()];
			detalles_array = detalles.toArray(detalles_array);
			compra.setDetalles(detalles_array);
			compras.add(compra);
		}
	
				
		
		return compras;
	}
}

