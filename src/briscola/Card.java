package briscola;
public class Card implements Comparable<Card>{
	private int number;
	private String suit;

	public Card(int number, String suit) {
		this.number = number;
		this.suit = suit;
	}

	public int getNumber() {
		return number;
	}

	public String getSuit() {
		return suit;
	}

	@Override
	public int hashCode(){
		return 0;
	}

	@Override
	public boolean equals(Object o){
		if(o==this) return true;
		if(o==null) return false;
		if(o.getClass()!=this.getClass()) return false;
		Card c=(Card) o;
		return c.number==this.number&&c.suit.equals(this.suit);
	}

	@Override
	public String toString(){
		return this.number+" "+this.suit;
	}

	@Override
	public int compareTo(Card c){
		if(this.number<c.number) return -1;
		if(this.number>c.number) return 1;

		if(this.suit=="bastoni"){
			return c.suit=="bastoni"?0:-1;
		}else{
			if(this.suit=="spade"){
				return c.suit=="spade"?0:(c.suit=="bastoni"?1:-1);
			}else{
				if(this.suit=="coppe"){
					return c.suit=="coppe"?0:(c.suit=="oro"?-1:1);
				}else{
					return c.suit=="oro"?0:1;
				}
			}
		}

	}

}
