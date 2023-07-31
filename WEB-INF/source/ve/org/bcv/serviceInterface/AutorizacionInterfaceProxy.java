package ve.org.bcv.serviceInterface;

public class AutorizacionInterfaceProxy implements ve.org.bcv.serviceInterface.AutorizacionInterface {
  private String _endpoint = null;
  private ve.org.bcv.serviceInterface.AutorizacionInterface autorizacionInterface = null;
  
  public AutorizacionInterfaceProxy() {
    _initAutorizacionInterfaceProxy();
  }
  
  public AutorizacionInterfaceProxy(String endpoint) {
    _endpoint = endpoint;
    _initAutorizacionInterfaceProxy();
  }
  
  private void _initAutorizacionInterfaceProxy() {
    try {
      autorizacionInterface = (new ve.org.bcv.service.AutorizacionLocator()).getAutorizacionPort();
      if (autorizacionInterface != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)autorizacionInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)autorizacionInterface)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (autorizacionInterface != null)
      ((javax.xml.rpc.Stub)autorizacionInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ve.org.bcv.serviceInterface.AutorizacionInterface getAutorizacionInterface() {
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface;
  }
  
  public java.lang.String VENTADIV(java.lang.String COTIMOVIMIENTO, java.lang.String COCLIENTE, java.lang.String NBCLIENTE, java.math.BigDecimal MOBASE, java.math.BigDecimal TSCAMBIO, java.lang.String COUCTATRANS, java.math.BigDecimal MOTRANS, long COMOTIVOOPERACION, java.lang.String NUCTACONV20, java.lang.String TXTLFCLIENTE, java.lang.String TXEMAILPINC) throws java.rmi.RemoteException, ve.org.bcv.serviceInterface.Exception{
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface.VENTADIV(COTIMOVIMIENTO, COCLIENTE, NBCLIENTE, MOBASE, TSCAMBIO, COUCTATRANS, MOTRANS, COMOTIVOOPERACION, NUCTACONV20, TXTLFCLIENTE, TXEMAILPINC);
  }
  
  public java.lang.String COMPRADIV(java.lang.String COTIMOVIMIENTO, java.lang.String COCLIENTE, java.lang.String NBCLIENTE, java.math.BigDecimal MOBASE, java.math.BigDecimal TSCAMBIO, java.lang.String COUCTATRANS, java.math.BigDecimal MOTRANS, long COMOTIVOOPERACION, java.lang.String NUCTACONV20, java.lang.String TXTLFCLIENTE, java.lang.String TXEMAILPINC, java.lang.String RIFCOMERCIO) throws java.rmi.RemoteException, ve.org.bcv.serviceInterface.Exception{
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface.COMPRADIV(COTIMOVIMIENTO, COCLIENTE, NBCLIENTE, MOBASE, TSCAMBIO, COUCTATRANS, MOTRANS, COMOTIVOOPERACION, NUCTACONV20, TXTLFCLIENTE, TXEMAILPINC, RIFCOMERCIO);
  }
  
  public java.lang.String MOTIVOS() throws java.rmi.RemoteException, ve.org.bcv.serviceInterface.Exception{
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface.MOTIVOS();
  }
  
  public java.lang.String TASASCAMBIO() throws java.rmi.RemoteException, ve.org.bcv.serviceInterface.Exception{
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface.TASASCAMBIO();
  }
  
  public java.lang.String ERRORES() throws java.rmi.RemoteException, ve.org.bcv.serviceInterface.Exception{
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface.ERRORES();
  }
  
  public java.lang.String TIPOSMOVIMIENTOS() throws java.rmi.RemoteException, ve.org.bcv.serviceInterface.Exception{
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface.TIPOSMOVIMIENTOS();
  }
  
  public java.lang.String MOVIMIENTOS(java.lang.String FEMOVIMIENTO) throws java.rmi.RemoteException, ve.org.bcv.serviceInterface.Exception{
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface.MOVIMIENTOS(FEMOVIMIENTO);
  }
  
  public java.lang.String ANULAR(java.lang.String CO_MOVIMIENTO, java.lang.String COTIMOVIMIENTO, java.lang.String TXMOTIVOANULA) throws java.rmi.RemoteException, ve.org.bcv.serviceInterface.Exception{
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface.ANULAR(CO_MOVIMIENTO, COTIMOVIMIENTO, TXMOTIVOANULA);
  }
  
  public java.lang.String cambiarClave(java.lang.String CLAVENUEVA) throws java.rmi.RemoteException, ve.org.bcv.serviceInterface.Exception{
    if (autorizacionInterface == null)
      _initAutorizacionInterfaceProxy();
    return autorizacionInterface.cambiarClave(CLAVENUEVA);
  }
  
  
}