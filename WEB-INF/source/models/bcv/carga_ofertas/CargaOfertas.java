package models.bcv.carga_ofertas;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.AbstractModel;
import megasoft.Logger;
import megasoft.db;
import models.bcv.alto_valor.ConsultasBCVAltoValor;
import models.bcv.alto_valor.ErroresAltoValor;

import com.bdv.infi.dao.OfertaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.OfertaBCV;
import com.bdv.infi.webservices.beans.OfertaBCVRespuesta;
import com.bdv.infi.webservices.client.ClienteWs;

public class CargaOfertas extends AbstractModel implements Runnable {
	private int cantidadTotalOrdenes=0;
	private int cantidadTotalOrdenesProcesadas=0;
	private int cantidadLoteOrdenesProcesadas=0;
	private ProcesosDAO procesosDAO;
	private OfertaDAO ofertaDao;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	private DataSource _dso;
	private String tipoTransaccion = TransaccionNegocio.WS_BCV_CARGA_OFERTA;
	private int idUsuario;
	private String unidadInversionId;
	private String fecha;
	private String origen;	
	private String nroJornada;
	
	public CargaOfertas ( DataSource _dso, int idUsuario, String fecha, String origen, String unidadInversionId,String nroJornada){
		this._dso = _dso;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.origen = origen;
		this.unidadInversionId=unidadInversionId;
		this.nroJornada=nroJornada;
	}

