package engine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Palette {

	private static Map<String, Palette> s_Palettes = new HashMap<>();

	public static void loadPalettes() {

		Palette players = new Palette("players");
		players.addColor(Color.parse("B8B8B8"));
		players.addColor(Color.parse("56282D"));
		players.addColor(Color.parse("FE855D"));
		players.addColor(Color.parse("659157"));
		players.addColor(Color.parse("254E7D"));
		players.addColor(Color.parse("9381FF"));

	}

	public static Palette palette(String name) {
		return s_Palettes.get(name);
	}

	private String m_Name;
	private List<Color> m_Colors;

	public Palette(String name) {
		s_Palettes.put(name, this);
		m_Name = name;
		m_Colors = new ArrayList<>();
	}

	public Color color(int index) {
		return m_Colors.get(index);
	}

	public void addColor(Color c) {
		m_Colors.add(c);
	}

	public String name() {
		return m_Name;
	}

}
