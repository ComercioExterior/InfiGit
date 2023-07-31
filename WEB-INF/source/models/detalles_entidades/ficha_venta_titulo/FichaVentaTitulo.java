package models.detalles_entidades.ficha_venta_titulo;

import java.util.Date;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.util.Utilitario;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;



public class FichaVentaTitulo extends MSCModelExtend {
	
	private long ordenID;
	
	
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
	
		OrdenDAO ordenDAO=new OrdenDAO(_dso);
		ClienteCuentasDAO clienteCuentas=new ClienteCuentasDAO(_dso);
		TitulosDAO tituloDAO=new TitulosDAO(_dso);
		
		//String ordeneID=_record.getValue("ID_ORDEN");		
		
		String ordeneID=_req.getParameter("ordene_id");
		
		ordenDAO.listarOrdenPorId(Long.parseLong(ordeneID),null);
		
		
		if(ordenDAO.getDataSet().count()>0){
			ordenDAO.getDataSet().next();
									
			String fechaValorString=ordenDAO.getDataSet().getValue("fecha_valor");
			
			String ordeneId=ordenDAO.getDataSet().getValue("ORDENE_ID");
			ordenID=Long.parseLong(ordeneId);
			Date fechaActual=new Date();
			String fechaActualString=Utilitario.DateToString(fechaActual, "dd/MM/yyyy");
			
			String [] fechas=Utilitario.fechaFormateada(fechaActualString);
			
												
			DataSet dsEncabezado=new DataSet();
			dsEncabezado.addNew();
			
			dsEncabezado.append("fecha_dia",java.sql.Types.VARCHAR);
			dsEncabezado.setValue("fecha_dia",fechas[0]);
			
			dsEncabezado.append("fecha_mes",java.sql.Types.VARCHAR);
			dsEncabezado.setValue("fecha_mes",fechas[1]);
			
			dsEncabezado.append("fecha_anio",java.sql.Types.VARCHAR);
			dsEncabezado.setValue("fecha_anio",fechas[2]);
			
			dsEncabezado.append("fecha_valor",java.sql.Types.VARCHAR);
			dsEncabezado.setValue("fecha_valor",fechaValorString);
			
			dsEncabezado.append("ORDENE_ID",java.sql.Types.VARCHAR);			
			dsEncabezado.setValue("ORDENE_ID",ordeneId);
									
			clienteCuentas.listarCuentaClientePorOrden(ordenID);
			
			if(clienteCuentas.getDataSet().count()>0){
				clienteCuentas.getDataSet().next();
				
				String aba=clienteCuentas.getDataSet().getValue("ctecta_bcocta_aba");
				dsEncabezado.append("aba",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("aba",aba);
				
				
				String nombre=clienteCuentas.getDataSet().getValue("nombre");
				dsEncabezado.append("nombre",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("nombre",nombre);
				
				String  nacionalidad=clienteCuentas.getDataSet().getValue("nacionalidad");
				dsEncabezado.append("nacionalidad",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("nacionalidad",nacionalidad);
				
				String cedula=clienteCuentas.getDataSet().getValue("cedula");
				dsEncabezado.append("cedula",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("cedula",cedula);
				
				String banco=clienteCuentas.getDataSet().getValue("banco");
				dsEncabezado.append("banco",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("banco",banco);
				
				String direccion_banco=clienteCuentas.getDataSet().getValue("direccion_banco");
				dsEncabezado.append("direccion_banco",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("direccion_banco",direccion_banco);
				
				String  swift=clienteCuentas.getDataSet().getValue("swift");
				dsEncabezado.append("swift",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("swift",swift);
				
				String nombre_beneficiario=clienteCuentas.getDataSet().getValue("nombre_beneficiario");
				dsEncabezado.append("nombre_beneficiario",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("nombre_beneficiario",nombre_beneficiario);
				
				String cuenta_beneficiario=clienteCuentas.getDataSet().getValue("cuenta_beneficiario");
				dsEncabezado.append("cuenta_beneficiario",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("cuenta_beneficiario",cuenta_beneficiario);
				
				//-----------DATOS DEL BANCO INTERMEDIARIO---------------------------------------------------------------				
				dsEncabezado.append("banco_intermediario",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("banco_intermediario","");				
				
				dsEncabezado.append("bic_intermediario",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("bic_intermediario","");
				
				dsEncabezado.append("aba_intermediario",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("aba_intermediario","");
				
				dsEncabezado.append("dir_intermediario",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("dir_intermediario","");
				
				dsEncabezado.append("telefono_intermediario",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("telefono_intermediario","");		
				
				dsEncabezado.append("cuenta_en_intermediario",java.sql.Types.VARCHAR);
				dsEncabezado.setValue("cuenta_en_intermediario","");					
			
				//Si tiene intermediario
				if(clienteCuentas.getDataSet().getValue("bic_intermediario")!=null && !clienteCuentas.getDataSet().getValue("bic_intermediario").equals("")){
					
					String banco_intermediario=clienteCuentas.getDataSet().getValue("banco_intermediario");					
					dsEncabezado.setValue("banco_intermediario",banco_intermediario);
					
					String bic_intermediario=clienteCuentas.getDataSet().getValue("bic_intermediario");					
					dsEncabezado.setValue("bic_intermediario",bic_intermediario);
					
					String aba_intermediario=clienteCuentas.getDataSet().getValue("aba_intermediario");					
					dsEncabezado.setValue("aba_intermediario",aba_intermediario);
					
					String dir_intermediario=clienteCuentas.getDataSet().getValue("dir_intermediario");					
					dsEncabezado.setValue("dir_intermediario",dir_intermediario);
					
					String telefono_intermediario=clienteCuentas.getDataSet().getValue("telefono_intermediario");					
					dsEncabezado.setValue("telefono_intermediario",telefono_intermediario);		
					
					String cuenta_en_intermediario=clienteCuentas.getDataSet().getValue("cuenta_en_intermediario");					
					dsEncabezado.setValue("cuenta_en_intermediario",cuenta_en_intermediario);							
				}
				//-------------------------------------------------------------------------------------------------------
				
			}
			storeDataSet("encabezado", dsEncabezado);
		}
		
		DataSet tabla=null;
		tituloDAO.listarOrdenesVentaTitulos(ordenID);
		
		if(tituloDAO.getDataSet().count()>0){
			tituloDAO.getDataSet().next();			
			tabla=tituloDAO.getDataSet();					
		}
		
		storeDataSet("tabla",tabla);					
	}	

}
