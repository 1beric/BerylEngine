package betrayal.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck<T> {

	private List<T> m_Elements;
	private List<T> m_Drawn;
	private List<T> m_Discarded;

	public Deck() {
		m_Elements = new ArrayList<>();
		m_Drawn = new ArrayList<>();
		m_Discarded = new ArrayList<>();
	}

	public void shuffleInAll() {
		m_Elements.addAll(m_Drawn);
		m_Elements.addAll(m_Discarded);
		shuffle();
		m_Drawn.clear();
		m_Discarded.clear();
	}

	public void shuffleInDiscard() {
		m_Elements.addAll(m_Discarded);
		shuffle();
		m_Discarded.clear();
	}

	public void shuffle() {
		Collections.shuffle(m_Elements);
	}

	public T draw() {
		if (m_Elements.isEmpty())
			shuffleInDiscard();
		m_Drawn.add(peek());
		return m_Elements.remove(0);
	}

	public T peek() {
		if (m_Elements.isEmpty())
			shuffleInDiscard();
		return m_Elements.get(0);
	}

	public void discard(T card) {
		m_Drawn.remove(card);
		m_Discarded.add(card);
	}

}
