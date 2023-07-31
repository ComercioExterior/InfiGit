package models.bcv.intervencion_lectura_inventario;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.model.inventariodivisas.Oficina;
import megasoft.DataSet;
import megasoft.db;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class LeerarchivoIntervencion extends Transaccion {

	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	private DataSet datos = new DataSet();
	int idUsuario;
	public String Errores = "";
	private Logger logger = Logger.getLogger(LeerarchivoIntervencion.class);
	HashMap<String, String> parametros = new HashMap<String, String>();
	int noRegistrada = 0;
	HSSFCell monto;
	HSSFCell montoAsignado;
	HSSFCell idMonedaExtranjera;
	List<String> error = new ArrayList<String>();
	Oficina ofi;
	public int registroProcesado = 0;

	public void execute() throws Exception {
		leer();
		datos.append("texto", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("texto", this.Errores);
		storeDataSet("datos", datos);

	}

	private void leer() {
		try {
			obetenerProceso();
			obtenerParametros();
			String contenidoDocumento = getSessionObject("contenidoDocumento").toString();
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = libro.getSheetAt(0);
			int numFila = hoja.getLastRowNum();
			numFila = numFila - 1;
			HSSFRow fila;

			for (int a = 1; a <= numFila; a++) {

				fila = hoja.getRow(a);
				if (fila != null) {

					ofi = new Oficina(_dso);
					ofi.Id = fila.getCell((short) 17).getStringCellValue();
					ofi.Id = ofi.Id.substring(0, 3);
					ofi.IdTabla = "INFI_SQ_030.NEXTVAL";
					ofi.Fecha = String.valueOf(fila.getCell((short) 2).getCellNum());
					ofi.Moneda = fila.getCell((short) 6).getStringCellValue();

					if (ofi.Moneda.contains("USD")) {
						ofi.Moneda = "USD";
					}
					if (ofi.Moneda.contains("EUR")) {
						ofi.Moneda = "EUR";
					}

					monto = fila.getCell((short) 20);
					ofi.Monto = new BigDecimal(monto.toString());
					montoAsignado = fila.getCell((short) 20);
					ofi.Porcentaje = new BigDecimal(parametros.get(ParametrosSistema.PORCENTAJE_INVENTARIO));
					ofi.MontoAsignado = new BigDecimal(montoAsignado.toString());
					ofi.Consumido = new BigDecimal(0);
					ofi.Disponible = ofi.Monto.multiply(ofi.Porcentaje).divide(new BigDecimal(100));
					ofi.diaEntrega = parametros.get(ParametrosSistema.DIAS_ENTREGA);
					ofi.Estatus = parametros.get(ParametrosSistema.ESTATUS_INVENTARIO);

					try {

						db.exec(_dso, ofi.CargarInventario());
					} catch (Exception e) {
						String err = "";
						if (e.getMessage().contains("parent key not found")) {
							err = "No se encuentra la oficina registrada :" + ofi.Id + "-" + ofi.Moneda;

						} else if (e.getMessage().contains("unique constraint")) {
							err = "Ya fue registrado el inventario :" + ofi.Id + "-" + ofi.Moneda;

						} else {
							err = e.toString().substring(30, 60);

						}
						
						Errores += err + "\n";
						noRegistrada++;
						continue;
					}
				} else {
					break;
				}

			}
			ofi.OficinaSinDisponible();
			documento.close();

			if (noRegistrada == 0) {

				proceso.agregarDescripcionError("Todo el inventario fue agregado");
			} else {
				proceso.agregarDescripcionError("Operaciones fallidas :" + noRegistrada + " : " + Errores);
			}

		} catch (Exception e) {
			System.out.println("LeerarchivoIntervencion : leer() " + e);
			logger.error("LeerarchivoIntervencion : leer() " + e);
			
		} finally {
			finalizarProceso();

		}
	}

	protected void obtenerParametros() throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametros = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_INTERVENCION_BANCARIA);
	}

	protected Proceso obetenerProceso() {

		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(TransaccionNegocio.LECTURA_INVENTARIO);
		proceso.setUsuarioId(this.idUsuario);

		try {
			int secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
			proceso.setEjecucionId(secuenciaProcesos);
			String queryProceso = procesosDAO.insertar(proceso);
			db.exec(_dso, queryProceso);

		} catch (Exception e) {
			logger.error("OficinasIntervencion : obetenerProceso()" + e);
			System.out.println("OficinasIntervencion : obetenerProceso()" + e);

		}

		return proceso;
	}

	
	private void finalizarProceso() {
		try {
			String queryProcesoCerrar = procesosDAO.modificar(proceso);
			db.exec(_dso, queryProcesoCerrar);
			logger.info("FIN DE PROCESO: " + new Date());

		} catch (Exception e) {
			logger.error("OficinasIntervencion : finalizarProceso()" + e);
			System.out.println("OficinasIntervencion : finalizarProceso()" + e);

		}

	}
}
