package models.custodia.consultas.clientes_titulos_exportar;

import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.util.Utilitario;

import megasoft.AbstractModel;
import megasoft.Logger;
/**
 * Clase Gen&eacute;rica que dado un c&oacute;digo espec&iacute;fico permite invocar un m&eacute;todo para una funci&oacute;n ajax dentro de una vista
 * @author elaucho
 */
public class Ajax extends AbstractModel {

	public void execute() throws Exception { 

			try {
				//codigo de Ajax a ejecutar
				int cod_ajax = Integer.parseInt(_req.getParameter("cod_ajax"));
								
				switch (cod_ajax) {
				case 1: 
					this.ajaxTitulosPorCliente();	
				break;
				
				case 2:	
					this.ajaxListarTitulos();
				break;
				}				
			} catch (RuntimeException e) {				
				Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			}
		}//fin execute
	
	/**
	 * M&eacute;todo que ejecuta la consulta de titulos por cliente para la funci&oacute;n ajax con c&oacute;digo = 1 (cod_ajax).
	 * Exporta los datos necesarios para llenar el combo de selecci&oacute;n de t&iacute;tulos en custodia de un cliente espec&iacute;fico.
	 * @throws Exception
	 */
	void ajaxTitulosPorCliente() throws Exception{
		
		CustodiaDAO custDAO = new CustodiaDAO(_dso);
		long idCliente =0;
		
		if(_req.getParameter("id_cliente")!=null && !_req.getParameter("id_cliente").equals(""))
			idCliente = Long.parseLong(_req.getParameter("id_cliente"));
		
		custDAO.listarTitulosEnCustodiaPorClienteAjax(idCliente,true);
		storeDataSet("datos", custDAO.getDataSet());		
	}//fin ajaxTitulosPorCliente
	/**
	 * M&eacute;todo que realiza una consulta de todos los t&iacute;tulos existentes (est&eacute;n o no en custodia)
	 * para la funci&oacute;n ajax con c&oacute;digo = 2 (cod_ajax)
	 * @throws Exception
	 */
	void ajaxListarTitulos() throws Exception{
		CustodiaDAO custDAO = new CustodiaDAO(_dso);		
		custDAO.listarTitulosEnCustodiaPorClienteAjax(0,false);
		storeDataSet("datos", custDAO.getDataSet());	
	}//fin ajaxListarTitulos
}
