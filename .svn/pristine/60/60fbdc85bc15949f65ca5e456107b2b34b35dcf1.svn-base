package com.bdv.infi_remediacion.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class remediacionmonto {
//protected Connection conn;

    public BigDecimal remediacionmonto1(BigDecimal monto,DataSource _dso) throws SQLException{
    	Connection cn = _dso.getConnection();
    	CallableStatement cst = cn.prepareCall("{call fconv_bsf (?)}");
    	cst.setBigDecimal(1, monto);
    	cst.registerOutParameter(2, java.sql.Types.VARCHAR);
        cst.execute();
        
        BigDecimal monto1 = cst.getBigDecimal(2);
        System.out.println("monto: " + monto1);
        //return remediacionmonto;
        cn.close();
        return monto1;
    }
   
}


