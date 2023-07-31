package models.intercambio.recepcion.cierre_unidad_inv_simadi_taquilla;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;

public class Confirm extends MSCModelExtend {
	
	private DataSet _ordenes;
	private DataSet _resumen;
		
	OrdenDAO ordenDAO;
	private long idUnInv;
	private String nombreUnInv;
	private String mensajeConfirm;
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		
		mensajeConfirm="Esta seguro de querer cerrar la Unidad de Inversion seleccionada ?";
				
		_resumen=new DataSet();			
		_resumen.append("undinv_nombre",java.sql.Types.VARCHAR);
		_resumen.append("undinv_id",java.sql.Types.VARCHAR);	
		_resumen.append("mensaje",java.sql.Types.VARCHAR);
		
		_resumen.addNew();
		_resumen.setValue("mensaje",mensajeConfirm);
		_resumen.setValue("undinv_nombre",nombreUnInv);	
		_resumen.setValue("undinv_id",String.valueOf(idUnInv));
		
		
		storeDataSet("resumen", _resumen);
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		
		System.out.println("INGRESA ");
			boolean flag=true;
			
			ordenDAO = new OrdenDAO(_dso);
			idUnInv=Long.parseLong(_record.getValue("undinv_id"));	
			nombreUnInv=_record.getValue("undinv_nombre");			
			ordenDAO.listarOrdenesPorEnviarBCVPorVentaTaquilla(ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR_TAQUILLA,idUnInv,ConstantesGenerales.SIN_VERIFICAR, StatusOrden.CRUZADA);
			_ordenes=ordenDAO.getDataSet();		
			
			if(_ordenes.count()>0) {
				_record.addError("Para su Informacion","No se puede cerrar la Unidad de Inversion ya que la misma tiene ordenes que no se han notificado al BCV");
				flag=false;
			}			
			System.out.println("INGRESA ");
		return flag;
	}
}
