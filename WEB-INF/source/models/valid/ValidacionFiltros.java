/*
 * Created on 05/12/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package models.valid;
import javax.sql.*;
import megasoft.*;

/**
 * Clase utilitaria para la validaci&oacute;n de filtros
 * @author Megasoft Computaci&oacute;n.
 *
 */
public class ValidacionFiltros{
	
	private boolean flag = true;
	
	/**
	 * Retorna el valor de flag
	 * @return
	 */
	public boolean getFlag()
	{
		return flag;
	}
	/**
	 * Retorna el record con los errores de campos obligatorios.
	 * @param nombre_filtro nombre de la solicitud
	 * @param record record para añadir solicitud
	 * @return
	 */		
	public DataSet getNewRecord(String nombre_filtro, DataSet _record, DataSource _dso) throws Exception
	{		
		if ((_record.getValue("fe_desde")!=null) &&(_record.getValue("fe_hasta")==null))
		{
			flag=false;
			_record.addError(nombre_filtro,"Debe Ingresar ambas Fechas");
		}
		if ((_record.getValue("fe_desde")==null) &&(_record.getValue("fe_hasta")!=null))
		{
			flag=false;
			_record.addError(nombre_filtro,"Debe Ingresar ambas Fechas");
		}							
		if ((_record.getValue("fe_desde")!=null) && (_record.getValue("fe_hasta")!=null))
		{
			if (_record.getValue("fe_desde").compareTo(_record.getValue("fe_hasta"))>0)
			{
				flag=false;
				_record.addError(nombre_filtro,"Fecha Desde es Mayor a la Fecha Hasta");
					
			}
			if (_record.getValue("fe_hasta").compareTo(Util.getDate())>0)
			{
				flag=false;
				_record.addError(nombre_filtro,"Fecha hasta es Mayor a la del Dia de hoy");			
			}
			
		}
		return _record;
	}
		
