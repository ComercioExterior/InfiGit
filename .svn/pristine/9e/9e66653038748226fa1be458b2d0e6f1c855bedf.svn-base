package org.bcv.service;

public class AutorizacionProxy implements org.bcv.service.Autorizacion_PortType {
  private String _endpoint = null;
  private org.bcv.service.Autorizacion_PortType autorizacion_PortType = null;
  
  public AutorizacionProxy() {
    _initAutorizacionProxy();
  }
  
  public AutorizacionProxy(String endpoint) {
    _endpoint = endpoint;
    _initAutorizacionProxy();
  }
  
  private void _initAutorizacionProxy() {
    try {
      autorizacion_PortType = (new org.bcv.service.Autorizacion_ServiceLocator()).getautorizacionPort();
      if (autorizacion_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)autorizacion_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)autorizacion_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (autorizacion_PortType != null)
      ((javax.xml.rpc.Stub)autorizacion_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.bcv.service.Autorizacion_PortType getAutorizacion_PortType() {
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType;
  }
  
  public java.lang.String cambiarClave(java.lang.String CLAVENUEVA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType.cambiarClave(CLAVENUEVA);
  }
  
  public java.lang.String VENTADIV(java.lang.String COTIMOVIMIENTO, java.lang.String COCLIENTE, java.lang.String NBCLIENTE, java.math.BigDecimal MOBASE, java.math.BigDecimal TSCAMBIO, java.lang.String COUCTATRANS, java.math.BigDecimal MOTRANS, long COMOTIVOOPERACION, java.lang.String NUCTACONV20, java.lang.String TXTLFCLIENTE, java.lang.String TXEMAILPINC) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType.VENTADIV(COTIMOVIMIENTO, COCLIENTE, NBCLIENTE, MOBASE, TSCAMBIO, COUCTATRANS, MOTRANS, COMOTIVOOPERACION, NUCTACONV20, TXTLFCLIENTE, TXEMAILPINC);
  }
  
  public java.lang.String COMPRADIV(java.lang.String COTIMOVIMIENTO, java.lang.String COCLIENTE, java.lang.String NBCLIENTE, java.math.BigDecimal MOBASE, java.math.BigDecimal TSCAMBIO, java.lang.String COUCTATRANS, java.math.BigDecimal MOTRANS, long COMOTIVOOPERACION, java.lang.String NUCTACONV20, java.lang.String TXTLFCLIENTE, java.lang.String TXEMAILPINC, java.lang.String RIFCOMERCIO) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType.COMPRADIV(COTIMOVIMIENTO, COCLIENTE, NBCLIENTE, MOBASE, TSCAMBIO, COUCTATRANS, MOTRANS, COMOTIVOOPERACION, NUCTACONV20, TXTLFCLIENTE, TXEMAILPINC, RIFCOMERCIO);
  }
  
  public java.lang.String MOTIVOS() throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType.MOTIVOS();
  }
  
  public java.lang.String TASASCAMBIO() throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType.TASASCAMBIO();
  }
  
  public java.lang.String ERRORES() throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType.ERRORES();
  }
  
  public java.lang.String TIPOSMOVIMIENTOS() throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType.TIPOSMOVIMIENTOS();
  }
  
  public java.lang.String MOVIMIENTOS(java.lang.String FEMOVIMIENTO) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType.MOVIMIENTOS(FEMOVIMIENTO);
  }
  
  public java.lang.String ANULAR(java.lang.String CO_MOVIMIENTO, java.lang.String COTIMOVIMIENTO, java.lang.String TXMOTIVOANULA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (autorizacion_PortType == null)
      _initAutorizacionProxy();
    return autorizacion_PortType.ANULAR(CO_MOVIMIENTO, COTIMOVIMIENTO, TXMOTIVOANULA);
  }
  
  
}