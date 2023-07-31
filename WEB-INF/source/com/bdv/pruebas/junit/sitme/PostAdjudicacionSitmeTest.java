package com.bdv.pruebas.junit.sitme;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.data.Orden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.pruebas.junit.PrincipalTest;

public class PostAdjudicacionSitmeTest extends PrincipalTest {

	int idUnidadInversion = 91;
	OrdenDAO ordenDao = null;
	
	public PostAdjudicacionSitmeTest(String nombre) {
		super(nombre);
	}

	@Before
	public void setUp() throws Exception {
		ordenDao = new OrdenDAO(ds);
	}

	/**
	 * Insertar ordenes de tipo SITME a la unidad de inversión indicada
	 */
	public void postAdjudicacion() {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Transaccion transaccion = null;
		try {
			Orden orden = null;
			transaccion = new Transaccion(this.ds);
			transaccion.begin();
			con = transaccion.getConnection();
			st = con.createStatement();
			rs = null;
			String sql = "select * from infi_tb_204_ordenes where uniinv_id=" + idUnidadInversion;
			rs = st.executeQuery(sql);
			while (rs.next()){				
				orden = ordenDao.listarOrden(rs.getInt("ORDENE_ID"), false, true, true, true, true);
				System.out.println("Verificando orden " + orden.getIdOrden());
				if (orden.getStatus().equals("ADJUDICADA")){
					if (orden.getIdTransaccion().equals("TOMA_ORDEN") || orden.getIdTransaccion().equals("TOMA_ORDEN_CARTERA_PROPIA")){
						if (orden.getMontoAdjudicado() == 0){
							//verificarAdjCero(rs);
						}else if (orden.getMonto() == orden.getMontoAdjudicado()){
							verificarAdjIgual(orden);
						}else if (orden.getMonto() < orden.getMontoAdjudicado()){
							//verificarAdjMenor(rs);
						}
					}else{
						System.out.println("Error en la orden " + orden.getIdOrden() + ". No tiene el campo TRANSA_ID con el valor esperado (TOMA_ORDEN o TOMA_ORDEN_CARTERA_PROPIA)");
						assertTrue(false);
					}
				}
			}
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		} finally{
			try{
				if (rs != null ){rs.close();}
				if (st != null ){st.close();}
				if (con != null ){con.close();}
				if (transaccion != null ){transaccion.closeConnection();}				
			}  catch (Exception e) {
				System.out.println("Error cerrando las conexiones");
			}
		}
	}

	

	@After
	public void tearDown() throws Exception {
		ordenDao = null;
		ds = null;
	}
	
	/**
	 * Verifica si la orden tiene los valores correctos si la adjudicación es igual al monto pedido
	 * @param orden objeto con los datos a verificar
	 * @throws Exception en caso de error
	 */
	private void verificarAdjIgual(Orden orden) throws Exception{
		//recorremos las operaciones de la orden
		ArrayList<OrdenOperacion> operaciones = orden.getOperacion();
		BigDecimal montoBloq = new BigDecimal(0);
		BigDecimal montoDeb = new BigDecimal(0);
		BigDecimal montoCre = new BigDecimal(0);
		BigDecimal montoMisc = new BigDecimal(0);
		for (OrdenOperacion ordenOperacion : operaciones) {
			if (ordenOperacion.getTipoTransaccionFinanc() == TransaccionFinanciera.BLOQUEO){
				montoBloq.add(ordenOperacion.getMontoOperacion()); 
			}else if (ordenOperacion.getTipoTransaccionFinanc() == TransaccionFinanciera.DEBITO){
				montoDeb.add(ordenOperacion.getMontoOperacion());
			}else if (ordenOperacion.getTipoTransaccionFinanc() == TransaccionFinanciera.MISCELANEO){
				montoMisc.add(ordenOperacion.getMontoOperacion());
			}else if (ordenOperacion.getTipoTransaccionFinanc() == TransaccionFinanciera.CREDITO){
				montoCre.add(ordenOperacion.getMontoOperacion());
			}			
		}
		if (montoBloq.doubleValue() > 0){
			if (montoBloq.doubleValue() == montoDeb.doubleValue()){
				if (orden.getMontoTotal() != montoDeb.doubleValue()){
					throw new Exception("Error, el monto de débito no concuerda con el monto total de la orden " + orden.getIdOrden());
				}
			}else{
				throw new Exception("Error, el monto de bloqueo no es igual del de débito de la orden " + orden.getIdOrden());
			}
		}else{
			if (montoDeb.doubleValue() > 0){
				if (orden.getMontoTotal() != montoDeb.doubleValue()){
					throw new Exception("Error, el monto de débito no concuerda con el monto total de la orden " + orden.getIdOrden());
				}else{
					if (montoBloq.doubleValue() > 0 || montoMisc.doubleValue() > 0 ){
						throw new Exception("Error, operaciones de más encontradas en la orden " + orden.getIdOrden());
					}
				}
			}else{
				if (montoMisc.doubleValue() > 0){
					if (montoBloq.doubleValue() > 0 || montoDeb.doubleValue() > 0 ){
					  throw new Exception("Error, operaciones de más encontradas en la orden " + orden.getIdOrden());
					}
				}else{
					throw new Exception("Error, orden inconsistente " + orden.getIdOrden());
				}
			}
		}		
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
		suite.addTest(new PostAdjudicacionSitmeTest("postAdjudicacion"));		

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
