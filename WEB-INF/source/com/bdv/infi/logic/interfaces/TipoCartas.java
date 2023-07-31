package com.bdv.infi.logic.interfaces;

/**Constantes para el manejo de los distintos tipos de cartas usados en toma de orden y adjudicación*/
public interface TipoCartas {

	/**Carta mandato*/
	public static final int CARTA_MANDATO = 1;
	/**Carta mandato con recompra*/	
	public static final int CARTA_MANDATO_WI = 2;
	/**Carta de adjudicación con recompra*/	
	public static final int CARTA_ADJ_WI = 3;
	/**Carta de adjudicación*/	
	public static final int CARTA_ADJ = 4;
	/**Carta de adjudicación con reintegro*/
	public static final int CARTA_ADJ_REINTEGRO = 5;
	/**Carta de adjudicación con reintegro y recompra*/
	public static final int CARTA_ADJ_REINTEGRO_WI = 6;
	/**Carta de adjudicación con reintegro y recompra*/
	public static final int CARTA_ADJ_DEBITO = 7;
	/**Carta que debe ser mostarda antes de tomar la orden de sitme*/
	public static final int CARTA_ANT_ORDEN = 8;
}
