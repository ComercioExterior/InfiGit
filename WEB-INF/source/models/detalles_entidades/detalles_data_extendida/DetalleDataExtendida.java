/**
 * 
 */
package models.detalles_entidades.detalles_data_extendida;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.InstruccionesPagoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TipoBloqueoDAO;
import com.bdv.infi.logic.interfaces.DataExtendida;

/**
 * @author eel
 *
 */
public class DetalleDataExtendida extends MSCModelExtend{

	public void execute() throws Exception
	{
		DataSet dataAux = new DataSet();
		DataSet dataExtendidaMostrar = new DataSet();
		 OrdenDAO ordenDetalleDataExtendida = new OrdenDAO(_dso);
		 String orden=_req.getParameter("ord_id");		 
		 DataSet _ordene_id=new DataSet();
		 
			_ordene_id.append("ordene_id",java.sql.Types.VARCHAR);
			_ordene_id.addNew();
			_ordene_id.setValue("ordene_id",_req.getParameter("ord_id"));
			storeDataSet( "ordene_id", _ordene_id);
		 int orden1=Integer.parseInt(orden);	
		 //Crear dataset final para la data extendida a mostrar
		 dataExtendidaMostrar.append("ordene_id",java.sql.Types.VARCHAR);
		 dataExtendidaMostrar.append("dtaext_id",java.sql.Types.VARCHAR);
		 dataExtendidaMostrar.append("dtaext_valor",java.sql.Types.VARCHAR);
		 dataExtendidaMostrar.append("dtaext_descripcion",java.sql.Types.VARCHAR);
		 
		 //Obtener detalles de la orden
		 ordenDetalleDataExtendida.listarRegistrosDataExtendida(orden1);
		 
		 dataAux = ordenDetalleDataExtendida.getDataSet();//guardar en dataset auxiliar
		 
		 //Sustituir los valores de data extendida de tipo Id por su respectiva descripción dependiendo del tipo de data extentida
		 while(dataAux.next()){
			 
			 if(dataAux.getValue("DTAEXT_VALOR")==null || (dataAux.getValue("DTAEXT_VALOR")!=null && (dataAux.getValue("DTAEXT_VALOR").equals("null") ||dataAux.getValue("DTAEXT_VALOR").equals("0")))){
				 dataAux.setValue("DTAEXT_VALOR", "");
			 }
			 
			 //Si la data extendida es una instruccion de Pago
			 if(dataAux.getValue("DTAEXT_ID")!=null && dataAux.getValue("DTAEXT_ID").equalsIgnoreCase(DataExtendida.TIPO_INSTRUCCION_PAGO)){
				 InstruccionesPagoDAO instruccionesPagoDAO = new InstruccionesPagoDAO(_dso);
				 instruccionesPagoDAO.listar(dataAux.getValue("DTAEXT_VALOR"));
				
				 if(instruccionesPagoDAO.getDataSet().next())//guardar descripcion de tipo de instruccion
					 dataAux.setValue("DTAEXT_VALOR", instruccionesPagoDAO.getDataSet().getValue("INSTRUCCION_NOMBRE"));
			 }
			 
			 //Si la data extendida es un Tipo de Bloqueo
			 if(dataAux.getValue("DTAEXT_ID")!=null && dataAux.getValue("DTAEXT_ID").equalsIgnoreCase(DataExtendida.TIPO_BLOQUEO)){
				 TipoBloqueoDAO tipoBloqueoDAO = new TipoBloqueoDAO(_dso);
				 tipoBloqueoDAO.listarPorId(dataAux.getValue("DTAEXT_VALOR"));
				
				 if(tipoBloqueoDAO.getDataSet().next())//Guardar descripcion de bloqueo
					 dataAux.setValue("DTAEXT_VALOR", tipoBloqueoDAO.getDataSet().getValue("tipblo_descripcion"));
			 }
			 
			 //Si la data extendida es de informacion de comisiones: NO MOSTRAR,
			 //NO AGREGAR A DATASET FINAL A MOSTRAR
			 if(dataAux.getValue("DTAEXT_ID")!=null && !dataAux.getValue("DTAEXT_ID").equalsIgnoreCase(DataExtendida.ID_COMISION_UI)){
				 dataExtendidaMostrar.addNew();
				 dataExtendidaMostrar.setValue("ordene_id",dataAux.getValue("ordene_id"));
				 dataExtendidaMostrar.setValue("dtaext_id",dataAux.getValue("dtaext_id"));
				 dataExtendidaMostrar.setValue("dtaext_valor",dataAux.getValue("dtaext_valor"));
				 dataExtendidaMostrar.setValue("dtaext_descripcion",dataAux.getValue("dtaext_descripcion"));
			 }

		 }
		 
		 
		//Exportar dataset con datos recuperados
		storeDataSet( "detalle_data_extendida", dataExtendidaMostrar);		
				
	}
}
