/**
 * This class is a subclass of the Hand class, 
 * and is used to model a hand of single in a Big Two card game.
 * 
 * @author Kwanyoung lee
 */

public class Single extends Hand {
	
	/**
	 * Creates and returns an instance of the Single class.
	 * 
	 * @param player
	 * 				the specific player playing the game
	 * @param cards
	 * 				the list of cards
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	//overriding method
	/**
	 * Checks if this is a valid hand
	 * 
	 */
	public boolean isValid() {

		if (this.size() == 1)
			return true;
		
		return false;
	}
	
	/**
	 * Returns a string specifying the type of this hand.
	 * 
	 * @return a string specifying the type of this hand
	 * 
	 */
	public String getType() {return "Single";}
	
}