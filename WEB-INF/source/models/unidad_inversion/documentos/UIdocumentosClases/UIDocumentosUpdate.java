package models.unidad_inversion.documentos.UIdocumentosClases;

import java.text.DateFormat;
import java.util.Date;

import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.TipoCartaDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.AbstractModel;
import megasoft.DataSet;

public class UIDocumentosUpdate extends AbstractModel{
	
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
//		Recuperamos de session el numero de la accion y la mandamos a la vista
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
		
		UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
		TipoPersonaDAO tiPer = new TipoPersonaDAO(_dso);
		TipoCartaDAO tiCarta = new TipoCartaDAO(_dso);
		
		String usuario=getSessionObject("framework.user.principal").toString();
		
//		Listamos lo unidad de inversion para saber si es inventario o subasta
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
		tiPer.listarPorUnidad(String.valueOf(idUnidadInversion), _record.getValue("bloter_id"));
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
					String idDocumento="documento"+name.toLowerCase();
					String radioButton="radio"+name.toLowerCase();
					if((_record.getValue(tempFile)!=null)&&(_record.getValue(nameFile)!=null)){
						DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);
						DocumentoDefinicion documentoDefinicion = new DocumentoDefinicion();
						
						String user=confiD.idUserSession(usuario);
						documentoDefinicion.setDocumentoId(Integer.parseInt(_record.getValue(idDocumento)));
						documentoDefinicion.setRutaDocumento(_record.getValue(tempFile));
						documentoDefinicion.setNombreDoc(_record.getValue(nameFile));
						documentoDefinicion.setModificarDocumento(Short.parseShort(_record.getValue(radioButton)));
						documentoDefinicion.setStatusDocumento(ConstantesGenerales.STATUS_REGISTRADO);
						documentoDefinicion.setAproUsuarioUserid(user);
						
				    	Date fechaActual = new Date();
				   		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
				        Date fecha = df.parse(df.format(fechaActual));
				 
						documentoDefinicion.setAproFecha(fecha);
								
						confiD.modificar(documentoDefinicion);
					}
				}
			}
		}
	}
}
