/*
 * Copyright (C) 2013 João Darcy Tinoco Sant´Anna Neto <jdtsncomp@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.joaodarcy.cronus.cabal.core.network;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class Packet {
    
    private final static int DUMP_COLUMN_PER_ROW = 19;
    
    protected final short opcode;
    protected final Header header;    
    protected final ByteBuffer data;    
    
    public Packet(Header header, short opcode, ByteBuffer data) throws IllegalArgumentException {
        if(header == null){
            throw new IllegalArgumentException("Packet header must no be null.");
        }
        if(data == null){
            throw new IllegalArgumentException("Packet raw data must not be null.");
        }
        this.header = header;
        this.opcode = opcode;
        this.data = data;
        
        if(this.data.order() != ByteOrder.LITTLE_ENDIAN){
            this.data.order(ByteOrder.LITTLE_ENDIAN);
        }
    }

    public Packet(Header header, short opcode, byte data[]) throws IllegalArgumentException {
        if(header == null){
            throw new IllegalArgumentException("Packet header must no be null.");
        }
        if(data == null){
            throw new IllegalArgumentException("Packet raw data must not be null.");
        }
        this.header = header;
        this.opcode = opcode;
        this.data = ByteBuffer.wrap(data);
        
        if(this.data.order() != ByteOrder.LITTLE_ENDIAN){
            this.data.order(ByteOrder.LITTLE_ENDIAN);
        }
    }

    public boolean getBoolean(){
        return getByte() == (byte)0x01;
    }
    
    public Header getHeader() {
        return header;
    }
    
    public short getOpcode() {
        return opcode;
    }
    
    public byte[] getData(){
        return data.array();
    }        
    
    public byte getByte(){
        return data.get();
    }
    
    public short getShort(){
        return data.getShort();
    }
    
    public int getInt(){
        return data.getInt();
    }
            
    public UInt8 getUInt8(){
        return UInt8.valueOf(getByte());
    }
    
    public UInt16 getUInt16(){
        return UInt16.valueOf(getShort());
    }
    
    public UInt32 getUInt32(){
        return new UInt32(getInt());
    }
    
    public String getString(int length){
        byte[] buffer = new byte[length];
        
        data.get(buffer);
        
        return new String(buffer);
    }
    
    public void skip(int byteCount){
        data.position(data.position() + byteCount);
    }
    
    public boolean write(OutputStream out){
        try{
            out.write(data.array());
            out.flush();
            return true;
        }catch(Throwable t){
            t.printStackTrace();
        }
        return false;
    }
    
    public String toByteString(){
        return toByteString(this.data.array());
    }
    
    public boolean isEqual(String filename, int ignoreBytesCount){
        FileInputStream fis = null;
        
        try{            
            fis = new FileInputStream(filename);
            byte buffer[] = new byte[512];
            int currentOffset = 0;
            int readSize = 0;
            int readCount;
            
            while((readCount = fis.read(buffer)) > 0){
                readSize += readCount;
                
                for(int i = 0 ; i < readCount && currentOffset < data.capacity() ; i++, currentOffset++){
                    if(data.array()[currentOffset] != buffer[i]){
                        System.out.printf("DIFF Offset: %04X (Current Value: %02X) (Expect Value: %02X)\n", currentOffset, data.array()[currentOffset], buffer[i]);                        
                        /*if(currentOffset > ignoreBytesCount){                            
                            return false;
                        }*/
                    }
                }
            }            
            
            if(readSize != currentOffset){
                System.out.printf("DIFF Current Size: %04X, Expected Size: %04X\n", currentOffset, readSize);
            }
            
            return true;
        }catch(Throwable t){
            t.printStackTrace();
            return false;
        }finally{
            if(fis != null){
                try{
                    fis.close();
                }catch(Throwable t){
                    t.printStackTrace();
                }
            }
        }        
    }
    
    public String toByteString(byte data[]){
        final char valueSeparator = ' ';
        final String rowNumberFormat = "%04X    ";
        final String valueFormat = "%02X";
        
        if(data.length > 0){
            StringBuilder stringBuilder = new StringBuilder((data.length * 3));
            StringBuilder charDataBuilder = new StringBuilder(DUMP_COLUMN_PER_ROW);
            
            stringBuilder.append('\n');
            
            // Print header
            String rowNumberSpacing = String.format(rowNumberFormat, 0);
            for(int i = 0 ; i < rowNumberSpacing.length() ; i++){
                stringBuilder.append(' ');
            }
            
            
            for(int i = 0 ; i < DUMP_COLUMN_PER_ROW ; i++){
               if(i != 0){
                    stringBuilder.append(valueSeparator);
                }
               stringBuilder.append(String.format(valueFormat, i));
            }
            stringBuilder.append('\n');
            stringBuilder.append('\n');
            
            for(int i = 0, c = 0 ; i < data.length ; i++, c++){
                if(i != 0){
                    stringBuilder.append(valueSeparator);
                }                                
                
                if(c != 0 && (c % DUMP_COLUMN_PER_ROW) == 0){                                        
                    stringBuilder.append("        ");
                    stringBuilder.append(charDataBuilder);                    
                    stringBuilder.append('\n');
                    
                    charDataBuilder = new StringBuilder(DUMP_COLUMN_PER_ROW);
                    c = 0;
                }
                
                if(c == 0){
                    stringBuilder.append(String.format(rowNumberFormat, i));
                }
                                
                stringBuilder.append(String.format(valueFormat, data[i]));
                if(Character.isLetterOrDigit((char)data[i])){
                    charDataBuilder.append((char)data[i]);
                }else{
                    charDataBuilder.append('.');
                }
                
                if(i + 1 >= data.length){
                    int charDataPadding = DUMP_COLUMN_PER_ROW - c;
                    
                    if(charDataPadding > 0){
                        for(int p = 0 ; p < charDataPadding ; p++){
                            stringBuilder.append("   ");
                        }
                    }
                    stringBuilder.append("        ");
                    stringBuilder.append(charDataBuilder);
                }
            }
            return stringBuilder.toString();
        }
        return "";
    }
    
}
