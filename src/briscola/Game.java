package briscola;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


public class Game {
	private Deck deck;
	private Card lastCard;
	private ArrayList<Card> playerHand;
	private ArrayList<Card> computerHand;
	private int turn;//turn  ==0 --> player turn turn ==1 --> computer turn
	private int capTurn;
	private static Random r=new Random();
	private static final String semi[]={"bastoni","spade","coppe","oro"};
	private Card card1;
	private Card card2;
	private int turnsLeft;
	private static final int[] numberStrength={1,10,2,9,8,7,6,5,4,3};
	private ArrayList<Card> playerCards;
	private ArrayList<Card> computerCards;
	private String briscola;
	private static final int[] numberPoint={11,0,10,0,0,0,0,2,3,4};
	private InputStreamReader isr;
	private BufferedReader br;


	public Game(){
		this.playerHand=new ArrayList<>();
		this.computerHand=new ArrayList<>();
		this.deck=new Deck();
		this.deck.shuffle();
		this.lastCard=this.deck.pickCard();

		this.turnsLeft=20;//SUBJECT TO CHANGE

		this.playerCards=new ArrayList<>();
		this.computerCards=new ArrayList<>();
		this.briscola=this.lastCard.getSuit();
		this.isr=new InputStreamReader(System.in);
		this.br=new BufferedReader(this.isr);
	}

	public void closeStreams() throws IOException {
		this.br.close();
		this.isr.close();
	}

	public String getBriscola(){
		return this.briscola;
	}

	private void printPlayerHand(){
		System.out.println("Player's hand:");
		int i=1;
		for(Card c:this.playerHand){
			if(c!=null)
				System.out.println(i+++" "+c);
		}
	}

	private void printComputerHand(){
		System.out.println("Computer's hand:");
		int i=1;
		for(Card c:this.computerHand){
			if(c!=null)
				System.out.println(i+++" "+c);
		}
	}

	private Card playCard() throws IOException, InterruptedException {
		this.printPlayerHand();
		System.out.println("\n");
		//this.printComputerHand();
		Card c=null;

		int choice=-2;//standard value, does nothing
			while(choice<0){
				System.out.println("Card: ");
				String s=null;
				try {
					s = br.readLine();
					choice = Integer.parseInt(s)-1;


					if (choice == -1) {//vuole sapere la briscola
						System.out.println("Briscola di " + getBriscola());
						choice = -2;
					} else {
						if (choice >= 0 && choice < this.playerHand.size()) {//ha giocato una carta valida
							c = this.playerHand.remove(choice);
							System.out.println("Hai giocato "+c);
						} else {//non ha giocato una carta valida.
							choice = -2;
							throw new NumberFormatException();
						}
					}
				}catch(NumberFormatException e){
					System.out.println("NOT A VALID CHOICE!!!\n");
				}
			}


		return c;
	}

	public boolean card1Wins(){
		String briscola=this.getBriscola();
		Comparator<Card> comp =new Comparator<>(){
			@Override
			public int compare(Card c1,Card c2){//supponendo che c1 sia stata giocata prima
				if(c1.getSuit()==briscola){
					return c2.getSuit()!=briscola?1:(numberStrength[c1.getNumber()-1]<numberStrength[c2.getNumber()-1]?1:(numberStrength[c1.getNumber()-1]==numberStrength[c2.getNumber()-1]?0:-1));
				}
				if(c2.getSuit()==briscola){
					return c1.getSuit()!=briscola?-1:(numberStrength[c2.getNumber()-1]<numberStrength[c1.getNumber()-1]?-1:(numberStrength[c1.getNumber()-1]==numberStrength[c2.getNumber()-1]?0:1));
				}
				if(c1.getSuit()==c2.getSuit()){
					return numberStrength[c1.getNumber()-1]<numberStrength[c2.getNumber()-1]?1:(numberStrength[c1.getNumber()-1]==numberStrength[c2.getNumber()-1]?0:-1);
				}
				return 1;
			}
		};
		return comp.compare(this.card1,this.card2)==1?true:false;
	}

