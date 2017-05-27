package py.una.pol.iin.pwb.mybatis;

import java.util.List;

import org.mybatis.cdi.Mapper;

import py.una.pol.iin.pwb.model.Proveedor;

@Mapper
public interface ProveedorMapper {
	public void insertProveedor(Proveedor proveedor);
	public void updateProveedor(Proveedor proveedor);
	public void deleteProveedorById(Long id);
	public List<Proveedor> findAllProveedores();
	public Proveedor findProveedorById(Long id);
}
