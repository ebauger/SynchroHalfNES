package com.grapeshot.halfnes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Closeable {

    private final int port = 1337;
    private final String hostName = "127.0.0.1";
    private Socket socket = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;
    private ServerSocket server = null;

    public void startServer() {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(2);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(port);
                    System.out.println("Waiting for clients to connect...");
                    while (true) {
                        socket = server.accept();
                        clientProcessingPool.submit(new ClientTask(socket));
                    }
                } catch (IOException e) {
                    System.err.println("Unable to process client request");
                    e.printStackTrace();
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();

    }

    private class ClientTask implements Runnable {

        private final Socket clientSocket;

        private ClientTask(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            System.out.println("Got a client !");

            receive();
            //loadROM()

            // Do whatever required to process the client's request

            /*
             try {
             clientSocket.close();
             } catch (IOException e) {
             e.printStackTrace();
             }
             */
        }
    }

    public void receive() {
        boolean done = false;
        try {
            //socket = server.accept();
            open();

            while (!done) {

                String line = is.readUTF();

                System.out.println(line);

                os.writeBytes(line + " du client");

                done = line.equals(".bye");

            }
        } catch (IOException ioe) {
            done = true;
        }
    }

    public void open() throws IOException {
        is = new DataInputStream(new BufferedInputStream(
                socket.getInputStream()));
        os = new DataOutputStream(socket.getOutputStream());
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (is != null) {
                is.close();
            }
            if (server != null) {
                server.close();
            }
        } catch (IOException ioe) {

        }
    }

}
