package py.una.pol.iin.pwb.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.cdi.Mapper;

import py.una.pol.iin.pwb.model.Venta;

@Mapper
public interface VentaMapper {
	public void insertVenta(Venta venta);
	public void updateVenta(Venta venta);
	public void deleteVentaById(Long id);
	public List<Venta> findAllVentas();
	public List<Venta> findAllVentas(@Param("offset") Long offset, @Param("limit") int limit);
	public Venta findVentaById(Long id);
}
