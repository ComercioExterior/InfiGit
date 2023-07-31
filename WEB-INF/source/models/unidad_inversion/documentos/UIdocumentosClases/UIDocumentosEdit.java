package models.unidad_inversion.documentos.UIdocumentosClases;

import java.io.File;
import java.io.FileWriter;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Page;
import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.TipoCartaDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.UIDocumentosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class UIDocumentosEdit extends AbstractModel {

	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	DataSet _master	 = null;
	DataSet _detalle = null;
	DataSet _body	 = null;
	DataSet personaB = null;
	String  bloter	 = null;
	
	
	public void execute() throws Exception {	
		
	  //Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
	  //fin de recuperacion y de envio a la vista

		bloter= _req.getParameter("id");
		String strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("idUnidadInversion", java.sql.Types.VARCHAR);
		dsApoyo.append("bloter_id", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		dsApoyo.append("total_records", java.sql.Types.VARCHAR);			

		dsApoyo.addNew();
		dsApoyo.setValue("idUnidadInversion", idUnidadInversion+"");	
		dsApoyo.setValue("bloter_id", bloter);
		
		TipoPersonaDAO tiPer = new TipoPersonaDAO(_dso);
		UIDocumentosDAO uIDocumentosDAO =new UIDocumentosDAO(_dso);
		
		tiPer.listarTodos();
		
		storeDataSet("personas", tiPer.getDataSet());
		storeDataSet("dsApoyo", dsApoyo);

		DataSet _examinar=new DataSet();
		_examinar.append("examinar",java.sql.Types.VARCHAR);
			
		StringBuffer buf = new StringBuffer();
		StringBuffer val = new StringBuffer();
		String tpl =  getResource(_config.configDir + "section.htm");
		String fields =  getResource(_config.configDir + "fields.txt");;
		String record =  getResource(_config.configDir + "record.xml");;
		
		int pretotal=0;
		int total=0;
		int cont=0;
		_master=getMaster();
		_detalle=getDetail();
		while (_master.next()){
			int a=0;
			Page p=new Page(tpl);
			Page f=new Page(fields);
			tiPer.listarPorUnidad(String.valueOf(idUnidadInversion), _master.getValue("bloter_id"));
			personaB = tiPer.getDataSet();
			StringBuffer input = new StringBuffer();
			StringBuffer bufer = new StringBuffer();
			String tipoPersona= null;
			while (personaB.next()){								
				if (personaB.getValue("bloter_id") == null ){
					input.append("<td width=\"25%\" align=\"center\">No Aplica </td>\n");					
				}else{
					String nombre= personaB.getValue("bloter_id")+personaB.getValue("tipper_id");
					//String docuemnto = 
					/*uIDocumentosDAO.listarNombreDoc(personaB.getValue("bloter_id"), personaB.getValue("tipper_id"), personaB.getValue("tipcar_id"));
					DataSet nameDoc= uIDocumentosDAO.getDataSet();
					nameDoc.first();nameDoc.next();
					String documento = nameDoc.getValue("nombre_doc");
					String idDoc = nameDoc.getValue("documento_id");*/
					//Para armar plantilla (table.htm)
					String plant =  getResource(_config.configDir + "inputs.htm");
					Page in=new Page(plant);
					in.replace("@nombre@", nombre.toLowerCase());
					//in.replace("@documento@", documento);
					in.replace("@cont@", String.valueOf(cont));
					//in.replace("@id_documento@", idDoc);
					/*_detalle..first*/
					input.append(in);
					//Para armar record (record.xml)
					String fieldBlo =  getResource(_config.configDir + "valores.txt");
					Page fi=new Page(fieldBlo);
					fi.replace("@nombre@", nombre.toLowerCase());
					fi.replace("@cont@", String.valueOf(cont));
					bufer.append(fi);
				}
			}
			cont=cont+1;
			p.replace("@tds@", input.toString());
			p.replace("@valor@", input.toString());
			p.repeat(getDetail(), "rows");			
			p.setFormValues(_master);
			p.setFormValues(getDetail());
			
			_detalle.first();
			while (_detalle.next()){
				personaB.first();
				while (personaB.next()){	
					uIDocumentosDAO.listarNombreDoc(_master.getValue("bloter_id"), personaB.getValue("tipper_id"), _detalle.getValue("tipcar_id"));
					DataSet nameDoc= uIDocumentosDAO.getDataSet();
					if (nameDoc.count()>0){
						nameDoc.first();nameDoc.next();
						String documento = nameDoc.getValue("nombre_doc");
						String idDoc = nameDoc.getValue("documento_id");
						p.replace("@id"+_master.getValue("bloter_id").toLowerCase()+personaB.getValue("tipper_id").toLowerCase()+_detalle.getValue("tipcar_id")+"@", idDoc);
						p.replace("@documento"+_master.getValue("bloter_id").toLowerCase()+personaB.getValue("tipper_id").toLowerCase()+_detalle.getValue("tipcar_id")+"@", documento);
					}
				}
				
			}
			
			buf.append(p.getPage());
			
			f.replace("@valores@", bufer.toString());
			f.repeat(_detalle, "rows");
			f.setFormValues(_detalle);
			val.append(f.getPage());
		}
		total= pretotal*_detalle.count();
		_body=new DataSet();
		_body.append("body",java.sql.Types.VARCHAR);
		_body.append("total",java.sql.Types.INTEGER);
		_body.addNew();
		_body.setValue("body",buf.toString());
		_body.setValue("total",String.valueOf(total));
		storeDataSet("body",_body);
		storeDataSet("master",_master);
		storeDataSet("examinar",_examinar);
		
		String separador = String.valueOf(File.separatorChar);
		String rutaRecord= _app.getRealPath("WEB-INF") + separador + "actions" + separador + "unidad_inversion" + separador + "documentos"+ separador + "update" + separador + "record.xml";
		
		Page r = new Page(record);
		r.replace("@fields@", val.toString());
		FileWriter file = new FileWriter(rutaRecord);
		file.write(r.getPage().toString(),0,r.getPage().length());
		file.close();
	}
	public DataSet getMaster() throws Exception{
		BlotterDAO uiBlo = new BlotterDAO(_dso);
		uiBlo.listar(bloter);
		DataSet _principal = uiBlo.getDataSet();
		return _principal;
	}
	public DataSet getDetail() throws Exception{
		UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
		TipoCartaDAO tiCarta = new TipoCartaDAO(_dso);
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
		DataSet _detalle = tiCarta.getDataSet();
		return _detalle;
	}
	
}