	public void run() {
		//INCIAR PROCESO
		try {
			iniciarProceso();
			cargarOfertasOpics();
			finalizarProceso();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void iniciarProceso() throws Exception {
		Logger.info(this,"INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(tipoTransaccion); 
		proceso.setUsuarioId(this.idUsuario); 

		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
	}
	
	private void finalizarProceso() throws Exception {
		String queryProcesoCerrar = procesosDAO.modificar(proceso);
		db.exec(_dso, queryProcesoCerrar);
		Logger.info(this,"FIN DE PROCESO: " + new Date());
	}
	
	private void cargarOfertasOpics() throws Exception{
		ArrayList<String> querysEjecutar=new ArrayList<String>();
		OfertaDAO ofertaDao= new OfertaDAO(_dso);
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		String cedula=null;
		String tipoDoc=null;
		String nombre=null;
		//String nroJornada=null;
		Logger.info(this,"Se cargaran de OPICS las operaciones del día "+fecha);
		try{
			//VALIDAR JORNADA ACTIVA
			nroJornada=unidadInversionDAO.consultarJornada(Integer.parseInt(ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR),fecha, unidadInversionId);
			//nroJornada="15042402";
			if(nroJornada!=null){ 
				
				//PROCESAR OFERTAS DE CLIENTES DEL BANCO DE VENEZUELA
				if(origen.equalsIgnoreCase(ConstantesGenerales.ORIGEN_BANCO_DE_VENEZUELA)){
					
					//LISTAR OFERTAS SIMADI REGISTRADAS EN OPICS	
					ofertaDao.listarOrdenesOfertaOPICS(fecha, ConstantesGenerales.ID_PRODUCTO_OPICS_OPER_CAMBIO,ConstantesGenerales.ID_SUBPROD_OFERTA_ALTOV_OPICS_PN,ConstantesGenerales.ID_SUBPROD_OFERTA_ALTOV_OPICS_PJ);
					
					if(ofertaDao.getDataSet().count()>0){
		
						//NM26659 10/04/2015 Cambio de estatus de ofertas no procesadas de dias anteriores 
						querysEjecutar.add(ofertaDao.actualizarOrdenOfertaSinProcesar());
						
						ofertaDao.getDataSet().first();
						while(ofertaDao.getDataSet().next()){
							try {
								//REGISTRAR OFERTAS SIMADI EN INFI PARA SU POSTERIOR NOTIFICACION VIA WEB SERVICE A BCV
								cedula=ofertaDao.getDataSet().getValue("TAXID").trim();								
								tipoDoc=ofertaDao.getDataSet().getValue("TAXID").substring(0,1);	
								
								//INCIDENCIA ITS-3069 Proceso de envio Web Services SIMADI Persona Juridica no informa digito verificador 
								//cedula=ofertaDao.getDataSet().getValue("TAXID").substring(1,cedula.length()-1);								
								cedula=ofertaDao.getDataSet().getValue("TAXID").substring(1,cedula.length());							
								cedula=String.valueOf(new BigDecimal(cedula));				
								nombre=ofertaDao.getDataSet().getValue("SN").trim();
								
								querysEjecutar.add(
										ofertaDao.insertarOrdenBCV(
												cedula,//cedRif
												tipoDoc, //tipoDocumento
												nombre, //nombreCliente
												ofertaDao.getDataSet().getValue("CCYAMT"), //monto
												ofertaDao.getDataSet().getValue("CCYBRATE_8"), //tasa
												nroJornada, 
												ConstantesGenerales.SIN_VERIFICAR,
												ofertaDao.getDataSet().getValue("DEALNO"),
												ofertaDao.getDataSet().getValue("DEALDATE"),
												ofertaDao.getDataSet().getValue("INPUTDATE"),
												ofertaDao.getDataSet().getValue("VDATE"),
												ofertaDao.getDataSet().getValue("PRODCODE"),
												ofertaDao.getDataSet().getValue("PRODTYPE"),
												origen,
												null));
								
								procesarQuerysPorLote(querysEjecutar);	
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								//querysEjecutar.clear();
							}
						}
						ofertaDao.ejecutarStatementsBatch(querysEjecutar);
					}else{
						proceso.setDescripcionError("No existen registros de Ofertas OPICS a cargar");  
					}
				}else{
					//PROCESAR OFERTAS DEL BANCO CENTRAL DE VENEZUELA / INTERBANCARIAS
					ConsultasBCVAltoValor consultaWSAltoValor= new ConsultasBCVAltoValor(_dso);
					ClienteWs clienteWs = new ClienteWs();
					String xmlRespuesta=null;
					OfertaBCVRespuesta ofertaBCVRespuesta = null;
					try {
						
						xmlRespuesta=consultaWSAltoValor.consultarOfertas(nroJornada);					
						ofertaBCVRespuesta=(OfertaBCVRespuesta)clienteWs.convertirXmlAObjeto(xmlRespuesta,OfertaBCVRespuesta.class);
						if(ofertaBCVRespuesta!=null&&ofertaBCVRespuesta.getOfertasBCVList()!=null){						
						
							Logger.info(this,"Cantidad de Ofertas BCV: "+ofertaBCVRespuesta.getOfertasBCVList().size());
							ArrayList<OfertaBCV> ofertasBCVList=ofertaBCVRespuesta.getOfertasBCVList();
							OfertaBCV ofertaBCV=null;
							String fechaActual=Utilitario.DateToString(new Date(), ConstantesGenerales.FORMATO_FECHA3);
							
							for (int i = 0; i < ofertasBCVList.size(); i++) {
								try{
									ofertaBCV=ofertasBCVList.get(i);
									//VALIDAR SI LA OFERTA YA ESTA REGISTRADA EN INFI
									if(!ofertaDao.isOfertaBCVRegistrada(ofertaBCV.getCodigo())){
										cedula=ofertaBCV.getOfertante();
										
										//INCIDENCIA ITS-3069 Proceso de envio Web Services SIMADI Persona Juridica no informa digito verificador
										//cedula=ofertaBCV.getOfertante().substring(1,cedula.length()-1);	
										cedula=ofertaBCV.getOfertante().substring(1,cedula.length());
										
										tipoDoc=ofertaBCV.getOfertante().substring(0, 1);
										nombre=ofertaBCV.getOfertante();
										
										querysEjecutar.add(
											ofertaDao.insertarOrdenBCV(
													cedula,//cedRif
													tipoDoc, //tipoDocumento
													nombre, //nombreCliente
													ofertaBCV.getMonto(), //monto
													ofertaBCV.getTasa(), //tasa
													ofertaBCV.getJornada(), //jornada
													ConstantesGenerales.VERIFICADA_APROBADA,
													"",//dealno
													fechaActual, //fecha pacto
													fechaActual, //fecha registro
													fechaActual, //fecha valor
													ConstantesGenerales.ID_PRODUCTO_OPICS_OPER_CAMBIO,
													ConstantesGenerales.ID_SUBPROD_OFERTA_OPICS_INTERBANCARIA,
													origen,
													ofertaBCV.getCodigo()));
											
											procesarQuerysPorLote(querysEjecutar);								
									}
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										//querysEjecutar.clear();
									}
								}
								ofertaDao.ejecutarStatementsBatch(querysEjecutar);						
						}else{
							proceso.setDescripcionError("No existen Ofertas Interbancarias");
						}
						
					} catch (Exception e) {
						Logger.error(this,e.getMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
						
						boolean errorControlado = false;
						String codigoError="";		
						
						//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
						for (ErroresAltoValor tmp: ErroresAltoValor.values() ) {
							if(e.toString().contains(tmp.getCodigoError())){
								errorControlado = true;	
								codigoError=tmp.getCodigoError();
								break;
							}
				        }			
						if(errorControlado){
							proceso.setDescripcionError("Error al consultar Ofertas. Valide si la jornada se encuentra activa. Código de error:"+codigoError);
						}else{
							proceso.setDescripcionError("Ha ocurrido un error de comunicación con el Web Service de consulta de Ofertas Alto Valor BCV"+e.getMessage());
						}
					}
					
				}
			}else{
				proceso.setDescripcionError("No existe Jornada Activa");
			}
		} catch (Exception e) {
			proceso.setDescripcionError("Error en la carga de ofertas:"+e.getMessage());
		}
	
	}
	
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	protected void procesarQuerysPorLote(ArrayList<String> sentencias) throws SQLException{
		//EJECUCIÓN DE QUERYS
		Logger.debug(this, "EJECUCIÓN DE QUERYS");
		++cantidadTotalOrdenes;
		++cantidadLoteOrdenesProcesadas;
		if (ConstantesGenerales.COMMIT_REGISTROS_CARGA_OFERTAS_OPICS == cantidadLoteOrdenesProcesadas) {
			cantidadTotalOrdenesProcesadas = cantidadTotalOrdenesProcesadas + cantidadLoteOrdenesProcesadas;
			ofertaDao.ejecutarStatementsBatchBool(sentencias);
			sentencias.clear();
			cantidadLoteOrdenesProcesadas = 0;
			Logger.info(this, "Ordenes enviadas por COMMIT en proceso de CARGA DE OFERTAS : " + cantidadLoteOrdenesProcesadas);
		}
		Logger.info(this, "Realizacion de commit al numero de registro N° " + cantidadTotalOrdenesProcesadas);
	}
	
	public static void main(String[] args) {
		System.out.println(new BigDecimal("00000009880726"));
	}
	
}
