package models.unidad_inversion.publicacion;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.bcv.alto_valor.ConsultasBCVAltoValor;

import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

public class Confirm extends AbstractModel {
	
	private TitulosDAO titulosDAO;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		//crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
	}
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		titulosDAO=new TitulosDAO(_dso);
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		Long idUnidadInversion = Long.parseLong(_req.getParameter("undinv_id"));
		String jornadaActiva=null;
		
		//si la unidad no posee todas las asociaciones no podemos publicar e informar del problema
		boolean asociaciones = boUI.dataCompleta(idUnidadInversion);
		if (!asociaciones) {
			_record.addError("Para su infomacion", "Debe registrar todos los datos necesarios, verifique la informacion mostrada en la seccion de Detalles");
			flag = false;
		}
		boUI.listarPorId(Long.parseLong(_req.getParameter("undinv_id")));
	
		String user="";
		String tipoProducto="";
		String nroJornada=null;
		if (boUI.getDataSet().count()>0){
			boUI.getDataSet().first();
			boUI.getDataSet().next();
			user = boUI.getDataSet().getValue("UPD_USUARIO_USERID");
			tipoProducto = boUI.getDataSet().getValue("TIPO_PRODUCTO_ID");
			nroJornada = boUI.getDataSet().getValue("NRO_JORNADA");
		}
		if(user.equals(getUserName())){
			_record.addError("Para su infomacion", "No es posible que el usuario "+getUserName()+" publique una Unidad de Inversion creada por el mismo");
			flag = false;
		}
		
		long unidadInversionID=Long.parseLong(_req.getParameter("undinv_id"));
		titulosDAO.listarVencimientoTituloPorUI(unidadInversionID);
				
		if(titulosDAO.getDataSet().next()){		
			
			String vencimientoTitulo=titulosDAO.getDataSet().getValue("MDATE");			
			String fechaLiquidacion=titulosDAO.getDataSet().getValue("UNDINV_FE_LIQUIDACION");
			//System.out.println("Fecha vencimiento Titulo " + vencimientoTitulo);
			//System.out.println("Fecha liquidacion UI " + fechaLiquidacion);
			if(fechaLiquidacion!=null&&(Utilitario.StringToDate(fechaLiquidacion,"dd-MM-yyyy")).compareTo(Utilitario.StringToDate(vencimientoTitulo, "dd-MM-yyyy"))>0){				
				_record.addError("Para su informaci&oacuten","No se puede realizar el proceso de publicaci&oacuten debido a que la fecha de liquidaci&oacuten de la unidad de inversi&oacuten es mayor a la fecha de vencimiento del titulo ("+vencimientoTitulo+") por favor corriga la fecha de liquidaci&oacuten de la Unidad de Inversi&oacuten");
				flag=false;
			}
		}

		//NM25287 25/03/2015 CONSULTAR ID DE JORNADA SIMADI. TTS-491 WS ALTO VALOR
		/*if(flag&&(nroJornada==null||nroJornada.length()==0)&&(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)||tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL))){
			try {
				ConsultasBCVAltoValor consultaWSAltoValor= new ConsultasBCVAltoValor(_dso);
				jornadaActiva = consultaWSAltoValor.consultarIdJornada();
				if(jornadaActiva==null){
					_record.addError("Para su informaci&oacuten","No existe jornada activa en el BCV para SIMADI o existe un error de comunicación vía Web Service");
					flag=false;
				}else{
					//REGISTRAR JORNADA ACTIVA
					boUI.actualizarIdJornada(unidadInversionID,jornadaActiva);
				}
			} catch (Exception e) {
				if(jornadaActiva==null){
					_record.addError("Para su informaci&oacuten","No existe jornada activa en el BCV para SIMADI o existe un error de comunicación vía Web Service "+e.getMessage());
					flag=false;
				}
			}
			
		}*/
				
		
		//Cogido comentado en requerimiento TTS_465 27/06/2014 NM26659		
		//VALIDAR QUE EXISTA SOLO UNA UNIDAD DE INVERSION ACTIVA PARA EL TIPO DE PRODUCTO TTS-401. NM25287
		//Modificado por NM25287. Inclusión de tipos de producto SICAD2PER y SICAD2RED 20/03/2014
		/*if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)||tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL))
		if(boUI.cantidadUnidadesPorTipoProdEstatus(unidadInversionID,tipoProducto,UnidadInversionConstantes.UISTATUS_PUBLICADA)>=1){
			_record.addError("Para su informacion","Debe existir solo una Unidad de Inversion asociada al tipo de producto "+tipoProducto);
			flag = false;
		}*/
		return flag;
	}
	
	
}


