package models.configuracion.generales.tipo_bloqueo;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoBloqueoDAO;
/*
 *Clase encargada de agregar un tipo de bloqueo de Titulos.Se verifica si existe un tipo de bloqueo de titulo con Bloqueo por financiamiento.
 *Se verifica si existe un tipo de bloqueo de titulo con Aprobacion de Financiamiento
 *
 */
public class TipoBloqueoAddnew extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		/*
		 * 
		 */
		TipoBloqueoDAO confiD       = new TipoBloqueoDAO(_dso);
	// DataSet Manuales
		/*DataSet _data				= new DataSet();
		_data.append("existe",java.sql.Types.VARCHAR);
		 DataSet _datos				=new DataSet();
		 _datos.append("aprobacion", java.sql.Types.VARCHAR);*/
		/*
		 * 
		 */
		/*confiD.verificar();
	
		//
		if(confiD.getDataSet().count()>0)
		{
			_data.addNew();
			_data.setValue("existe", "1");
		}//fin del if
		else{
			_data.addNew();
			_data.setValue("existe", "0");
		}//fin del else
		
		confiD.verificarAprobacionFinanciamiento();
		 if (confiD.getDataSet().count()>0)
		 {
			 _datos.addNew();
				_datos.setValue("aprobacion", "1"); 
		 }//fin del if
		 else
		 {
		_datos.addNew();
		_datos.setValue("aprobacion", "0");
		 }//fin del else
		/*
		 * 
		 */
		 //storeDataSet("aprobacion", _datos);
		//storeDataSet("existe", _data);	*/
		storeDataSet("status",confiD.status());
		//storeDataSet("indicador",confiD.indicador());
		
	}//
}//

