package py.una.pol.iin.pwb.bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Producto;
import py.una.pol.iin.pwb.mybatis.ProductoMapper;
import py.una.pol.iin.pwb.validator.CustomValidator;

@Stateless
public class VulnerableProductoBean implements IVulnerableProductoBean {

	static long idCounter = 1;

	@Inject ProductoMapper productoMapper;
	
	@Override
	public List<Producto> getAllProductos() throws Exception {
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(new String[] { "sh", "-c", "echo \"[" + LocalDateTime.now() + "] consulta todos los productos\" >> log_producto" });
		
		Connection con = getConnection();
		String query = "SELECT * FROM producto";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		List<Producto> productos = new ArrayList<Producto>();		
        while (rs.next()) {
            Long id = rs.getLong("id");
            Integer cantidad = rs.getInt("cantidad");
            String descripcion = rs.getString("descripcion");
            Producto p = new Producto();
            p.setId(id);
            p.setCantidad(cantidad);
            p.setDescripcion(descripcion);
            productos.add(p);
        }
		
		return productos;		
	}

	@Override
	public Producto getProducto(String id) throws DataNotFoundException, Exception {
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(new String[] { "sh", "-c", "echo \"[" + LocalDateTime.now() + "] consulta producto id " + id + "\" >> log_producto" });

		Connection con = getConnection();
		String query = "SELECT * FROM producto where id=" + id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
			
		if (rs.next()) {
            Long productoid = rs.getLong("id");
            Integer cantidad = rs.getInt("cantidad");
            String descripcion = rs.getString("descripcion");
            Producto p = new Producto();
            p.setId(productoid);
            p.setCantidad(cantidad);
            p.setDescripcion(descripcion);
            return p;
        }else{
			throw new DataNotFoundException("El producto con el id " + id + " no existe" );
        }
	}
	
	@Override
	public Producto addProducto(Producto producto) throws InvalidFormatException, Exception {
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(new String[] { "sh", "-c", "echo \"[" + LocalDateTime.now() + "] agrega producto nombre " + producto.getDescripcion() + "\" >> log_producto" });
		
		producto.setId(idCounter);
		idCounter++;
		Connection con = getConnection();
		String query = "INSERT INTO producto VALUES(" + producto.getId() + ", " + producto.getCantidad() + ",'" + producto.getDescripcion() + "')";
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
		
		return producto;
	}

	@Override
	public Producto updateProducto(Producto producto)  throws DataNotFoundException, InvalidFormatException, Exception {
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(new String[] { "sh", "-c", "echo \"[" + LocalDateTime.now() + "] actualiza producto nombre " + producto.getDescripcion() + "\" >> log_producto" });

		CustomValidator.validateAndThrow(producto);		
		
		try{
			Connection con = getConnection();
			String query = "UPDATE producto SET cantidad=" + producto.getCantidad() + ", descripcion='" + producto.getDescripcion() + "' WHERE id=" + producto.getId();
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
		}catch(Exception e){
			System.out.print(e.getMessage());
		}
		
		return producto;
	}

	@Override
	public void removeProducto(String id) throws Exception {
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(new String[] { "sh", "-c", "echo \"[" + LocalDateTime.now() + "] elimina producto id " + id + "\" >> log_producto" });

		Connection con = getConnection();
		String query = "DELETE FROM producto WHERE id=" + id;
		Statement stmt = con.createStatement();
		stmt.executeQuery(query);
	}

	private Connection getConnection() throws Exception{
		Context ic = new InitialContext();
		Context ctx = (Context) ic.lookup("java:jboss/datasources/");
		DataSource ds = (DataSource) ctx.lookup("pwbDS");
		return ds.getConnection();
	}
	
}
