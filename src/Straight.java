/**
 * This class is a subclass of the Hand class, 
 * and is used to model a hand of triple in a Big Two card game.
 * 
 * @author Kwanyoung lee
 */

public class Straight extends Hand {
	/**
	 * Creates and returns an instance of the Straight class.
	 * 
	 * @param player
	 * 				the specific player playing the game
	 * @param cards
	 * 				the list of cards
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	//overriding method
	/**
	 * Checks if this is a valid hand
	 * 
	 */
	public boolean isValid() {
		
		int temp0 = this.getCard(0).rank;
		int temp1 = this.getCard(1).rank;
		int temp2 = this.getCard(2).rank;
		int temp3 = this.getCard(3).rank;
		int temp4 = this.getCard(4).rank;
		
		if (temp0 == 0)
			temp0 = 13;
		else if (temp0 == 1)
			temp0 = 14;
		
		if (temp1 == 0)
			temp1 = 13;
		else if (temp1 == 1)
			temp1 = 14;
		
		if (temp2 == 0)
			temp2 = 13;
		else if (temp2 == 1)
			temp2 = 14;
		
		if (temp3 == 0)
			temp3 = 13;
		else if (temp3 == 1)
			temp3 = 14;
		
		if (temp4 == 0)
			temp4 = 13;
		else if (temp4 == 1)
			temp4 = 14;
		
		int tempnum = this.size();
		if (tempnum == 5)
			if((this.getCard(0).suit != this.getCard(1).suit) || (this.getCard(1).suit != this.getCard(2).suit) || (this.getCard(2).suit != this.getCard(3).suit) || (this.getCard(3).suit != this.getCard(4).suit))
				if ((temp0 == (temp1 - 1)) && (temp0 == (temp2 - 2)) && (temp0 == (temp3 - 3)) && (temp0 == (temp4 - 4)))
				return true;
		return false;
	}
		
	//overriding method
	/**
	 * Returns a string specifying the type of this hand.
	 * 
	 * @return a string specifying the type of this hand
	 * 
	 */
	public String getType() {return "Straight";}
	
}