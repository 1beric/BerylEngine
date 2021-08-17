package betrayal.util;

import engine.util.string.Getter;

public class Height {

	private int m_Feet;
	private int m_Inches;

	public Height(int feet, int inches) {
		m_Feet = feet;
		m_Inches = inches;
	}

	@Getter
	public int feet() {
		return m_Feet;
	}

	@Getter
	public int inches() {
		return m_Inches;
	}

	@Override
	public String toString() {
		return feet() + "'" + inches() + '"';
	}

}
