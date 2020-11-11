import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;


//to be implemented:
// Class ClientThread extends Thread
// Class TheServer extends Thread
//

public class Server {

//    private static int count;
    int count = 1;
    public int portNum;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;
    MorraInfo obj = new MorraInfo();;
    Server(Consumer<Serializable> call , int portNumber){

        callback = call;
        server = new TheServer(); //provides run() for server.
        portNum = portNumber;
        server.start();// calls run() for server.
    }

    public int serverLogic(MorraInfo Game ) {
        if ((Game.p1Guess) == (Game.p1Plays) + (Game.p2Plays)) {
            Game.p1Wins = true;
        }
        if ((Game.p2Guess) == (Game.p1Plays) + (Game.p2Plays)) {
            Game.p2Wins = true;
        }
        if (Game.p1Wins && Game.p2Wins) {
            System.out.println("Both Won, no points awarded.");
            return 0;
            //no points are awarded
        } else if (Game.p1Wins) {
            System.out.println("P1 Won!");
            Game.p1Points++;
            return 1;
        } else if (Game.p2Wins) {
            System.out.println("P2 Won!");
            Game.p2Points++;
            return 2;
        } else {

            System.out.println("Both Lost, no points awarded.");
            //both lose
            // no points awarded
            return 3;
        }
    }

    public class TheServer extends Thread{

        //extending thread means writing run function
        public void run(){

            try (ServerSocket mysocket = new ServerSocket(portNum);){
                //trying the above statement results in a server socket being created.
                System.out.println("server is waiting for a client");
                System.out.println(" with portNum = " + portNum + " count: " + count);

                while(clients.size()<=2 ){

                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    //means new client has connected
                    callback.accept("client has connected to server: " + "client #" + count);
                    clients.add(c);
                    c.start(); // calls run() in ClientThread
                    count++;
                    if(clients.size() == 2){
                        System.out.println("2 clients connected");
                    }

                }

            }catch (Exception e){
                callback.accept("Server socket did not launch");
            }

        }

    }

    class ClientThread extends Thread{

        Socket connection;
        int count2;
        ObjectInputStream in;
        ObjectOutputStream out;

        //Pass in an a Socket of server to indicate what this client is connecting to
        // and count value to indicate which client this is.
        ClientThread(Socket s, int count){
            this.connection = s;
            this.count2 = count;
        }

