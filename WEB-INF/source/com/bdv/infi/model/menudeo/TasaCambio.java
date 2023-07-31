package com.bdv.infi.model.menudeo;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class TasaCambio extends AutorizacionStub {

	private List<Monedas> lstMoneda = new ArrayList<Monedas>();

	public TasaCambio() throws Exception {
		String WS = Stub.TASASCAMBIO();		
		Cargar(WS,"MONEDA");
	}
	
	public void Ejecutar(String Funcion) throws Exception{
		TasaCambio tsc= new TasaCambio();
		Method Fn = TasaCambio.class.getMethod(Funcion,int.class);
		Fn.invoke(tsc, 0);
	}
	
	

	public void lecturaBcv() {

		BigDecimal CompraBig;
		BigDecimal VentaBig;

		Monedas moneda = new Monedas();
		org.w3c.dom.Node element = nodo;

		String CodigoISO = ((org.w3c.dom.Element) element).getAttribute("CODIGO");
		String Compra = ((org.w3c.dom.Element) element).getElementsByTagName("COMPRA").item(0).getTextContent();
		String Venta = ((org.w3c.dom.Element) element).getElementsByTagName("VENTA").item(0).getTextContent();

		VentaBig = new BigDecimal(Venta);
		CompraBig = new BigDecimal(Compra);

		moneda.setSiglas(CodigoISO);
		moneda.setVenta(VentaBig.setScale(8, RoundingMode.DOWN));
		moneda.setCompra(CompraBig.setScale(8, RoundingMode.DOWN));
		lstMoneda.add(moneda);
		

	}
/**
 * clave para el manejo de los operaciones menudeo(Compra)
 */
	public void generarDocumento() {

		org.w3c.dom.Node ele = nodo;
		String codigo = ((org.w3c.dom.Element) ele).getAttribute("CODIGO");
		ele.equals(ConstantesGenerales.SIGLAS_MONEDA_DOLAR);

		String compraUSD = ((org.w3c.dom.Element) ele).getElementsByTagName("COMPRA").item(0).getTextContent();

		Documento.put(codigo + ConstantesGenerales.COD_COMPRA, compraUSD);
		String ventaUSD = ((org.w3c.dom.Element) ele).getElementsByTagName("VENTA").item(0).getTextContent();

		Documento.put(codigo + ConstantesGenerales.COD_VENTA, ventaUSD);

		ele.equals(ConstantesGenerales.SIGLAS_MONEDA_EURO);
		String compraEUR = ((org.w3c.dom.Element) ele).getElementsByTagName("COMPRA").item(0).getTextContent();

		Documento.put(codigo + ConstantesGenerales.COD_COMPRA, compraEUR);
		String ventaEUR = ((org.w3c.dom.Element) ele).getElementsByTagName("VENTA").item(0).getTextContent();

		Documento.put(codigo + ConstantesGenerales.COD_VENTA, ventaEUR);
	}
	
	public List<Monedas> ListarMoneda(){
		return lstMoneda;
	}
	
}
