package com.bdv.infi.logic.function.document;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import megasoft.DataSet;

public class DatosInstruccionesPago extends DatosGenerales{
	
	public DatosInstruccionesPago(DataSource ds, Map<String, String> mapa){
		super(ds,mapa);
	}
	
	public void buscarDatos(long idOrden, String transaccion, ServletContext contexto, String ip) throws Exception{
		
		Map<String, String> mapaInstruccionesPago = this.getMapa();
		
		DataSet _usoCuenta=null;
		
		if (!transaccion.equals(TransaccionNegocio.VENTA_TITULOS)){
			ClienteCuentasDAO ctecta = new ClienteCuentasDAO(this.getDataSource());
			String usoCuenta = UsoCuentas.PAGO_DE_CUPONES;
			ctecta.listarUsoParaCuenta(idOrden,usoCuenta);
			_usoCuenta= ctecta.getDataSet();
		}else{
			OperacionDAO operacionDAO = new OperacionDAO(this.getDataSource());
			operacionDAO.listarOperacionVentaTitulos(idOrden);
			_usoCuenta= operacionDAO.getDataSet();
		}		
		
		//Se inicializan los valores en blanco
		mapaInstruccionesPago.put("banco", "");
		mapaInstruccionesPago.put("direccion_banco", "");
		mapaInstruccionesPago.put("telefono_banco", "");
		mapaInstruccionesPago.put("cod_aba", "");		
		mapaInstruccionesPago.put("cod_swift", "");
		mapaInstruccionesPago.put("beneficiario", "");
		mapaInstruccionesPago.put("cedula_beneficiario", "");
		mapaInstruccionesPago.put("cuenta_beneficiario", "");
		mapaInstruccionesPago.put("banco_intermediario", "");
		mapaInstruccionesPago.put("direccion_intermediario", "");
		mapaInstruccionesPago.put("cuenta_intermediario", "");
		mapaInstruccionesPago.put("telefono_intermediario", "");
		mapaInstruccionesPago.put("cod_aba_intermediario", "");
		mapaInstruccionesPago.put("cod_swift_intermediario", "");
		mapaInstruccionesPago.put("referencia", "");		
		
		if(_usoCuenta.count()>0){
			_usoCuenta.first();
			_usoCuenta.next();
			String banco=_usoCuenta.getValue("ctecta_bcocta_bco");
			String direccion=_usoCuenta.getValue("ctecta_bcocta_direccion");
			String telefono=_usoCuenta.getValue("ctecta_bcocta_telefono");
			String aba=_usoCuenta.getValue("ctecta_bcocta_aba");
			String swift=_usoCuenta.getValue("ctecta_bcocta_bic");
			String beneficiario=_usoCuenta.getValue("nombre_beneficiario");
			String cedula_beneficiario=_usoCuenta.getValue("cedula_beneficiario");
			String cuenta_beneficiario=_usoCuenta.getValue("ctecta_numero");
			String banco_intermediario=_usoCuenta.getValue("ctecta_bcoint_bco");
			String direccion_intermediario=_usoCuenta.getValue("ctecta_bcoint_direccion");
			//String cuenta_intermediario=_usoCuenta.getValue("ctecta_bcocta_swift");
			String cuenta_intermediario=_usoCuenta.getValue("ctecta_bcoint_swift");
			String telefono_intermediario=_usoCuenta.getValue("ctecta_bcoint_telefono");
			String aba_intermediario=_usoCuenta.getValue("ctecta_bcoint_aba");
			String swift_intermediario=_usoCuenta.getValue("ctecta_bcoint_bic");
			String referencia="";//falta
	 		
			if(banco!=null&&!banco.equals("")){
				mapaInstruccionesPago.put("banco", banco);//ok
			}
			if(direccion!=null&&!direccion.equals("")){
				mapaInstruccionesPago.put("direccion_banco", direccion);//ok
			}
			if(telefono!=null&&!telefono.equals("")){
				mapaInstruccionesPago.put("telefono_banco", telefono);//ok
			}
			if(aba!=null&&!aba.equals("")){
				mapaInstruccionesPago.put("cod_aba", aba);//ok
			}
			if(swift!=null&&!swift.equals("")){
				mapaInstruccionesPago.put("cod_swift", swift);//ok
			}
			if(beneficiario!=null&&!beneficiario.equals("")){
				mapaInstruccionesPago.put("beneficiario", beneficiario);
			}
			if(cedula_beneficiario!=null&&!cedula_beneficiario.equals("")){
				mapaInstruccionesPago.put("cedula_beneficiario", cedula_beneficiario);
			}
			if(cuenta_beneficiario!=null&&!cuenta_beneficiario.equals("")){
				mapaInstruccionesPago.put("cuenta_beneficiario", cuenta_beneficiario);
			}
			if(banco_intermediario!=null&&!banco_intermediario.equals("")){
				mapaInstruccionesPago.put("banco_intermediario", banco_intermediario);//ok
			}
			if(direccion_intermediario!=null&&!direccion_intermediario.equals("")){
				mapaInstruccionesPago.put("direccion_intermediario", direccion_intermediario);//ok
			}
			if(cuenta_intermediario!=null&&!cuenta_intermediario.equals("")){
				mapaInstruccionesPago.put("cuenta_intermediario", cuenta_intermediario);//Corregido
			}
			if(telefono_intermediario!=null&&!telefono_intermediario.equals("")){
				mapaInstruccionesPago.put("telefono_intermediario", telefono_intermediario);//ok
			}
			if(aba_intermediario!=null&&!aba_intermediario.equals("")){
				mapaInstruccionesPago.put("cod_aba_intermediario", aba_intermediario);//ok
			}
			if(swift_intermediario!=null&&!swift_intermediario.equals("")){
				mapaInstruccionesPago.put("cod_swift_intermediario", swift_intermediario);//ok
			}
			if(referencia!=null&&!referencia.equals("")){
				mapaInstruccionesPago.put("referencia", referencia);
			}
		}
	}
	
