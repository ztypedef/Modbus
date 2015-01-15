/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argos.controlx.Modbus;

import com.argos.controlx.Modbus.MbEvent.MbStatus;


/**
 * example:
 * 
 * SerialPortCmd s = new SerialPortCmd("COM3");
 * MODBUS mb = new MODBUS();
 * s.openPort(name)
 * int[] pack = s.sendData();
 * mb.parsePack(pack);
 * 
 * mb.getStatus();
 * mb.getSlaveAddress();
 * mb.getCMD();
 * mb.getData();
 * 
 */

/**
 *
 * @author Zaparivanny P
 */
public class MbParse {

    FramePack frame;

    private class FramePack {
        int slaveAddress;
        int cmd;
        int[] data;
        int CRC;
        MbStatus status;
    }
    
    
    
    /**
     *
     * @param pack
     */
    public void parsePacket(byte[] pack) throws ArrayIndexOutOfBoundsException{
        
        frame = new FramePack();
        if(pack == null){
            frame.status = MbStatus.TIME_OUT;
            return;
        }
        frame.status = MbStatus.OK;
        /*
        System.out.print("receive byte: ");
        for(int i = 0; i < pack.length; i++){
            System.out.print("0x" + Integer.toHexString(pack[i] & 0xff) + " ");
        }
        System.out.println("");
        */
        
        if(pack.length < 3){
            frame.status = MbStatus.DATA_CORRUPTION;
            return ;
        }
        if(!compareCRC(pack)){
            //System.out.println("ERROR CRC");
            frame.status = MbStatus.ERROR_CRC;
            return;
        }
        try{
            frame.slaveAddress = (int) pack[0];
            frame.cmd = (int) pack[1];

            switch(frame.cmd)
            {
                case 0x3:
                case 0x4:
                {
                    if(pack.length != (pack[2] + 3 + 2) ){
                        frame.status = MbStatus.DATA_CORRUPTION;
                        return;
                    }
                    int[] data = new int[pack[2]];
                    for(int i = 0; i < data.length; i+=2){
                        data[i / 2] = ((int) ((pack[3 + i] << 8) & 0xFF00) | (int) (pack[3 + i + 1] & 0xFF));
                    }
                    frame.data = data;
                    frame.CRC = ((int) ((pack[pack.length - 1] << 8) & 0xFF00) | (int) (pack[pack.length - 2] & 0xFF));
                    break;
                }

                case 0x0f:
                case 0x10:
                    break;

                default:
                {
                    frame.status = MbStatus.UNKNOWN;
                    return;
                }
            }
        }catch(ArrayIndexOutOfBoundsException ex){
            System.err.println(ex);
        }
    }
    
    private boolean compareCRC( byte[] pack){
        int CRCpack = 0;
        int crc;
        try{
            crc = CRC.calcCRC(pack, pack.length - 2);
            CRCpack = ((int) ((pack[pack.length - 1] << 8) & 0xFF00) | (int) (pack[pack.length - 2] & 0xFF));
            if(crc == CRCpack){
                return true;
            }
            //System.out.print(crc);
            //System.out.print(" ");
            //System.out.println(CRCpack);
        }catch(ArrayIndexOutOfBoundsException ex){
            System.out.println(ex);
        }
        return false;   
    }
    
    public int[] getData(){ //TODO add exeption
        if((frame.cmd == 16) || (frame.cmd == 15)){
            return null;
        }
        return frame.data;
    }
    
    public int getCmd()
    {
        return frame.cmd;
    }
    
    public int getSlaveAddress(){
        return frame.slaveAddress;
    }
    
    public MbStatus getStatus(){
        return frame.status;
    }

}

