/**
 * This class is a subclass of the Hand class, 
 * and is used to model a hand of fullhouse in a Big Two card game.
 * 
 * @author Kwanyoung lee
 */

public class FullHouse extends Hand {
	/**
	 * Creates and returns an instance of the FullHouse class.
	 * 
	 * @param player
	 * 				the specific player playing the game
	 * @param cards
	 * 				the list of cards
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks if this hand beats a specified hand
	 * 
	 * @param hand
	 * 			the specified hand to be compared
	 */
	public boolean beats(Hand hand) {
		
		Straight temp = (Straight) hand;
		if(temp.isValid() && this.isValid())
			return true;
		
		Flush temp2 = (Flush) hand;
		boolean isFlush = temp2.isValid();
		if(isFlush && this.isValid())
			return true;
		
		Card tempthis = this.getTopCard();
		Card tempspec = hand.getTopCard();
		
		if (tempthis.rank > tempspec.rank)
			return true;
		else if (tempthis.rank < tempspec.rank)
			return false;
		else if (tempthis.suit > tempspec.suit)
			return true;
		else if (tempthis.suit < tempspec.suit)
			return false;
		return false;
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
		
		int tempnum = this.size();
		if (tempnum == 5) {
			if ((temp0 == temp1) && (temp1 == temp2) && (temp2!= temp3) && (temp3 == temp4))
				return true;
			if ((temp0 == temp1) && (temp1 != temp2) && (temp2 == temp3) && (temp3 == temp4))
					return true;
		}
		return false;
	}
		
	/**
	 * Returns a string specifying the type of this hand.
	 * 
	 * @return a string specifying the type of this hand
	 * 
	 */
	public String getType() {return "FullHouse";}
	
}