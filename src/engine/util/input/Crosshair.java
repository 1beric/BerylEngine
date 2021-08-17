package engine.util.input;

public enum Crosshair {

	ARROWS("crosshairs/arrowsCrosshair"),
	ARROWS_CENTER("crosshairs/arrowsCenterCrosshair"),
	BOX("crosshairs/boxCrosshair"),
	DIAGONAL("crosshairs/diagonalCrosshair"),
	DIAGONAL_ZAG("crosshairs/zagDiagonalCrosshair"),
	DIAMOND("crosshairs/diamondCrosshair"),
	DIAMOND_CENTER("crosshairs/diamondCenterCrosshair"),
	LARGE_BOX("crosshairs/largeBoxCrosshair"),
	LARGE_BOX_CENTER("crosshairs/largeBoxCenterCrosshair"),
	LARGE_BOX_DETAIL("crosshairs/largeBoxDetailCrosshair"),
	LARGE_DIAMOND_DISCONNECTED_CENTER("crosshairs/largeDiamondDisconnectedCenterCrosshair"),
	LARGE_DIAMOND_DISCONNECTED_CENTER_DETAIL("crosshairs/largeDiamondDisconnectedCenterDetailCrosshair"),
	LARGE_RING_CENTER("crosshairs/largeRingCenterCrosshair"),
	LARGE_RING_CENTER_DETAIL("crosshairs/largeRingCenterDetailCrosshair"),
	PLUS("crosshairs/plusCrosshair"),
	RING("crosshairs/ringCrosshair"),
	RING_CENTER("crosshairs/ringCenterCrosshair"),
	SQUARE("crosshairs/squareCrosshair"),
	;
	
	
	private String m_Path;
	
	Crosshair(String path) {
		m_Path = path;
	}

	/**
	 * @return m_Path
	 */
	public String path() {
		return m_Path;
	}

	/**
	 * @param m_Path m_Path to set
	 */
	public void path(String m_Path) {
		this.m_Path = m_Path;
	}
	
}
