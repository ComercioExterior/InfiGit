package models.picklist.pick_unidad_inversion;

import com.bdv.infi.dao.UnidadInversionDAO;

import megasoft.*;

public class PickUnidadInversion extends AbstractModel
{
	/**
	 * Ejecuta la transaccion del modelo 
	 */
	public void execute() throws Exception
	{	
		String nb_nombre_unidad="";
		String filtro="";
		UnidadInversionDAO unidad=new UnidadInversionDAO(_dso);
		
		if(_req.getParameter("filtro")!=null && !_req.getParameter("filtro").equals("")){
			nb_nombre_unidad=_req.getParameter("filtro");
			filtro= " and upper(undinv_nombre) like upper('%" + nb_nombre_unidad + "%')";
		}
		
		unidad.listarUnidadPicklist(filtro);
		storeDataSet("table",unidad.getDataSet());
	}
	
	/*public boolean isValid() throws Exception
	{
		String nombre=_req.getParameter("filtro");
		int longitud=0;
		if(_req.getParameter("filtro")!=null)
			longitud=nombre.length();
		boolean flag = super.isValid();

		if (flag)
		{
			if (longitud==1 || longitud==2 || longitud==0)
			{ 
				_record.addError("Nombre Unidad Inversi&oacute;n", "Debe ingresar por lo menos 3 car&aacute;cteres");
				flag=false;
			}
	}
		return flag;
	}*/
	
}