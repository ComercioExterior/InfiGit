package models.liquidacion.instrucciones_venta_titulos;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import models.msc_utilitys.MSCModelExtend;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.util.Utilitario;


public class InstruccionesVentaTituloImpresionMasiva extends MSCModelExtend  {

	public void execute()throws Exception {	
				
		int cantidadOrdenesTitulo=0;
	
			
		String separador 			= String.valueOf(File.separatorChar);
		String nombrePlantilla 		= "fichaClientePorOrden.xls"; // Plantilla anterior: ordenesBCV.xls

		String documentoFinal		= "plantilla_excel.xls";//null;
		String rutaTemplate			= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "ordenesTemplate" + separador + nombrePlantilla;
		
			
		HSSFWorkbook workbook		= null;		
		Map beans					= new HashMap();		
		
		
		ArrayList<Orden> ListaOrdenes=new ArrayList<Orden>();
		ArrayList<OrdenTitulo> listaOrdenesTitulos=null; 
		
		OrdenDAO ordenDAO=new OrdenDAO(_dso);

		ClienteCuentasDAO clienteCuentas=new ClienteCuentasDAO(_dso);
		TitulosDAO tituloDAO=new TitulosDAO(_dso);
		Orden orden=null;
		Cliente cliente=null;
		OrdenTitulo ordenTitulo=null;
		CuentaCliente cuentaSwift=null;
		
		String ordeneID=_req.getParameter("ordene_id");
		String [] ordenes=ordeneID.split(",");
		
		ordenDAO.listarOrdenPorId(0,ordenes);
		
		Date fechaActual=new Date();
		String fechaActualString=Utilitario.DateToString(fechaActual, "dd/MM/yyyy");
		
		String [] fechas=Utilitario.fechaFormateada(fechaActualString);
		
		
		while(ordenDAO.getDataSet().next()){
			
			orden=new Orden();	
						
			orden.setIdOrden(Long.parseLong(ordenDAO.getDataSet().getValue("ORDENE_ID")));
			
			orden.setFechaValorString(ordenDAO.getDataSet().getValue("fecha_valor"));
			orden.setMontoAdjudicado(Double.parseDouble(ordenDAO.getDataSet().getValue("ORDENE_ADJ_MONTO"))); 
			
			clienteCuentas.listarCuentaClientePorOrden(orden.getIdOrden());
			
			if(clienteCuentas.getDataSet().next()){
				
				cuentaSwift=new CuentaCliente();
				cuentaSwift.setCtecta_bcocta_bco(clienteCuentas.getDataSet().getValue("banco"));
				cuentaSwift.setCtecta_bcocta_direccion(clienteCuentas.getDataSet().getValue("direccion_banco"));
				cuentaSwift.setCtecta_bcocta_bic(clienteCuentas.getDataSet().getValue("swift"));
				cuentaSwift.setCtecta_bcocta_aba(clienteCuentas.getDataSet().getValue("ctecta_bcocta_aba"));
				cuentaSwift.setNombre_beneficiario(clienteCuentas.getDataSet().getValue("nombre_beneficiario"));
				cuentaSwift.setCtecta_numero(clienteCuentas.getDataSet().getValue("cuenta_beneficiario"));
				//Datos del Intermediario:
				if(clienteCuentas.getDataSet().getValue("banco_intermediario")!=null && !clienteCuentas.getDataSet().getValue("banco_intermediario").equals("")){
					
					cuentaSwift.setCtecta_bcoint_bco(clienteCuentas.getDataSet().getValue("banco_intermediario")); //Nombre banco intermediario
					cuentaSwift.setCtecta_bcoint_bic(clienteCuentas.getDataSet().getValue("bic_intermediario")); //BIC banco intermediario				
					cuentaSwift.setCtecta_bcoint_aba(clienteCuentas.getDataSet().getValue("aba_intermediario")); //ABA banco intermediario
					cuentaSwift.setCtecta_bcoint_direccion(clienteCuentas.getDataSet().getValue("dir_intermediario")); //Dirección Banco Intermediario
					cuentaSwift.setCtecta_bcoint_telefono(clienteCuentas.getDataSet().getValue("telefono_intermediario")); //Teléfono del banco intermediario
					cuentaSwift.setCtecta_bcoint_swift(clienteCuentas.getDataSet().getValue("cuenta_en_intermediario")); //Cuenta del banco destino en el banco intermediario
				}else{
					cuentaSwift.setCtecta_bcoint_bco("No posee"); //Nombre banco intermediario
					cuentaSwift.setCtecta_bcoint_bic("No posee"); //BIC banco intermediario				
					cuentaSwift.setCtecta_bcoint_aba("No posee"); //ABA banco intermediario
					cuentaSwift.setCtecta_bcoint_direccion("No posee"); //Dirección Banco Intermediario
					cuentaSwift.setCtecta_bcoint_telefono("No posee"); //Teléfono del banco intermediario
					cuentaSwift.setCtecta_bcoint_swift("No posee"); //Cuenta del banco destino en el banco intermediario
				}
				
				orden.setCuentaSwift(cuentaSwift);
				
				cliente=new Cliente();
				clienteCuentas.getDataSet().next();				
				cliente.setNombre(clienteCuentas.getDataSet().getValue("nombre"));				
				cliente.setNacionalidad(clienteCuentas.getDataSet().getValue("nacionalidad"));				
				long cedula=Long.parseLong(clienteCuentas.getDataSet().getValue("cedula"));				
				cliente.setRifCedula(cedula);												
				orden.setCliente(cliente);
				
				cliente=null;
				cuentaSwift=null;
			}
		
			
			tituloDAO.listarOrdenesVentaTitulos(orden.getIdOrden());
			
			while(tituloDAO.getDataSet().next()){
				++cantidadOrdenesTitulo;
				ordenTitulo=new OrdenTitulo();
				
				listaOrdenesTitulos=new ArrayList<OrdenTitulo>();
				
					ordenTitulo.setTituloId(tituloDAO.getDataSet().getValue("titulo"));

					orden.setIdMoneda(tituloDAO.getDataSet().getValue("moneda_denominacion"));
					ordenTitulo.setPrecioMercado(Double.parseDouble(tituloDAO.getDataSet().getValue("precio_venta")));
					orden.setMontoCobrado(Double.parseDouble(tituloDAO.getDataSet().getValue("monto_efectivo")));
					listaOrdenesTitulos.add(ordenTitulo);
					orden.agregarOrdenTitulo(listaOrdenesTitulos);
					ordenTitulo=null;
			}
			
			ListaOrdenes.add(orden);
			orden=null;

			cantidadOrdenesTitulo=0;
		}
		
		
		

		FileInputStream file		= new FileInputStream(rutaTemplate);
		XLSTransformer transformer	= new XLSTransformer ();
		beans.put("ordenes",ListaOrdenes);
		beans.put("dia",fechas[0]);
		beans.put("mes",fechas[1]);
		beans.put("anio",fechas[2]);
		workbook=transformer.transformXLS(file,beans);
		int areaImpresion=0;
		areaImpresion=((ListaOrdenes.size())*25);
		for (int i=1;i<ListaOrdenes.size();++i){
						
			if(i==1){				
				
				//System.out.println("ORDEN PRIMERA " + areaImpresion + " Iteracion " + i);
				workbook.getSheetAt(0).setRowBreak((24+cantidadOrdenesTitulo));
			} else {
				
				//System.out.println("ORDEN SEGUIDA " + areaImpresion + " Iteracion " + i);
				
				workbook.getSheetAt(0).setRowBreak((((25*i)-1)+cantidadOrdenesTitulo));
			}
		}
		
		workbook.setPrintArea(0,"$A$1:$F$"+areaImpresion);
		//System.out.println("DESDE A1 HASTA F"+areaImpresion);
		areaImpresion=0;
		cantidadOrdenesTitulo=0;
		documentoFinal		= "Venta_Titulos.xls";
		
		_res.addHeader("Content-Disposition","attachment;filename="+documentoFinal); 
		_res.setContentType("application/x-download"); 
		
		ServletOutputStream os=_res.getOutputStream();
		workbook.write(os); // excel

		os.flush();	
			
	}		
}
