package models.ordenes.consultas.ordenes_cliente;

import java.text.SimpleDateFormat;
import java.util.Date;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.OrdenesClienteDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi_toma_orden.dao.ClienteDAO;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);
		ClienteDAO clienteDAO = new ClienteDAO(null,_dso);
		
		long idCliente; 
		String nombre_cliente = null;
		String fecha_desde = null;
		String fecha_hasta = null;
		
		//REQUERIMIENTO EXPORTACION DE ORDENES POR CLIENTE
		//NM32454
		_record.setValue("ordenes_por_cliente", "1");
		
		if(_record.getValue("pick_cliente")!=null){
			if (_record.getValue("pick_cliente")!=null)
				nombre_cliente= _record.getValue("pick_cliente");
			    idCliente = Long.valueOf(_record.getValue("client_id")==null?"0":_record.getValue("client_id"));
			if (_record.getValue("fe_ord_desde")!=null)
				fecha_desde= _record.getValue("fe_ord_desde");
			if (_record.getValue("fe_ord_hasta")!=null)
				fecha_hasta= _record.getValue("fe_ord_hasta");
	
			//Realizar consulta
			confiD.listar(idCliente,fecha_desde, fecha_hasta,_record.getValue("transaccion"));
			//registrar los datasets exportados por este modelo
			storeDataSet("table", confiD.getDataSet());
			_config.template="table2.htm";
		}else{
			if (_record.getValue("pick_cliente")!=null)
				nombre_cliente= _record.getValue("pick_cliente");
		    idCliente = Long.valueOf(_record.getValue("client_id")==null?"0":_record.getValue("client_id"));			
			if (_record.getValue("fe_ord_desde")!=null)
				fecha_desde= _record.getValue("fe_ord_desde");
			if (_record.getValue("fe_ord_hasta")!=null)
				fecha_hasta= _record.getValue("fe_ord_hasta");
	
			//Realizar consulta
			confiD.listar(idCliente, fecha_desde, fecha_hasta,_record.getValue("transaccion"));
			//registrar los datasets exportados por este modelo
			storeDataSet("table", confiD.getDataSet());
		}
		
		_req.getSession().setAttribute("datos_ordenes_exportar-browse.framework.page.record",_record);
		
		storeDataSet("registros", confiD.getTotalRegistros());
		if(confiD.getTotalRegistros().getValue("t_registros").equalsIgnoreCase("0")){
			DataSet _cliente=new DataSet();
			String tipperId="", cedRif="";
			try {
				clienteDAO.listarPorId(idCliente);
				if(clienteDAO.getCliente()!=null){
					tipperId=clienteDAO.getCliente().getTipoPersona();
					cedRif=String.valueOf(clienteDAO.getCliente().getRifCedula());
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_cliente.append("client_nombre",java.sql.Types.VARCHAR);
			_cliente.append("tipper_id",java.sql.Types.VARCHAR);
			_cliente.append("client_cedrif",java.sql.Types.VARCHAR);
			_cliente.addNew();
			_cliente.setValue("client_nombre",nombre_cliente);
			_cliente.setValue("tipper_id",tipperId);
			_cliente.setValue("client_cedrif",cedRif);
			storeDataSet("table_2", _cliente);
		}else{
			DataSet _cliente=new DataSet();
			_cliente.append("client_nombre1",java.sql.Types.VARCHAR);
			_cliente.addNew();
			_cliente.setValue("client_nombre1",nombre_cliente);
			storeDataSet("table_2", _cliente);
		}
	}
	
	public boolean isValid() throws Exception
	{

		boolean flag = super.isValid();
		
		String fecha_desde=_record.getValue("fe_ord_desde");
		String fecha_hasta=_record.getValue("fe_ord_hasta");
		java.util.Date fecha = new Date();
		java.text.SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		
		if (flag)
		{

			if ((fecha_desde!=null)&&(fecha_hasta!=null))		
			{	
				Date fecha_1=formato.parse(fecha_desde);
				Date fecha_2=formato.parse(fecha_hasta);
				
				if (fecha_1.compareTo(fecha_2) >0)
				{  
					_record.addError("Fecha hasta","Este campo debe tener una fecha posterior o igual al de Fecha Desde");
						flag = false;
				}
				
				if (fecha_1.compareTo(fecha) >0)
				{  
					_record.addError("Fecha Desde","Este campo debe ser menor a la fecha actual:"+fecha);
						flag = false;
				}
				
				if (fecha_2.compareTo(fecha) >0)
				{  
					_record.addError("Fecha Hasta*","Este campo debe ser menor a la fecha actual:"+fecha);
						flag = false;
				}
				
			}

	}
		return flag;
	}
}