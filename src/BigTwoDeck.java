/**
 * This class is a subclass of the Deck class, 
 * and is used to model a deck of cards used in a Big Two card game.
 * 
 * @author Kwanyoung lee
 */

public class BigTwoDeck extends Deck {

	//overriding method
	/**
	 * Initialize the deck of cards.
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 2; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
			BigTwoCard card = new BigTwoCard(i, 0);
			addCard(card);
			BigTwoCard card2 = new BigTwoCard(i, 1);
			addCard(card2);
		}
	}
	
}