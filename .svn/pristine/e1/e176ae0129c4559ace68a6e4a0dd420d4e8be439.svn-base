package models.eventos.generacion_amortizacion.browse;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaDAO;

/**
 * Lista los titulos que se van a amortizar 
 *
 */
public class Browse extends MSCModelExtend 
{
	
	public void execute() throws Exception 
	{

		CustodiaDAO custodiaDAO= new CustodiaDAO(_dso);
		String boton="";
		custodiaDAO.listarTitulosAmortizar();
		//public el dataset
		storeDataSet("table", custodiaDAO.getDataSet());
		
		//Si el dataset trae datos se muestra el boton procesar si viene vacio se oculta el boton
		if(custodiaDAO.getDataSet().count()>0)
		{
			custodiaDAO.getDataSet().first();
			custodiaDAO.getDataSet().next();
			boton="<button TYPE='submit'>Procesar</button>";
			_record.setValue("_boton", boton);
		}//fin del if
		else
			{
				_record.setValue("_boton", "");
			}//fin del else
		//publica el dataset
		storeDataSet("record",_record);
	}//fin del execute
}//fin de la clase