	public void buscarDatosDocumentos(long idOrden, String transaccion, ServletContext contexto, String ip, String cedula) throws Exception{
		
		Map<String, String> mapaInstruccionesPago = this.getMapa();
		
		DataSet _usoCuenta=null;
		ClienteCuentasDAO ctecta = new ClienteCuentasDAO(this.getDataSource());
		
		if (!transaccion.equals(TransaccionNegocio.VENTA_TITULOS)){
			//ClienteCuentasDAO ctecta = new ClienteCuentasDAO(this.getDataSource());
			String usoCuenta = UsoCuentas.RECOMPRA;
			ctecta.listarUsoParaCuenta(idOrden,usoCuenta);
			_usoCuenta= ctecta.getDataSet();
		}else{
			//OperacionDAO operacionDAO = new OperacionDAO(this.getDataSource());
			//operacionDAO.listarOperacionVentaTitulos(idOrden);
			ctecta.browseClienteCuentas(String.valueOf(cedula), "1",null,null);
			_usoCuenta= ctecta.getDataSet();
		}		
		
		//Se inicializan los valores en blanco
		mapaInstruccionesPago.put("banco", "");
		mapaInstruccionesPago.put("direccion_banco", "");
		mapaInstruccionesPago.put("telefono_banco", "");
		mapaInstruccionesPago.put("cod_aba", "");		
		mapaInstruccionesPago.put("cod_swift", "");
		mapaInstruccionesPago.put("beneficiario", "");
		mapaInstruccionesPago.put("cedula_beneficiario", "");
		mapaInstruccionesPago.put("cuenta_beneficiario", "");
		mapaInstruccionesPago.put("banco_intermediario", "");
		mapaInstruccionesPago.put("direccion_intermediario", "");
		mapaInstruccionesPago.put("cuenta_intermediario", "");
		mapaInstruccionesPago.put("telefono_intermediario", "");
		mapaInstruccionesPago.put("cod_aba_intermediario", "");
		mapaInstruccionesPago.put("cod_swift_intermediario", "");
		mapaInstruccionesPago.put("referencia", "");		
		
		if(_usoCuenta.count()>0){
			_usoCuenta.first();
			_usoCuenta.next();
			String banco=_usoCuenta.getValue("ctecta_bcocta_bco");
			String direccion=_usoCuenta.getValue("ctecta_bcocta_direccion");
			String telefono=_usoCuenta.getValue("ctecta_bcocta_telefono");
			String aba=_usoCuenta.getValue("ctecta_bcocta_aba");
			String swift=_usoCuenta.getValue("ctecta_bcocta_bic");
			String beneficiario=_usoCuenta.getValue("nombre_beneficiario");
			String cedula_beneficiario=_usoCuenta.getValue("cedula_beneficiario");
			String cuenta_beneficiario=_usoCuenta.getValue("ctecta_numero");
			String banco_intermediario=_usoCuenta.getValue("ctecta_bcoint_bco");
			String direccion_intermediario=_usoCuenta.getValue("ctecta_bcoint_direccion");
			//String cuenta_intermediario=_usoCuenta.getValue("ctecta_bcocta_swift");
			String cuenta_intermediario=_usoCuenta.getValue("ctecta_bcoint_swift");
			String telefono_intermediario=_usoCuenta.getValue("ctecta_bcoint_telefono");
			String aba_intermediario=_usoCuenta.getValue("ctecta_bcoint_aba");
			String swift_intermediario=_usoCuenta.getValue("ctecta_bcoint_bic");
			String referencia="";//falta
	 		
			if(banco!=null&&!banco.equals("")){
				mapaInstruccionesPago.put("banco", banco);//ok
			}
			if(direccion!=null&&!direccion.equals("")){
				mapaInstruccionesPago.put("direccion_banco", direccion);//ok
			}
			if(telefono!=null&&!telefono.equals("")){
				mapaInstruccionesPago.put("telefono_banco", telefono);//ok
			}
			if(aba!=null&&!aba.equals("")){
				mapaInstruccionesPago.put("cod_aba", aba);//ok
			}
			if(swift!=null&&!swift.equals("")){
				mapaInstruccionesPago.put("cod_swift", swift);//ok
			}
			if(beneficiario!=null&&!beneficiario.equals("")){
				mapaInstruccionesPago.put("beneficiario", beneficiario);
			}
			if(cedula_beneficiario!=null&&!cedula_beneficiario.equals("")){
				mapaInstruccionesPago.put("cedula_beneficiario", cedula_beneficiario);
			}
			if(cuenta_beneficiario!=null&&!cuenta_beneficiario.equals("")){
				mapaInstruccionesPago.put("cuenta_beneficiario", cuenta_beneficiario);
			}
			if(banco_intermediario!=null&&!banco_intermediario.equals("")){
				mapaInstruccionesPago.put("banco_intermediario", banco_intermediario);//ok
			}
			if(direccion_intermediario!=null&&!direccion_intermediario.equals("")){
				mapaInstruccionesPago.put("direccion_intermediario", direccion_intermediario);//ok
			}
			if(cuenta_intermediario!=null&&!cuenta_intermediario.equals("")){
				mapaInstruccionesPago.put("cuenta_intermediario", cuenta_intermediario);//Corregido
			}
			if(telefono_intermediario!=null&&!telefono_intermediario.equals("")){
				mapaInstruccionesPago.put("telefono_intermediario", telefono_intermediario);//ok
			}
			if(aba_intermediario!=null&&!aba_intermediario.equals("")){
				mapaInstruccionesPago.put("cod_aba_intermediario", aba_intermediario);//ok
			}
			if(swift_intermediario!=null&&!swift_intermediario.equals("")){
				mapaInstruccionesPago.put("cod_swift_intermediario", swift_intermediario);//ok
			}
			if(referencia!=null&&!referencia.equals("")){
				mapaInstruccionesPago.put("referencia", referencia);
			}
		}
	}
	
