/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.controlx.Modbus;

import jssc.SerialPortException;

/**
 *
 * @author Zaparivanny Pavel
 */
public class Modbus {
    
    private MbAction mbAction;
    private SerialPortcmd sp;
    
    public Modbus() {
        sp = new SerialPortcmd();
        sp.addAction(spAction);
    }
    
    public void addActionMb(MbAction e){
        mbAction = e;
    }
    
    SerialPortAction spAction = new SerialPortAction() {

        @Override
        public void actionPerfomed(byte[] data) {
            MbParse parse = new MbParse();
            parse.parsePacket(data);
            mbAction.actionPerfomed(new MbEvent(parse.getSlaveAddress(), parse.getCmd(), parse.getData(), parse.getStatus()));
        }
    };
    
    public void send(byte[] pack){
        sp.sendPackage(pack);
    }
    
    
    public void openPort(String namePort) throws SerialPortException{
        sp.openPort(namePort);
    }
    
    public void closePort(){
        sp.closePort();
    }
}
