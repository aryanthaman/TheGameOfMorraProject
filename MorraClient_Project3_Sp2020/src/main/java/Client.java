import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{


	Socket socketClient;

	ObjectOutputStream out;
	ObjectInputStream in;

	String ip;
	int portnum;
	int playerNum;
	MorraInfo obj;
	MorraInfo obj2;
	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call, String ip, int port){

		callback = call;
		this.ip = ip;
		this.portnum = port;
	}

	public void run() {

		try {
			//socketClient = new Socket("127.0.0.1",1234);
			socketClient = new Socket(ip, portnum);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {

			//    System.out.println("Sv not found");// Debugging?
		}

		while(true) {

			try {
//				Serializable obj = (Serializable) in.readObject(); //receives object from user.
//				if(obj.getClass().getName() == "MorraInfo"){
//					System.out.println("obj is morrainfo");
//					callback.accept(obj);
//					MorraInfo obj2 = (MorraInfo) obj;
					//ADDED:todo //basically, when all the info is complete, send the object Game to the server.
//		But, here both p1 and p2 will return the object since this if condition will be simultaenouly be true for both players.
					// the good thing is, both these objects will be identical so it doesn't matter which one the server reads in.
//					while( !(obj2.p1Plays != "" && obj2.p2Plays != "" && obj2.p1Guess != "" && obj2.p2Guess != "") ){
//						//block
//					}
//					if((obj2.p1Plays != "" && obj2.p2Plays != "" && obj2.p1Guess != "" && obj2.p2Guess != "")){
//						System.out.println("Calculating Info for this round. Sending Values to server");
//						send2(obj);
//					}
			Serializable testobj = (Serializable) in.readObject();
			obj = (MorraInfo) testobj;
			if(obj.p1 && obj.p2){
				callback.accept("ChangeScene");
			}else if(obj.disOut){
//				callback.accept(" Message from server: \n" +
//						"\n P1 Play-Guess: " + obj.p1Plays + " - " + obj.p1Guess +
//						"\n P2 Play-Guess: " + obj.p2Plays + " - " + obj.p2Guess +
//						"\n P1Points: " + obj.p1Points + ", P2Points: " + obj.p2Points);
			}

				callback.accept(" Round Info: \n Message from server: \n" +
						"\n P1 Play-Guess: " + obj.p1Plays + " - " + obj.p1Guess +
						"\n P2 Play-Guess: " + obj.p2Plays + " - " + obj.p2Guess +
						"\n P1Points: " + obj.p1Points + ", P2Points: " + obj.p2Points);






//			}else if (obj.getClass().getName() == "java.lang.String"){
//					System.out.println("obj is string");
//					callback.accept(obj.toString());
//				}else if(obj.getClass().getName() == "java.lang.Integer"){ //not sure about this. a lot of things can go wrong with this.
//					//read int.
//					System.out.println("obj is int");
//					playerNum = (int) obj;
//					System.out.println("playerNum: " + playerNum);
//					callback.accept(obj);
//				}else{
//					System.out.println("else");
//					System.out.println("getSimpleName(): " + obj.getClass().getSimpleName());
//					System.out.println("getName(): " + obj.getClass().getName());
//				}


//				String message = in.readObject().toString();
//				callback.accept(message);
			}
			catch(Exception e) {
//				System.out.println("client not connected to server."); //goes to infinite loop\.....
			}
		}

	}

//	public void send(String data) {
//
//		try {
//			out.writeObject(data);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void send2(int d1, int d2) {

		obj2 = new MorraInfo();

		try {

			obj2.p1Plays = d1;
			obj2.p1Guess = d2;
			System.out.println("obj2: " + obj2.p1Plays + ", " + obj2.p1Guess);
			//2 extra lines here
			out.writeObject(obj2);


//			MorraInfo t = (MorraInfo) Game;
////			System.out.println("send2: "+ t.p1Plays + " " + t.p2Plays + " " + t.p1Guess + " " + t.p2Guess);
//			out.writeObject(Game);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