	/**
	 * Busca todos los datos de una instrucción de pago (cuenta del cliente) de acuerdo al id ctes_cuentas_id de la tabla 202
	 * @param ctesCuentasId
	 * @param contexto
	 * @param ip
	 * @throws Exception
	 */
	public void buscarDatosPreOrden(long idInstruccionPago, ServletContext contexto, String ip) throws Exception{
		
		Map<String, String> mapaInstruccionesPago = this.getMapa();
		
		DataSet _usoCuenta= new DataSet();
		ClienteCuentasDAO ctecta = new ClienteCuentasDAO(this.getDataSource());
		
		//Buscar la cuenta de acuerdo al id unico de la tabla 202
		ctecta.listarCuentaCliente(idInstruccionPago);		
		_usoCuenta = ctecta.getDataSet();
		
		//Se inicializan los valores en blanco
		mapaInstruccionesPago.put("banco", "");
		mapaInstruccionesPago.put("direccion_banco", "");
		mapaInstruccionesPago.put("telefono_banco", "");
		mapaInstruccionesPago.put("cod_aba", "");		
		mapaInstruccionesPago.put("cod_swift", "");
		mapaInstruccionesPago.put("beneficiario", "");
		mapaInstruccionesPago.put("cedula_beneficiario", "");
		mapaInstruccionesPago.put("cuenta_beneficiario", "");
		mapaInstruccionesPago.put("banco_intermediario", "");
		mapaInstruccionesPago.put("direccion_intermediario", "");
		mapaInstruccionesPago.put("cuenta_intermediario", "");
		mapaInstruccionesPago.put("telefono_intermediario", "");
		mapaInstruccionesPago.put("cod_aba_intermediario", "");
		mapaInstruccionesPago.put("cod_swift_intermediario", "");
		mapaInstruccionesPago.put("referencia", "");		
		
		if(_usoCuenta.count()>0){
			_usoCuenta.first();
			_usoCuenta.next();
			String banco=_usoCuenta.getValue("ctecta_bcocta_bco");
			String direccion=_usoCuenta.getValue("ctecta_bcocta_direccion");
			String telefono=_usoCuenta.getValue("ctecta_bcocta_telefono");
			String aba=_usoCuenta.getValue("ctecta_bcocta_aba");
			String swift=_usoCuenta.getValue("ctecta_bcocta_bic");
			String beneficiario=_usoCuenta.getValue("nombre_beneficiario");
			String cedula_beneficiario=_usoCuenta.getValue("cedula_beneficiario");
			String cuenta_beneficiario=_usoCuenta.getValue("ctecta_numero");
			String banco_intermediario=_usoCuenta.getValue("ctecta_bcoint_bco");
			String direccion_intermediario=_usoCuenta.getValue("ctecta_bcoint_direccion");
			String cuenta_intermediario=_usoCuenta.getValue("ctecta_bcocta_swift");
			String telefono_intermediario=_usoCuenta.getValue("ctecta_bcoint_telefono");
			String aba_intermediario=_usoCuenta.getValue("ctecta_bcoint_aba");
			String swift_intermediario=_usoCuenta.getValue("ctecta_bcoint_bic");
			String referencia="";//falta
	 		
			if(banco!=null&&!banco.equals("")){
				mapaInstruccionesPago.put("banco", banco);
			}
			if(direccion!=null&&!direccion.equals("")){
				mapaInstruccionesPago.put("direccion_banco", direccion);
			}
			if(telefono!=null&&!telefono.equals("")){
				mapaInstruccionesPago.put("telefono_banco", telefono);
			}
			if(aba!=null&&!aba.equals("")){
				mapaInstruccionesPago.put("cod_aba", aba);
			}
			if(swift!=null&&!swift.equals("")){
				mapaInstruccionesPago.put("cod_swift", swift);
			}
			if(beneficiario!=null&&!beneficiario.equals("")){
				mapaInstruccionesPago.put("beneficiario", beneficiario);
			}
			if(cedula_beneficiario!=null&&!cedula_beneficiario.equals("")){
				mapaInstruccionesPago.put("cedula_beneficiario", cedula_beneficiario);
			}
			if(cuenta_beneficiario!=null&&!cuenta_beneficiario.equals("")){
				mapaInstruccionesPago.put("cuenta_beneficiario", cuenta_beneficiario);
			}
			if(banco_intermediario!=null&&!banco_intermediario.equals("")){
				mapaInstruccionesPago.put("banco_intermediario", banco_intermediario);
			}
			if(direccion_intermediario!=null&&!direccion_intermediario.equals("")){
				mapaInstruccionesPago.put("direccion_intermediario", direccion_intermediario);
			}
			if(cuenta_intermediario!=null&&!cuenta_intermediario.equals("")){
				mapaInstruccionesPago.put("cuenta_intermediario", cuenta_intermediario);
			}
			if(telefono_intermediario!=null&&!telefono_intermediario.equals("")){
				mapaInstruccionesPago.put("telefono_intermediario", telefono_intermediario);
			}
			if(aba_intermediario!=null&&!aba_intermediario.equals("")){
				mapaInstruccionesPago.put("cod_aba_intermediario", aba_intermediario);
			}
			if(swift_intermediario!=null&&!swift_intermediario.equals("")){
				mapaInstruccionesPago.put("cod_swift_intermediario", swift_intermediario);
			}
			if(referencia!=null&&!referencia.equals("")){
				mapaInstruccionesPago.put("referencia", referencia);
			}
		}
	}	
	

}
