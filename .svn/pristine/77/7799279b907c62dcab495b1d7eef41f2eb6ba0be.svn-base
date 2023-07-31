package models.intercambio.operaciones_DICOM.cruce_operaciones;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.UnidadInversionDAO;

public class Previo extends MSCModelExtend {
	static final Logger logger = Logger.getLogger(Previo.class);
	private String nroJornada;
	private String cod_moneda;
	UnidadInversionDAO unidadInvDAO;
	DataSet unidadesInversionProcesar;
	public void execute() throws Exception {
		
		//setSessionDataSet("unidades_inversion", unidadesInversionProcesar);
		//storeDataSet("unidades_inversion", unidadesInversionProcesar);
	}

	public boolean isValid() throws Exception {
		boolean valido = true;		
		nroJornada=_record.getValue("nro_jornada");
		cod_moneda=_record.getValue("cod_moneda");
				
		/*if(nroJornada==null||nroJornada.length()==0){
			_record.addError("Numero de Jornada", "Debe ingresar el numero de jornada " );
			valido = false;
		}*/
		// Comentado por Alexander Rincón NM11383 21/02/2018
		 /*else {
			unidadInvDAO=new UnidadInversionDAO(_dso);
			unidadInvDAO.listaUnidadInversionPorIdJornada(nroJornada);
			unidadesInversionProcesar=unidadInvDAO.getDataSet();
			if(unidadesInversionProcesar.count()==0){//No existen unidades de inversion asociadas a la jornada ingresada			
				_record.addError("No existen Unidades de Inversion para la Jornada", "Estimado usuario no existen Unidades de Inversion asociadas a la jornada que ha ingresado" );
				valido = false;
			}
			
		}*/
						
		return valido;
	}
}
