/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grapeshot.halfnes.net;

import com.grapeshot.halfnes.NES;
import com.grapeshot.halfnes.ui.ControllerInterface;
import com.grapeshot.halfnes.ui.NetworkController;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author etienne
 */
public class LANManager implements Runnable {

    private boolean isHost;
    private String ipAddress;
    private ControllerInterface controller1, controller2;
    private NetworkController controllernet;
    private String ROMpath;

    private ServerSocket socketserver;
    private Socket socketclient;
    private final int PORT = 1337;

    private DataInputStream in;
    private DataOutputStream out;
    private NES nes;

    private static volatile LANManager instance = null;

    private LANManager() {
    }

    public final static LANManager getInstance() {
        if (LANManager.instance == null) {
            synchronized (LANManager.class) {
                if (LANManager.instance == null) {
                    LANManager.instance = new LANManager();
                }
            }
        }
        return LANManager.instance;
    }
    /*
     public void init(boolean isHost, String whiteIpAddress, NES nes) {
     this.isHost = isHost;
     this.nes = nes;
     this.controller1 = nes.controller1;
     this.controller2 = nes.controller2;
     this.initHost();

     }*/

    public void init(boolean isHost, String ipAddress, NES nes) {
        this.isHost = isHost;
        this.ipAddress = ipAddress;
        this.nes = nes;
        this.controller1 = nes.controller1;
        this.controller2 = nes.controller2;
        if (this.isHost) {
            this.initHost();
        } else {
            this.initClient();
        }
    }

    private void initHost() {

        this.nes.isLAN = true;
        this.nes.isHost = true;

        try {

            this.socketserver = new ServerSocket(PORT);
            System.out.println("Host IP : " + this.socketserver.getInetAddress().getHostAddress());
            this.socketclient = this.socketserver.accept();

            System.out.println("Client IP : " + this.socketclient.getInetAddress().toString());
            System.out.println("Allowed IP : " + ipAddress);
            String allowIP = "/" + ipAddress;
            if (this.socketclient.getInetAddress().toString().equals(allowIP)) {
                this.initStream();
                this.controllernet = new NetworkController();
                this.nes.controller1 = this.controller1;
                this.nes.controller2 = this.controllernet;
                //this.nes.setControllers(this.controller1, this.controllernet);
            } else {
                this.closeConnection();
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void controllerSendReceive() {

        if (!this.isHost) {
            int bytecontroller = 0;
            bytecontroller = this.nes.controller2.getLatchByte();
            try {
                bytecontroller = this.nes.controller2.getLatchByte();
                //System.out.println(bytecontroller);
                this.out.writeInt(bytecontroller); // send controller info
                this.out.flush();
                this.controllernet.setNetByte(this.in.readInt()); // read controller info and change state
                this.nes.controller1 = this.controllernet;
                //this.nes.controller2 = this.controller2;
                //this.nes.setControllers(controllernet, controller2); // reset controller
            } catch (IOException e) {
                //((GUIImpl)this.nes.gui).connectionStopped();
                System.out.println("Connection Lost");
                this.runEmulation(false);
                try {
                    this.out.close();
                } catch (IOException e1) {
                    System.out.println("Socket did not close");
                }
            }
        } else {
            int bytecontroller = 0;
            bytecontroller = this.nes.controller1.getLatchByte();
            try {
                bytecontroller = this.nes.controller1.getLatchByte();
                //System.out.println(bytecontroller);
                this.out.writeInt(bytecontroller); // send controller info
                this.out.flush();
                this.controllernet.setNetByte(this.in.readInt()); // read controller info and change state
                //this.nes.controller1 = this.controller1;
                this.nes.controller2 = this.controllernet;
                //this.nes.setControllers(controller1, controllernet); // reset controller
            } catch (IOException e) {
                System.out.println("Connection Lost");
                this.runEmulation(false);
                try {
                    this.out.close();
                } catch (IOException e1) {
                    System.out.println("Socket did not close");
                }
            }

        }

    }

    private void initClient() {

        this.nes.isLAN = true;
        this.nes.isHost = false;
        this.nes.runEmulation = false;
        try {
            socketclient = new Socket(ipAddress, PORT);
            System.out.println("Client IP : " + this.socketclient.getRemoteSocketAddress().toString());

            this.initStream();
            this.controllernet = new NetworkController();
            this.nes.controller1 = this.controllernet;
            this.nes.controller2 = this.controller2;
            //this.nes.setControllers(this.controllernet, this.controller2);
            //return true;
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            //return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //return false;
    }

    private void initStream() throws IOException {
        this.in = new DataInputStream(this.socketclient.getInputStream());
        this.out = new DataOutputStream(this.socketclient.getOutputStream());
    }

    public void runEmulation(boolean state) {
        this.nes.runEmulation = state;
    }

    @Override
    public void run() {
        boolean ready = false;
        try {
            while (!ready) {
            String reading = this.in.readUTF();
            if (reading.equals("ready")) {
                System.out.println("Ready in!");
                ready = true;
                this.runEmulation(true); // runEmulation on NES.run
            } else {
                //this.closeConnection();
                System.out.println("Did not receive ready closing");
            }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /*
     * Send "ready" state and launch a Thread for receive the same state in run() method
    
     */
    public void ready() {
        try {
            //LANManager lm = new LANManager(this.nes.in, this);
            //this.nes.loadROM(this.ROMpath);
            this.out.writeUTF("ready");
            System.out.println("Ready out!");
            new Thread(getInstance()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    public void closeConnection() {
        if (this.nes.isHost) {
            try {
                this.socketclient.close();
                this.socketserver.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //resert instance
            this.socketclient = null;
            this.socketserver = null;
            //this.initHost(); // retry connection for the server
        } else {
            try {
                this.socketclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //no more LAN mode
            this.nes.isLAN = false;
            this.socketclient = null;
        }
    }*/

    public void closeConnection() {
        try {
            if (this.nes.isHost) {
                this.socketclient.close();
                this.socketserver.close();
            } else {
                this.socketclient.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //resert instance
        this.socketclient = null;
        this.socketserver = null;
        this.nes.isLAN = false;
            //this.initHost(); // retry connection for the server

    }

    public void resetConnection() {
        this.closeConnection();
        this.initHost();
    }

    public void setPathROM(String ROMpath) {
        this.ROMpath = ROMpath;
    }

}
