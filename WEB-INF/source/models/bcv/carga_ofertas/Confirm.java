package models.bcv.carga_ofertas;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Confirm extends MSCModelExtend {

	public void execute() throws Exception {
		String fecha=(String) _record.getValue("fechaHoy"); 
		String origen=(String) _record.getValue("origen"); 
		String unidadInvId =_record.getValue("undinv_id");  
		String nroJornada=null;
		String mensaje="";
		String mostrarBtnProcesar="";
		
		DataSet confirm= new DataSet();
		confirm.append("mostrar_btn_procesar", java.sql.Types.VARCHAR);
		confirm.append("mensaje", java.sql.Types.VARCHAR);
		confirm.append("nro_jornada", java.sql.Types.VARCHAR);
		confirm.addNew();	
		
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		nroJornada=unidadInversionDAO.consultarJornada(Integer.parseInt(ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR),fecha, unidadInvId);
		
		if(nroJornada==null){
			mensaje="No existe jornada activa para la unidad de Inversion o la Unidad seleccionada no es Alto Valor";
			mostrarBtnProcesar="none";
		}else{
			mensaje="¿Desea cargar las ofertas asociadas a la jornada "+nroJornada+"?";
			mostrarBtnProcesar="block";
			
		}
		confirm.setValue("nro_jornada", nroJornada);
		confirm.setValue("mostrar_btn_procesar", mostrarBtnProcesar);
		confirm.setValue("mensaje", mensaje);

		System.out.println(_record);
		storeDataSet("configuracion", confirm);	
		storeDataSet("record", _record);		
	}

	
}
