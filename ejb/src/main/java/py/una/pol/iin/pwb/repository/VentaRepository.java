package py.una.pol.iin.pwb.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import py.una.pol.iin.pwb.model.DetalleVenta;
import py.una.pol.iin.pwb.model.Venta;

@Stateless
public class VentaRepository {

	@Inject
	EntityManager em;
	
	public List<Venta> findAllVentas(int offset_rows, int limit_rows)
	{
		
		
		String sql = "SELECT v.id, v.montototal, v.cliente_id, d.producto_id, "
				+ "d.cantidad, d.preciounitario "
				+ "FROM venta v "
				+ "INNER JOIN detalleventa d "
				+ "ON v.id = d.venta_id "
				+ "ORDER BY v.id "				
				+ "OFFSET " + offset_rows + " LIMIT " + limit_rows +";";
		
		Query q = em.createNativeQuery(sql);
		List<Object[]> ventas_result = q.getResultList();
		
		Venta venta = null;
		ArrayList<DetalleVenta> detalles = null;
		ArrayList<Venta> ventas = new ArrayList<>();
		
		for (Object[] venta_result : ventas_result)
		{
			Long ventaId = ((BigInteger)venta_result[0]).longValue();
			Double montoTotal = (Double)venta_result[1];
			Long clienteId = ((BigInteger)venta_result[2]).longValue();
			
			Long productoId = ((BigInteger)venta_result[3]).longValue();
			Integer cantidad = (Integer)venta_result[4];
			Double precioUnitario = (Double)venta_result[5];
			
			if (venta == null)
			{
				venta = new Venta();
				venta.setId(ventaId);
				venta.setMontoTotal(montoTotal);
				venta.setClienteId(clienteId);	
				detalles = new ArrayList<>();
			}
			
			if (ventaId.equals(venta.getId()))
			{
				DetalleVenta detalleVenta = new DetalleVenta();
				detalleVenta.setCantidad(cantidad);
				detalleVenta.setPrecioUnitario(precioUnitario);
				detalleVenta.setProductoId(productoId);
				detalles.add(detalleVenta);
				
			} else {								
				DetalleVenta[] detalles_array = new DetalleVenta[detalles.size()];
				detalles_array = detalles.toArray(detalles_array);
				venta.setDetalles(detalles_array);
				ventas.add(venta);
				
				venta = new Venta();
				venta.setId(ventaId);
				venta.setMontoTotal(montoTotal);
				venta.setClienteId(clienteId);	
				detalles = new ArrayList<>();
				
				DetalleVenta detalleVenta = new DetalleVenta();
				detalleVenta.setCantidad(cantidad);
				detalleVenta.setPrecioUnitario(precioUnitario);
				detalleVenta.setProductoId(productoId);
				detalles.add(detalleVenta);
				
			}
			
		}
		
		
		if (venta != null)
		{
			DetalleVenta[] detalles_array = new DetalleVenta[detalles.size()];
			detalles_array = detalles.toArray(detalles_array);
			venta.setDetalles(detalles_array);
			ventas.add(venta);
		}
	
				
		
		return ventas;
	}
}

