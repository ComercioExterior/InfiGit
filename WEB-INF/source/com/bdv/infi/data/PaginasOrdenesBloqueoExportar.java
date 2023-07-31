package com.bdv.infi.data;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PaginasOrdenesBloqueoExportar {
		private ArrayList<GrupoOrdenesBloqueo> paginas;
		private BigDecimal montoCapitalTotal; //monto capital total
		private BigDecimal montoComisionTotal;//monto comision total
		private BigDecimal montoDesbloqueoTotal;//monto desbloqueo total
		private String unidadInversion;
		private int cantRegistros=0;
		
		public BigDecimal getMontoCapitalTotal() {
			return montoCapitalTotal;
		}
		public void setMontoCapitalTotal(BigDecimal montoCapitalTotal) {
			this.montoCapitalTotal = montoCapitalTotal;
		}
		public BigDecimal getMontoComisionTotal() {
			return montoComisionTotal;
		}
		public void setMontoComisionTotal(BigDecimal montoComisionTotal) {
			this.montoComisionTotal = montoComisionTotal;
		}
		public BigDecimal getMontoDesbloqueoTotal() {
			return montoDesbloqueoTotal;
		}
		public void setMontoDesbloqueoTotal(BigDecimal montoDesbloqueoTotal) {
			this.montoDesbloqueoTotal = montoDesbloqueoTotal;
		}
		public ArrayList<GrupoOrdenesBloqueo> getPaginas() {
			return paginas;
		}
		public void setPaginas(ArrayList<GrupoOrdenesBloqueo> paginas) {
			this.paginas = paginas;
		}
		public String getUnidadInversion() {
			return unidadInversion;
		}
		public void setUnidadInversion(String unidadInversion) {
			this.unidadInversion = unidadInversion;
		}
		public int getCantRegistros() {
			return cantRegistros;
		}
		public void setCantRegistros(int cantRegistros) {
			this.cantRegistros = cantRegistros;
		}
		
				
}
