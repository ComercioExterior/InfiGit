/**
 * 
 */
package models.custodia.informes.posicion_global;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * @author eel
 * 
 */
public class PosicionGlobalExportarCSV extends ExportableOutputStream {
	//SimpleDateFormat formateador = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);//nm36635
	private long client_id;
	private String fe_em_hasta;
	//private String fe_funcion;//nm36635	
	private String idioma;
	private String reporte;	
	private String titulo_id = "";
	private String tipo_producto_id = "";
	private final String  IDIOMA_INGLES = "ingles";
	private final String  VALOR_MERCADO = "valorMercado";

	/**
	 * Constructor de la clase
	 * 
	 * @param long client_id
	 * @param String
	 *            fe_em_hasta
	 * @param DataSource
	 *            _dso
	 * @param ServletContext
	 *            _app
	 * @param HttpServletResponse
	 *            _res
	 * @throws Exception
	 */
	public PosicionGlobalExportarCSV(long client_id, String idTitulo, String tipo_producto_id, String fe_em_hasta, DataSource _dso, ServletContext _app, HttpServletResponse _res, String idioma, String reporte) throws Exception {
		this.client_id = client_id;
		this.fe_em_hasta = fe_em_hasta;
		//this.fe_funcion = formateador.format(fe_em_hasta);//nm36635
		this._dso = _dso;
		this._app = _app;
		this._res = _res;
		this.idioma = idioma;
		this.titulo_id = idTitulo;
		this.tipo_producto_id = tipo_producto_id;
		this.reporte = reporte;
		

	}

	public void execute() throws Exception {
		CustodiaDAO custodia = new CustodiaDAO(_dso);
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-mm-dd");
		String formatoreme = formato.format("yyyymmdd");//nm36635

		Statement statement = null;
		ResultSet custodiaRs = null;
		// Recuperamos el dataset con la informacion para exportarla a excel
		Transaccion transaccion = new Transaccion(this._dso);

		try {
			this.aplicarFormato = false;
			
			registrarInicio(obtenerNombreArchivo("PosicionGlobalExcel"));
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
			custodiaRs = statement.executeQuery(custodia.listarPosicionGlobalDataExcel(client_id, titulo_id, tipo_producto_id, fe_em_hasta));
			if (idioma.equals(IDIOMA_INGLES)){
				crearCabeceraIngles();	
			}else{
				crearCabecera();
			}
			
			while (custodiaRs.next()) {
				escribir(custodiaRs.getString("client_nombre"));
				escribir(";");				
				escribir(custodiaRs.getString("client_cedrif"));
				escribir(";");
				escribir(custodiaRs.getString("client_cta_custod_id"));
				escribir(";");
				escribir(custodiaRs.getString("titulo_id"));
				escribir(";");
				escribir(custodiaRs.getString("tipo_producto_id"));
				escribir(";");
				if (custodiaRs.getString("titulo_fe_vencimiento")!=null){
					escribir(formato.parse(custodiaRs.getString("titulo_fe_vencimiento")));
				}else{
					escribir("");
				}
				escribir(";");
				escribir(custodiaRs.getString("titcus_cantidad"));
				escribir(";");
				//escribir(custodiaRs.getDouble("TITULOS_PRECIO_RECOMPRA"));
				//escribir(";");
				escribir(custodiaRs.getString("titulo_moneda_den"));
				escribir(";");
				escribir(custodiaRs.getString("estados"));
				escribir(";\r\n");
				//escribir(custodiaRs.getDouble("tcc_tasa_cambio_compra"));
				//escribir(";");
				
				/*if (reporte.equals(VALOR_MERCADO)){
					escribir((custodiaRs.getLong("titcus_cantidad") * custodiaRs.getDouble("TITULOS_PRECIO_RECOMPRA") * custodiaRs.getDouble("tcc_tasa_cambio_compra")) / 100);
					escribir(";\r\n");
				}else{
					escribir(custodiaRs.getLong("titcus_cantidad") * custodiaRs.getDouble("tcc_tasa_cambio_compra"));
					escribir(";\r\n");
				}*/
			}
			registrarFin();
			obtenerSalida();

		} catch (Exception e) {
			obtenerSalida();
			Logger.error(this, "Error en la exportación del Excel", e);
			throw new Exception("Error en la exportación del Excel"+e);
		} finally {
			if (custodiaRs != null) {
				custodiaRs.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (transaccion != null) {
				transaccion.closeConnection();
			}
		}
	}// fin execute

	private void crearCabecera() throws Exception {
		//escribir("CLIENTE;RIF/C.I.;CUENTA CUSTODIA;CLAVE VALOR;TIPO DE PRODUCTO;VCTO TITULO;CANT. NOMINAL;PRECIO;MONEDA;ESTADO;CAMBIO;VALOR TOTAL;");
		escribir("CLIENTE;RIF/C.I.;CUENTA CUSTODIA;CLAVE VALOR;TIPO DE PRODUCTO;VCTO TITULO;CANT. NOMINAL;MONEDA;ESTADO;");
		escribir("\r\n");
	}
	
	private void crearCabeceraIngles() throws Exception {
		//escribir("NAME;RIF/C.I.;CODE;SECURITY;PRODUCT;MATURUTY DATE;QUANTITY OF SECURITIES IN NOMINAL VALUE;PRICE;CURRENCY;STATUS;EXCHANGE RATE;VALUES;");
		escribir("NAME;RIF/C.I.;CODE;SECURITY;PRODUCT;MATURUTY DATE;QUANTITY OF SECURITIES IN NOMINAL VALUE;CURRENCY;STATUS;");
		escribir("\r\n");
	}
	
	protected void registrarInicio(String nombre) throws Exception{
		String nombreReporte = "";
		if (idioma.equals(IDIOMA_INGLES)){
			if (reporte.equals(VALOR_MERCADO)){
				nombreReporte = "PosicionGlobalExcelInglesMercado";
			}else{
				nombreReporte = "PosicionGlobalExcelInglesNominal";
			}
		}else{
			if (reporte.equals(VALOR_MERCADO)){
				nombreReporte = "PosicionGlobalExcelEspanolMercado";
			}else{
				nombreReporte = "PosicionGlobalExcelEspanolNominal";
			}
		}		
		super.registrarInicio(obtenerNombreArchivo(nombreReporte));	

	}
	
}// fin ValoresCustodiaExportarExcel
