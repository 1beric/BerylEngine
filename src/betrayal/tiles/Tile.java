package betrayal.tiles;

import java.util.List;

import betrayal.cards.Card;
import betrayal.monsters.Monster;
import betrayal.player.Player;
import engine.components.renderable.Mesh3;
import engine.models.creation.ModelFileType;

public class Tile extends Mesh3 {

	private static final String MODEL_FILE = "";

	private HouseLayer m_AllowedHouseLayers;
	private PickupType m_PickupType;

	private boolean m_InHouse;
	private List<Direction> m_Doorways;

	private List<Card> m_Cards;
	private List<Player> m_Players;
	private List<Monster> m_Monsters;

	public Tile() {
		super(MODEL_FILE, ModelFileType.OBJ);

	}

	public boolean canPlaceUpper() {
		return m_AllowedHouseLayers == HouseLayer.UPPER || m_AllowedHouseLayers == HouseLayer.UPPER_GROUND
				|| m_AllowedHouseLayers == HouseLayer.UPPER_BASEMENT
				|| m_AllowedHouseLayers == HouseLayer.UPPER_GROUND_BASEMENT;
	}

	public boolean canPlaceGround() {
		return m_AllowedHouseLayers == HouseLayer.GROUND || m_AllowedHouseLayers == HouseLayer.UPPER_GROUND
				|| m_AllowedHouseLayers == HouseLayer.GROUND_BASEMENT
				|| m_AllowedHouseLayers == HouseLayer.UPPER_GROUND_BASEMENT;
	}

	public boolean canPlaceBasement() {
		return m_AllowedHouseLayers == HouseLayer.BASEMENT || m_AllowedHouseLayers == HouseLayer.UPPER_BASEMENT
				|| m_AllowedHouseLayers == HouseLayer.GROUND_BASEMENT
				|| m_AllowedHouseLayers == HouseLayer.UPPER_GROUND_BASEMENT;
	}

	public PickupType pickupType() {
		return m_PickupType;
	}

}
