package py.una.pol.iin.pwb.bean;

import java.util.List;

import javax.ejb.Local;

import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Producto;

@Local
public interface IVulnerableProductoBean {
	
	public List<Producto> getAllProductos() throws Exception;
	public Producto getProducto(String id)  throws DataNotFoundException, Exception;
	public Producto addProducto(Producto producto) throws InvalidFormatException, Exception;
	public Producto updateProducto(Producto producto)  throws DataNotFoundException, InvalidFormatException, Exception;
	public void removeProducto(String id) throws Exception;
	
}
