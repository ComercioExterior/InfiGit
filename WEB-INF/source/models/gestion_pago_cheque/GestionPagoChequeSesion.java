package models.gestion_pago_cheque;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que monta en sesion los registros aun no registrados de cheques
 * @author elaucho
 */
public class GestionPagoChequeSesion extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
	/*
	 * variables a utilizar
	 */
		String nombreArray[] = _req.getParameterValues("nombre");
		String cedulaArray[] = _req.getParameterValues("cedula");
		String montoArray[]  = _req.getParameterValues("monto");
		DataSet _cheques     = new DataSet();
	//Columnas que contendra en el DataSet
		_cheques.append("nombre",java.sql.Types.VARCHAR);
		_cheques.append("cedula",java.sql.Types.VARCHAR);
		_cheques.append("monto",java.sql.Types.VARCHAR);
		
		if(nombreArray!=null){
			for(int i=0;i<nombreArray.length;i++){
				_cheques.addNew();
				_cheques.setValue("nombre",nombreArray[i]==null?"":nombreArray[i]);
				_cheques.setValue("cedula",cedulaArray[i]==null?"":cedulaArray[i]);
				_cheques.setValue("monto",montoArray[i]==null?"":montoArray[i]);
			}//fin for
	/*
	 * Se monta ens esion
	 */
			_req.getSession().setAttribute("infi.cheques.sesion",_cheques);
			
			_config.nextActionType="redirect";
			_config.nextAction="transferencias_internacionales";
		}//FIN IF
	}//FIN EXECUTE
}//FIN CLASE
