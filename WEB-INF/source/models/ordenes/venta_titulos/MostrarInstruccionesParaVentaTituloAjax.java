package models.ordenes.venta_titulos;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class MostrarInstruccionesParaVentaTituloAjax  extends MSCModelExtend {

	private DataSet instruccionVentaTitulo=new DataSet();
	private DataSet datosIntermediario=new DataSet();
	private ClienteCuentasDAO instruccionVentaTitDAO = null;
	private String idInstruccion=null;
	
	
	public void execute() throws Exception {
				
		instruccionVentaTitDAO=new ClienteCuentasDAO(_dso);
		try {
			// codigo de Ajax a ejecutar				
			ajaxCargarInstruccionesVentaTitulo();									
			datosIntermediario.append("visibilidad_intermediario",java.sql.Types.VARCHAR);			
			datosIntermediario.addNew();
			
			if(instruccionVentaTitulo.count()>0){
					
				instruccionVentaTitulo.first();
				while(instruccionVentaTitulo.next()){
					if(instruccionVentaTitulo.getValue("ctecta_bcoint_bic")!=null){											
						datosIntermediario.setValue("visibilidad_intermediario","block");
						datosIntermediario.append("ctecta_bcoint_bic",java.sql.Types.VARCHAR);
						datosIntermediario.setValue("ctecta_bcoint_bic",instruccionVentaTitulo.getValue("ctecta_bcoint_bic"));
						
						if(instruccionVentaTitulo.getValue("ctecta_bcoint_bco")!=null){																						
							datosIntermediario.append("ctecta_bcoint_bco",java.sql.Types.VARCHAR);
							datosIntermediario.setValue("ctecta_bcoint_bco",instruccionVentaTitulo.getValue("ctecta_bcoint_bco"));						
						}
						
						if(instruccionVentaTitulo.getValue("ctecta_bcoint_bco")!=null){																						
							datosIntermediario.append("ctecta_bcoint_bco",java.sql.Types.VARCHAR);
							datosIntermediario.setValue("ctecta_bcoint_bco",instruccionVentaTitulo.getValue("ctecta_bcoint_bco"));						
						}
						
						if(instruccionVentaTitulo.getValue("ctecta_bcoint_direccion")!=null){																						
							datosIntermediario.append("ctecta_bcoint_direccion",java.sql.Types.VARCHAR);
							datosIntermediario.setValue("ctecta_bcoint_direccion",instruccionVentaTitulo.getValue("ctecta_bcoint_direccion"));						
						}
					}else {
						datosIntermediario.setValue("visibilidad_intermediario","none");
					}										
					
				}
				
			}
			storeDataSet("datos_intermediario", datosIntermediario);
			storeDataSet("instrucciones_cuenta_swift",instruccionVentaTitulo);//Instrucciones de pago para cuentas nacional en dolares
		} catch (RuntimeException e) {
			e.printStackTrace();
        
		} catch (Exception e) {
			throw e;
		}
	}// fin execute

	public void ajaxCargarInstruccionesVentaTitulo() throws Exception{
				
			instruccionVentaTitDAO.browseClienteCuentas(null,String.valueOf(TipoInstruccion.CUENTA_SWIFT),null,idInstruccion,UsoCuentas.VENTA_TITULO);			
			instruccionVentaTitulo=instruccionVentaTitDAO.getDataSet();
															
	}
	
	public boolean isValid(){
		boolean flag=true;
		
		if(_req.getParameter("id_instruccion")==null || _req.getParameter("id_instruccion").equals("")){
		
			flag=false;
		}
		idInstruccion=_req.getParameter("id_instruccion");
		
		
		return flag;
	}
}
