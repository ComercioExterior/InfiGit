package models.generacion_archivo_bash.creditos;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletOutputStream;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.enterprisedt.util.debug.Logger;

import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

public class GenerarArchivoDeCredito extends MSCModelExtend{
	@Override
	public void execute() throws Exception {
		
		generarCapital();
	}
	
	private void generarCapital() throws Exception{
		Date fecha = new Date();
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		
		String unidadDeInversion=_record.getValue("undinv_id");
		int enteroUI=Integer.parseInt(unidadDeInversion);
		String tipoDeArchivo=_record.getValue("tipodearchivo");
		String tipoDeOperacion=_record.getValue("tipodeoperacion");
		String nombreArchivo="";
		nombreArchivo=tipoDeArchivo+tipoDeOperacion+".txt";
		
		if (tipoDeArchivo.equals("Capital"))
		{
			tipoDeArchivo="CAP";
		}else if (tipoDeArchivo.equals("Comision")){
			tipoDeArchivo="COM";
		}
		if (tipoDeOperacion.equals("Debito"))
		{
			tipoDeOperacion="D";
			
		}else if (tipoDeOperacion.equals("Credito")){
			tipoDeOperacion="C";
		}
				
		System.out.println("Tipo Operacion: "+tipoDeOperacion+" ,Tipo de Archivo: "+tipoDeArchivo+" Nombre de Archivo: "+nombreArchivo);
			
		try {
			OrdenDAO orden = new OrdenDAO(_dso);
						
			if (tipoDeArchivo != null && tipoDeOperacion != null){
				orden.listarOrdenesPorPagar(enteroUI,tipoDeOperacion,tipoDeArchivo,"to_date('"+formatoDeFecha.format(fecha)+"','"+ConstantesGenerales.FORMATO_FECHA4+"'))");
				
			}// del if
		}catch (Exception e){
			e.printStackTrace();
			
		}// del Catch
	}// del metodo
}// del la clase
