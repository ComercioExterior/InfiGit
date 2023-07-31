package models.configuracion.generales.campos_dinamicos;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class Edit extends MSCModelExtend {
	
	private String tipoCampoDinamico;
	private String campo_nombre;
	
	private DataSet _dinamicosAdicionales;
	private CamposDinamicos confiD;
	String idCampo=null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		 
		_dinamicosAdicionales=new DataSet();
		
		
	
		if(_req.getParameter("campo_id")!=null){
			idCampo = _req.getParameter("campo_id");
		}
		
		//Realizar consulta
		confiD.listarPorId(idCampo);
		
		if(confiD.getDataSet().count()>0){
			System.out.println("Campo dinamico true");
			confiD.getDataSet().first();
			confiD.getDataSet().next();
			tipoCampoDinamico=confiD.getDataSet().getValue("campo_tipo");	
			campo_nombre=confiD.getDataSet().getValue("campo_nombre");
			//System.out.println("tipoCampoDinamico -----------------> " + tipoCampoDinamico);
			//System.out.println("campo_nombre ----------------------> " + campo_nombre);
		}
		
		_dinamicosAdicionales.addNew();
		_dinamicosAdicionales.append("tipo_campo",java.sql.Types.VARCHAR);
		_dinamicosAdicionales.setValue("tipo_campo",tipoCampoDinamico);
		
						
		if(tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO) 
				|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA)
				|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR) 
				|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR)){
			
			ParametrosDAO   param=new ParametrosDAO(_dso);		
			param.buscarPorGrupoNombreParametro(ParametrosSistema.FECHAS_CAMPOS_DINAMICOS,campo_nombre);			
			if(param.getDataSet().count()>0){
				param.getDataSet().first();
				param.getDataSet().next();								
				if(tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO)){				
					_dinamicosAdicionales.append("fecha_rango_1",java.sql.Types.VARCHAR);
					_dinamicosAdicionales.append("fecha_rango_2",java.sql.Types.VARCHAR);
					_dinamicosAdicionales.setValue("fecha_rango_1",param.getDataSet().getValue("PARTIP_VALOR_DEFECTO"));
					_dinamicosAdicionales.setValue("fecha_rango_2",param.getDataSet().getValue("PARVAL_VALOR"));
				}else {					
					_dinamicosAdicionales.append("fecha_1",java.sql.Types.VARCHAR);
					_dinamicosAdicionales.setValue("fecha_1",param.getDataSet().getValue("PARVAL_VALOR"));
				}
			}
		}
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datosDinamicos", _dinamicosAdicionales);
		storeDataSet("tipo",confiD.tipo());
	}
	
	public boolean isValid() throws Exception {
		confiD=new CamposDinamicos(_dso);
		
		boolean flag=true;
		idCampo=_req.getParameter("campo_id");
		String tipoCampo=null;
						
			
		confiD.listarPorId(idCampo);		
		confiD.getDataSet().first();		
		confiD.getDataSet().next();			
		tipoCampo=confiD.getDataSet().getValue("campo_tipo");
		
		if(tipoCampo!=null && tipoCampo.equals(CamposDinamicosConstantes.TIPO_LISTA_DINAMICA)){
			_record.addError("Lista Dinamica"," Los campos de tipo Lista Dinamica no pueden ser modificados");
			flag=false;
		}
		
		
		return flag;
	}
}