package py.una.pol.iin.pwb.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.cdi.Mapper;

import py.una.pol.iin.pwb.model.Compra;

@Mapper
public interface CompraMapper {
	public void insertCompra(Compra compra);
	public void updateCompra(Compra compra);
	public void deleteCompraById(Long id);
	public List<Compra> findAllCompras();
	public List<Compra> findAllCompras(@Param("offset") Long offset, @Param("limit") int limit);
	public Compra findCompraById(Long id);
}
