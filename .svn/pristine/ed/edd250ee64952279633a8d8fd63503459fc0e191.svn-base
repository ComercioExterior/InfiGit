package models.validacion_documento;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.TransaccionCampoDocDAO;
import com.bdv.infi.dao.UICamposDinamicosDAO;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.util.Utilitario;

import megasoft.AbstractModel;
import megasoft.DataSet;

/**Clase que valida el contenido del documento
 * Debera estar bien formados los '@ @' presentes en el documento
 * y
 * deberan existir en el mapa de la transacion asociada al Documento
 * @author Jessica Villegas
 *
 */
public class ValidacionDocumento extends AbstractModel {
	
	private String idDocumento = null;	
	private String transaccion = null;
	private String unidadInversion = null;
	private String valorSustituir = "CORRECTO";
	private String etiqueta = "@";
	private Logger logger = Logger.getLogger(ValidacionDocumento.class);
	
	public void execute() throws Exception {
		
		//Idenificador del documento a validar
		//idDocumento = _req.getParameter("idDocumento");
		
//ITS-2329  Correccion de incidencia asociada a documentos de Unidad de Inversion NM26659 05/11/2014
		if(_req.getParameter("idDocumento")!=null){
			idDocumento = _req.getParameter("idDocumento");			
			String documentoIdSplit[]=idDocumento.split(",");
			idDocumento=documentoIdSplit[0].replace(".","");	
		}
		//Transaccion asociada al documento
		transaccion = _req.getParameter("transa_id");
		
		if(Integer.parseInt(_req.getParameter("unidadInversion"))!=0){
			unidadInversion = _req.getParameter("unidadInversion");
		}
			
		DocumentoDefinicion documentoDefinicion = new DocumentoDefinicion();
		DocumentoDefinicionDAO documentoDefinicionDAO = new DocumentoDefinicionDAO(_dso);
		UICamposDinamicosDAO uICamposDinamicosDAO = new UICamposDinamicosDAO(_dso);
		TransaccionCampoDocDAO transaccionCampoDocDAO = new TransaccionCampoDocDAO(_dso);
		
		Map<String, String> mapa = new HashMap<String, String>();
		
		mapa.put(etiqueta+"fecha"+etiqueta, valorSustituir);
		mapa.put(etiqueta+"anio"+etiqueta,valorSustituir);
		mapa.put(etiqueta+"dia"+etiqueta, valorSustituir);
		mapa.put(etiqueta+"mes"+etiqueta, valorSustituir);
		mapa.put(etiqueta+"mes_actual"+etiqueta, valorSustituir);

		transaccionCampoDocDAO.listar(transaccion);//Lista de Campos Generales (tabla _026)
		DataSet _datos = transaccionCampoDocDAO.getDataSet();
		if (_datos.count()>0){
			_datos.first();
			while (_datos.next()){
				String key = _datos.getValue("nombre_campo");
				mapa.put(etiqueta+key+etiqueta, valorSustituir);
			}
		}
		if (unidadInversion!=null){
			_datos.clear();
			//El mapa para los titulos debera contener los mismo que se muestran en pantalla
			//(revisar el archivo ubicado en: /INFI/WEB-INF/actions/configuracion/documentos/definicion/plantilla/titulos/plantilla.htm para comparaciones)
			uICamposDinamicosDAO.listarTitulosParaUnidadesInv(Integer.parseInt(unidadInversion));// Lista de Campos de Titulos
			_datos = uICamposDinamicosDAO.getDataSet();
			if (_datos.count()>0){
				_datos.first();
				while (_datos.next()){
					String keyTitulo = _datos.getValue("titulo");
					String keyPrecio = _datos.getValue("precio");
					mapa.put(etiqueta+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+keyPrecio+etiqueta, valorSustituir);
					mapa.put(etiqueta+"monto_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+"porcentaje_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+keyTitulo+"_monto_pedido"+etiqueta, valorSustituir);
					mapa.put(etiqueta+keyTitulo+"_monto_adjudicado"+etiqueta, valorSustituir);
					mapa.put(etiqueta+keyTitulo+"_contra_valor"+etiqueta, valorSustituir);
					mapa.put(etiqueta+keyTitulo+"_intereses_caidos"+etiqueta, valorSustituir);
					mapa.put(etiqueta+keyTitulo+"_dias_int_transcurridos"+etiqueta, valorSustituir);
					mapa.put(etiqueta+"base_calculo_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+"tasa_cupon_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+"moneda_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+"dia_vencimiento_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+keyTitulo+"_dias_al_vencimiento"+etiqueta, valorSustituir);
					mapa.put(etiqueta+"mes_vencimiento_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+"nombre_mes_vencimiento_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+"anio_vencimiento_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+"recompra_"+keyTitulo+etiqueta, valorSustituir);
					mapa.put(etiqueta+"recompra_"+keyTitulo+"_pct"+etiqueta, valorSustituir);
					mapa.put(etiqueta+"recompra_"+keyTitulo+"_monto"+etiqueta, valorSustituir);
					mapa.put(etiqueta+"recompra_"+keyTitulo+"_total"+etiqueta, valorSustituir);
				}
			}
			_datos.clear();
			uICamposDinamicosDAO.listarCamposDinamicosParaUnidadesInv(Integer.parseInt(unidadInversion));//Lista de Campos Dinamicos
			_datos = uICamposDinamicosDAO.getDataSet();
			if (_datos.count()>0){
				_datos.first();
				while (_datos.next()){
					String key = _datos.getValue("nombre");
					mapa.put(etiqueta+key+etiqueta, valorSustituir);
				}
			}
		}
		/* Buscar el documento seg&uacute;n un documento_id y almacenarlo en un objeto DocumentoDefinicion */
		documentoDefinicion = documentoDefinicionDAO.listarDocumento(idDocumento);
		String plantilla = new String(documentoDefinicion.getContenido());
		try {				
			Iterator it = mapa.entrySet().iterator();
			while (it.hasNext()) {						
			  Map.Entry e = (Map.Entry) it.next();
			  if (e.getValue() instanceof String){
				  plantilla = plantilla.replaceAll((String) e.getKey(), (String) e.getValue());
			  }
			}
			String nuevaPlantilla = plantilla;
			
			DataSet _listaArrobasSinReemplazar = new DataSet();
			//Busca caracteres y caracteres especiales que esten dentro de dos @
			Pattern patron = Pattern.compile("\\@[\\w\\W]+\\@", Pattern.MULTILINE); // El patr&oacute;n de b&uacute;squeda
			Matcher matcher = patron.matcher(nuevaPlantilla);
			
			//Matcher matcher = patron.matcher(""); // El objeto que efect&uacute;a la b&uacute;squeda del patr&oacute;n
			//matcher.reset(nuevaPlantilla);
			//matcher.start();
			_listaArrobasSinReemplazar.append("campo",java.sql.Types.VARCHAR);
			/* Iterar mientras se encuentren etiquetas en el documento */
			int cont = 0;
			while (matcher.find()) {
				_listaArrobasSinReemplazar.addNew();
				_listaArrobasSinReemplazar.setValue("campo",matcher.group());
				cont = cont + 1;
			}
			if (_listaArrobasSinReemplazar.count()<=0){
				_config.template="table_exito.htm";
			}
			
			
			
			storeDataSet("table", _listaArrobasSinReemplazar);
			storeDataSet("recomendaciones", documentoDefinicionDAO.recomendaciones());
		} catch (Throwable e) {
			logger.error("Error en la validacion del documento "+e.getMessage()+" "+Utilitario.stackTraceException(e));
		}			
	}

}
