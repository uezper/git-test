package py.una.pol.iin.pwb.bean;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Producto;
import py.una.pol.iin.pwb.mybatis.ProductoMapper;
import py.una.pol.iin.pwb.validator.CustomValidator;

@Stateless
public class ProductoBean implements IProductoBean {

	@Inject ProductoMapper productoMapper;
	
	@Override
	public List<Producto> getAllProductos() throws Exception {
		List<Producto> productos = null;		
		productos = productoMapper.findAllProductos();
		return productos;		
	}

	@Override
	public Producto getProducto(Long id) throws DataNotFoundException, Exception {
		
		Producto producto = productoMapper.findProductoById(id);
		if (producto == null)
			throw new DataNotFoundException("El producto con el id " + id + " no existe" );		
		return producto;
	}

	@Override
	public Producto addProducto(Producto producto) throws InvalidFormatException, Exception {
		
		productoMapper.insertProducto(producto);
		return producto;
	}

	@Override
	public Producto updateProducto(Producto producto)  throws DataNotFoundException, InvalidFormatException, Exception {
		CustomValidator.validateAndThrow(producto);		
		Producto producto2 = productoMapper.findProductoById(producto.getId());
		if (producto2 == null) throw new DataNotFoundException("El producto con el id " + producto.getId() + " no existe.");
		
		producto2.setCantidad(producto.getCantidad());
		producto2.setDescripcion(producto.getDescripcion());
		
		productoMapper.updateProducto(producto2);
		return producto2;
	}

	@Override
	public void removeProducto(Long id) throws Exception {
		productoMapper.deleteProductoById(id);		
	}

	
}
