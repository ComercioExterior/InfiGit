package models.generacion_archivo_bash.debitos;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenesClienteDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class DebitosBrowse extends MSCModelExtend{
	@Override
	public void execute() throws Exception {
			//Se verifica si la contingencia se encuentra activa para mostrar la vista de generacion de archivos de debito
			String contingenciaActiva=ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.INDICADOR_CONTINGENCIA, this._dso);
			if(contingenciaActiva.equalsIgnoreCase("0"))
				throw new Exception (ConstantesGenerales.MENSAJE_VALIDACION_CONTINGENCIA);
		
			//Instanciacion de clases
			OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);
			UnidadInversionDAO unInv= new UnidadInversionDAO(_dso);
			
			
			//Mostrar en el filtro las unidades de ibversion
			unInv.listar();
			storeDataSet("unidad_inversion", unInv.getDataSet());
			storeDataSet("parametros", getDataSetFromRequest());
			
			//System.out.println("Paramentros: "+getDataSetFromRequest());
						
			//Mostrar en el filtro los Blotters
			
			if(_req.getParameter("unInv")!=null){
								
				unInv.listarBloterPorUi(Long.parseLong(_req.getParameter("unidad_inversion")));
				storeDataSet("bloter", unInv.getDataSet());
				DataSet _unidadSelecc=new DataSet();
				_unidadSelecc.append("unidad_seleccionada",java.sql.Types.VARCHAR);
				_unidadSelecc.addNew();
				_unidadSelecc.setValue("unidad_seleccionada",_req.getParameter("unidad_inversion"));
				storeDataSet("unidad_seleccionada", _unidadSelecc);
			
			}else{
					
				//Verificacion de la pantalla desde la que se llama la seccion de codigo (Si se llama desde la pantalla del browse se configura en la session el tipo de producto seleccionado)
				//ITS-617
				if(_req.getRequestURI().toUpperCase().indexOf("archivo_de_cobro-browse".toUpperCase())>-1) {																  
					String tipoOperacion= _record.getValue("tipo_operacion");
				  _req.getSession().setAttribute("tipoOperacion",tipoOperacion);						  		
				}
				
				DataSet _bloter=new DataSet();
				_bloter.append("bloter_id",java.sql.Types.VARCHAR);
				_bloter.append("bloter_descripcion",java.sql.Types.VARCHAR);
				_bloter.addNew();
				_bloter.setValue("bloter_id","");
				_bloter.setValue("bloter_descripcion","");
				storeDataSet("bloter", _bloter);
				
				DataSet _unidadSelecc=new DataSet();
				_unidadSelecc.append("unidad_seleccionada",java.sql.Types.VARCHAR);
				_unidadSelecc.addNew();
				_unidadSelecc.setValue("unidad_seleccionada","");
				storeDataSet("unidad_seleccionada", _unidadSelecc);
			}
			
			
			//Mostrar por defectos las fechas en el filtro
			DataSet fechas=confiD.mostrar_fechas_filter();
			storeDataSet("fechas", fechas);
			
		}
	}
