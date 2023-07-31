package models.detalles_entidades.detalle_plantillas;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.Orden;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;


public class DetallesPlantillas extends MSCModelExtend {
	
	private DataSet _datos;	
	public void execute() throws Exception
	{
		_datos = new DataSet();
		_datos.append("mensaje_error", java.sql.Types.VARCHAR);
		_datos.addNew();		
		_datos.setValue("mensaje_error", "");
		
		if(_req.getSession().getAttribute("mensaje_error")!=null){
			String mensajeError = (String) _req.getSession().getAttribute("mensaje_error");		
			_config.template = "toma_orden-error.htm";
			_datos.setValue("mensaje_error", mensajeError);
			
			storeDataSet("documentos", new DataSet());
			storeDataSet( "ordene_id", new DataSet());
			
			_req.getSession().removeAttribute("mensaje_error");
			
		}else{

				OrdenDAO orden=new OrdenDAO(_dso);
				if(_req.getParameter("ord_id")!=null){//viene de consulta
					orden.listarDocumentosOrden(Long.parseLong(_req.getParameter("ord_id")));
		//			Se publica ordene id
					DataSet _ordene_id=new DataSet();
					_ordene_id.append("ordene_id",java.sql.Types.VARCHAR);
					_ordene_id.addNew();
					_ordene_id.setValue("ordene_id",_req.getParameter("ord_id"));
					storeDataSet( "ordene_id", _ordene_id);
					storeDataSet("documentos", orden.getDataSet());
				}else{//viene de la generacion de una orden nueva
					Orden ordenSesion=(Orden)_req.getSession().getAttribute("OrdenDocumentos");
					orden.listarDocumentosOrden(ordenSesion.getIdOrden());
		//			Se publica ordene id
					DataSet _ordene_id=new DataSet();
					_ordene_id.append("ordene_id",java.sql.Types.VARCHAR);
					_ordene_id.addNew();
					_ordene_id.setValue("ordene_id",String.valueOf(ordenSesion.getIdOrden()));
					storeDataSet( "ordene_id", _ordene_id);
					storeDataSet("documentos", orden.getDataSet());				
				}
		
		}		
		
		storeDataSet("datos", _datos);		
	}
}
