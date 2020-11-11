import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.w3c.dom.Text;

public class TheGameOfMorra extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	int p1p;
	int p2p;
	int p1g;
	int p2g;

	//	BorderPane pane;
	VBox  vb, vb_2, vb_3; //vb_2 - scene 2 vbox

	HBox h1,h2,h3,h4; //scene 2 4 hboxes
	TextField L1,L2;
	Button guess, pick, playAgain, quit;
	BorderPane pane;

	Image zero, one, two, three, four, five;
	ImageView p0, p1, p2, p3, p4, p5;
	double picHeight, picWidth;

	TextField ip, portnum, messageBox; //User enters the ip and portnum here
	Button b1,b2,b3; // to establish connection to sv - call start method (calls the run)
	Button sendInfo;
	HashMap<String, Scene> sceneMap;
	Client clientConnection;
	ListView<String> listItems, listItems2;
	ObservableList<String> storeQueueItemsInListView;
	Scene scene1;
	String ipInfo;
	int portInfo;

	MorraInfo Game = new MorraInfo();

	//Initial Scene - Client enters ip & port
	public Scene initScene1(){
		TextField messagebox1 = new TextField("only port 1234 & IP: 127.0.0.1 are available ");

		messageBox = new TextField("Provide IP, port number below, then click IP & Port to enter the values & click connect to establish connection to server ");
		//messageBox.setPadding(new Insets(10));

		ip = new TextField();
		b1 = new Button("Enter IP");

		portnum = new TextField();
		//portnum.setPadding(new Insets(10));
		b2 = new Button("Enter Port Num");

		//		vb_1 = new VBox(ip, b1);
		//		vb_2 = new VBox(portnum, b2);

		b3 = new Button("Connect"); //will establish connection to sv

		vb = new VBox(messageBox, messagebox1,ip, b1, portnum, b2, b3);  //vb_1, vb_2);

		vb.setSpacing(10.0);
		vb.setPadding(new Insets(10));

		return new Scene(vb, 400, 275);
	}

	public Scene initScene2() {

					pane = new BorderPane();

					L1.setMaxWidth(100.0);

					L2.setMaxWidth(100.0);
					//L2.setPadding(new Insets(0,0,0,20));

					h1 = new HBox(L1,guess,L2); h1.setSpacing(20.0);
					h1.setPadding(new Insets(50,0,50,200));
					picHeight = 120.0;
					picWidth = 120.0;

					h2 = new HBox(p0, p1, p2, p3, p4, p5);
					h2.setSpacing(10.0);
					h2.setPadding(new Insets(0,0,50,50));

					playAgain = new Button("Play Again");
					quit = new Button("Quit");
					h3 = new HBox(playAgain, quit, sendInfo); h3.setSpacing(50.0);
					h3.setPadding(new Insets(0,0,50, 275));

					vb_2 = new VBox(h1, h2, h3);
					vb_2.setStyle("-fx-background-color: coral");
					pane.setTop(vb_2);


					vb_3 = new VBox();
					listItems2 = new ListView<String>();
					listItems = new ListView<String>();
					storeQueueItemsInListView = FXCollections.observableArrayList();
					storeQueueItemsInListView.add("Client");
					listItems.setItems(storeQueueItemsInListView);

					pane.setBottom(listItems);

					return new Scene(pane, 800, 800);
				}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Client) Let's Play Morra!!!");

		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("Establish Connection", initScene1());

		String ipTemp;
		int portTemp;
//		Game = new MorraInfo();
		listItems2 = new ListView<String>();
		sendInfo = new Button("SendInfo");
		Scene scene = initScene1(); //starts initial scene
		guess = new Button("Total Guess?");
		L1 = new TextField();
		L2 = new TextField("Your Pick: ");
		zero 	= new Image( "zero.jpg"  ); p0 = new ImageView(zero);
		p0.setFitHeight(picHeight); p0.setFitWidth(picWidth); p0.setPreserveRatio(true);

		one 	= new Image( "one.jpg"   );	p1 = new ImageView(one);
		p1.setFitHeight(picHeight); p1.setFitWidth(picWidth); p1.setPreserveRatio(true);

		two 	= new Image( "two.jpg"	); p2 = new ImageView(two);
		p2.setFitHeight(picHeight); p2.setFitWidth(picWidth); p2.setPreserveRatio(true);

		three = new Image( "three.jpg" ); p3 = new ImageView(three);
		p3.setFitHeight(picHeight); p3.setFitWidth(picWidth); p3.setPreserveRatio(true);

		four  = new Image( "four.jpg"  ); p4 = new ImageView(four);
		p4.setFitHeight(picHeight); p4.setFitWidth(picWidth); p4.setPreserveRatio(true);

		five  = new Image( "five.jpg"  ); p5 = new ImageView(five);
		p5.setFitHeight(picHeight); p5.setFitWidth(picWidth); p5.setPreserveRatio(true);

		b1.setOnAction(e -> { ipInfo = ip.getText(); });  //works
		b2.setOnAction(e -> { portInfo = Integer.parseInt( portnum.getText() ); }); //works

