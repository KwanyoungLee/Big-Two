/**
 * This class is a subclass of the CardList class, 
 * and is used to model a hand of cards.
 * 
 * @author Kwanyoung lee
 */

public abstract class Hand extends CardList {

	//public constructor
	/**
	 * Creates and returns an instance of the Hand class.
	 * 
	 * @param player
	 * 				the specific player playing the game
	 * @param cards
	 * 				the list of cards
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		
		this.player = player;
		
		for (int i=0; i<cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
		
		this.sort();
	}
	
	
	//private instance variable
	private CardGamePlayer player;
	
	//public methods
	/**
	 * Retrieves the player of this hand
	 * @return player of this hand
	 */
	public CardGamePlayer getPlayer() {return player;}
	
	/**
	 * Retrieves the top card of this hand
	 * @return top card of this hand
	 */
	public Card getTopCard() {

		return this.getCard(this.size() - 1); 
	}
	
	/**
	 * Checks if this hand beats a specified hand
	 * 
	 * @param hand
	 * 			the specified hand to be compared
	 * 
	 * @return true if this hand beats a specified hand, false in opposite situation 
	 */
	public boolean beats(Hand hand) {

			BigTwoCard tempthis = (BigTwoCard) this.getTopCard();
			BigTwoCard tempspec = (BigTwoCard) hand.getTopCard();
			
			int temp1 = tempthis.rank;
			int temp2 = tempspec.rank;
			
			if (temp1 == 0 )
				temp1 = 13;
			else if (temp1 == 1 )
				temp1 = 14;
			if (temp2 == 0 )
				temp2 = 13;
			else if (temp2 == 1 )
				temp2 = 14;
			
			if (temp1 > temp2)
				return true;
			else if (temp1 < temp2)
				return false;
			else if (tempthis.suit > tempspec.suit)
				return true;
			else if (tempthis.suit < tempspec.suit)
				return false;
			return false;
	}
	
	//abstract methods
	public abstract boolean isValid();
	public abstract String getType();
	
}