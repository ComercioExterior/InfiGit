package models.unidad_inversion.documentos;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Util;

import com.bdv.infi.dao.TipoCartaDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.UIDocumentosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class UIDocumentosAddnew extends AbstractModel {

	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	private String unidad = null;
	private String idBloter = null;
	private String bloter = null;
	private String idPersona = null;
	private String persona = null;
	private String mensaje = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	public void execute() throws Exception {	
		
		unidad= _req.getParameter("unidad");
		idBloter=_req.getParameter("id_blotter");
		bloter=_req.getParameter("blotter");
		idPersona=_req.getParameter("id_tipper");
		mensaje=_req.getParameter("mensaje");
		
		if(idPersona.equals("J")){
			persona="Jurídico";
		}
		if(idPersona.equals("G")){
			persona="Gubernamental";
		}
		if(idPersona.equals("V")){
			persona="Venezolano";
		}
		if(idPersona.equals("E")){
			persona="Extrajero";
		}
		
	  //Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
	  //fin de recuperacion y de envio a la vista
		
		String strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("idUnidadInversion", java.sql.Types.VARCHAR);
		dsApoyo.append("unidad", java.sql.Types.VARCHAR);
		dsApoyo.append("id_blotter", java.sql.Types.VARCHAR);
		dsApoyo.append("blotter", java.sql.Types.VARCHAR);
		dsApoyo.append("idpersona", java.sql.Types.VARCHAR);
		dsApoyo.append("persona", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		dsApoyo.append("total_records", java.sql.Types.VARCHAR);			

		dsApoyo.addNew();
		dsApoyo.setValue("idUnidadInversion", idUnidadInversion+"");
		dsApoyo.setValue("unidad", unidad);
		dsApoyo.setValue("id_blotter", idBloter);
		dsApoyo.setValue("blotter", bloter);
		dsApoyo.setValue("idpersona", idPersona);
		dsApoyo.setValue("persona", persona);
		
		UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
		TipoCartaDAO tiCarta = new TipoCartaDAO(_dso);
		//Listamos lo unidad de inversion para saber si es inventario o subasta
		unidadDAO.listarPorId(idUnidadInversion);
		DataSet instrumento = unidadDAO.getDataSet();
		instrumento.first();
		instrumento.next();
		String idsCartas = null;
		instrumento.getValue("insfin_forma_orden");
		mensaje =mensaje.toLowerCase();
		if (mensaje.startsWith("sin")){//si no se han registrado documentos
			//Caso 1: si no se han agregado ninguno de los documentos los mostraremos todos correspondientes al instrumento
		
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
			//fin Caso 1
		}else if (mensaje.startsWith("falta")){//si faltan documentos
			//Caso 2: si no se han agregado todos los documentos (falto o fallo alguno) mostraremos los faltantes y que correspondan al instrumento
			
			UIDocumentosDAO uiDocDAO = new UIDocumentosDAO(_dso);
			uiDocDAO.listarCartasFaltantes(idUnidadInversion, idBloter, idPersona);
			DataSet _idcartas = uiDocDAO.getDataSet();
			_idcartas.first();
			String cartasAgregar="";
			while (_idcartas.next()){
				cartasAgregar= cartasAgregar+_idcartas.getValue("tipcar_id");
				if(Integer.parseInt(_idcartas.getValue("_row"))<_idcartas.count())//al ultimo registro no le anexamos la coma
					cartasAgregar=cartasAgregar+",";
			}
			if ((instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_INVENTARIO))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO))){
				cartasAgregar = cartasAgregar.substring(0, 1);
				idsCartas = cartasAgregar;
			}
			//if ((instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SITME))){
			if ((instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA) ||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)))){
				idsCartas = cartasAgregar;
			}
							
			//fin Caso 2
		}//fin mensaje
		
		tiCarta.listarPorTipoCartas(idsCartas);
		
		storeDataSet("cartas", tiCarta.getDataSet());
		storeDataSet("dsApoyo", dsApoyo);

	}
}
