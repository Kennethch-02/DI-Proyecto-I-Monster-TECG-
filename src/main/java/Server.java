import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private int port;
    private ServerSocket Server;
    private Socket Client;
    private boolean ServerActive;
    private DataOutputStream OUT;
    private DataInputStream IN;
    private String InMessage;
    public Server(){
        this.port = 1000;
        this.Server = null;
        this.Client = null;
        this.ServerActive = true;
    }

    public void setServerActive(boolean active){this.ServerActive = active;}
    public boolean isServerActive(){return this.ServerActive;}
    public Socket getClient(){return this.Client;}
    public ServerSocket getServer(){return this.Server;}
    public int getPort(){return this.port;}
    public void setPort(int port){this.port = port;}

    public void increasePort(){
        int port = getPort();
        setPort(port++);
    }
    public void SendMessage(String OutMessage){
        try{
            OUT = new DataOutputStream(this.Client.getOutputStream());
            OUT.writeUTF(OutMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void run() {
        while(this.Server == null){
            try{
                this.Server = new ServerSocket(getPort());
                System.out.println("Server connected to port " + getPort());
            } catch (Exception e) {
                System.out.println(e);
                increasePort();
            }
        }
        while (this.Client == null){
            System.out.println("Waiting for a connection");
            try{
                this.Client = this.Server.accept();
                System.out.println("Client " + this.Client + "connected");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        System.out.println("executing instructions");
        while (isServerActive()){
            try{
                IN = new DataInputStream(this.Client.getInputStream());
                this.InMessage = IN.readUTF();
                System.out.println("Message received by the server: " + this.InMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
