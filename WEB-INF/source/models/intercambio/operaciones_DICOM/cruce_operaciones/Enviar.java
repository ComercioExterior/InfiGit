package models.intercambio.operaciones_DICOM.cruce_operaciones;

import java.util.HashMap;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_operaciones_DICOM.RecepcionCruceOperacionesDICOM;

public class Enviar extends MSCModelExtend {

	private ParametrosDAO parametrosDAO;
	private String usuario;
	private DataSet unidadesInv;
	private String TRAN="";
	public String monedaSubasta="";
	public String cod_moneda="";
	int monedaInt;
	int constatemoneda;
	protected HashMap<String, String> parametrosRecepcionDICOM;
	public void execute() throws Exception {
	
		monedaSubasta=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);//nm36635
		monedaInt= Integer.parseInt(monedaSubasta.trim());
		constatemoneda= Integer.parseInt(ConstantesGenerales.CODIGO_MONEDA_ISO_USD_DICOM);
		cod_moneda=parametrosRecepcionDICOM.get(monedaSubasta);
		//TRAN = TransaccionNegocio.PROC_RECEP_CRUCE_DICOM+monedaSubasta;
		if(monedaInt==constatemoneda){
		TRAN = TransaccionNegocio.CICLO_BATCH_DICOM;
		}else{
		TRAN =TransaccionNegocio.CICLO_BATCH_DICOM+cod_moneda;
	    }
		System.out.println("TRAN--->"+TRAN);
		
		parametrosDAO = new ParametrosDAO(_dso);	
		usuario = parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES, _dso);
		unidadesInv=getSessionDataSet("unidades_inversion");
		
		RecepcionCruceOperacionesDICOM recepcionCruceOperaciones=new RecepcionCruceOperacionesDICOM(unidadesInv,usuario,_dso);
		Thread t = new Thread(recepcionCruceOperaciones);
		t.start();
		t.join();
				
	}
	
	
	public boolean isValid()throws Exception{
		boolean valido=true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		
		procesosDAO.listarPorTransaccionActiva(TRAN);
				if (procesosDAO.getDataSet().count() > 0) {
					_record
							.addError(
									"Cruce DICOM ",
									"No se puede procesar la solicitud porque otra "
											+ "persona realizó esta acción y esta actualmente activa");
					valido = false;
		
				}
		return valido;
	}
}
		