		/**
		 * Retorna los dias de un mes y año especifico
		 * @author Francisco Javier Pant&oacute; Doce
		 *
		 * To change the template for this generated type comment go to
		 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
		 */
		private static int diaPorMes(int mes, int ano) throws Exception
		{
			if (mes==0)
			{
				return 31;
			}
			else if (mes==1)
			{
				if (ano==1900)
				{
					return 28;
				}
				else
				{
					if (ano % 4==0)
						return 29;
					else
						 return 28;
				}
			}
			else if (mes==2)
			{
				return 31;
			}			
			else if (mes==3)
			{
				return 30;
			}
			else if (mes==4)
			{
				return 31;
			}
			else if (mes==5)
			{
				return 30;
			}
			else if (mes==6)
			{
				return 31;
			}		
			else if (mes==7)
			{
				return 31;
			}
			else if (mes==8)
			{
				return 30;
			}
			else if (mes==9)
			{
				return 31;
			}
			else if (mes==10)
			{
				return 30;
			}
			else if (mes==11)
			{
				return 31;
			}
			else
				throw new Exception("El mes "+ mes +" no es correcto");
		}
		/**
		 * Retorna el mes que sigue
		 * @param mes
		 * @return
		 * @throws Exception
		 */
		private static int mesSig(int mes) throws Exception
		{
			if (mes==11)
				return 0;
			else if (mes<0)
				throw new Exception("El mes "+ mes +" no es correcto");
			else if (mes<11)
				return mes+1;
			else			
				throw new Exception("El mes "+ mes +" no es correcto");
		}
		/**
		 * Retorna la resta en dias de dos fechas
		 * @param dia1
		 * @param mes1
		 * @param ano1
		 * @param dia2
		 * @param mes2
		 * @param ano2
		 * @return
		 */
		public static int restaDias(int dia1, int mes1, int ano1, int dia2, int mes2, int ano2) throws Exception
		{
		
			try
			{ 
				if (ano1>ano2)
					throw new Exception("El primer año "+ ano1 +" es mayor al segundo año " + ano2);
				else if ((mes1>mes2) && (ano1==ano2))
					throw new Exception("El primer mes "+ mes1 +" es mayor al segundo mes " + ano2 +" para el mismo año");		
			
				else if ((mes1==mes2) && (ano1==ano2))
					return dia2 - dia1;
			
				else if ((mes1<mes2) && (ano1==ano2))
				{
					int dia_restantes = 0;
					int mes_i = mes1;
					while (mes_i<mes2)
					{
						dia_restantes = dia_restantes + diaPorMes(mes_i,ano1);
						mes_i++;
					}
				
					return dia_restantes - dia1 + dia2;
				}
				else
				{
					int dia_restantes = 0;
					int ano_i = ano1;
					int mes_i = mes1;
					while (ano_i<=ano2)
					{
						if (ano_i==ano2)
						{
							if (mes_i==mes2)
							{
								dia_restantes=dia_restantes+dia2;
								ano_i++;
							}	
							else 
							{
								dia_restantes = dia_restantes + diaPorMes(mes_i,ano1);
								mes_i = mesSig(mes_i);
							
								if (mes_i==0)
									ano_i++;
							}
						}
						else
						{
							dia_restantes = dia_restantes + diaPorMes(mes_i,ano1);
							mes_i = mesSig(mes_i);
						
							if (mes_i==0)
							   ano_i++; 
						}
					}
					return dia_restantes - dia1;
				}
			}
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
				return 0;
			}
		}
		
	   public DataSet getNewRecord2(String nombre_filtro, DataSet _record, DataSource _dso) throws Exception
	   {
		   if ((_record.getValue("fe_desde")!=null) && (_record.getValue("fe_hasta")==null))
		   {
			   flag=false;
			   _record.addError(nombre_filtro,"Debe Ingresar ambas Fechas de Requisici&oacute;n");
		   }
		   if ((_record.getValue("fe_desde")==null)&&(_record.getValue("fe_hasta")!=null))
		   {
			   flag=false;
			   _record.addError(nombre_filtro,"Debe Ingresar ambas Fechas de Requisici&oacute;n");
		   }
		   if ((_record.getValue("fe_desde")!=null) && (_record.getValue("fe_hasta")!=null))
		   {
			   if (_record.getValue("fe_desde").compareTo(_record.getValue("fe_hasta"))>0)
			   {
				   flag=false;
				   _record.addError(nombre_filtro,"Fecha Desde es Mayor a la Fecha Hasta");
			   }
			   int dia1 =Integer.parseInt(_record.getValue("fe_desde").substring(8,10));
			   int mes1 =Integer.parseInt(_record.getValue("fe_desde").substring(5,7)) - 1  ;
			   int ano1 =Integer.parseInt(_record.getValue("fe_desde").substring(0,4));
			   int dia2 =Integer.parseInt(_record.getValue("fe_hasta").substring(8,10));
			   int mes2 =Integer.parseInt(_record.getValue("fe_hasta").substring(5,7)) - 1  ;
			   int ano2 =Integer.parseInt(_record.getValue("fe_hasta").substring(0,4));

			   //** Obtener par&aacute;metros de configuracion para validacion de dias para     filtro Inventario
			   int dias_filtro_inventario = 0;
			   DataSet dsParamConf = db.get(_dso,"Select * from parametros_sistema where parametro = 'dias_validacion_filtro_inventario'");
			   if (dsParamConf.next())
				   dias_filtro_inventario = Integer.parseInt(dsParamConf.getValue("valor"));
			   if (restaDias(dia1, mes1, ano1, dia2, mes2, ano2)>dias_filtro_inventario)
			   {
				   flag=false;
				   _record.addError(nombre_filtro,"Rango de fechas no debe ser mayor a " + dias_filtro_inventario + " Dias");
			   }
		   }
		   return _record;
	   }
		
		public String getRedirectParameters() throws Exception
		{
			return "pagenumber=1";		
		}
}