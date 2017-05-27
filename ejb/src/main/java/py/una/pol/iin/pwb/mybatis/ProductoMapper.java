package py.una.pol.iin.pwb.mybatis;

import java.util.List;

import org.mybatis.cdi.Mapper;

import py.una.pol.iin.pwb.model.Producto;

@Mapper
public interface ProductoMapper {
	public void insertProducto(Producto producto);
	public void updateProducto(Producto producto);
	public void deleteProductoById(Long id);
	public List<Producto> findAllProductos();
	public Producto findProductoById(Long id);
}
