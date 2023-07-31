package models.unidad_inversion.documentos.UIdocumentosClases;

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
	
	DataSet personaBlotter 	= null;
	DataSet cartas 			= null;
	DataSet _master			= null;
	DataSet _body			= null;
	DataSet personaB 		= null;
	
	public void execute() throws Exception {

		String strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);
		DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);
		DocumentoDefinicion documentoDefinicion = new DocumentoDefinicion();
		UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
		UIDocumentosDAO uIDocumentosDAO =new UIDocumentosDAO(_dso);
		UIDocumentos uIDocumento = new UIDocumentos();
		TipoPersonaDAO tiPer = new TipoPersonaDAO(_dso);
		TipoCartaDAO tiCarta = new TipoCartaDAO(_dso);
		String usuario=getSessionObject("framework.user.principal").toString();
		String user=confiD.idUserSession(usuario);

		//Listamos lo unidad de inversion para saber si es inventario o subasta
		unidadDAO.listarPorId(idUnidadInversion);
		DataSet instrumento = unidadDAO.getDataSet();
		instrumento.first();
		instrumento.next();
		String idsCartas = null;
		instrumento.getValue("insfin_forma_orden");
		if ((instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_INVENTARIO))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO))){
			idsCartas = "1";
		}
		if ((instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA))||(instrumento.getValue("insfin_forma_orden").equals(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA))){
			idsCartas = "1,2,3,4,5,6";
		}
		tiCarta.listarPorTipoCartas(idsCartas);
		tiPer.listarPorUnidad(String.valueOf(idUnidadInversion), null);
		personaBlotter = tiPer.getDataSet();
		cartas = tiCarta.getDataSet();
		personaBlotter.first();
		while (personaBlotter.next()){
			if(personaBlotter.getValue("bloter_id")!=null){
				cartas.first();
				while (cartas.next()){
					String name = personaBlotter.getValue("bloter_id")+personaBlotter.getValue("tipper_id")+cartas.getValue("tipcar_id");			
					String nameFile=name.toLowerCase()+".filename";
					String tempFile=name.toLowerCase()+".tempfile";
					if((_record.getValue(tempFile)!=null)&&(_record.getValue(nameFile)!=null)){
						String documento_id = GenericoDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_TRANSACCION_DOC);
						documentoDefinicion.setDocumentoId(Integer.parseInt(documento_id));
						documentoDefinicion.setCreUsuarioUserid(user);
						documentoDefinicion.setIdUnidadInversion(Integer.parseInt(strIdUnidadInversion));
						documentoDefinicion.setTipoPersona(personaBlotter.getValue("tipper_id"));
						documentoDefinicion.setRutaDocumento(_record.getValue(tempFile));
						documentoDefinicion.setNombreDoc(_record.getValue(nameFile));
						if((cartas.getValue("tipcar_id").equals("1"))||(cartas.getValue("tipcar_id").equals("2"))){
							documentoDefinicion.setTransaId(TransaccionNegocio.TOMA_DE_ORDEN);
						}
						if((cartas.getValue("tipcar_id").equals("3"))||(cartas.getValue("tipcar_id").equals("4"))||(cartas.getValue("tipcar_id").equals("5"))||(cartas.getValue("tipcar_id").equals("6"))){
							documentoDefinicion.setTransaId(TransaccionNegocio.ADJUDICACION);
						}
						confiD.insertar(documentoDefinicion);
						
						String idUnico = GenericoDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_UI_DOCUMENTOS);
						
						uIDocumento.setIdUnidadInversion(idUnidadInversion);
						uIDocumento.setIdTipoCarta(Integer.parseInt(cartas.getValue("tipcar_id")));
						uIDocumento.setIdTipoPersona(personaBlotter.getValue("tipper_id"));
						uIDocumento.setIdBloter(personaBlotter.getValue("bloter_id"));
						uIDocumento.setIdDocumento(Integer.parseInt(documento_id));
						uIDocumento.setIdUnico(Integer.parseInt(idUnico));
						String query = uIDocumentosDAO.insertarUIDoc(uIDocumento);
						db.exec(_dso,query);
					}
				}
			}
		}
	}
}