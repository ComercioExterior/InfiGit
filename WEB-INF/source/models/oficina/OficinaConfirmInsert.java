package models.oficina;

import com.bdv.infi.dao.OficinaDAO;

import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que confirma si se desea ejecutar la transacción, publica el dataset del request
 * @author elaucho
 */
public class OficinaConfirmInsert extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//Se publica el DataSet del Request
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
			if(oficinaDAO.verificarOficina(_req.getParameter("oficina")))
			{
				_record.addError("Oficina ","El registro que intenta ingresar ya existe.");
				flag = false;
			}
		}
		return flag;
	}

}
