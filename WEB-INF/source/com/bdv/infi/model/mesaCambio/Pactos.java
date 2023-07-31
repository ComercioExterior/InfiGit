package com.bdv.infi.model.mesaCambio;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.bdv.infi.dao.MesaCambioDAO;
import com.bdv.infi.dao.TasaCambioDAO;
import com.bdv.infi.data.ClientesMesa;
import com.bdv.infi.data.ClientesMesaBancario;

/**
 * 
 * @author CT24667
 * 
 */
public class Pactos extends MesaCambioDAO {

	List<ClientesMesaBancario> lst = new ArrayList<ClientesMesaBancario>();
	protected TasaCambioDAO tscDao;
	protected DataSource _ds;
	String fecha;

	public Pactos(DataSource ds) {
		super(ds);
		this._ds = ds;
	}
	public int Iniciar(String idOperacion, String jornada, String monto, 
			String montoBase, String contravalor, String tasa , 
			String tipoPacto, String codDemand, String codOferta) throws Exception {
			int mensaje = 0;
			NotificarOD ntf = new NotificarOD();
			mensaje = 2;
			if (ntf.ReportarPacto(idOperacion,jornada,monto,montoBase,contravalor,
					tasa,tipoPacto,codDemand,codOferta, _ds)) {
				mensaje = 1;
			}

		

		return mensaje;
	}
//	public int Iniciar(String tipoOperacion, String fecha, String estatusEnvio, String tipoMovimiento, String idOrdenes) throws Exception {
//		int mensaje = 0;
//		this.lst = ListarEnvioInterbancario(tipoOperacion, fecha, estatusEnvio, tipoMovimiento, idOrdenes);
//
//		if (this.lst.size() > 0) {
//			NotificarOD ntf = new NotificarOD();
//			mensaje = 2;
//			if (ntf.ReportarPacto(this.lst, _ds)) {
//				mensaje = 1;
//			}
//
//		}
//
//		return mensaje;
//	}

	protected void _vender() {
		Vender ntf = new Vender();

	}

}