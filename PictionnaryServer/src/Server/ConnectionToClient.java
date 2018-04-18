// This file contains material supporting section 3.8 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com
package Server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * An instance of this class is created by the server when a client connects. It
 * accepts messages coming from the client and is responsible for sending data
 * to the client since the socket is private to this class. The AbstractServer
 * contains a set of instances of this class and is responsible for adding and
 * deleting them.<p>
 *
 * Several public service methods are provided to applications that use this
 * framework, and several hook methods are also available<p>
 *
 * Project Name: OCSF (Object Client-Server Framework)
 * <p>
 *
 * @author Dr Robert Lagani&egrave;re
 * @author Dr Timothy C. Lethbridge
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version December 2003 (2.31)
 */
public class ConnectionToClient extends Thread {

    /**
     * A reference to the Server that created this instance.
     */
    private AbstractServer server;

    /**
     * Sockets are used in the operating system as channels of communication
     * between two processes.
     *
     * @see java.net.Socket
     */
    private Socket clientSocket;

    /**
     * Stream used to read from the client.
     */
    private ObjectInputStream input;

    /**
     * Stream used to write to the client.
     */
    private ObjectOutputStream output;

    /**
     * Indicates if the thread is ready to stop. Set to true when closing of the
     * connection is initiated.
     */
    private boolean readyToStop;

    /**
     * Map to save information about the client such as its login ID. The
     * initial size of the map is small since it is not expected that concrete
     * servers will want to store many different types of information about each
     * client. Used by the setInfo and getInfo methods.
     */
    private HashMap<String, Object> savedInfo = new HashMap<>(10);

    /**
     * Constructs a new connection to a client.
     *
     * @param clientSocket contains the client's socket.
     * @param server a reference to the server that created this instance
     * @exception IOException if an I/O error occur when creating the
     * connection.
     */
    protected ConnectionToClient(Socket clientSocket,
            AbstractServer server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;

        clientSocket.setSoTimeout(0); // make sure timeout is infinite

        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            try {
                closeAll();
            } catch (Exception exc) {
            }
            throw ex;
        }

        readyToStop = false;
        start(); // Start the thread waits for data from the socket
    }

    /**
     * Closes all connection to the server.
     *
     * @exception IOException if an I/O error occur when closing the connection.
     */
    private void closeAll() throws IOException {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
        } finally {
            output = null;
            input = null;
            clientSocket = null;
        }
    }

    /**
     * Returns the address of the client.
     *
     * @return the client's Internet address.
     */
    final public InetAddress getInetAddress() {
        return clientSocket == null ? null : clientSocket.getInetAddress();
    }

    /**
     * Saves arbitrary information about this client. Designed to be used by
     * concrete subclasses of AbstractServer. Based on a hash map.
     *
     * @param infoType identifies the type of information
     * @param info the information itself.
     */
    public void setInfo(String infoType, Object info) {
        savedInfo.put(infoType, info);
    }

    /**
     * Returns information about the client saved using setInfo. Based on a hash
     * map.
     *
     * @param infoType identifies the type of information
     * @return
     */
    public Object getInfo(String infoType) {
        return savedInfo.get(infoType);
    }

    /**
     * Closes the client. If the connection is already closed, this call has no
     * effect.
     *
     * @exception IOException if an error occurs when closing the socket.
     */
    final public void close() throws IOException {
        readyToStop = true; // Set the flag that tells the thread to stop
        closeAll();
    }

    /**
     * Hook method called each time a new message is received by this client. If
     * this method return true, then the method
     * <code>handleMessageFromClient()</code> of <code>AbstractServer</code>
     * will also be called after. The default implementation simply returns
     * true.
     *
     * @param message the message sent.
     * @return
     */
    protected boolean handleMessageFromClient(Object message) {
        return true;
    }

    /**
     * Sends an object to the client. This method can be overriden, but if so it
     * should still perform the general function of sending to client, by
     * calling the <code>super.sendToClient()</code> method perhaps after some
     * kind of filtering is done.
     *
     * @param msg the message to be sent.
     * @exception IOException if an I/O error occur when sending the message.
     */
    public void sendToClient(Object msg) throws IOException {
        if (clientSocket == null || output == null) {
            throw new SocketException("socket does not exist");
        }
        output.reset();
        output.writeObject(msg);
    }

    /**
     * Return true if the client is connected.
     *
     * @return true if the client is connected.
     */
    final public boolean isConnected() {
        return clientSocket != null && output != null;
    }

    /**
     * Constantly reads the client's input stream. Sends all objects that are
     * read to the server. Not to be called.
     */
    @Override
    final public void run() {
        server.clientConnected(this);
        // This loop reads the input stream and responds to messages
        // from clients
        try {
            // The message from the client
            Object msg;

            while (!readyToStop) {
                // This block waits until it reads a message from the client
                // and then sends it for handling by the server
                try {
                    // wait to receive an object
                    msg = input.readObject();

                    if (!readyToStop && handleMessageFromClient(msg)) {
                        server.receiveMessageFromClient(msg, this);
                    }
                } catch (ClassNotFoundException | RuntimeException ex) { // when an unknown class is received
                    // or thrown by handleMessageFromClient or receiveMessageFromClient
                    server.clientException(this, ex);
                }
            }
        } catch (Exception exception) {
            if (!readyToStop) {
                try {
                    closeAll();
                } catch (Exception ex) {
                }
                server.clientException(this, exception);
            }
        } finally {
            server.clientDisconnected(this);
        }
    }

    /**
     * Returns a string representation of the client.
     *
     * @return the client's description.
     */
    @Override
    public String toString() {
        return clientSocket == null ? null
                : clientSocket.getInetAddress().getHostName()
                + " (" + clientSocket.getInetAddress().getHostAddress() + ")";
    }

    /**
     * This method is called by garbage collection.
     */
    @Override
    protected void finalize() {
        try {
            closeAll();
        } catch (IOException e) {
        }
    }
}
