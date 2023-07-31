package models.bcv.mesas_operaciones;

import java.lang.reflect.Method;
import java.util.List;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.model.mesaCambio.ConsultaOperaciones;
import com.bdv.infi.model.mesaCambio.OperacionesBCV;
import com.bdv.infi.model.mesaCambio.OperacionesOfertaDemandaBCV;

public class ExportarCVS extends ExportableOutputStream {
	List<OperacionesBCV> listaOperaciones;
	List<OperacionesOfertaDemandaBCV> listaOperacionesOD;

	public void execute() throws Exception {
	
		
		String jornadaa = _req.getParameter("jornada");
		System.out.println("jornada :" + jornadaa);
		int metodo = Integer.valueOf(_req.getParameter("metodo"));
		System.out.println("pruebaaaaaaa : "+ metodo);
		ConsultaOperaciones consulta = new ConsultaOperaciones(jornadaa,metodo);
		System.out.println("pruebaaaaaaa 1");
		if (metodo == 1) {
			Method Fn = ConsultaOperaciones.class.getMethod("lecturaBcvOperaciones");
			consulta.procesar(consulta, Fn);
			this.listaOperaciones = consulta.ListarOperaciones();
			

			try {
				registrarInicio("jornada.csv");

				crearCabecera("codigo; oferta; demanda; tasa; estado; jornada; monedaPacto; montoPactoBase; tcPactoBase; ofertante; demandante; tipoPacto");

				escribir("\r\n");
				for (OperacionesBCV lst : listaOperaciones) {
					escribir(lst.getCodigo() == null ? " ;" : lst.getCodigo() + ";");
					escribir(lst.getOferta() == null ? " ;" : lst.getOferta() + ";");
					escribir(lst.getDemanda() == null ? " ;" : lst.getDemanda() + ";");
					escribir(lst.getTasa() == null ? " ;" : lst.getTasa() + ";");
					escribir(lst.getEstado() == null ? " ;" : lst.getEstado() + ";");
					escribir(lst.getJornada() == null ? " ;" : lst.getJornada() + ";");
					escribir(lst.getMonedaPacto() == null ? " ;" : lst.getMonedaPacto() + ";");
					escribir(lst.getMontoPactoBase() == null ? " ;" : lst.getMontoPactoBase() + ";");
					escribir(lst.getTasaPactoBase() == null ? " ;" : lst.getTasaPactoBase() + ";");
					escribir(lst.getOfertante() == null ? " ;" : lst.getOfertante() + ";");
					escribir(lst.getDemandante() == null ? " ;" : lst.getDemandante() + ";");
					escribir(lst.getTipoPacto() == null ? " ;" : lst.getTipoPacto() + ";");

					escribir("\r\n");
				}
				registrarFin();
				obtenerSalida();
			} catch (Exception e) {
				_record.addError("Nombre", "Error en la exportación del Excel" + "Error:" + e.getMessage());
				Logger.error(this, "Error en la exportación del Excel", e);
			}
		}else{
			Method Fn = ConsultaOperaciones.class.getMethod("lecturaBcvDemandaOferta");
			consulta.procesar(consulta, Fn);
			this.listaOperacionesOD = consulta.ListarOperacionesOD();
			
			try {
				registrarInicio("jornada.csv");

				crearCabecera("codigo; institucion; tasa; estado; jornada; cedula; nombre; monto");

				escribir("\r\n");
				for (OperacionesOfertaDemandaBCV lst : listaOperacionesOD) {
					escribir(lst.getCodigo() == null ? " ;" : lst.getCodigo() + ";");
					escribir(lst.getCodigoInstitucion() == null ? " ;" : lst.getCodigoInstitucion() + ";");
					escribir(lst.getTasa() == null ? " ;" : lst.getTasa() + ";");
					escribir(lst.getEstado() == null ? " ;" : lst.getEstado() + ";");
					escribir(lst.getJornada() == null ? " ;" : lst.getJornada() + ";");
					escribir(lst.getCedulaRif() == null ? " ;" : lst.getCedulaRif() + ";");
					escribir(lst.getNombre() == null ? " ;" : lst.getNombre() + ";");
					escribir(lst.getMonto() == null ? " ;" : lst.getMonto() + ";");
					escribir("\r\n");
				}
				registrarFin();
				obtenerSalida();
			} catch (Exception e) {
				_record.addError("Nombre", "Error en la exportación del Excel" + "Error:" + e.getMessage());
				Logger.error(this, "Error en la exportación del Excel", e);
			}
		}
	
	}

	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());

	}
}