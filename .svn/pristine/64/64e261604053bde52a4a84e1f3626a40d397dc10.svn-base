package models.configuracion.generales.campos_dinamicos;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.GrupoParametros;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class Update extends MSCModelExtend {

	private String tipoCampoDinamico;
	private String idGrupoParametro;
	private ParametrosDAO parametrosDAO;
	private GrupoParametros grupoParametros;
	private com.bdv.infi.dao.Transaccion transaccion;
	private Logger logger = Logger.getLogger(Update.class);
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		CamposDinamicos confiD = new CamposDinamicos(_dso);
		CampoDinamico campoDinamico = new CampoDinamico();
		
		
		ArrayList<String> sqls=new ArrayList<String>();
		//String sql ="";
		
		tipoCampoDinamico=_req.getParameter("campo_tipo");
					
		campoDinamico.setDescripcion(_req.getParameter("campo_descripcion"));
		campoDinamico.setValor(_req.getParameter("campo_nombre"));
		campoDinamico.setTipoDato(Integer.parseInt(_req.getParameter("campo_tipo")));
		campoDinamico.setIdCampo(Integer.parseInt(_req.getParameter("campo_id")));
		
		if(tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO) 
				|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA)
				|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR) 
				|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR)){
			
			ParametrosDAO   param=new ParametrosDAO(_dso);
			param.buscarGrupoParametro(ParametrosSistema.FECHAS_CAMPOS_DINAMICOS);
			
			if(param.getDataSet().count()>0){
				param.getDataSet().first();
				param.getDataSet().next();
				idGrupoParametro=param.getDataSet().getValue("PARGRP_ID") ;
				
			}
			
			parametrosDAO=new ParametrosDAO(_dso);
			grupoParametros=new GrupoParametros();
			grupoParametros.setIdParametro(idGrupoParametro);
			grupoParametros.setNombreParametro(_req.getParameter("campo_nombre"));
			
			if(tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO)){				
				grupoParametros.setValorDefectoParametro(_req.getParameter("fecha_rango_1"));
				grupoParametros.setValorParametro(_req.getParameter("fecha_rango_2"));
			}else {				
				grupoParametros.setValorParametro(_req.getParameter("fecha_1"));				
			}
			sqls.add(parametrosDAO.modificar(grupoParametros));			
		}
		
		try {		
			transaccion = new com.bdv.infi.dao.Transaccion(_dso);
			transaccion.begin();
			Statement statement=transaccion.getConnection().createStatement();
							
			for (String element : sqls) {					
				statement.addBatch(element);					
			}
			
			statement.executeBatch();						
			statement.close();
			transaccion.getConnection().commit();
			
		}catch(SQLException sqlEx){
			transaccion.getConnection().rollback();
			logger.error("Ha ocurrido un error de tipo SQLException en el proceso de Actualizacion del campo Dinamico " + _req.getParameter("campo_nombre") +"  Error: " + sqlEx.getMessage());
			//System.out.println("Ha ocurrido un error de tipo SQLException en el proceso de Actualizacion del campo Dinamico " + _req.getParameter("campo_nombre") +"  Error: " + sqlEx.getMessage());
		}catch (Exception ex){
			transaccion.getConnection().rollback();
			logger.error("Ha ocurrido un error de Inesperado en el proceso de Actualizacion del campo Dinamico " + _req.getParameter("campo_nombre") +"  Error: " + ex.getMessage());
			//System.out.println("Ha ocurrido un error de Inesperado en el proceso de Actualizacion del campo Dinamico " + _req.getParameter("campo_nombre") +"  Error: " + ex.getMessage());
		}finally{
			if(transaccion.getConnection()!=null){							
				transaccion.closeConnection();
			}
		}
	}
}