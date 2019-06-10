package com.ysttench.application.msd.settings.web.rdto.util;

import com.ysttench.application.common.modbus4j.ModbusFactory;
import com.ysttench.application.common.modbus4j.ModbusMaster;
import com.ysttench.application.common.modbus4j.exception.ModbusInitException;
import com.ysttench.application.common.modbus4j.ip.IpParameters;
import com.ysttench.application.common.modbus4j.requset.ModbusReq;
import com.ysttench.application.common.modbus4j.requset.OnRequestBack;

public class Modbus {
    private static String MIP;
    private int Port;
    private ModbusMaster mModbus;

    private static Modbus mInstance;

    public Modbus(String IP,int Port){
        this.MIP=IP;
        this.Port=Port;
        call();
    }

    public static Modbus getInstance(String IP, int Port) {
        if (mInstance == null|| !MIP.equals(IP)) {
            synchronized (Modbus.class) {
                if (mInstance == null|| !MIP.equals(IP)) {
                    mInstance = new Modbus(IP,Port);
                }
            }
        }
        return mInstance;
    }

    private void call(){
        //连接modbus
        ModbusFactory mModbusFactory = new ModbusFactory();
        IpParameters params = new IpParameters();
        params.setHost(MIP);
        params.setPort(Port);
        params.setEncapsulated(false);

        mModbus = mModbusFactory.createTcpMaster(params, true);
        mModbus.setRetries(4);
        mModbus.setTimeout(2000);
        mModbus.setRetries(0);
        try {
            mModbus.init();
        }catch (ModbusInitException e){
            mModbus.destroy();
        }
    }

    public String writeCoil(int address, boolean bool){

        if(mInstance==null){
            synchronized (Modbus.class) {
                if (mInstance == null) {
                    mInstance = new Modbus(MIP,Port);
                }
            }
        }
        final String[] t = {"1"};
        //改变开关
        ModbusReq.writeCoil(mModbus, new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
                t[0] ="0";
            }
            @Override
            public void onFailed(String msg) {

            }
        }, 1, address, bool);

        return t[0];
    }
}
