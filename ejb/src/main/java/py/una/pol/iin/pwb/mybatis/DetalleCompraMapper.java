package py.una.pol.iin.pwb.mybatis;

import java.util.List;
import java.util.Set;

import org.mybatis.cdi.Mapper;

import py.una.pol.iin.pwb.model.DetalleCompra;

@Mapper
public interface DetalleCompraMapper {
	public void insertDetalleCompra(DetalleCompra detalleCompra);
	public void updateDetalleCompra(DetalleCompra detalleCompra);
	public void deleteDetalleCompraById(Long id);
	public List<DetalleCompra> findAllDetalleCompras();
	public DetalleCompra findDetalleCompraById(Long id);
	public Set<DetalleCompra> findDetalleCompraFromCompra(Long id);
}
