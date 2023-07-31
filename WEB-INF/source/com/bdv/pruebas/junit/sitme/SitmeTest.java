package com.bdv.pruebas.junit.sitme;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;
import megasoft.db;

import org.junit.After;
import org.junit.Before;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.FormaPago;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.pruebas.junit.PrincipalTest;

public class SitmeTest extends PrincipalTest {

	int idUnidadInversion = 91;
	OrdenDAO ordenDao = null;
	
	public SitmeTest(String nombre) {
		super(nombre);
	}

	@Before
	public void setUp() throws Exception {
		ordenDao = new OrdenDAO(ds);
	}

	/**
	 * Insertar ordenes de tipo SITME a la unidad de inversión indicada
	 */
	public void testInsertarOrdenes() {
		try {
			for (int i=0; i < 8; i++){
				//creando orden
				Orden orden = new Orden();
				orden.setActividadEconomicaId("21");
				orden.setCarteraPropia(false);
				orden.setComisionEmisor(new BigDecimal(0));
				orden.setComisionOficina(new BigDecimal(0));
				orden.setComisionOperacion(new BigDecimal(0));
				orden.setConceptoId("2");
				orden.setCuentaCliente("01020136540005751309");
				orden.setFechaOrden(new Date());
				orden.setFechaPactoRecompra(new Date());
				orden.setFechaValor(new Date());
				orden.setIdBloter("1");
				orden.setIdCliente(2772); //BIMA
				orden.setIdTransaccion(TransaccionNegocio.TOMA_DE_ORDEN);
				orden.setIdUnidadInversion(idUnidadInversion);
				orden.setMonto(6000);
				orden.setMontoTotal(13029);
				orden.setNombreUsuario("NM22862");
				orden.setPrecioCompra(100);
				orden.setStatus("REGISTRADA");
				orden.setTipoProducto("SITME");
				orden.setTerminal("120.48.30.154");
				orden.setVehiculoColocador("1");
				orden.setVehiculoRecompra("1");
				orden.setVehiculoTomador("1");
				
				//Creando operaciones
				OrdenOperacion ordenOperacion = new OrdenOperacion();
				ordenOperacion.setMontoOperacion(new BigDecimal("25800"));
				ordenOperacion.setTipoTransaccionFinanc(TransaccionFinanciera.MISCELANEO);
				ordenOperacion.setIdMoneda("VEF");
				ordenOperacion.setFechaAplicar(new Date());
				ordenOperacion.setStatusOperacion("APLICADA");
				ordenOperacion.setIdTransaccionFinanciera(String.valueOf(com.bdv.infi.logic.interfaces.TransaccionFija.CAPITAL_SIN_IDB));
				ordenOperacion.setNumeroCuenta("01020136540005751309");
				ordenOperacion.setTasa(new BigDecimal(100));
				ordenOperacion.setInComision(0);
				ordenOperacion.setNombreOperacion("Capital exento de IDB");
				ordenOperacion.setIndicadorComisionInvariable(1);
				orden.agregarOperacion(ordenOperacion);
	
				OrdenOperacion ordenOperacionComision = new OrdenOperacion();
				ordenOperacionComision.setMontoOperacion(new BigDecimal("258"));
				ordenOperacionComision.setTipoTransaccionFinanc(TransaccionFinanciera.MISCELANEO);
				ordenOperacionComision.setIdMoneda("VEF");
				ordenOperacionComision.setFechaAplicar(new Date());
				ordenOperacionComision.setStatusOperacion("APLICADA");
				ordenOperacionComision.setIdTransaccionFinanciera(String.valueOf(com.bdv.infi.logic.interfaces.TransaccionFija.COMISION_DEB));
				ordenOperacionComision.setNumeroCuenta("01020136540005751309");
				ordenOperacionComision.setTasa(new BigDecimal(1));
				ordenOperacionComision.setInComision(1);
				ordenOperacionComision.setNombreOperacion("Comision por monto");
				ordenOperacionComision.setIndicadorComisionInvariable(1);
				orden.agregarOperacion(ordenOperacionComision);
	
				//Insertando títulos
				OrdenTitulo ordenTitulo = new OrdenTitulo();
				ordenTitulo.setTituloId("PDVSA FIN LTD 7.40");			
				ordenTitulo.setMonto(6000);
				ordenTitulo.setPorcentaje("100");
				ordenTitulo.setUnidades(6000);
				ordenTitulo.setPorcentajeRecompra(75);
				ordenTitulo.setPrecioMercado(75);
				ordenTitulo.setMontoIntCaidos(new BigDecimal(0));
				ordenTitulo.setMontoNeteo(new BigDecimal(0));
				orden.agregarOrdenTitulo(ordenTitulo);
				
				//DataExtendida (Pendiente cuando se cambie el id de la comisión)
				OrdenDataExt ordenDataExt = new OrdenDataExt();
				ordenDataExt.setIdData(DataExtendida.ID_COMISION_UI);
				ordenDataExt.setValor("72;1.0000");
				orden.agregarOrdenDataExt(ordenDataExt);
				
				//Forma de pago
				FormaPago formaPago = new FormaPago(); 
				formaPago.setIdFrmPago(11825);
				orden.agregarFormaPago(formaPago);
				
				String[] sql = ordenDao.insertar(orden);
				db.execBatch(ds, sql);
				
				db.exec(ds, "update infi_tb_206_ordenes_titulos set titulo_mto_neteo=0 where ordene_id=" + orden.getIdOrden());
				db.exec(ds, "update infi_tb_204_ordenes set ordene_ped_comision=258,ordene_id_relacion = null,ordene_fe_ult_act = sysdate, ordene_ped_fe_orden = trunc(sysdate), ordene_ped_fe_valor = trunc(sysdate) where ordene_id=" + orden.getIdOrden());
				db.exec(ds, "update infi_tb_207_ordenes_operacion set fecha_aplicar=trunc(fecha_aplicar) where ordene_id=" + orden.getIdOrden());
				
				System.out.println("Orden creada " + orden.getIdOrden());
				assertTrue(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	

	@After
	public void tearDown() throws Exception {
		ordenDao = null;
		ds = null;
	}

	/**
	 * Assembles and returns a test suite for all the test methods of this test case.
	 * 
	 * @return A non-null test suite.
	 */
	public static Test suite() {

		//
		// Reflection is used here to add all
		// the testXXX() methods to the suite.
		//
		TestSuite suite = new TestSuite();
		suite.addTest(new SitmeTest("testInsertarOrdenes"));		

		//
		// Alternatively, but prone to error when adding more
		// test case methods...
		//
		// TestSuite suite = new TestSuite();
		// suite.addTest(new ShoppingCartTest("testEmpty"));
		// suite.addTest(new ShoppingCartTest("testProductAdd"));
		// suite.addTest(new ShoppingCartTest("testProductRemove"));
		// suite.addTest(new ShoppingCartTest("testProductNotFound"));
		//

		return suite;
	}

	/**
	 * Runs the test case.
	 */
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}
}
