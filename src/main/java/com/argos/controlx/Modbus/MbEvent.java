/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.controlx.Modbus;

/**
 *
 * @author Zaparivanny Pavel
 */
public class MbEvent{
    private final int slaveAddress;
    private final int functionCode;
    private final int[] data;
    private final MbStatus status;
    
    /**
     * 
     * @param slaveAddress - slave address 0x0 - 0xff
     * @param functionCode - function code 0x0 - 0x20
     * @param data - array integer
     * @param status - 0 - ok, 1 - error crc, 2 - time out
     *  3 - data corruption, default - unknown
     */
    public MbEvent(int slaveAddress, int functionCode, int[] data, MbStatus status){
        this.data = data;
        this.functionCode = functionCode;
        this.slaveAddress = slaveAddress;
        this.status = status;
    }
    
    public enum MbStatus{
        OK,
        TIME_OUT,
        DATA_CORRUPTION,
        ERROR_CRC,
        UNKNOWN,
        ANYTHUNG,
    }
   
    public MbStatus getStatus(){
        /*
        switch(status){
            case 0: return MbStatus.OK;
            case 1: return MbStatus.ERROR_CRC;
            case 2: return MbStatus.TIME_OUT;
            case 3: return MbStatus.DATA_CORRUPTION;
        }
        return MbStatus.UNKNOWN;*/
        return status;
    }
    
    public int getAddress(){
        return slaveAddress;
    }
    
    public int getFunctionCode(){
        return functionCode;
    }
    
    public int[] getData(){
        return data;
    }
    
}
