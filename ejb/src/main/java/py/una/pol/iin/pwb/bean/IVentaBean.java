package py.una.pol.iin.pwb.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Local;

import py.una.pol.iin.pwb.exception.InvalidArgumentException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Venta;

@Local
public interface IVentaBean {
	public Venta addVenta(Venta venta) throws InvalidFormatException, InvalidArgumentException, Exception;
	public boolean addVentasFromFile(String path) throws InvalidFormatException, InvalidArgumentException, Exception;
	public Entry<ArrayList<Venta>, Long> getAllVentas(Long offset, int num_ventas) throws Exception;
	
	public Venta getVenta(Long id) throws Exception;
}