//		p0.setOnMouseClicked(e->{
//			System.out.println("check");
//		});

		p0.setOnMouseClicked(e -> {
			L2.clear(); L2.appendText("Your Pick: 0");
//			if(clientConnection.playerNum == 1){ Game.p1Plays = "0"; }
//			else { Game.p2Plays = "0"; }
			p1p = 0;
		});

		p1.setOnMouseClicked(e -> {
			L2.clear(); L2.appendText("Your Pick: 1");
			p1p = 1;
		});

		p2.setOnMouseClicked(e -> {
			L2.clear(); L2.appendText("Your Pick: 2");
//			if(clientConnection.playerNum == 1) { Game.p1Plays = "2"; }
//			else { Game.p2Plays = "2"; }
			p1p = 2;
		});

		p3.setOnMouseClicked(e -> {
			L2.clear(); L2.appendText("Your Pick: 3");
//			if(clientConnection.playerNum == 1) { Game.p1Plays = "3"; }
//			else { Game.p2Plays = "3"; }
			p1p = 3;
		});

		p4.setOnMouseClicked(e -> {
			L2.clear(); L2.appendText("Your Pick: 4");
//			if(clientConnection.playerNum == 1) { Game.p1Plays = "4"; }
//			else { Game.p2Plays = "4"; }
			p1p = 4;
		});

		p5.setOnMouseClicked(e -> {
			L2.clear(); L2.appendText("Your Pick: 5");
//			if(clientConnection.playerNum == 1) { Game.p1Plays = "5"; }
//			else { Game.p2Plays = "5"; }
			p1p = 5;
		});

		guess.setOnAction(e -> {
			System.out.println(L1.getText()); //debugging
//			if(clientConnection.playerNum == 1) { Game.p1Guess = L1.getText(); }
//			else { Game.p2Guess = L1.getText(); }
			p1g = Integer.parseInt(L1.getText());
			L1.clear();
		});

		//Connect Button,
				/*
				Checks if the ip and port entered are valid then goes ahead to connect
				If either of them is not valid they'll have to re enter both
				* */
		b3.setOnAction(e -> {
//			portInfo = 1234;
//			ipInfo = "127.0.0.1";
			//both correct then establish connection
			if (  Objects.equals(ipInfo, new String("127.0.0.1"))  && (portInfo == 1234)  )
			{
//				try {
//					clientConnection.sleep(1000);
//				} catch (InterruptedException ex) {
//					ex.printStackTrace();
//				}
				clientConnection = new Client(data-> {

					try {
						Client.sleep(1500);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}

					Platform.runLater( ()-> {
//						if(data.getClass().getName() == "MorraInfo"){
//							Game = (MorraInfo) data;
////							System.out.println("p1Point: " + Game.p1Points);
//							System.out.println("Received object from the server");
//							primaryStage.setScene(initScene2());
//
////							} //todo: create new button to send info for round. Add the above if statement in event handler.
//
//						}else if(data.getClass().getName() == "java.lang.String"){
//							System.out.println("Received string from the server");
//							listItems2.getItems().add(data.toString());
//						}else if(data.getClass().getName() == "java.lang.Integer"){
//							System.out.println("Received Integer from server");
//							primaryStage.setTitle("This is Player: "+ clientConnection.playerNum);
//						}else{
//							System.out.println("error in sending object from server");
//						}
						if(data.toString() == "ChangeScene"){
							primaryStage.setScene(initScene2());
						}else{
							listItems.getItems().add(data.toString());
							System.out.println(data.toString());
						}


					});
				}, ipInfo, portInfo );
				clientConnection.start();



//									System.out.println("check1");
//									clientConnection.send2(Game);
//									System.out.println("check2");

				if(Game.have2Players) //To check if both player are here the first time.
				{
					System.out.println("2 players are here.");
					messageBox.setText("2 players are here.");

					//		TODO: Display text - Connection established. Waiting for player two
					//		 Send this from server side, and then call scene when the connection is established ?
					//TODO Call Scene 2

				}
				else
				{
					messageBox.clear();
					messageBox.setText("Need a minimum of 2 players to play. Waiting...");
				}

			}

			else {
				if ( ! Objects.equals(ipInfo, new String("127.0.0.1"))  && (portInfo != 1234)  )
				{ messageBox.setText("Enter a Valid ip and port number");  ip.clear(); portnum.clear(); }

				else if ( !(Objects.equals(ipInfo, new String("127.0.0.1")))  )
				{ messageBox.setText("Enter a Valid ip"); ip.clear(); }

				else if ( !(portInfo == 1234) )  { messageBox.setText("Enter a Valid port number"); portnum.clear(); }

			}

		});

		sendInfo.setOnAction(e->{
//			System.out.println("sendinfo: " + Game.p1Plays + " " + Game.p2Plays + " " + Game.p1Guess + " " + Game.p2Guess);
			System.out.println(p1p + ", " + p1g);
//			if(!Game.p1Plays.isEmpty() && !Game.p2Plays.isEmpty() && !Game.p1Guess.isEmpty() && !Game.p2Guess.isEmpty()){
				clientConnection.send2(p1p, p1g);
//			}
		});

		primaryStage.setScene(scene);
//		primaryStage.setScene(initScene2());
		primaryStage.show();
	}
}
