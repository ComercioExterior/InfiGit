
package models.custodia.transacciones.pago_cupones;
import com.bdv.infi.dao.PagoCuponesDao;
import models.msc_utilitys.MSCModelExtend;
/**
 *lista los titulos que se venceran y tambien los vencidos.
 *
 */
public class PagoCuponesBrowse extends MSCModelExtend
{
	public void execute() throws Exception 
	{

			PagoCuponesDao cuponesDao = new PagoCuponesDao(_dso);
			
			String boton="";
			cuponesDao.listarTitulosPorVencerse();
			//publica el Dataset
			storeDataSet("table", cuponesDao.getDataSet());
			
			cuponesDao.listarTitulosVencidos();	
			//publica el Dataset
			storeDataSet("table2", cuponesDao.getDataSet());
			
			//Si el dataset trae data se muestra el boton de procesar, si viene vacio no muestra el boton
			if(cuponesDao.getDataSet().count()>0)
			{
				cuponesDao.getDataSet().first();
				cuponesDao.getDataSet().next();
					boton="<button TYPE='submit'>Procesar</button> ";
				_record.setValue("boton", boton);
			}//fin del if
				else 
				{
					_record.setValue("boton", "");
				}//fin del else
			//publica el Dataset
			storeDataSet("record", _record);
	}//fin execute
}//fin de la clase


