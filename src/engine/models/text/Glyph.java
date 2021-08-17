package engine.models.text;

public class Glyph {

	private int m_Width;
	private int m_Height;
	private int m_X;
	private int m_Y;
	private float m_Advance;

	public Glyph(int w, int h, int x, int y, float advance) {
		width(w);
		height(h);
		x(x);
		y(y);
		advance(advance);
	}

	/**
	 * @return m_Width
	 */
	public int width() {
		return m_Width;
	}

	/**
	 * @param m_Width m_Width to set
	 */
	public void width(int m_Width) {
		this.m_Width = m_Width;
	}

	/**
	 * @return m_Height
	 */
	public int height() {
		return m_Height;
	}

	/**
	 * @param m_Height m_Height to set
	 */
	public void height(int m_Height) {
		this.m_Height = m_Height;
	}

	/**
	 * @return m_X
	 */
	public int x() {
		return m_X;
	}

	/**
	 * @param m_X m_X to set
	 */
	public void x(int m_X) {
		this.m_X = m_X;
	}

	/**
	 * @return m_Y
	 */
	public int y() {
		return m_Y;
	}

	/**
	 * @param m_Y m_Y to set
	 */
	public void y(int m_Y) {
		this.m_Y = m_Y;
	}

	/**
	 * @return m_Advance
	 */
	public float advance() {
		return m_Advance;
	}

	/**
	 * @param m_Advance m_Advance to set
	 */
	public void advance(float m_Advance) {
		this.m_Advance = m_Advance;
	}

}
