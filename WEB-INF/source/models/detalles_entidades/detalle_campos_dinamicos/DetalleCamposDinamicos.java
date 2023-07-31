/**
 * 
 */
package models.detalles_entidades.detalle_campos_dinamicos;

import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.TitulosDAO;
import megasoft.AbstractModel;
import megasoft.DataSet;

/**
 * Clase encargada de recuperar detalles de un t&iacute;tulo determinado. 
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class DetalleCamposDinamicos extends AbstractModel
{
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		CamposDinamicos camposDinamicos=new CamposDinamicos(_dso);
		//Obtener detalles del t&iacute;tulo

		DataSet _ordene_id=new DataSet();
		_ordene_id.append("ordene_id",java.sql.Types.VARCHAR);
		_ordene_id.addNew();
		_ordene_id.setValue("ordene_id",_req.getParameter("ord_id"));
		storeDataSet( "ordene_id", _ordene_id);
		
		//Campos Dinamicos
		DataSet dinamicos=camposDinamicos.listarCamposDinamicosOrdenes(Long.parseLong(_req.getParameter("ord_id")), 1);
		storeDataSet("dinamicos", dinamicos);
	}

}
