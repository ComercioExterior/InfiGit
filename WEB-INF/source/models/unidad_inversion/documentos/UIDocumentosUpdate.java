package models.unidad_inversion.documentos;

import java.text.DateFormat;
import java.util.Date;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.TipoCartaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

import megasoft.AbstractModel;
import megasoft.DataSet;

public class UIDocumentosUpdate extends AbstractModel implements UnidadInversionConstantes{
	
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	DataSet cartas = new DataSet();
	String strIdUnidadInversion = null;
	
	public void execute() throws Exception {

		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);
		DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);
		DocumentoDefinicion documentoDefinicion = new DocumentoDefinicion();
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		String user=confiD.idUserSession(getUserName());

		String idblotter = _record.getValue("id_blotter");
		String tipoPersona = _record.getValue("id_persona");
		
		
		String blotter =getSessionObject("bloter").toString();
		String unidad =getSessionObject("unidad").toString();
		
		cartas.first();
		while (cartas.next()){
			String radioButton = "doc"+cartas.getValue("tipcar_id");
			if(_record.getValue(radioButton)!=null){
				int valueRadio = Integer.parseInt(_record.getValue(radioButton));
				if(valueRadio==1){
					String tempfile = "archivo"+cartas.getValue("tipcar_id")+".tempfile";
					String namefile = "archivo"+cartas.getValue("tipcar_id")+".filename";
					String idDocumento = "id_documento"+cartas.getValue("tipcar_id");
		
					documentoDefinicion.setDocumentoId(Integer.parseInt(_record.getValue(idDocumento)));
					documentoDefinicion.setRutaDocumento(_record.getValue(tempfile));
					documentoDefinicion.setIdUnidadInversion(Integer.parseInt(String.valueOf(idUnidadInversion)));
					documentoDefinicion.setTipoPersona(tipoPersona);
					documentoDefinicion.setNombreDoc(_record.getValue(namefile));
					documentoDefinicion.setModificarDocumento(Short.parseShort(_record.getValue(radioButton)));
					documentoDefinicion.setStatusDocumento(ConstantesGenerales.STATUS_REGISTRADO);
					documentoDefinicion.setAproUsuarioUserid(user);
					if((cartas.getValue("tipcar_id").equals("1"))||(cartas.getValue("tipcar_id").equals("2"))||(cartas.getValue("tipcar_id").equals("8"))){
						documentoDefinicion.setTransaId(TransaccionNegocio.TOMA_DE_ORDEN);
					}
					if((cartas.getValue("tipcar_id").equals("3"))||(cartas.getValue("tipcar_id").equals("4"))||(cartas.getValue("tipcar_id").equals("5"))||(cartas.getValue("tipcar_id").equals("6"))||(cartas.getValue("tipcar_id").equals("7"))){
						documentoDefinicion.setTransaId(TransaccionNegocio.ADJUDICACION);
					}
					
			    	Date fechaActual = new Date();
			   		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			        Date fecha = df.parse(df.format(fechaActual));
			 
					documentoDefinicion.setAproFecha(fecha);
							
					confiD.modificar(documentoDefinicion);
					
					//Actualizamos estatus de documentos Registrados
									
					documentoDefinicion.setIdUnidadInversion(Integer.parseInt(strIdUnidadInversion));
					documentoDefinicion.setStatusDocumento(ConstantesGenerales.STATUS_REGISTRADO);
					documentoDefinicion.setCreFecha(fechaActual);
					documentoDefinicion.setCreUsuarioUserid(user);
					documentoDefinicion.setDocumentoId(Integer.parseInt(_record.getValue(idDocumento)));
					
					confiD.modificarStatusDoc(documentoDefinicion);
					
				}
			}
		}
		
		//Si venimos del modulo de modificacion de unidad de inversion debemos cambiar el estatus de la unidad 
		//a Registrada o en inicio para que vuelva a ser publicada ya que se cambio informacion
		String accion= getSessionObject("accion").toString();
		if (Integer.parseInt(accion)==4){
			boolean asociaciones = boUI.dataCompleta(idUnidadInversion);
			if (!asociaciones) {
				boUI.modificarStatus(idUnidadInversion, UISTATUS_INICIO);
			}else{
				boUI.modificarStatus(idUnidadInversion, UISTATUS_REGISTRADA);
			}
		}
		_config.nextAction="unidad_inversion_documentos-personas_edit?id_blotter="+idblotter+"&blotter="+blotter+"&unidad="+unidad;
		_req.getSession().removeAttribute("bloter");
		_req.getSession().removeAttribute("unidad");
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		TipoCartaDAO tiCarta = new TipoCartaDAO(_dso);
		UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
		
		strIdUnidadInversion=(String)_req.getSession().getAttribute("idUnidadInversion");
		
		//Listamos lo unidad de inversion para saber si es inventario o subasta
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);
		unidadDAO.listarPorId(idUnidadInversion);
		DataSet instrumento = unidadDAO.getDataSet();
		instrumento.first();
		instrumento.next();
		String idsCartas = null;
		if ((instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_INVENTARIO))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO))){
			idsCartas = "1,2";
		}
		//if ((instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SITME))){
		if ((instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA))){	
			idsCartas = "1,2,3,4,5,6,7";
			if (instrumento.getValue("insfin_descripcion").equals(ConstantesGenerales.INST_TIPO_SITME)){
				idsCartas += ",8";
			}			
		}
		tiCarta.listarPorTipoCartas(idsCartas);
		cartas = tiCarta.getDataSet();
		while (cartas.next()){
			String radioButton = "doc"+cartas.getValue("tipcar_id");			
			if(_record.getValue(radioButton)!=null){
				int valueRadio = Integer.parseInt(_record.getValue(radioButton));
				if(valueRadio==1){
					String documento = "archivo"+cartas.getValue("tipcar_id")+".filename";
					String nombreDocumento = _record.getValue(documento);
					if(!nombreDocumento.endsWith(ConstantesGenerales.EXTENSION_DOC_HTM) && !nombreDocumento.endsWith(ConstantesGenerales.EXTENSION_DOC_HTML)){
						_record.addError("Documento","La extension del archivo que intenta ingresar es incorrecta. Verifique que sea "+ConstantesGenerales.EXTENSION_DOC_HTM+" &oacute; "+ConstantesGenerales.EXTENSION_DOC_HTML+" e intente de nuevo");
						flag = false;
						break;
					}
					if(nombreDocumento.length()>80){
						_record.addError("Error","El nombre del documento es mayor a 80 Caracteres");
						flag = false;
						break;
					}
				}
			}
		}
		return flag;		
	}
	
}
