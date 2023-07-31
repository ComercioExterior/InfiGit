package models.bcv.mesas_operaciones;

import java.util.ArrayList;
import java.util.List;
import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.*;
import com.bdv.infi.model.mesaCambio.ConsultaOperaciones;
import com.bdv.infi.model.mesaCambio.OperacionesBCV;
import com.bdv.infi.model.mesaCambio.OperacionesOfertaDemandaBCV;

import java.lang.reflect.Method;

public class Browse extends MSCModelExtend {

	private DataSet _operaciones = new DataSet();
	List<OperacionesBCV> listaOperaciones;
	List<OperacionesOfertaDemandaBCV> listaOperacionesOD;

	public void execute() throws Exception {
		String jornadas = _record.getValue("jornada");
		int metodo = Integer.valueOf(_record.getValue("metodo"));

		try {
			ConsultaOperaciones consulta = new ConsultaOperaciones(jornadas,metodo);
			System.out.println("metodo : " + metodo);
			if (metodo == 1) {
				
				Method Fn = ConsultaOperaciones.class.getMethod("lecturaBcvOperaciones");
				consulta.procesar(consulta, Fn);
				this.listaOperaciones = consulta.ListarOperaciones();

				if (listaOperaciones != null) {
					iniciar_datos_sql(metodo);
				}

				List<String> lstcodigo = new ArrayList<String>();
				List<String> lstoferta = new ArrayList<String>();
				List<String> lstdemanda = new ArrayList<String>();
				List<String> lsttasa = new ArrayList<String>();
				List<String> lstestado = new ArrayList<String>();
				List<String> lstjornada = new ArrayList<String>();
				List<String> lstmonedaPacto = new ArrayList<String>();
				List<String> lstpactoMonto = new ArrayList<String>();
				List<String> lstmonedaBase = new ArrayList<String>();
				List<String> lstmontoPactoBase = new ArrayList<String>();
				List<String> lsttcPactoBase = new ArrayList<String>();
				List<String> lstofertante = new ArrayList<String>();
				List<String> lstdemandante = new ArrayList<String>();
				List<String> lsttipoPacto = new ArrayList<String>();

				for (OperacionesBCV lst : listaOperaciones) {
					System.out.println("lst.getCodigo() : " + lst.getCodigo());
					lstcodigo.add(lst.getCodigo());
					System.out.println("lst.getCodigo() : " + lst.getCodigo());
					lstoferta.add(lst.getOferta());
					lstdemanda.add(lst.getDemanda());
					lsttasa.add(lst.getTasa());
					lstestado.add(lst.getEstado());
					lstjornada.add(lst.getJornada());
					lstmonedaBase.add(lst.getMonedaBase());
					lstpactoMonto.add(lst.getPactoMonto());
					lstmonedaPacto.add(lst.getMonedaPacto());
					lstmontoPactoBase.add(lst.getMontoPactoBase());
					lsttcPactoBase.add(lst.getTasaPactoBase());
					lstofertante.add(lst.getOfertante());
					lstdemandante.add(lst.getDemandante());
					System.out.println("lst.getTipoPacto() : " + lst.getTipoPacto());
					lsttipoPacto.add(lst.getTipoPacto());
				}

				_operaciones.setValue("codigo", lstcodigo.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("oferta", lstoferta.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("demanda", lstdemanda.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("tasa", lsttasa.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("estado", lstestado.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("jornada", lstjornada.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("jornadaa", lstjornada.get(0));
				_operaciones.setValue("monedapacto", lstmonedaPacto.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("pactomonto", lstpactoMonto.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("monedabase", lstmonedaBase.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("montopactobase", lstmontoPactoBase.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("tcpactobase", lsttcPactoBase.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("ofertante", lstofertante.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("demandante", lstdemandante.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("tipopacto", lsttipoPacto.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("metodo", String.valueOf(metodo));
			} else if (metodo == 2 || metodo == 3){
				
				Method Fn = ConsultaOperaciones.class.getMethod("lecturaBcvDemandaOferta");
				consulta.procesar(consulta, Fn);
				this.listaOperacionesOD = consulta.ListarOperacionesOD();

				if (listaOperacionesOD != null) {
					iniciar_datos_sql(metodo);
				}

				List<String> lstcodigo = new ArrayList<String>();
				List<String> lstcodigoInstitucion = new ArrayList<String>();
				List<String> lsttasa = new ArrayList<String>();
				List<String> lstestado = new ArrayList<String>();
				List<String> lstjornada = new ArrayList<String>();
				List<String> lstcedula = new ArrayList<String>();
				List<String> lstnombre = new ArrayList<String>();
				List<String> lstmonto = new ArrayList<String>();

				for (OperacionesOfertaDemandaBCV lst : listaOperacionesOD) {
					lstcodigo.add(lst.getCodigo());
					lstcodigoInstitucion.add(lst.getCodigoInstitucion());
					lsttasa.add(lst.getTasa());
					lstestado.add(lst.getEstado());
					lstjornada.add(lst.getJornada());
					lstcedula.add(lst.getCedulaRif());
					lstnombre.add(lst.getNombre());
					lstmonto.add(lst.getMonto());
				}

				_operaciones.setValue("codigo", lstcodigo.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("codigoinstitucion", lstcodigoInstitucion.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("tasa", lsttasa.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("estado", lstestado.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("jornada", lstjornada.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("cedula", lstcedula.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("nombre", lstnombre.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("monto", lstmonto.toString().replace("[", "").replace("]", ""));
				_operaciones.setValue("metodo", String.valueOf(metodo));
				_operaciones.setValue("jornadaa", lstjornada.get(0));
				
			}
			

			storeDataSet("jornadas", _operaciones);
//			getSeccionPaginacion(listaOperaciones.size(), getPageSize(), getNumeroDePagina());
		} catch (Exception e) {
			storeDataSet("jornadas", _operaciones);
			System.out.println("Browse : execute() Transacciones " + e);
			Logger.error(this, "Browse : execute() Transacciones " + e);
		}

	}

	public String[] campos_datos() {

		String Campos[] = { "codigo", "oferta", "demanda", "tasa", "estado", "jornada", "jornadaa", "monedapacto", "montopactobase", "tcpactobase", "ofertante", "demandante", "tipopacto", "pactomonto", "monedabase", "metodo" };
		return Campos;
	}
	
	public String[] campos_datos_od() {

		String Campos[] = { "codigo", "operacion", "codigoinstitucion", "tasa", "estado", "jornada", "jornadaa", "cedula", "nombre", "monto", "metodo"};
		return Campos;
	}

	public void iniciar_datos_sql(int metodo) {
		String Campos[];
		if (metodo == 1) {
			Campos = campos_datos();
		}else{
			Campos = campos_datos_od();
		}
		
		int cant = Campos.length;
		for (int i = 0; i < cant; i++) {
			_operaciones.append(Campos[i], java.sql.Types.VARCHAR);
		}
		try {
			_operaciones.addNew();
		} catch (Exception e) {
			System.out.println("Browse : iniciar_datos_sql()" + e);
			Logger.error(this, "Browse : iniciar_datos_sql()" + e);
		}

	}

	public boolean pru() throws Exception {
		return false;
	}

}