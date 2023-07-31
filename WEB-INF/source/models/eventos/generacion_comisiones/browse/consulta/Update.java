package models.eventos.generacion_comisiones.browse.consulta;

import java.math.BigDecimal;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;

import models.msc_utilitys.MSCModelExtend;

public class Update extends MSCModelExtend {
	private String tasaNueva[]=null;
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		/*
		 * tipo=procesar se raliza el proceso de cobro de la comision calculada
		 * */
		if(_req.getParameter("tipo").equals("procesar")){
				
		}
		/*
		 * tipo=edit esta accion se realiza actulizacion del porcentaje de tasa del coboro de comisiones
		 */
		if(_req.getParameter("tipo").equals("edit")){
			String ordenOperacionId[]=_req.getParameterValues("ordene_operacion_id");
			OrdenDAO ordenDAO=new OrdenDAO(_dso);
			OrdenOperacion ordenOperacion=null;
			OperacionDAO operacionDAO=new OperacionDAO(_dso);
			for(int i=0; i<ordenOperacionId.length;i++){
				ordenDAO.listarOrdenOperacion(ordenOperacionId[i]);
				if(ordenDAO.getDataSet().count()>0){
					ordenDAO.getDataSet().next();
					ordenOperacion=new OrdenOperacion();
					ordenOperacion.setIdOperacion(Long.parseLong(ordenDAO.getDataSet().getValue("ordene_operacion_id")));
					ordenOperacion.setIdTitulo(ordenDAO.getDataSet().getValue("titulo_id"));
					ordenOperacion.setTasa(new BigDecimal(ordenDAO.getDataSet().getValue("tasa")));
					ordenOperacion.setMontoOperacion(new BigDecimal(Double.parseDouble(ordenDAO.getDataSet().getValue("monto_operacion"))));
					ordenOperacion.setIdOrden(Long.parseLong(ordenDAO.getDataSet().getValue("ordene_id")));
					ordenOperacion.setStatusOperacion(ordenDAO.getDataSet().getValue("status_operacion"));
					
					//Verifica si son diferentes
					if(ordenOperacion.getTasa().compareTo(new BigDecimal(this.tasaNueva[i]))!= 0){
						ordenOperacion.recalcularMonto(new BigDecimal(this.tasaNueva[i]));
						operacionDAO.modificarOperacion(ordenOperacion);
					}
				}
			}
			_config.nextAction="generacion_comisiones-browse";
		}
	}
	public boolean isValid()throws Exception{
		boolean valido=super.isValid();
		if(valido){
			this.tasaNueva=_req.getParameterValues("tasa");
			for(int i=0;i<tasaNueva.length;i++){
				try{
					tasaNueva[i]=tasaNueva[i].replace(",",".");
					Double tasa=Double.parseDouble(tasaNueva[i]);
					if(tasa>100){
						_record.addError("Tasa","La Tasa No Debe Ser Mayor 100%");
						valido=false;
					}
					if(tasa<0){
						_record.addError("Tasa","La Tasa No Debe Ser Menor 0%");
						valido=false;
					}
				}catch (NumberFormatException e) {
					// TODO: handle exception
					_record.addError("Tasa","Posee Tasa con Formato Incorrecto");
					valido=false;
				}
			}
		}
		return valido;
	}

}
