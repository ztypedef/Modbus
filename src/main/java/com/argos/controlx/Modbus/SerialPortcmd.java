/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argos.controlx.Modbus;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author Zaparivanny P
 */
public class SerialPortcmd {
    
    private SerialPort serialPort;
    private final Timer timer;
    SerialPortAction spAction = null;
    
    public SerialPortcmd(){
        
        int delay = 10; //TODO config
        timer = new Timer(delay, taskPerformer);
        timer.setRepeats(false);
    }
    
    public void openPort(String namePort) throws SerialPortException{
        serialPort = new SerialPort(namePort);
        
        serialPort.openPort();//Open port
        serialPort.setParams(115200, 8, 1, 0);//Set params
        serialPort.addEventListener(new SerialPortHandler());//Add SerialPortEventListener
    }
    
    public void addAction(SerialPortAction a){
        spAction = a;
    }
    
    ActionListener taskPerformer = new ActionListener() {
    @Override
        public void actionPerformed(ActionEvent evt) {
            System.out.println("\u001B[31m[" + Thread.currentThread().getName() + "] Time out\u001B[37m");
            spAction.actionPerfomed(null);
        }
    };

    private class SerialPortHandler implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() > 0){//Check bytes count in the input buffer 
                    timer.stop();
                    try {
                        spAction.actionPerfomed(serialPort.readBytes());//TODO write
                    } catch (SerialPortException ex) {
                        Logger.getLogger(SerialPortcmd.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    public boolean sendPackage(byte[] pack) {
        try {
           serialPort.writeBytes(pack);
           timer.restart();
       } catch (SerialPortException ex) {
           System.out.println(ex);
           return false;
       }
       return true;
    }
     
    public boolean closePort(){
       try {
           serialPort.closePort();
       } catch (SerialPortException ex) {
           System.out.println(ex);
           return false;
       }
       return true;
    }

}
