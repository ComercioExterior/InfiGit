package models.generar_opics;

import java.text.SimpleDateFormat;
import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

import megasoft.DataSet;
import models.msc_utilitys.*;

/**
 * Lista los registros de la tabla INFI_TB_215_MENSAJE_OPICS según los parametros recibidos
 *@author elaucho
 */
public class GenerarDatosOpicsBrowse extends MSCModelExtend {
	
	private String mensajeOpics;
	private DataSet _filtro;
	public void execute() throws Exception {
		_filtro=new DataSet();
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(_dso);
		
		if(mensajeOpics.equals(ConstantesGenerales.MENSAJE_OPICS_OPERACION_CAMBIO)){			
			mensajeOpicsDAO.listarOperacionesCambio(Long.parseLong(_record.getValue("enviado")), formato.parse(_record.getValue("fecha_desde")),formato.parse(_record.getValue("fecha_hasta")),mensajeOpics);
			//System.out.println("GENERACION OPERACION CAMBIO");
		} else {
			//NM25287 SICAD 2. Modificación para registro de id_orden en Deal Opics
			//System.out.println("GENERACION DEALS OPICS");
			mensajeOpicsDAO.listarDealsRentaFija(Long.parseLong(_record.getValue("enviado")), formato.parse(_record.getValue("fecha_desde")),formato.parse(_record.getValue("fecha_hasta")),"RENTA_FIJA");
			//mensajeOpicsDAO.listar(Long.parseLong(_record.getValue("enviado")), formato.parse(_record.getValue("fecha_desde")),formato.parse(_record.getValue("fecha_hasta")));	
		}
		_filtro.append("tipo_mensaje",java.sql.Types.VARCHAR);
		_filtro.append("fecha_desde",java.sql.Types.VARCHAR);
		_filtro.append("fecha_hasta",java.sql.Types.VARCHAR);
		_filtro.addNew();
		_filtro.setValue("fecha_desde",_record.getValue("fecha_desde"));
		_filtro.setValue("fecha_hasta",_record.getValue("fecha_hasta"));
		_filtro.setValue("tipo_mensaje",_record.getValue("tipo_mensaje"));
		//Se publica el dataset
		storeDataSet("filtro", _filtro);
		storeDataSet("registros", mensajeOpicsDAO.getDataSet());
		storeDataSet("total", mensajeOpicsDAO.getTotalRegistros());
	}//fin execute

	public boolean isValid() throws Exception{
		boolean flag=true;
		
		if(_record.getValue("tipo_mensaje")==null || _record.getValue("tipo_mensaje").equals("")){			
			_record.addError("Tipo Mensaje", "Debe seleccionar un Tipo de mensaje");
			flag = false;
		}else {
			mensajeOpics=_record.getValue("tipo_mensaje");			
		}
		
		return flag;
	}
}