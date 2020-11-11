import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.util.HashMap;
import java.util.Queue;

public class TheGameOfMorra extends Application {

//	boolean p1Wins, p2Wins;


	Button serverSwitch; //Turn server on/off
	Button startGame;
	TextField TF_portNum, messageBoxServer, messageBoxServer2;
	TextArea gameInfo;
//	ListView<String> listItems;
	HashMap<String, Scene> sceneMap;
	VBox scene1main;
	HBox scene2main;

	Scene scene, scene2;
	Server server;
	MorraInfo Game;

	ListView<String> queueList;
	ObservableList<String> storeQueueItemsInListView;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	public Scene initScene1(){
		messageBoxServer = new TextField("Please enter portNum for server below and turn on the server to play the game.");
		serverSwitch = new Button("Server On/Off");
		TF_portNum = new TextField();
		scene1main = new VBox(messageBoxServer, serverSwitch, TF_portNum);
		return new Scene(scene1main, 400, 400);
	}

	public Scene initScene2(){
		startGame = new Button("Start Game!");
		messageBoxServer2 = new TextField("test message...");
		gameInfo = new TextArea("Checking GameInfo textArea");
		queueList = new ListView<String>();
		storeQueueItemsInListView = FXCollections.observableArrayList();
		storeQueueItemsInListView.add("Check input from obv list");
		queueList.setItems(storeQueueItemsInListView);
		scene2main = new HBox(new VBox(gameInfo, queueList) , startGame);
		return new Scene(scene2main, 400, 400);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Server) Let's Play Morra!!!");

		Game = new MorraInfo();

		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("server1", initScene1());
		sceneMap.put("server2", initScene2());

		scene = initScene1();
		serverSwitch.setOnAction(e -> {

				if(TF_portNum.getText().isEmpty()){
					messageBoxServer.setText("Please Enter the portNum for server to run on.");
				}else {
				primaryStage.setScene(sceneMap.get("server2"));
				primaryStage.setTitle("Server running 2");
					server = new Server(data -> {
						Platform.runLater(() -> {

							queueList.getItems().add(data.toString());

//							if(data.getClass().getName() == "MorraInfo"){
////								System.out.println("received Morra obj");
//								Game = (MorraInfo) data;
//								System.out.println("Game: "+ Game.p1Plays + " "  + Game.p1Guess + " " + Game.p2Plays + " " + Game.p2Guess);
////								Game = new MorraInfo();
////								Game.have2Players = true;
//								if(server.clients.size() == 2){
//
//									Game.have2Players = true;
//
//									server.clients.get(0).updateClients2(Game);
//								}else{
//									//do something //todo
//								}
////								display winner. // todo
//							}else{
//								//incase we recieve a string we simply output that to the server listview.
//								System.out.println("received other obj");
//								queueList.getItems().add(data.toString()); //Why??
//							}

						});
					}, Integer.parseInt(TF_portNum.getText()));
				}
			});

		startGame.setOnAction(e->{
			if(server.clients.size() == 2){
				Game.have2Players = true;
				Game.p1 = true;
				Game.p2 = true;
//				server.clients.get(0).updateOneClient(1, server.clients.get(0));
//				server.clients.get(0).updateOneClient(2, server.clients.get(1));
				server.clients.get(0).updateClients2(Game);
			}else{
				//do something //todo
			}
		});
//		B_portNum.setOnAction(e -> {
//			portNum = Integer.parseInt(TF_portNum.getText());
//			messageBoxServer.setText("Port Number Entered is: " + Integer.toString(portNum) );
//			TF_portNum.clear();
//		});

//		messageBoxServer.setText("Waiting for 2 clients to add ...");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
