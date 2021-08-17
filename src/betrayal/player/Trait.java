package betrayal.player;

public class Trait {

	public static Trait createTrait(String fileLine) {

		String[] split = fileLine.split("\\s*;\\s*");
		TraitType type = TraitType.valueOf(split[0].toUpperCase());
		String[] strVals = split[2].split("\\s*=\\s*")[1].split("\\s*,\\s*");
		int[] vals = new int[strVals.length];
		for (int i = 0; i < vals.length; i++)
			vals[i] = Integer.parseInt(strVals[i]);
		int index = Integer.parseInt(split[1].split("\\s*=\\s*")[1]);
		return new Trait(type, vals, index);
	}

	private TraitType m_Type;

	private int[] m_Values;
	private int m_ValueIndex;

	public Trait(TraitType type, int[] values, int initialIndex) {
		m_Type = type;
		m_Values = values;
		m_ValueIndex = initialIndex;
	}

	public int[] values() {
		return m_Values;
	}

	public int valueIndex() {
		return m_ValueIndex;
	}

	public int value() {
		return m_Values[m_ValueIndex];
	}

	public boolean physical() {
		return m_Type == TraitType.MIGHT || m_Type == TraitType.SPEED;
	}

	public boolean mental() {
		return m_Type == TraitType.SANITY || m_Type == TraitType.KNOWLEDGE;
	}

	@Override
	public String toString() {
		String out = m_Type + ": index - " + m_ValueIndex + "; values - [";
		for (int i = 0; i < m_Values.length; i++) {
			out += m_Values[i];
			if (i < m_Values.length - 1)
				out += ", ";
		}
		out += "]";
		return out;
	}

}