	public void play() throws IOException, InterruptedException {
		System.out.println("Coin toss, player starts if head: ");
		int start=r.nextInt()%2;
		this.turn=start;
		this.capTurn=this.turn+40;
		if(start==0){//the way to give the cards is different based on who starts
			System.out.println("head. Player starts!\n");

			for(int i=0;i<6;i++) {
				if (i % 2 == 0)
					this.addCardToPlayerHand(this.deck.pickCard());
				else
					this.addCardToComputerHand(this.deck.pickCard());
			}
		}else{
			System.out.println("tail. Computer starts!\n");
			for(int i=0;i<6;i++){
				if(i%2==1)
					this.addCardToPlayerHand(this.deck.pickCard());
				else
					this.addCardToComputerHand(this.deck.pickCard());
			}
		}

		System.out.println("La briscola è di "+this.getBriscola()+"\n");
		while (this.turnsLeft>0) {//true until the end of the game
			cardsExchange();
			this.turnsLeft--;
		}

		int p=countPoints(this.playerCards);
		int c=countPoints(this.computerCards);

		System.out.println("Player points: "+p);
		System.out.println("Computer points: "+c);

		if(p>c){
			System.out.println("Congrats, player won");
		}else{
			System.out.println("Seriusly? Lost to someone who plays randomly?");
		}

	}

	public int countPoints(ArrayList<Card> cards){
		int acc=0;
		for(Card c:cards){
			acc+= numberPoint[c.getNumber()-1];
		}

		return acc;
	}

	public void cardsExchange() throws IOException, InterruptedException {
		if(this.turn==0) {
			this.card1=playCard();

			int rnd=Math.abs(r.nextInt()%this.computerHand.size());

			this.card2=this.computerHand.remove(rnd);
			System.out.println("Computer played "+this.card2+"\n");

			//CHECK WHICH WINS AND PRINT IT
			if(card1Wins()){//player wins
				playerWinsHand();
			}else{//computer wins
				computerWinsHand();
			}
		}else{
			int rnd=Math.abs(r.nextInt()%this.computerHand.size());
			this.card1=this.computerHand.remove(rnd);

			System.out.println("Computer played "+this.card1.toString()+"\n");

			this.card2=playCard();

			if(card1Wins()){//computer wins
				computerWinsHand();
			}else{//player wins
				playerWinsHand();
			}
		}
	}

	public void playerWinsHand(){
		this.playerCards.add(this.card1);
		this.playerCards.add(this.card2);
		this.turn=0;
		this.addCardToPlayerHand(this.deck.pickCard());
		Card cc;
		if((cc=this.deck.pickCard())!=null)
			this.addCardToComputerHand(cc);
		else{
				this.addCardToComputerHand(this.lastCard);
				this.lastCard = null;
		}
		System.out.println("PLAYER WON THIS HAND\n\n");
	}

	public void computerWinsHand(){
		this.computerCards.add(this.card1);
		this.computerCards.add(this.card2);
		this.turn=1;
		this.addCardToComputerHand(this.deck.pickCard());
		Card cc;
		if((cc=this.deck.pickCard())!=null)
			this.addCardToPlayerHand(cc);
		else {

				this.addCardToPlayerHand(this.lastCard);
				this.lastCard=null;

		}
		System.out.println("COMPUTER WON THIS HAND\n\n");
	}

	private void addCardToPlayerHand(Card c){
		if(c!=null)
			this.playerHand.add(c);
	}

	private void addCardToComputerHand(Card c){
		if(c!=null)
			this.computerHand.add(c);
	}

	public void whoseTurnIsIt(){
		if(this.turn==0)
			System.out.println("Player's turn");
		else
			System.out.println("Computer's turn");
	}
}
