/**
 * This class is a subclass of the Card class, 
 * and is used to model a card used in a Big Two card game.
 * 
 * @author Kwanyoung lee
 */

public class BigTwoCard extends Card{

	//public constructor
	/**
	 * Creates and returns an instance of the BigTwoCard class.
	 * 
	 * @param suit
	 * 				the suit of cards being used. 
	 * @param rank
	 * 				the rank of cards being used. 
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	
	//overriding method
	/**
	 * Compares this card with the specified card for order.
	 * 
	 * @param card
	 *            the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is
	 *         less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card) {
		int tempthis = this.rank;
		int tempcard = card.rank;
		
		if (tempthis == 0 )
			tempthis = 13;
		else if (tempthis == 1 )
			tempthis = 14;
		if (tempcard == 0 )
			tempcard = 13;
		else if (tempcard == 1 )
			tempcard = 14;
			
		if (tempthis > tempcard) {
			return 1;
		} else if (tempthis < tempcard) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}	
}