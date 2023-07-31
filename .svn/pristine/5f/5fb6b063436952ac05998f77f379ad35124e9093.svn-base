package models.configuracion.generales.instrumentos_financieros;

import megasoft.DataSet;
import models.msc_utilitys.*;

import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.FormaInstrumentoDAO;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.util.helper.Html;

public class Edit extends MSCModelExtend {
	
	InstrumentoFinancieroDAO confiD;
	OrdenDAO ordenDAO;
	TitulosDAO titulosDAO;
	String idInstrumento=null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		DataSet _datos = new DataSet();
		_datos.append("combo_tipo_producto", java.sql.Types.VARCHAR);
		 //confiD = new InstrumentoFinancieroDAO(_dso);
		EmpresaDefinicionDAO empresa = new EmpresaDefinicionDAO(_dso);
		FormaInstrumentoDAO formaInstrumentoDAO  = new FormaInstrumentoDAO(_dso);
		
		_datos.addNew();
		//armar combo para tipo de producto
		_datos.setValue("combo_tipo_producto", Html.getSelectTipoProducto(_dso));

		
	
		
		
		//Realizar consulta
		confiD.listarPorId(idInstrumento);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("indicador",empresa.indicador());
		
		formaInstrumentoDAO.listarTodos();
		storeDataSet("formaorden",formaInstrumentoDAO.getDataSet());
		
		confiD.listarManejosProductos();
		storeDataSet("manejoprod",confiD.getDataSet());
		
		Addnew addn = new Addnew();
		storeDataSet("cupones",addn.cupones());
		
		storeDataSet("datos", _datos);
		
	}
	
	public boolean isValid()throws Exception{
		boolean flag=true;
		int indicadorFalla=0;
		if(_req.getParameter("insfin_id")!=null){
			idInstrumento = _req.getParameter("insfin_id");
		}
		confiD = new InstrumentoFinancieroDAO(_dso);
		ordenDAO=new OrdenDAO(_dso);
		titulosDAO=new TitulosDAO(_dso);
		confiD.busquedaUnidadInversionPorInstrumentoFinancieroId(idInstrumento);
		
		String unidadInversionId="";
		
		if(confiD.getDataSet().count()>0){
			confiD.getDataSet().first();
			
			ciclo_unidad_inversion:
			 while(confiD.getDataSet().next()){
				unidadInversionId=confiD.getDataSet().getValue("UNDINV_ID");
				ordenDAO.listarOrdenesPorUnidadInversionCount(Long.parseLong(unidadInversionId));		
				
				if(ordenDAO.getDataSet().count()>0){
					ordenDAO.getDataSet().first();					
					while(ordenDAO.getDataSet().next()){
						String ordenes=ordenDAO.getDataSet().getValue("total_unidad_ordenes");
						if(Long.parseLong(ordenes)>0){
							indicadorFalla=1;
							_record.addError("Para su informaci&oacute;n","El instrumento seleccionado ya ha sido utilizado por una Unidad de Inversi&oacute;n que tiene ordenes asociadas");							
							flag=false;							
						}
					}				
				}
				
				titulosDAO.listarCantidadTitulosPorUnudadInversionId(Long.parseLong(unidadInversionId));
				if(titulosDAO.getDataSet().count()>0){
					titulosDAO.getDataSet().first();					
					while(titulosDAO.getDataSet().next()){
						String titulos=titulosDAO.getDataSet().getValue("TITULOS");
						if(Long.parseLong(titulos)>0){
							indicadorFalla=1;
							_record.addError("Para su informaci&oacute;n","El instrumento seleccionado ya ha sido utilizado por una Unidad de Inversi&oacute;n que tiene asociado uno o mas t&iacute;tulos.");							
							flag=false;							
						}
					}				
				}				
				if(indicadorFalla>0){
					break ciclo_unidad_inversion;
				}				
			}	
		}
	
		//confiD.verificacionTitulosAsociadosInstrumentoPorId(idInstrumento);
		/*if(confiD.getDataSet().count()>0){
			_record.addError("Para su informaci&oacute;n","El instrumento seleccionado ya ha sido utilizado por una Unidad de Inversi&oacute;n que tiene asociado uno o mas t&iacute;tulos.");
			flag=false;
		}*/
		
		return flag;
	}
}