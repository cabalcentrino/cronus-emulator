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

package br.com.joaodarcy.cronus.worldsvr;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class DeveloperConsole extends javax.swing.JFrame {

    private final static int DUMP_COLUMN_PER_ROW = 16;
            
    private class PacketEntry {
        private final File file;
        private final byte data[];        

        public PacketEntry(File file) throws IOException {
            this.file = file;
            this.data = loadData(file);
        }
                
        public void putStructData(StringBuilder sb){
            ByteBuffer dt = ByteBuffer.wrap(data);
            dt.order(ByteOrder.LITTLE_ENDIAN);
            
            short opcode;
            
            sb.append("\n\n");
            
            put(dt.getShort(), "Magic", sb);
            put(dt.getShort(), "Size", sb);
            
            opcode = dt.getShort();
            
            putOpcode(opcode, "Opcode", sb);
                                    
            switch(opcode){
                case 0x0099: putOpcodeInfo("WORLD_BAG_ITEM_LOOTING", sb); break;                
                case 0x00B0: putOpcodeInfo("WORLD_PLAYER_ATTACK_MOB", sb); break;
                case 0x00C8: putOpcodeInfo("WORLD_PLAYER_SEND_INFO", sb); break;
                case 0x00CA: putOpcodeInfo("WORLD_MOB_INFO", sb); break;
                case 0x00CC: putOpcodeInfo("WORLD_MOB_DROP", sb); break;
                //case 0x00D2: putOpcodeInfo("DICA?????", sb); break;
                case 0x00D2: putOpcodeInfo("WORLD_ANOTHER_PLAYER_MOVE", sb); break;
                case 0x00D3: putOpcodeInfo("WORLD_ANOTHER_PLAYER_MOVE_END", sb); break;
                case 0x00D4: putOpcodeInfo("WORLD_ANOTHER_PLAYER_MOVE_TECLA?", sb); break;
                case 0x00D5: putOpcodeInfo("WORLD_MOB_MOVE_BEGIN", sb); break;
                case 0x00D6: putOpcodeInfo("WORLD_MOB_MOVE_END", sb); break;
                case 0x00E1: putOpcodeInfo("WORLD_UNK", sb); break;                    
                case 0x0120: putOpcodeInfo("WORLD_PLAYER_LEVEL_UP_EFFECT", sb); break;
                case 0x0188: putOpcodeInfo("WORLD_ANOTHER_PLAYER_CHANGE_DIRECTION", sb); break;                
                case 0x018A: putOpcodeInfo("WORLD_GRITO", sb); break;

                case 0x0193: putOpcodeInfo("WORLD_ANOTHER_PLAYER_MOVE_FORWARD", sb); break;
                case 0x0196: putOpcodeInfo("WORLD_ANOTHER_PLAYER_MOVE_DIAGONAL", sb); break;                    
                    
                    
                // EP 3
                case 0x03EA: putOpcodeInfo("WORLD_EVENT_NOTIFY_PLAYER", sb); break;
                case 0x03D7: putOpcodeInfo("Código de Verificação do Cabal", sb); break;
                case 0x03D8: putOpcodeInfo("Remover código de verificação cabal", sb); break;
                case 0x03F0: putOpcodeInfo("Coloca o icone de evento ????", sb); break;
                    
                case 0x041E: putOpcodeInfo("Dica", sb); break;
                    
                // EP 8
                case 0x0893: putOpcodeInfo("WORLD_MEMORIAL_SWORD_INFOR_OWNER", sb); break;
                
            }
        }
                        
        private void put(short value, String name, StringBuilder sb){
            sb.append(
                String.format(
                    "%-32s : %04X     ( Signed: %-10d, Unsigned: %-10s )\n", 
                    name, 
                    value, 
                    value, 
                    UInt16.valueOf(value)
                )
            );
        }
        
        private void putOpcodeInfo(String info, StringBuilder sb){
            sb.append(info);
            sb.append('\n');
            sb.append('\n');
        }
        
        private void putOpcode(short value, String name,StringBuilder sb){
            sb.append(
                String.format(
                    "\n%-32s : %04X     ", 
                    name, 
                    value
                )
            );
        }
        
        public String getContent(){

            final char valueSeparator = ' ';
            final String rowNumberFormat = "%04X    ";
            final String valueFormat = "%02X";

            if(data.length > 0){
                StringBuilder stringBuilder = new StringBuilder((data.length * 3));
                StringBuilder charDataBuilder = new StringBuilder(DUMP_COLUMN_PER_ROW);

                //stringBuilder.append('\n');

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
                putStructData(stringBuilder);
                
                return stringBuilder.toString();
            }
            return "";    
        }
        
        private byte[] loadData(File file) throws IOException{
            FileInputStream inputStream = null;
            FileChannel channel = null;
            
            try{
                inputStream = new FileInputStream(file);
                channel = inputStream.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
                
                channel.read(buffer);
                
                return buffer.array();
            }finally{
                if(channel != null){
                    try{
                        channel.close();
                    }catch(Throwable t){
                        t.printStackTrace();
                    }
                }
                if(inputStream != null){
                    try{
                        inputStream.close();
                    }catch(Throwable t){
                        t.printStackTrace();
                    }
                }                
            }                        
        }
        
        @Override
        public String toString() {
            return file.getName();
        }               
    }
    
    public ComboBoxModel loadPackets(String directory){        
        File dir = new File(directory);
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        comboBoxModel.addElement("Nenhum");
        
        File[] fileArray = dir.listFiles();
        
        if(fileArray.length == 0){
            return comboBoxModel;
        }
        
        List<File> files = new ArrayList<>(fileArray.length);
        
        Collections.addAll(files, fileArray);
        Collections.sort(files);
        
        for(File file : files){            
            if(file.isFile() && file.getName().length() > 7){
                String tempFileName = file.getName().substring(6);
                //System.out.println(tempFileName);
                if(tempFileName.startsWith("WORLD") && tempFileName.endsWith("SERVER.dec")){
                    try{
                        comboBoxModel.addElement(new PacketEntry(file));                    
                    }catch(Throwable t){
                        t.printStackTrace();
                    }
                }
            }
        }
        
        return comboBoxModel;
    }
    
    private void loadPackets(){
        jCBPacketFile.setModel(loadPackets("./packets"));
    }
    
    /**
     * Creates new form DeveloperConsole
     */
    public DeveloperConsole() {
        this.setLocationRelativeTo(null);
        
        initComponents();        
        loadPackets();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jCBPacketFile = new javax.swing.JComboBox();
        jBTEnviar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTAPacketData = new javax.swing.JTextArea();
        jBTRecarregarPacotes = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTFIdxOld = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTFIdxNew = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMNEnviarPacote = new javax.swing.JMenu();
        jMIEnviar0065 = new javax.swing.JMenuItem();
        jMNServer = new javax.swing.JMenu();
        jMIStopServer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Developer Console");

        jLabel1.setText("Packet List");

        jCBPacketFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBPacketFileActionPerformed(evt);
            }
        });

        jBTEnviar.setText("Enviar");
        jBTEnviar.setEnabled(false);
        jBTEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTEnviarActionPerformed(evt);
            }
        });

        jTAPacketData.setEditable(false);
        jTAPacketData.setColumns(20);
        jTAPacketData.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTAPacketData.setRows(5);
        jScrollPane2.setViewportView(jTAPacketData);

        jBTRecarregarPacotes.setText("Recarregar Pacotes");
        jBTRecarregarPacotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTRecarregarPacotesActionPerformed(evt);
            }
        });

        jLabel2.setText("Replace CharacterIdx:");

        jTFIdxOld.setText("79DC6500");

        jLabel3.setText("-");

        jTFIdxNew.setText("08000000");

        jMNEnviarPacote.setText("Enviar Pacote");

        jMIEnviar0065.setText("Enviar 0x0065");
        jMIEnviar0065.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEnviar0065ActionPerformed(evt);
            }
        });
        jMNEnviarPacote.add(jMIEnviar0065);

        jMenuBar1.add(jMNEnviarPacote);

        jMNServer.setText("Server");

        jMIStopServer.setText("Stop Server");
        jMIStopServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIStopServerActionPerformed(evt);
            }
        });
        jMNServer.add(jMIStopServer);

        jMenuBar1.add(jMNServer);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
                    .addComponent(jCBPacketFile, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jBTRecarregarPacotes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFIdxOld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFIdxNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBTEnviar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBPacketFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBTEnviar)
                    .addComponent(jBTRecarregarPacotes)
                    .addComponent(jLabel2)
                    .addComponent(jTFIdxOld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTFIdxNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCBPacketFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBPacketFileActionPerformed
        if(jCBPacketFile.getSelectedIndex() == 0){
            jTAPacketData.setText("");
            jBTEnviar.setEnabled(false);
        }else{
            jTAPacketData.setText(((PacketEntry)jCBPacketFile.getSelectedItem()).getContent());
            jBTEnviar.setEnabled(true);
        }
    }//GEN-LAST:event_jCBPacketFileActionPerformed

    private byte[] getByteArrayFromString(String value){
        if(value.length() == 8){
            try{
                Long longValue = Long.valueOf(value, 16);                
                return UInt32.valueOf(longValue).getBytesBE();                  
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
        return null;
    }
    
    private byte[] getOldCharacterIdx(){
        return getByteArrayFromString(jTFIdxOld.getText());
    }
    
    private byte[] getNewCharacterIdx(){
        return getByteArrayFromString(jTFIdxNew.getText());
    }
    
    private byte[] preparePacket(){                
        byte rawData[] = ((PacketEntry)jCBPacketFile.getSelectedItem()).data;
        byte oldCharacterIdx[] = getOldCharacterIdx();
        
        if(oldCharacterIdx != null){        
            byte preparedData[] = new byte[rawData.length];
            byte newCharacterIdx[] = getNewCharacterIdx();

            System.arraycopy(rawData, 0, preparedData, 0, preparedData.length);

            int match = 0, startMatch = 0;
            
            for(int i = 0 ; i < rawData.length ; i++){
                if(preparedData[i] == oldCharacterIdx[match]){
                    if(match == 0){
                        startMatch = i;
                    }
                    ++match;
                }
                if(match == oldCharacterIdx.length){
                    System.out.println("Replacing old character idx in " + startMatch + " with new one.");                                       
                    System.arraycopy(newCharacterIdx, 0, preparedData, startMatch, newCharacterIdx.length);
                    match = 0;
                    startMatch = 0;
                }
            }
            return preparedData;
        }
        return rawData;
    }
    
    
    private void jBTEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTEnviarActionPerformed
        ServerNode.sendToAll(preparePacket());
    }//GEN-LAST:event_jBTEnviarActionPerformed
        
    private String ask(String message){
        return JOptionPane.showInputDialog(this, message);
    }
    
    private short askShort(String message, short defaultValue){
        String value = ask(message);
        if(value == null || value.isEmpty()){
            return defaultValue;
        }
        try{
            return Short.valueOf(value);
        }catch(Throwable t){
            
        }
        return defaultValue;
    }    
    
    private void jMIEnviar0065ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEnviar0065ActionPerformed
        short creatureId = askShort("Informe o código do monstro", (short)0x0028);        
        ServerNode.send00CA(creatureId);
    }//GEN-LAST:event_jMIEnviar0065ActionPerformed

    private void jMIStopServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIStopServerActionPerformed
        ServerNode.requestStop();
    }//GEN-LAST:event_jMIStopServerActionPerformed

    private void jBTRecarregarPacotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTRecarregarPacotesActionPerformed
        this.setEnabled(false);
        
        int selectedIndex = jCBPacketFile.getSelectedIndex();
        Object selectedItem = jCBPacketFile.getSelectedItem();
        String stringItem = null;
                
        if(!(selectedItem instanceof String)){
            stringItem = selectedItem.toString();
        }else{
            if(selectedItem != null){
                stringItem = (String)selectedItem;
            }
        }
        
        loadPackets();
        
        // Reselect old packet
        if(stringItem != null){                                            
            boolean found = false;
            
            ComboBoxModel packetModel = jCBPacketFile.getModel();
            if(selectedIndex < packetModel.getSize()){
                if(packetModel.getElementAt(selectedIndex).toString().equals(stringItem)){
                    jCBPacketFile.setSelectedIndex(selectedIndex);
                    found = true;
                }
            }
            
            if(!found){
                for(int i = 0 ; i < packetModel.getSize() ; i++){
                    if(packetModel.getElementAt(i).toString().equals(stringItem)){
                        jCBPacketFile.setSelectedIndex(i);
                        break;
                    }
                }    
            }
        }
        
        //JOptionPane.showMessageDialog(this, "Pacotes recarregados com sucesso !");
        this.setEnabled(true);
    }//GEN-LAST:event_jBTRecarregarPacotesActionPerformed

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
                
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DeveloperConsole().setVisible(true);
            }
        });
    }*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBTEnviar;
    private javax.swing.JButton jBTRecarregarPacotes;
    private javax.swing.JComboBox jCBPacketFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuItem jMIEnviar0065;
    private javax.swing.JMenuItem jMIStopServer;
    private javax.swing.JMenu jMNEnviarPacote;
    private javax.swing.JMenu jMNServer;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTAPacketData;
    private javax.swing.JTextField jTFIdxNew;
    private javax.swing.JTextField jTFIdxOld;
    // End of variables declaration//GEN-END:variables
}
