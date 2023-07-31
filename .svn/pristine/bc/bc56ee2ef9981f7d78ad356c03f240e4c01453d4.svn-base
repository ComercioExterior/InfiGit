package models.liquidacion.consulta.conciliacion_cobranza;

import com.bdv.infi.dao.*;
import com.bdv.infi.logic.interfaces.StatusOrden;

import megasoft.DataSet;
import models.msc_utilitys.*;


/**
 *
 * @author elaucho
 *
 */
public class ConciliacionCobranzaFilter extends MSCModelExtend {
    /**
     * Ejecuta la transaccion del modelo
     */
    public void execute() throws Exception {
        //Instanciacion de clases
        UnidadInversionDAO unInv = new UnidadInversionDAO(_dso);
        OrdenDAO ordsta = new OrdenDAO(_dso);
     		
		//Se listan las unidades de inversion para el filtro
        unInv.listarUIParaConciliaciones();
                        
        storeDataSet("unidad_inversion", unInv.getDataSet());

        //Mostrar en el filtro los Blotters
        if (_req.getParameter("unInv") != null) {
        	
            //Listamos blotter
            unInv.listarBloterPorUi(Long.parseLong(_req.getParameter("unInv")));

            //Publicamos dataset
            storeDataSet("bloter", unInv.getDataSet());

            //Creamos un dataset manual para la unidad de inversion
            DataSet _unidadSelecc = new DataSet();
            _unidadSelecc.append("unidad_seleccionada", java.sql.Types.VARCHAR);
            _unidadSelecc.addNew();
            _unidadSelecc.setValue("unidad_seleccionada",
                _req.getParameter("unInv"));

            //Publicamos dataset
            storeDataSet("unidad_seleccionada", _unidadSelecc);
        } else {
            //Creamos un dataset manual para blotter
            DataSet _bloter = new DataSet();
            _bloter.append("bloter_id", java.sql.Types.VARCHAR);
            _bloter.append("bloter_descripcion", java.sql.Types.VARCHAR);
            _bloter.addNew();
            _bloter.setValue("bloter_id", "");
            _bloter.setValue("bloter_descripcion", "");

            //Publicamos dataset
            storeDataSet("bloter", _bloter);

            //Creamos un dataset manual para la unidad de inversion
            DataSet _unidadSelecc = new DataSet();
            _unidadSelecc.append("unidad_seleccionada", java.sql.Types.VARCHAR);
            _unidadSelecc.addNew();
            _unidadSelecc.setValue("unidad_seleccionada", "");
            storeDataSet("unidad_seleccionada", _unidadSelecc);
        } //fin else

        //Mostrar en el filtro los Status
        ordsta.listarStatusOrden();

        //Publicamos dataset
        storeDataSet("status", ordsta.getDataSet());
    }
}