        //WRITING TO ALL CLIENTS
        //Write a message to all clients, message is taken as a parameter,
        // might have to change the parameter to something else that is
        // serializable
        public void updateClients(String message){
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    t.out.writeObject(message); // to send info back to client.
                }
                catch(Exception e) {}
            }
        }

        public void updateClients2(Serializable Game){
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    t.out.writeObject(Game); // to send info back to client.
                }
                catch(Exception e){}
            }
        }

        public void updateOneClient(Serializable Game, ClientThread c){
            try {
                c.out.writeObject(Game); // to send info back to client.
            }
            catch(Exception e) {}
        }

        public void run(){
            try{
                //Below, "in" is client input stream. Give it input stream of server.
                //Below, "out" is client output stream. Give it output stream of server.
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }catch(Exception e){
                System.out.println("Streams not open");
            }

            updateClients("new client on server: client #" + count);

            while(true){

                try{

                    // we recieve the object here. and we printout who sent it.
                    //t hen we call callback to send it to gui.

                    Serializable obj3 = (MorraInfo) in.readObject();
                    if(obj3.getClass().getName() == "MorraInfo"){
                        MorraInfo obj2 = (MorraInfo) obj3;
                        System.out.println("obj reached server");
                        System.out.println("obj3: " + obj2.p1Plays + ", " + obj2.p1Guess);
                    }else{
                        System.out.println("else");
                    }

                    MorraInfo obj2 = (MorraInfo) obj3;
                    if(clients.size() == 2){
                        if(this.count2 == 1){
                            callback.accept("Player 1 sent something.");
                            obj.p1Plays = obj2.p1Plays;
                            obj.p1Guess = obj2.p1Guess;
                            System.out.println( "P1 sent:  " + obj.p1Plays + " " + obj.p1Guess + " " + obj.p2Plays + " " + obj.p2Guess);
                        }
                        if(this.count2 == 2){
                            callback.accept("Player 2 sent something.");
                            obj.p2Plays = obj2.p1Plays;
                            obj.p2Guess = obj2.p1Guess;
                            System.out.println( "P2 sent:  " + obj.p1Plays + " " + obj.p1Guess + " " + obj.p2Plays + " " + obj.p2Guess);
                        }

                        if(obj.p1Plays!= -1 && obj.p1Guess!= -1 && obj.p2Plays!= -1 && obj.p2Guess!= -1 ){

                            int res = serverLogic(obj);

                            if(res==0){
                                callback.accept("Both Won, no points awarded." +  "\n Message from server: \n" +
                                "\n P1 Play-Guess: " + obj.p1Plays + " - " + obj.p1Guess +
                                        "\n P2 Play-Guess: " + obj.p2Plays + " - " + obj.p2Guess +
                                        "\n P1Points: " + obj.p1Points + ", P2Points: " + obj.p2Points);
                            }else if(res==1){
                                callback.accept("P1 Won: Points: " +  "\n Message from server: \n" +
                                        "\n P1 Play-Guess: " + obj.p1Plays + " - " + obj.p1Guess +
                                        "\n P2 Play-Guess: " + obj.p2Plays + " - " + obj.p2Guess +
                                        "\n P1Points: " + obj.p1Points + ", P2Points: " + obj.p2Points);
                            }else if(res==2){
                                callback.accept("P2 Won: Points: " +  "\n Message from server: \n" +
                                        "\n P1 Play-Guess: " + obj.p1Plays + " - " + obj.p1Guess +
                                        "\n P2 Play-Guess: " + obj.p2Plays + " - " + obj.p2Guess +
                                        "\n P1Points: " + obj.p1Points + ", P2Points: " + obj.p2Points);
                            }else if(res==3){
                                callback.accept("Both Lost" +  "\n Message from server: \n" +
                                        "\n P1 Play-Guess: " + obj.p1Plays + " - " + obj.p1Guess +
                                        "\n P2 Play-Guess: " + obj.p2Plays + " - " + obj.p2Guess +
                                        "\n P1Points: " + obj.p1Points + ", P2Points: " + obj.p2Points);
                            }else{
                                System.out.println("something went wrong");
                            }
                            updateClients(" Message from server: \n" +
                                    "\n P1 Play-Guess: " + obj.p1Plays + " - " + obj.p1Guess +
                                    "\n P2 Play-Guess: " + obj.p2Plays + " - " + obj.p2Guess +
                                    "\n P1Points: " + obj.p1Points + ", P2Points: " + obj.p2Points);
                            obj.p1Plays = -1;
                            obj.p2Plays = -1;
                            obj.p1Guess = -1;
                            obj.p2Guess = -1;
                            if(obj.p1Points == obj.totalRounds){
                                obj.p1Wins = true;
                                callback.accept("Player 1 won the whole game!");
                                callback.accept("Disconnecting both clients. Please reconnect to the server");
                                clients.remove(clients.get(0));
                                count = 1;
                                clients.remove(clients.get(1));
                                count = 2;
                            }
                            if (obj.p2Points == obj.totalRounds){
                                obj.p2Wins = true;
                                callback.accept("Player 2 won the whole game!");
                            }

                        }


                    }

//                    if(obj.getClass().getName() == "MorraInfo"){
//                        System.out.println("client: " + count + " sent MorraInfo.");
//                        callback.accept(obj);
//                    }else{
//                        System.out.println("client: " + count + " message.");
//                        callback.accept(obj.toString());
//                    }

                    //What should client do??????
//                    String data = in.readObject().toString(); //receives data from client's send function
//                    callback.accept("client: " + count + " sent: " + data);
//                    updateClients("client #"+count+" said: "+ data);

               }catch (Exception e) {
//                  //WHAT HAPPENS WHEN CLIENT DISCONNECTS/CAN'T CONNECT
//
//                    //callback.accept tells server what has happenned.
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    updateClients("Client #" + count + " has left the server!"); //Inform other clients.

                    if (this.count2 == 1) {
                        clients.remove(this); //Remove //Clients
                        count = 1;
                    }
                    if (this.count2 == 2){
                        clients.remove(this); //Remove //Clients
                    count = 2;
                }
                    break; //Not sure about this.
               }

            }
        }


//        public void updateOneClients(int i, ClientThread clientThread) {
//        }



    }


}
