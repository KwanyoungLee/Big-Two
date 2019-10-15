/**
 * This class is a subclass of the Hand class, 
 * and is used to model a hand of quad in a Big Two card game.
 * 
 * @author Kwanyoung lee
 */

public class Quad extends Hand {
	/**
	 * Creates and returns an instance of the Quad class.
	 * 
	 * @param player
	 * 				the specific player playing the game
	 * @param cards
	 * 				the list of cards
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	//overriding method
	/**
	 * Checks if this is a valid hand
	 * 
	 */
	public boolean isValid() {

		if (this.size() == 4) {
			if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank())
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
	public String getType() {return "Quad";}
	
}