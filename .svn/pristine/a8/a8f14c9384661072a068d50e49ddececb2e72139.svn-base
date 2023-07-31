package models.oficina;

import com.bdv.infi.dao.OficinaDAO;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que confirma actualizar un registro de oficina
 * @author elaucho
 */
public class OficinaConfirmUpdate extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		//Se publica el dataset del request
		storeDataSet("request",getDataSetFromRequest());
		
	}//fin execute
	
	@Override
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		
		//DAO a utilizar
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
		
		if(flag)
		{
			//Se valida si la oficina ya se encuentra registrada en base de datos
			if(_req.getParameter("oficina").equals(null) || _req.getParameter("oficina").equals(""))
			{
				_record.addError("Oficina ","Es obligatorio para procesar el formulario");
				flag = false;
			}else
			{
				if(_req.getParameter("oficina").equals(_req.getParameter("oficina_anterior")))
				{
					//No se hace nada
				}//fin if
				else
				{
					//Si las oficinas son diferentes se hace la validacion
					if(!_req.getParameter("oficina").equals(_req.getParameter("oficina_anterior")))
					{
						//Se verifica si el registro que se intenta insertar existe en base de datos
						if(oficinaDAO.verificarOficina(_req.getParameter("oficina")))
						{
							_record.addError("Oficina ","El registro que intenta ingresar ya existe.");
							flag = false;
							
						}//fin if
					}//fin if
				}//fin else
				
			}//fin else
		}//fin if flag
		return flag;
	}//fin isValid
	
}//fin clase
