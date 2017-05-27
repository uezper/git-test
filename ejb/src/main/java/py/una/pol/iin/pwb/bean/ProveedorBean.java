package py.una.pol.iin.pwb.bean;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;

import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Proveedor;
import py.una.pol.iin.pwb.mybatis.ProveedorMapper;
import py.una.pol.iin.pwb.mybatis.MyBatisUtil;
import py.una.pol.iin.pwb.validator.CustomValidator;

@Stateless
public class ProveedorBean implements IProveedorBean {

	@Inject ProveedorMapper proveedorMapper;
	
	@Override
	public List<Proveedor> getAllProveedores() throws Exception {
				
		List<Proveedor> proveedores = null;		
		proveedores = proveedorMapper.findAllProveedores();
		return proveedores;		
	}

	@Override
	public Proveedor getProveedor(Long id) throws DataNotFoundException, Exception {
		
		Proveedor proveedor = null;		
		proveedor = proveedorMapper.findProveedorById(id);
		if (proveedor == null)
			throw new DataNotFoundException("El proveedor con id " + id + " no existe");
		
		
		return proveedor;
	}

	@Override
	public Proveedor addProveedor(Proveedor proveedor) throws InvalidFormatException, Exception {

		CustomValidator.validateAndThrow(proveedor);
		
		proveedorMapper.insertProveedor(proveedor);
		return proveedor;
	}

	@Override
	public Proveedor updateProveedor(Proveedor proveedor)  throws DataNotFoundException, InvalidFormatException, Exception {
		CustomValidator.validateAndThrow(proveedor);
		
		
		Proveedor proveedor2 = proveedorMapper.findProveedorById(proveedor.getId());
		if (proveedor2 == null) throw new DataNotFoundException("El proveedor con el id " + proveedor.getId() + " no existe");
		
		proveedor2.setNombre(proveedor.getNombre());
		proveedor2.setTelefono(proveedor.getTelefono());
		
		proveedorMapper.updateProveedor(proveedor2);
		return proveedor2;
	}

	@Override
	public void removeProveedor(Long id) throws Exception {
		
		proveedorMapper.deleteProveedorById(id);				
	}

}
