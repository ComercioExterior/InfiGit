/**
 * 
 */
package models.custodia.transacciones.pago_cupones.consulta;

import java.text.NumberFormat;
import java.util.HashMap;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase que muestra por proceso las ordenes relacionadas a pago de cupones por titulo
 * @author eel
 */
public class PagoCuponesConsultaBrowse extends MSCModelExtend{
		public void execute() throws Exception {
					
		//DAO a utilizar
		ProcesosDAO cupones	= new ProcesosDAO(_dso);
		DataSet _resumen= new DataSet();
		//int cantCheques=0,cantNacional=0,cantSwift=0,cantOC=0,totalGeneralCant=0;
		int cantEnEspera[]= new int [4];
		int cantAplicadas[]= new int [4];
		int cantRechazadas[]= new int [4];
		int totalGeneralCantX[]= new int [4];
		int totalGeneralCantY[]= new int [4];
		double montoEnEspera[]= new double [4];
		double montoAplicada[]= new double [4];
		double montoRechazada[] = new double [4];
		double totalGeneralMontoX[]=new double [4];
		double totalGeneralMontoY[]=new double [4];
		double totalGeneral=0;
		
		NumberFormat nf= NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		
		String parametrosRequest[] = _req.getParameter("ejecucion_id").split("-");
		
		DataSet _titulo = new DataSet();
		
		_req.getSession().setAttribute("infi.exportar.transaccion",parametrosRequest[1]);
		if(parametrosRequest[1].equals(TransaccionNegocio.CUSTODIA_COMISIONES))
		{
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			ordenDAO.ListarComisionesProceso(Long.parseLong(parametrosRequest[0]));
			
			//Publicamos el Dataset
			storeDataSet("comisiones", ordenDAO.getDataSet());
			storeDataSet("calculo",new DataSet());
			
			//Se monta en sesion dataset
			_req.getSession().setAttribute("eventos.excel", ordenDAO.getDataSet());
				
			if(ordenDAO.getDataSet().count()==0)
			{
				_titulo.append("div", java.sql.Types.VARCHAR);
				_titulo.addNew();
				_titulo.setValue("div","none");
			}else{
				
				_titulo.append("div", java.sql.Types.VARCHAR);
				_titulo.addNew();
				_titulo.setValue("div","block");
			}
			//Se publica
			storeDataSet("titulo", _titulo);
			
//--------------------------------------------------------------------------------------------------------
			DataSet _comisionesP = new DataSet();
			
			if(ordenDAO.getDataSet().count()>0)
			{	
				_comisionesP = ordenDAO.getDataSet();
				_comisionesP.first();
				while(_comisionesP.next())
				{
					if(_comisionesP.getValue("status_operacion").equals("EN ESPERA")){
						montoEnEspera[0]=Double.valueOf(_comisionesP.getValue("monto_operacion"))+montoEnEspera[0];
						cantEnEspera[0]++;
					}
					if(_comisionesP.getValue("status_operacion").equals("APLICADA")){
						montoAplicada[0]=Double.valueOf(_comisionesP.getValue("monto_operacion"))+montoAplicada[0];
						cantAplicadas[0]++;	
					}
					if(_comisionesP.getValue("status_operacion").equals("RECHAZADA")){
						montoRechazada[0]=Double.valueOf(_comisionesP.getValue("monto_operacion"))+montoRechazada[0];
						cantRechazadas[0]++;	
					}
					totalGeneralMontoX[0]=montoEnEspera[0]+montoAplicada[0]+montoRechazada[0];
					totalGeneralCantX[0]=cantEnEspera[0]+cantAplicadas[0]+cantRechazadas[0];
				}//while
			}// del if recorrido del dataSet
			
			_resumen.append("cantenespera", java.sql.Types.VARCHAR);
			_resumen.addNew();
			_resumen.setValue("cantenespera", String.valueOf(cantEnEspera[0]));
			_resumen.append("montoenespera", java.sql.Types.VARCHAR);
			_resumen.setValue("montoenespera", String.valueOf(nf.format(montoEnEspera[0])));
			
			_resumen.append("cantaplicadas", java.sql.Types.VARCHAR);
			_resumen.setValue("cantaplicadas", String.valueOf(cantAplicadas[0]));
			_resumen.append("montoaplicada", java.sql.Types.VARCHAR);
			_resumen.setValue("montoaplicada", String.valueOf(nf.format(montoAplicada[0])));
			
			_resumen.append("cantrechazadas", java.sql.Types.VARCHAR);
			_resumen.setValue("cantrechazadas", String.valueOf(cantRechazadas[0]));
			_resumen.append("montorechazada", java.sql.Types.VARCHAR);
			_resumen.setValue("montorechazada", String.valueOf(nf.format(montoRechazada[0])));
			
			_resumen.append("totalgeneralcant", java.sql.Types.VARCHAR);
			_resumen.setValue("totalgeneralcant", String.valueOf(totalGeneralCantX[0]));
			_resumen.append("totalgeneralmonto", java.sql.Types.VARCHAR);
			_resumen.setValue("totalgeneralmonto", String.valueOf(nf.format(totalGeneralMontoX[0])));
			
//--------------------------------------------------------------------------------------------------------			
			//Redireccionamos a un nuevo HTM
			_config.template="comisiones.htm";
		}
		
		else{
			
//			Se listan las ordenes de pago de cupon por proceso
			cupones.listarOrdenesPorProceso(Long.parseLong(parametrosRequest[0]));
			
			//Se clona el Dataset para gestionar data
			DataSet _procesos = new DataSet();
							
			if(cupones.getDataSet().count()>0)
			{
				_procesos = cupones.getDataSet();
				//_procesos.removeRow(1);
				_procesos.first();
				HashMap<String,String> procesoHashMap = new HashMap<String, String>();
				while(_procesos.next())
				{

					if(_procesos.getValue("proceso_id")==null || _procesos.getValue("proceso_id").equals("") || _procesos.getValue("proceso_id").equals(" ")){
						
						_procesos.setValue("disable", "none");
					}
					else if(procesoHashMap.containsKey(_procesos.getValue("proceso_id")))
					{
						
						_procesos.setValue("disable", "none");
					}else
					{
						_procesos.setValue("disable", "block");						
						procesoHashMap.put(_procesos.getValue("proceso_id"), "proceso");
					}
					
					if(_procesos.getValue("tipo_instruccion_id")!=null){
						if(_procesos.getValue("tipo_instruccion_id").equals("CHEQUE")){
							//montoCH=Double.valueOf(_procesos.getValue("monto_operacion"))+montoCH;
							//cantCheques++;
							if(_procesos.getValue("status_operacion").equals("EN ESPERA")){
								montoEnEspera[0]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoEnEspera[0];
								cantEnEspera[0]++;
							}
							if(_procesos.getValue("status_operacion").equals("APLICADA")){
								montoAplicada[0]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoAplicada[0];
								cantAplicadas[0]++;	
							}
							if(_procesos.getValue("status_operacion").equals("RECHAZADA")){
								montoRechazada[0]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoRechazada[0];
								cantRechazadas[0]++;	
							}
							totalGeneralMontoX[0]=montoEnEspera[0]+montoAplicada[0]+montoRechazada[0];
							totalGeneralCantX[0]=cantEnEspera[0]+cantAplicadas[0]+cantRechazadas[0];
							
						}//Cheque
						if(_procesos.getValue("tipo_instruccion_id").equals("SWIFT")){
							//montoSW=Double.valueOf(_procesos.getValue("monto_operacion"))+montoSW;
							//cantSwift++;
							if(_procesos.getValue("status_operacion").equals("EN ESPERA")){
								montoEnEspera[1]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoEnEspera[1];
								cantEnEspera[1]++;
							}
							if(_procesos.getValue("status_operacion").equals("APLICADA")){
								montoAplicada[1]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoAplicada[1];
								cantAplicadas[1]++;	
							}
							if(_procesos.getValue("status_operacion").equals("RECHAZADA")){
								montoRechazada[1]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoRechazada[1];
								cantRechazadas[1]++;	
							}
							totalGeneralMontoX[1]=montoEnEspera[1]+montoAplicada[1]+montoRechazada[1];
							totalGeneralCantX[1]=cantEnEspera[1]+cantAplicadas[1]+cantRechazadas[1];
							
						}//Swift
						if(_procesos.getValue("tipo_instruccion_id").equals("NACIONAL")){
							//montoNA=Double.valueOf(_procesos.getValue("monto_operacion"))+montoNA;
							//cantNacional++;
							if(_procesos.getValue("status_operacion").equals("EN ESPERA")){
								montoEnEspera[2]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoEnEspera[2];
								cantEnEspera[2]++;
							}
							if(_procesos.getValue("status_operacion").equals("APLICADA")){
								montoAplicada[2]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoAplicada[2];
								cantAplicadas[2]++;	
							}
							if(_procesos.getValue("status_operacion").equals("RECHAZADA")){
								montoRechazada[2]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoRechazada[2];
								cantRechazadas[2]++;	
							}
							totalGeneralMontoX[2]=montoEnEspera[2]+montoAplicada[2]+montoRechazada[2];
							totalGeneralCantX[2]=cantEnEspera[2]+cantAplicadas[2]+cantRechazadas[2];
							
						}//Nacional
						if(_procesos.getValue("tipo_instruccion_id").equals("OPERACION DE CAMBIO")){
							//montoOC=Double.valueOf(_procesos.getValue("monto_operacion"))+montoOC;
							//cantOC++;
							if(_procesos.getValue("status_operacion").equals("EN ESPERA")){
								montoEnEspera[3]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoEnEspera[3];
								cantEnEspera[3]++;
							}
							if(_procesos.getValue("status_operacion").equals("APLICADA")){
								montoAplicada[3]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoAplicada[3];
								cantAplicadas[3]++;	
							}
							if(_procesos.getValue("status_operacion").equals("RECHAZADA")){
								montoRechazada[3]=Double.valueOf(_procesos.getValue("monto_operacion"))+montoRechazada[3];
								cantRechazadas[3]++;	
							}
							totalGeneralMontoX[3]=montoEnEspera[3]+montoAplicada[3]+montoRechazada[3];
							totalGeneralCantX[3]=cantEnEspera[3]+cantAplicadas[3]+cantRechazadas[3];
							
						}//Operaciones de cambio
						//totalGeneralMonto=montoCH+montoSW+montoNA+montoOC;
						//totalGeneralCant=cantCheques+cantSwift+cantNacional+cantOC;
						
					}//del if tipo de Instruccion . . . 
				}
					
				_resumen.append("cantaplicadas0", java.sql.Types.VARCHAR);
				_resumen.addNew();
				
				for (int i=0;i<=3;++i)
				{
					totalGeneralMontoY[0]=totalGeneralMontoY[0]+montoEnEspera[i];
					totalGeneralCantY[0]=totalGeneralCantY[0]+cantEnEspera[i];
					totalGeneralMontoY[1]=totalGeneralMontoY[1]+montoAplicada[i];
					totalGeneralCantY[1]=totalGeneralCantY[1]+cantAplicadas[i];
					totalGeneralMontoY[2]=totalGeneralMontoY[2]+montoRechazada[i];
					totalGeneralCantY[2]=totalGeneralCantY[2]+cantRechazadas[i];
					totalGeneral=totalGeneral+totalGeneralMontoX[i];
				}
					
				}
				for (int i=0;i<=3;++i)
				{
					if (i==0)
					{
						_resumen.setValue("cantaplicadas".concat(String.valueOf(i)), String.valueOf(cantAplicadas[i]));
						_resumen.append("montoaplicadas".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("montoaplicadas".concat(String.valueOf(i)), String.valueOf(montoAplicada[i]));
					
						_resumen.append("cantenespera".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("cantenespera".concat(String.valueOf(i)), String.valueOf(nf.format(cantEnEspera[i])));
						_resumen.append("montoenespera".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("montoenespera".concat(String.valueOf(i)), String.valueOf(nf.format(montoEnEspera[i])));
					
						_resumen.append("cantrechazadas".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("cantrechazadas".concat(String.valueOf(i)), String.valueOf(nf.format(cantRechazadas[i])));
						_resumen.append("montorechazadas".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("montorechazadas".concat(String.valueOf(i)), String.valueOf(nf.format(montoRechazada[i])));
						
						_resumen.append("totalgeneralcantX".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("totalgeneralcantX".concat(String.valueOf(i)), String.valueOf(totalGeneralCantX[i]));
						
						_resumen.append("totalgeneralmontoX".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("totalgeneralmontoX".concat(String.valueOf(i)), String.valueOf(totalGeneralMontoX[i]));
						
						_resumen.append("totalgeneralmontoY".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("totalgeneralmontoY".concat(String.valueOf(i)), String.valueOf(totalGeneralMontoY[i]));
												
						_resumen.append("totalgeneralcantY".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("totalgeneralcantY".concat(String.valueOf(i)), String.valueOf(totalGeneralCantY[i]));
						
					}
					if (i>0){
						_resumen.append("cantaplicadas".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("cantaplicadas".concat(String.valueOf(i)), String.valueOf(cantAplicadas[i]));
						_resumen.append("montoaplicadas".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("montoaplicadas".concat(String.valueOf(i)), String.valueOf(montoAplicada[i]));
					
						_resumen.append("cantenespera".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("cantenespera".concat(String.valueOf(i)), String.valueOf(nf.format(cantEnEspera[i])));
						_resumen.append("montoenespera".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("montoenespera".concat(String.valueOf(i)), String.valueOf(nf.format(montoEnEspera[i])));
					
						_resumen.append("cantrechazadas".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("cantrechazadas".concat(String.valueOf(i)), String.valueOf(nf.format(cantRechazadas[i])));
						_resumen.append("montorechazadas".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("montorechazadas".concat(String.valueOf(i)), String.valueOf(nf.format(montoRechazada[i])));
						
						_resumen.append("totalgeneralcantX".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("totalgeneralcantX".concat(String.valueOf(i)), String.valueOf(totalGeneralMontoX[i]));
						
						_resumen.append("totalgeneralmontoX".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("totalgeneralmontoX".concat(String.valueOf(i)), String.valueOf(totalGeneralMontoX[i]));
						
						_resumen.append("totalgeneralmontoY".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("totalgeneralmontoY".concat(String.valueOf(i)), String.valueOf(totalGeneralMontoY[i]));
												
						_resumen.append("totalgeneralcantY".concat(String.valueOf(i)), java.sql.Types.VARCHAR);
						_resumen.setValue("totalgeneralcantY".concat(String.valueOf(i)), String.valueOf(totalGeneralCantY[i]));
						
					}
				}// del for
				_resumen.append("totalgeneral", java.sql.Types.VARCHAR);
				_resumen.setValue("totalgeneral", String.valueOf(totalGeneral));
				
						
			//Se publica el dataset
			storeDataSet("calculo",_procesos);
			storeDataSet("comisiones", new DataSet());
				

			//Se monta en sesion dataset
			_req.getSession().setAttribute("eventos.excel", _procesos);	
			
			storeDataSet("titulo", _titulo);
		}//fin else
		storeDataSet("resumen",_resumen);
		
	}//fin execute
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		if (flag)
		{
				if (_req.getParameter("ejecucion_id")==null)		
				{
					_record.addError("Proceso","Debe seleccionar un proceso para continuar");
					flag = false;
				}
		}
		return flag;
	}
}
