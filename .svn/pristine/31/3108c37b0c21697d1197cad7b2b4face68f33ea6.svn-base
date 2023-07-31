package org.bcv.serviceAltoValor;

public class BancoUniversalProxy implements org.bcv.serviceAltoValor.BancoUniversal_PortType {
  private String _endpoint = null;
  private org.bcv.serviceAltoValor.BancoUniversal_PortType bancoUniversal_PortType = null;
  
  public BancoUniversalProxy() {
    _initBancoUniversalProxy();
  }
  
  public BancoUniversalProxy(String endpoint) {
    _endpoint = endpoint;
    _initBancoUniversalProxy();
  }
  
  private void _initBancoUniversalProxy() {
    try {
      bancoUniversal_PortType = (new org.bcv.serviceAltoValor.BancoUniversal_ServiceLocator()).getBancoUniversalPort();
      if (bancoUniversal_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)bancoUniversal_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)bancoUniversal_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (bancoUniversal_PortType != null)
      ((javax.xml.rpc.Stub)bancoUniversal_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.bcv.serviceAltoValor.BancoUniversal_PortType getBancoUniversal_PortType() {
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType;
  }
  
  public java.lang.String cambiarClave(java.lang.String CLAVENUEVA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.cambiarClave(CLAVENUEVA);
  }
  
  public java.lang.String oferta(java.lang.String NURIFCLIENTE, java.lang.String NBCLIENTE, java.lang.String COMONEDAOFE, java.math.BigDecimal MOOFERTA, java.math.BigDecimal TSOFERTA, java.lang.String COREFINSTITUCION, java.lang.String COJORNADA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.oferta(NURIFCLIENTE, NBCLIENTE, COMONEDAOFE, MOOFERTA, TSOFERTA, COREFINSTITUCION, COJORNADA);
  }
  
  public java.lang.String demanda(java.lang.String NURIFCLIENTE, java.lang.String NBCLIENTE, java.lang.String COMONEDADEM, java.math.BigDecimal MODEMANDA, java.math.BigDecimal TSDEMANDA, java.lang.String COREFINSTITUCION, java.lang.String COJORNADA, java.lang.String COCTACONVENIO) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.demanda(NURIFCLIENTE, NBCLIENTE, COMONEDADEM, MODEMANDA, TSDEMANDA, COREFINSTITUCION, COJORNADA, COCTACONVENIO);
  }
  
  public java.lang.String jornadaActiva() throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.jornadaActiva();
  }
  
  public java.lang.String ANULAOFERTA(java.lang.String COJORNADA, java.lang.String COOFERTA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.ANULAOFERTA(COJORNADA, COOFERTA);
  }
  
  public java.lang.String ANULADEMANDA(java.lang.String COJORNADA, java.lang.String CODEMANDA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.ANULADEMANDA(COJORNADA, CODEMANDA);
  }
  
  public java.lang.String GETPACTOXML(java.lang.String COJORNADA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.GETPACTOXML(COJORNADA);
  }
  
  public java.lang.String errores() throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.errores();
  }
  
  public java.lang.String pacto(java.lang.String COJORNADA, java.lang.String COOFERTA, java.lang.String CODEMANDA, java.math.BigDecimal MOPACTO) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.pacto(COJORNADA, COOFERTA, CODEMANDA, MOPACTO);
  }
  
  public java.lang.String statusjornada(java.lang.String COJORNADA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.statusjornada(COJORNADA);
  }
  
  public java.lang.String OFERTASXBCV(java.lang.String COJORNADA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.OFERTASXBCV(COJORNADA);
  }
  
  public java.lang.String OFERTASXML(java.lang.String COJORNADA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.OFERTASXML(COJORNADA);
  }
  
  public java.lang.String DEMANDASXML(java.lang.String COJORNADA) throws java.rmi.RemoteException, org.bcv.service.Exception{
    if (bancoUniversal_PortType == null)
      _initBancoUniversalProxy();
    return bancoUniversal_PortType.DEMANDASXML(COJORNADA);
  }
  
  
}