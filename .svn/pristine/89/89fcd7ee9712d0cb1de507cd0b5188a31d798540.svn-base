package models.unidad_inversion.documentos;

import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.GenericoDAO;
import com.bdv.infi.dao.TipoCartaDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.UIDocumentosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.data.UIDocumentos;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;

public class UIDocumentosInsert extends AbstractModel{
	
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
		UIDocumentosDAO uIDocumentosDAO =new UIDocumentosDAO(_dso);
		UIDocumentos uIDocumento = new UIDocumentos();
		
		String usuario=getSessionObject("framework.user.principal").toString();
		String user=confiD.idUserSession(usuario);

		String blotter = _record.getValue("id_blotter");
		String tipoPersona = _record.getValue("id_persona");
		cartas.first();
		while (cartas.next()){
			String tempfile = "archivo"+cartas.getValue("tipcar_id")+".tempfile";
			String namefile = "archivo"+cartas.getValue("tipcar_id")+".filename";
			if((_record.getValue(tempfile)!=null) || (_record.getValue(namefile)!=null)){
				String documento_id = GenericoDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_TRANSACCION_DOC);
				documentoDefinicion.setDocumentoId(Integer.parseInt(documento_id));
				documentoDefinicion.setCreUsuarioUserid(user);
				documentoDefinicion.setIdUnidadInversion(Integer.parseInt(strIdUnidadInversion));
				documentoDefinicion.setTipoPersona(tipoPersona);
				documentoDefinicion.setRutaDocumento(_record.getValue(tempfile));
				documentoDefinicion.setNombreDoc(_record.getValue(namefile));
				documentoDefinicion.setStatusDocumento(ConstantesGenerales.STATUS_REGISTRADO);
				if((cartas.getValue("tipcar_id").equals("1"))||(cartas.getValue("tipcar_id").equals("2"))||(cartas.getValue("tipcar_id").equals("8"))){
					documentoDefinicion.setTransaId(TransaccionNegocio.TOMA_DE_ORDEN);
				}
				if((cartas.getValue("tipcar_id").equals("3"))||(cartas.getValue("tipcar_id").equals("4"))||(cartas.getValue("tipcar_id").equals("5"))||(cartas.getValue("tipcar_id").equals("6"))||(cartas.getValue("tipcar_id").equals("7"))){
					documentoDefinicion.setTransaId(TransaccionNegocio.ADJUDICACION);
				}
				confiD.insertar(documentoDefinicion);
				
				String idUnico = GenericoDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_UI_DOCUMENTOS);
				
				uIDocumento.setIdUnidadInversion(idUnidadInversion);
				uIDocumento.setIdTipoCarta(Integer.parseInt(cartas.getValue("tipcar_id")));
				uIDocumento.setIdTipoPersona(tipoPersona);
				uIDocumento.setIdBloter(blotter);
				uIDocumento.setIdDocumento(Integer.parseInt(documento_id));
				uIDocumento.setIdUnico(Integer.parseInt(idUnico));
				String query = uIDocumentosDAO.insertarUIDoc(uIDocumento);
				db.exec(_dso,query);
			}
		}
		
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
		if ((instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA) ||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)))){
			idsCartas = "1,2,3,4,5,6,7";
			if (instrumento.getValue("insfin_descripcion").equals(ConstantesGenerales.INST_TIPO_SITME)){
				idsCartas += ",8";
			}
		}
		tiCarta.listarPorTipoCartas(idsCartas);
		cartas = tiCarta.getDataSet();
		while (cartas.next()){
			String documento = "archivo"+cartas.getValue("tipcar_id")+".filename";
			String nombreDocumento = _record.getValue(documento);
			if(nombreDocumento!=null){
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
		return flag;		
	}
	
}