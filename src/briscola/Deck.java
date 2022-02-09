package briscola;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
	private Card[] cards=new Card[40];
	private static Random r=new Random();
	private static final String semi[]={"bastoni","spade","coppe","oro"};
	private int currentCard;

	public void shuffle(){
		ArrayList<Card> al=new ArrayList<>();
		for(int i=0;i<40;i++){
			al.add(this.cards[i]);
		}
		Collections.shuffle(al);
		for(int i=0;i<40;i++){
			this.cards[i]=al.get(i);
		}
	}

	public void print(){
		for(int i=0;i<40;i++){
			System.out.println(this.cards[i].toString());
		}
	}

	public Deck(){
		this.currentCard=0;
		int n=0;
		for(String seme:semi){
			for(int i=1;i<11;n++,i++){
				cards[n]=new Card(i,seme);
			}
		}
	}

	public Card pickCard(){
		if(this.currentCard<40) {
			return this.cards[this.currentCard++];
		}
		return null;
	}
}
