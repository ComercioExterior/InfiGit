package models.configuracion.generales.campos_dinamicos;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ListasDinamicas;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.GrupoParametros;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class Delete extends MSCModelExtend {

	private String idCampoDinamico;
	private String idGrupoParametro;
	private String nombreCampoDinamico;
	private CamposDinamicos camposDinamicos;
	private ParametrosDAO parametrosDAO;
	private String tipoCampoDinamico;
	private GrupoParametros grupoParametros;
	private com.bdv.infi.dao.Transaccion transaccion;
	private Logger logger = Logger.getLogger(Delete.class);
	private String tipoCampo;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ArrayList<String> sqls=new ArrayList<String>();
		parametrosDAO=new ParametrosDAO(_dso);
		
		CamposDinamicos confiD = new CamposDinamicos(_dso);
		ListasDinamicas listasD = new ListasDinamicas(_dso);
		CampoDinamico campoDinamico = new CampoDinamico();		
		//String sql ="";
		
		campoDinamico.setIdCampo(Integer.parseInt(_req.getParameter("campo_id")));
		//MODIFICACION DE CODIGO PARA REQUERIMIENTO TTS_437 - NM26659
		confiD.listarPorId(String.valueOf(campoDinamico.getIdCampo()));
		confiD.getDataSet().first();
		confiD.getDataSet().next();		
		tipoCampo=confiD.getDataSet().getValue("campo_tipo");		
		if(tipoCampo!=null && tipoCampo.equals(CamposDinamicosConstantes.TIPO_LISTA_DINAMICA)){
			sqls.add(listasD.eliminar(campoDinamico));
		}
		
		sqls.add(confiD.eliminar(campoDinamico));
		
		//sql=confiD.eliminar(campoDinamico);
		camposDinamicos=new CamposDinamicos(_dso);
		camposDinamicos.listarPorId(idCampoDinamico);		
		if(camposDinamicos.getDataSet().count()>0){
			camposDinamicos.getDataSet().first();
			camposDinamicos.getDataSet().next();			
			nombreCampoDinamico=camposDinamicos.getDataSet().getValue("campo_nombre");			
			tipoCampoDinamico=camposDinamicos.getDataSet().getValue("campo_tipo");
		}
		
		if(tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO) 
				|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA)
				|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR) 
				|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR)){
			
			parametrosDAO.buscarPorGrupoNombreParametro(ParametrosSistema.FECHAS_CAMPOS_DINAMICOS,nombreCampoDinamico);			
			if(parametrosDAO.getDataSet().count()>0){
				parametrosDAO.getDataSet().first();
				parametrosDAO.getDataSet().next();
				idGrupoParametro=parametrosDAO.getDataSet().getValue("pargrp_id");				
				grupoParametros=new GrupoParametros();
				grupoParametros.setIdParametro(idGrupoParametro);				
				grupoParametros.setNombreParametro(nombreCampoDinamico);
			}			
			sqls.add(parametrosDAO.eliminarParametro(grupoParametros));
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
			logger.error("Ha ocurrido un error de tipo SQLException en el proceso de Eliminacion del campo Dinamico " + nombreCampoDinamico +"  Error: " + sqlEx.getMessage());
			System.out.println("Ha ocurrido un error de tipo SQLException en el proceso de Eliminacion del campo Dinamico " + nombreCampoDinamico +"  Error: " + sqlEx.getMessage());
		}catch (Exception ex){
			transaccion.getConnection().rollback();
			logger.error("Ha ocurrido un error de Inesperado en el proceso de Eliminacion del campo Dinamico " + nombreCampoDinamico +"  Error: " + ex.getMessage());
			System.out.println("Ha ocurrido un error de Inesperado en el proceso de Eliminacion del campo Dinamico " + nombreCampoDinamico +"  Error: " + ex.getMessage());
		}finally{
			if(transaccion.getConnection()!=null){							
				transaccion.closeConnection();
			}
		}
		//db.exec(_dso, sql);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		CamposDinamicos confiD = new CamposDinamicos(_dso);
		CampoDinamico campoDinamico = new CampoDinamico();
		idCampoDinamico=_req.getParameter("campo_id");

		campoDinamico.setIdCampo(Integer.parseInt(_req.getParameter("campo_id")));		
		confiD.verificar(campoDinamico);
		
		if (confiD.getDataSet().count()>0){
			_record.addError("Campo Din&aacute;mico","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
			flag = false;
		}else{
			confiD.verificar1(campoDinamico);
			if (confiD.getDataSet().count()>0){
				_record.addError("Campo Din&aacute;mico","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
				flag = false;
			}
		}
		if( _req.getParameter("desc_campo_tipo").equals("Lista Dinamica") && flag){
			
			confiD.verificarUnidadPublicada(Integer.parseInt(_req.getParameter("campo_id")));
			
			System.out.println("cantidad: "+confiD.getDataSet().count());
			if (confiD.getDataSet().count()>0){
				_record.addError("Campo Din&aacute;mico","No se puede eliminar el Registro. Ya que sta relacionado a una unidad de inversi&oacute;n que esta publicada.");
				flag = false;
			}
		}
		return flag;
	}
}