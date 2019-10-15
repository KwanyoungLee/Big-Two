/**
 * This class is a subclass of the Hand class, 
 * and is used to model a hand of straight flush in a Big Two card game.
 * 
 * @author Kwanyoung lee
 */

public class StraightFlush extends Hand {
	/**
	 * Creates and returns an instance of the StraightFlush class.
	 * 
	 * @param player
	 * 				the specific player playing the game
	 * @param cards
	 * 				the list of cards
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
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
		boolean isStraight = temp.isValid();
		if(isStraight && this.isValid())
			return true;
		
		Flush temp2 = (Flush) hand;
		boolean isFlush = temp2.isValid();
		if(isFlush && this.isValid())
			return true;
		
		FullHouse temp3 = (FullHouse) hand;
		boolean isFullhouse = temp3.isValid();
		if(isFullhouse && this.isValid())
			return true;
		
		Quad temp4 = (Quad) hand;
		boolean isQuad = temp4.isValid();
		if(isQuad && this.isValid())
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
			if((this.getCard(0).suit == this.getCard(1).suit) && (this.getCard(1).suit == this.getCard(2).suit) && (this.getCard(2).suit == this.getCard(3).suit) && (this.getCard(3).suit == this.getCard(4).suit))
				if ((temp0 == (temp1 - 1)) && (temp0 == (temp2 - 2)) && (temp0 == (temp3 - 3)) && (temp0 == (temp4 - 4)))
				return true;
		return false;
	}
	
	/**
	 * Returns a string specifying the type of this hand.
	 * 
	 * @return a string specifying the type of this hand
	 * 
	 */
	public String getType() {return "StraightFlush";}
	
}