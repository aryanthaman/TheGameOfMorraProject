import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

class MorraTest {

	Server server;
	Server.ClientThread obj_ct;
	Consumer<Serializable> callback, tempCall;
	MorraInfo Game;
	int result;

	@BeforeEach
	void init() {
		server = new Server(callback, 1234);
		Game = new MorraInfo();
	}

	@Test
	void test1()
	{
		Game.p1Guess = 0;
		Game.p1Plays = 0;
		Game.p2Guess = 0;
		Game.p2Plays = 0;
		result = server.serverLogic(Game);
		assertEquals(0, result ,"Wrong Conditions");
	}

	@Test
	void test2()
	{
		Game.p1Guess = 1;
		Game.p1Plays = 0;
		Game.p2Guess = 0;
		Game.p2Plays = 1;
		result = server.serverLogic(Game);
		assertEquals(1, result ,"Wrong Conditions");
	}

	@Test
	void test3()
	{
		Game.p1Guess = 0;
		Game.p1Plays = 1;
		Game.p2Guess = 1;
		Game.p2Plays = 0;
		result = server.serverLogic(Game);
		assertEquals(2, result ,"Wrong Conditions");
	}

	@Test
	void test4()
	{
		Game.p1Guess = 1;
		Game.p1Plays = 2;
		Game.p2Guess = 3;
		Game.p2Plays = 4;
		result = server.serverLogic(Game);
		assertEquals(3, result ,"Wrong Conditions");
	}

	@Test
	void test5()
	{
		Game.p1Guess = 0;
		Game.p1Plays = 0;
		Game.p2Guess = 0;
		Game.p2Plays = 0;
		result = server.serverLogic(Game);
		assertEquals(0, result ,"Wrong Conditions");
	}

	@Test
	void test6()
	{
		Game.p1Guess = 5;
		Game.p1Plays = 2;
		Game.p2Guess = 2;
		Game.p2Plays = 3;
		result = server.serverLogic(Game);
		assertTrue(Game.p1Wins,"Wrong Conditions");
	}

	@Test
	void test7()
	{
		Game.p1Guess = 0;
		Game.p1Plays = 1;
		Game.p2Guess = 2;
		Game.p2Plays = 1;
		result = server.serverLogic(Game);
		assertTrue(Game.p2Wins,"Wrong Conditions");
	}

	@Test
	void test8()
	{
		Game.p1Guess = 1;
		Game.p1Plays = 0;
		Game.p2Guess = 0;
		Game.p2Plays = 1;
		result = server.serverLogic(Game);
		assertEquals(1, Game.p1Points ,"Wrong Conditions");
	}

	@Test
	void test9()
	{
		Game.p1Guess = 0;
		Game.p1Plays = 1;
		Game.p2Guess = 1;
		Game.p2Plays = 0;
		result = server.serverLogic(Game);
		assertEquals(1, Game.p2Points ,"Wrong Conditions");
	}

}
