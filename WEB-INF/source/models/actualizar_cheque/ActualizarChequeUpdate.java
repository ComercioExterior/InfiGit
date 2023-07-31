package models.actualizar_cheque;

import java.util.ArrayList;
import java.util.Iterator;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * 
 * @author elaucho
 */
public class ActualizarChequeUpdate extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		//Dao a utilizar
		GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
		OperacionDAO operacionDAO     = new OperacionDAO(_dso);
		OrdenDAO ordenDao             = new OrdenDAO(_dso);
		String idOperacionesArray[]   = _req.getParameterValues("operacion_id");
		String idOrdenArray[]   = _req.getParameterValues("orden_id");
		ArrayList<String> consultas   = new ArrayList<String>();
		
		if(_req.getParameter("esrecompra").equals("true"))
		{
			//Ejecutamos la transaccion de actualizacion 
			db.exec(_dso,operacionDAO.actualizarChequeoperacion( _req.getParameter("numerocheque"),Long.parseLong(_req.getParameter("id_operacion"))));
		
		}else
		{
			//Recorremos las operaciones a actualizar
			
			//for(int i=0;i<idOperacionesArray.length;i++){
				//Buscamos la orden relacion asociada a las ordenes originales
			
				Orden orden = ordenDao.listarOrden(Long.parseLong(idOrdenArray[0]), false, false, false, false, false);
				if (orden.getIdOrdenRelacionada() > 0 ){
					orden = ordenDao.listarOrden(orden.getIdOrdenRelacionada(), false, false, false, true, false);	
				}else{
					//Busca la orden de pago que tenga relación con la original
					long[] numerosOrden = ordenDao.listarNumeroOrdenIdRelacion(orden.getIdOrden());
					if (numerosOrden == null){
						throw new Exception("Error, no han sido encontradas ordenes de pago");
					}
					orden = ordenDao.listarOrden(numerosOrden[0], false, false, false, true, false);
				}								
				
				//Debe ser una orden de pago
				if (orden.getIdTransaccion().equals(TransaccionNegocio.ORDEN_PAGO)){
					ArrayList listaOperaciones = orden.getOperacion();
					Iterator it = listaOperaciones.iterator();
					OrdenOperacion op = new OrdenOperacion();
					while (it.hasNext()){
						op = (OrdenOperacion)it.next();
						consultas.add(operacionDAO.actualizarChequeoperacion( _req.getParameter("numerocheque"),op.getIdOperacion()));
					}
					//Actualizacion 811
					consultas.add(gestionPagoDAO.modificarIntruccion(Long.parseLong(_req.getParameter("proceso")), _req.getParameter("numerocheque")));
					//Se ejecuta el Execbatch
					db.execBatch(_dso,(String[]) consultas.toArray(new String[consultas.size()]));					
				}else{
					throw new Exception("La orden relacionada debe ser de pago");
				}
			//}
		}		
	}
}
