package models.help;

//import armada.mantenimiento.MSCModelExtend;
import megasoft.*;

/**
 * Carga la pantalla para la ayuda en linea.
 * @author Megasoft Computaci&oacute;n
 */

public class Index extends AbstractModel
{
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		//Armar dataSet para mantener indicador de grupo del elemento
		DataSet _dsParam = new DataSet();
		_dsParam.append("cod_help",java.sql.Types.VARCHAR);
		_dsParam.addNew();
		//_dsParam.setValue("cod_help", reqGetParameter("cod_help"));
		_dsParam.setValue("cod_help", _req.getParameter("cod_help"));
		

		//Guardar DataSet
		storeDataSet("param", _dsParam);
		
	}

}
