package models.unidad_inversion.unidad_inversion;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.sql.DataSource;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import electric.xml.Document;
import electric.xml.Element;

/**
 * Clase que valida la informacion de una unidad de inversion
 * @author Megasoft Computaci&oacute;n
 */
public class UnidadInversionValidar extends AbstractModel {

	/**
	 * Formato de Date
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	/**
	 * Calendarios para manejo de las fechas : validacion y conversion a Date
	 */	
	private Calendar fechaEmision = Calendar.getInstance();
	private Calendar fechaAdjudicacion = Calendar.getInstance();
	private Calendar fechaLiquidacion = Calendar.getInstance();	
	private Calendar fechaCierre = Calendar.getInstance();	
	/**
	 * Instrumento Fininciero
	 */
	private String idInstrumentoFinanciero = "";
	private String idTipoInstrumento = "";
	private String descTipoInstrumento = "";
	private DataSource ds=null;

	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid(DataSet record) throws Exception {
		InstrumentoFinancieroDAO instrumentoFinancieroDAO = new InstrumentoFinancieroDAO(ds);
		boolean flag = true;		
		BigDecimal bdAux1;
				
		if (record.getValue("nombreUnidadInversion") == null || record.getValue("nombreUnidadInversion").trim().equals("")) {
			record.addError("Nombre","Este campo es requerido.");
			flag = false;
		}
		boolean flagIF = true;
		if (record.getValue("idInstrumentoFinanciero") == null || record.getValue("idInstrumentoFinanciero").trim().equals("")) {
			record.addError("Tipo de Instrumento","Este campo es requerido.");
			flag = false;
			flagIF = false;
		} else {
			idInstrumentoFinanciero = record.getValue("idInstrumentoFinanciero");
			idTipoInstrumento = record.getValue("tipoInstrumentoFinanciero");
		}

		if (record.getValue("idEmpresaEmisor") == null || record.getValue("idEmpresaEmisor").trim().equals("-1")) {
			record.addError("Emisor","Este campo es requerido.");
			flag = false;
		}
		
		if (record.getValue("idMoneda") == null || record.getValue("idMoneda").trim().equals("-1")) {
			record.addError("Moneda de Negociacion","Este campo es requerido.");
			flag = false;
		}
		
		if (record.getValue("tasaCambio") == null || record.getValue("tasaCambio").trim().equals("")) {
			record.addError("Tasa Cambio a Aplicar","Este campo es requerido.");
			flag = false;
		} else {
			bdAux1 = new BigDecimal(record.getValue("tasaCambio"));
			if (bdAux1.doubleValue() == 0.0) {
				record.addError("Tasa de Cambio","Este campo es requerido y no puede ser cero.");
				flag = false;
			}
			if (bdAux1.doubleValue() > 999999999.99) {
				record.addError("Tasa de Cambio","Los enteros del valor no debe exceder de 999999999");
				flag = false;
			}
		}

		boolean flagFE = true;
		try {
			fechaEmision.setTime(sdIODate.parse(record.getValue("fechaEmisionUI")));
		} catch (Exception e) {
			record.addError("Fecha de Emisi&oacute;n","Este campo debe tener formato: dd-mm-yyyy");
			flagFE = false;
		}
		boolean flagFC = true;
		try {
			fechaCierre.setTime(sdIODate.parse(record.getValue("fechaCierreUI")));
		} catch (Exception e) {
			record.addError("Fecha de Cierre","Este campo debe tener formato: dd-mm-yyyy");
			flagFC = false;
		}
		if (flagFE && flagFC) {
			if (fechaCierre.before(fechaEmision)) {
				record.addError("Fecha de Cierre","La fecha deber ser mayor a la Fecha de Emision");
				flag = false;
			}			
		}
		
		instrumentoFinancieroDAO.listarPorId(idInstrumentoFinanciero);
		if(instrumentoFinancieroDAO.getDataSet().count()>0){
			instrumentoFinancieroDAO.getDataSet().first();
			instrumentoFinancieroDAO.getDataSet().next();
			descTipoInstrumento=instrumentoFinancieroDAO.getDataSet().getValue("insfin_descripcion");
		}
		
		//	Validacion de Fechas asociadas a UI tipo Subasta
		boolean flagFL = true; boolean flagFA = true;

		if (idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_INVENTARIO) ||
				idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO)||
				record.getValue("idTipoProductoNuevo").equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)) {
			record.setValue("fechaLiquidaUI","");
			record.setValue("fechaAdjudicacionUI","");
		} else {			
			if (record.getValue("fechaLiquidaUI") == null || record.getValue("fechaLiquidaUI").equals("")) {
				record.addError("Fecha de Liquidaci&oacute;n","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta.");
				flagFL = false;
			}
			if (record.getValue("fechaAdjudicacionUI") == null || record.getValue("fechaAdjudicacionUI").equals("")) {
				record.addError("Fecha de Adjudicaci&oacute;n","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta.");
				flagFA = false;
			}	
			if (flagFE && flagFA) {
				try {
					fechaAdjudicacion.setTime(sdIODate.parse(record.getValue("fechaAdjudicacionUI")));
				} catch (Exception e) {
					record.addError("Fecha de Adjudicaci&oacute;n","Este campo debe tener formato: dd-mm-yyyy");
					flagFA = false;
				}
				try {
					fechaLiquidacion.setTime(sdIODate.parse(record.getValue("fechaLiquidaUI")));
				} catch (Exception e) {
					record.addError("Fecha de Liquidaci&oacute;n","Este campo debe tener formato: dd-mm-yyyy");
					flagFL = false;
				}
				if (flagFE && flagFA) {
					if (fechaAdjudicacion.before(fechaCierre)) {
						record.addError("Fecha de Adjudicaci&oacute;n","La fecha deber ser mayor a la Fecha de Cierre");
						flag = false;
					}	
					if (fechaLiquidacion.before(fechaCierre) || fechaLiquidacion.before(fechaAdjudicacion)) {
						record.addError("Fecha de Liquidaci&oacute;n","La fecha deber ser mayor a la Fecha de Cierre y de Adjudicaci&oacute;n");
						flag = false;
					}		
				}
			}	
		} 


		if (record.getValue("montoMultiplos") == null || record.getValue("montoMultiplos").trim().equals("")) {
			record.addError("Multiplos","Este campo es requerido para Unidades de Inversi&oacute;n.");
			flag = false;
		} else {
			double total = new Double(record.getValue("montoMultiplos")).doubleValue();
			if (total == 0.0) {
				record.addError("Multiplos","Este campo es requerido para Unidades de Inversi&oacute;n.");
				flag = false;
			}
		}

		bdAux1 = new BigDecimal(0); 
		// Validacion de Unidad Minima de Inversion
		//	Estos campos son requeridos para Instrumento Financiero es de Tipo Inventario = Inventario
		//	1.- Total Autorizado 			: requerido 	Valor > 0
		//	2.-	Tasa Pool					: requerido		Valor > 0
		//	3.-	Porcentaje de rendimiento 	: requerido		Valor > 0
		if (flagIF && 
				(idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_INVENTARIO) ||
				 idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO))) {
			if (record.getValue("totalInversion") == null || record.getValue("totalInversion").trim().equals("")) {
				record.addError("Total autorizado","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
				flag = false;
			} else {
				double total = new Double(record.getValue("totalInversion")).doubleValue();
				if (total == 0.0) {
					record.addError("Total autorizado","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
					flag = false;
				}
			}
			if (record.getValue("tasaPool") == null || record.getValue("tasaPool").trim().equals("")) {
				record.addError("Tasa Pool","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
				flag = false;
			} else {
				bdAux1 = new BigDecimal(record.getValue("tasaPool"));
				if (bdAux1.doubleValue() == 0.0) {
					record.addError("Tasa Pool","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
					flag = false;
				}
				if (bdAux1.doubleValue() > 999.99) {
					record.addError("Tasa Pool","El valor no debe exceder de 999.99");
					flag = false;
				}
			}
			if (record.getValue("pctRendimiento") == null || record.getValue("pctRendimiento").trim().equals("")) {
				record.addError("Porcentaje de Rendimiento","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
				flag = false;
			} else {
				bdAux1 = new BigDecimal(record.getValue("pctRendimiento"));
				if (bdAux1.doubleValue() == 0.0) {
					record.addError("Porcentaje de Rendimiento","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
					flag = false;
				}
				if (bdAux1.doubleValue() > 999.99) {
					record.addError("Porcentaje de Rendimiento","El valor no debe exceder de 999.99");
					flag = false;
				}
			}
		}			
		
		// Validacion de Unidad Minima de Inversion
		//	Estos campos son requeridos para Instrumento Financiero es de Tipo Inventario = Subasta
		//	1.-	Monto Minimo de Inversio		: requerido		Valor > 0
		if (flagIF && (idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_SUBASTA) || idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA))) {

			if (record.getValue("montoMinimoSubastaInversion") == null || record.getValue("montoMinimoSubastaInversion").trim().equals("")) {
				record.addError("Monto M&iacute;nimo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta.");
				flag = false;
			} else {
				bdAux1 = new BigDecimal(record.getValue("montoMinimoSubastaInversion"));
				if (bdAux1.doubleValue() == 0.0) {
					record.addError("Monto M&iacute;nimo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta.");
					flag = false;
				}
			}
		}		
		if (!flagFE || !flagFA || !flagFL || !flagFC)
			flag = false;
		
		return flag;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
	
}
