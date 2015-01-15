/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argos.controlx.Modbus;


/**
 *
 * @author Zaparivanny P
 */

public class FPackage extends CRC {
    

    public byte[] readDiscreteInputs(int slaveAddress, int startAddress, int quantityInputs){
        byte[] sendPackage = new byte[8];
        sendPackage[0] = (byte) slaveAddress;
        sendPackage[1] = (byte) 0x2;
        sendPackage[2] = (byte) (startAddress >> 8);
        sendPackage[3] = (byte) (startAddress & 0xff);
        //System.out.print("[MODBUS] quantityInputs: ");
        //System.out.println(quantityInputs);
        sendPackage[4] = (byte) ((quantityInputs >> 8) & 0xFF);
        sendPackage[5] = (byte) (quantityInputs & 0x00FF);
        //System.out.print("[MODBUS] quantityInputs: ");
        
        int CRC = super.calcCRC(sendPackage, 6);
        sendPackage[6] = (byte) (CRC & 0xff);
        sendPackage[7] = (byte) (CRC >> 8);
               
        return sendPackage;
    }
    
    public byte[] readHoldingRegisters(int slaveAddress, int startAddress, int quantityInputs){
        byte[] sendPackage = new byte[8];
        sendPackage[0] = (byte) slaveAddress;
        sendPackage[1] = (byte) 0x3;
        sendPackage[2] = (byte) (startAddress >> 8);
        sendPackage[3] = (byte) (startAddress & 0xff);
        //System.out.print("[MODBUS] quantityInputs: ");
        //System.out.println(quantityInputs);
        sendPackage[4] = (byte) ((quantityInputs >> 8) & 0xFF);
        sendPackage[5] = (byte) (quantityInputs & 0x00FF);
        //System.out.print("[MODBUS] quantityInputs: ");
        
        int CRC = super.calcCRC(sendPackage, 6);
        sendPackage[6] = (byte) (CRC & 0xff);
        sendPackage[7] = (byte) (CRC >> 8);
               
        return sendPackage;
    }
    
    public byte[] writeMultipleRegisters (int slaveAddress, int startAddress, int quantityRegisters, int[] data){
        
        if(quantityRegisters > 123 && quantityRegisters < 1){
           return null;
        }
        
        // calc size send package
        byte[] sendPackage = new byte[9 + quantityRegisters * 2]; 
        
        int count = 0;
        sendPackage[count++] = (byte) slaveAddress;
        sendPackage[count++] = (byte) 0x10;
        sendPackage[count++] = (byte) (startAddress >> 8);
        sendPackage[count++] = (byte) (startAddress & 0xff);
        
        sendPackage[count++] = (byte) (quantityRegisters >> 8);
        sendPackage[count++] = (byte) (quantityRegisters & 0xff);
        sendPackage[count++] = (byte) (quantityRegisters * 2);
        
        for(int i = 0; i < quantityRegisters; i++){
            sendPackage[count++] = (byte) ((data[i] >> 8) & 0xFF);
            sendPackage[count++] = (byte) (data[i] & 0xFF);
        }
        
        int CRC = super.calcCRC(sendPackage, count);
        sendPackage[count++] = (byte) (CRC & 0xff);
        sendPackage[count++] = (byte) (CRC >> 8);
        
        return sendPackage;
    }
    
    public byte[] writeMultipleRegisters (int slaveAddress, int startAddress, int[] data){

        int quantityRegisters = data.length;
        
        // calc size send package
        byte[] sendPackage = new byte[9 + quantityRegisters * 2]; 
        
        int count = 0;
        sendPackage[count++] = (byte) slaveAddress;
        sendPackage[count++] = (byte) 0x10;
        sendPackage[count++] = (byte) (startAddress >> 8);
        sendPackage[count++] = (byte) (startAddress & 0xff);
        
        sendPackage[count++] = (byte) (quantityRegisters >> 8);
        sendPackage[count++] = (byte) (quantityRegisters & 0xff);
        sendPackage[count++] = (byte) (quantityRegisters * 2);
        
        for(int i = 0; i < quantityRegisters; i++){
            sendPackage[count++] = (byte) ((data[i] >> 8) & 0xFF);
            sendPackage[count++] = (byte) (data[i] & 0xFF);
        }
        
        int CRC = super.calcCRC(sendPackage, count);
        sendPackage[count++] = (byte) (CRC & 0xff);
        sendPackage[count++] = (byte) (CRC >> 8);
        
        return sendPackage;
    }
}
