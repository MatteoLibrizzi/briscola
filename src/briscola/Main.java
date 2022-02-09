package briscola;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import briscola.Game;

public class Main {




	public static void main(String... args) throws IOException, InterruptedException {
		Game g=new Game();
		g.play();
		g.closeStreams();

	}
}
