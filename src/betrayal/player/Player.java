package betrayal.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import betrayal.cards.Card;
import betrayal.tiles.Direction;
import betrayal.tiles.Tile;
import betrayal.util.Date;
import betrayal.util.Height;
import engine.Material;
import engine.components.renderable.Mesh3;
import engine.components.updateable.UpdateableComponent;
import engine.models.creation.CubeBuilder;
import engine.util.Color;
import engine.util.string.Getter;

public class Player extends UpdateableComponent {

	private static final String PLAYERS_PATH = "res/betrayal/players";
	private static final String FILE_NAMES = "fileNames.dat";

	public static List<Player> createPlayers() {
		List<Player> players = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(PLAYERS_PATH + "/" + FILE_NAMES))) {
			while (scanner.hasNextLine())
				players.add(createPlayer(PLAYERS_PATH + "/" + scanner.nextLine()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return players;
	}

	private static Player createPlayer(String filepath) throws FileNotFoundException {

		Scanner scanner = new Scanner(new File(filepath));
		Player player = new Player(scanner.nextLine());

		player.m_Speed = Trait.createTrait(scanner.nextLine());
		player.m_Might = Trait.createTrait(scanner.nextLine());
		player.m_Sanity = Trait.createTrait(scanner.nextLine());
		player.m_Knowledge = Trait.createTrait(scanner.nextLine());
		while (scanner.hasNextLine()) {
			String[] split = scanner.nextLine().split("\\s*=\\s*");
			switch (split[0].toLowerCase()) {
			case "age":
				player.m_Age = Integer.parseInt(split[1]);
				break;
			case "height":
				String[] heightSplit = split[1].split("\\s*,\\s*");
				player.m_Height = new Height(Integer.parseInt(heightSplit[0]), Integer.parseInt(heightSplit[1]));
				break;
			case "weight":
				player.m_Weight = Integer.parseInt(split[1]);
				break;
			case "hobbies":
				List<Hobby> hobbies = new ArrayList<>();
				for (String hobby : split[1].split("\\s*,\\s*"))
					hobbies.add(Hobby.valueOf(hobby));
				player.m_Hobbies = hobbies;
				break;
			case "birthday":
				String[] bdaySplit = split[1].split("\\s*,\\s*");
				player.m_Birthday = new Date(Integer.parseInt(bdaySplit[0]), Integer.parseInt(bdaySplit[1]));
				break;
			case "color":
				player.m_Color = Color.parse(split[1]);
				break;
			case "mat":
				player.m_MatName = split[1];
				break;
			}
		}
		scanner.close();
		return player;
	}

	private String m_Name;
	private Trait m_Speed;
	private Trait m_Might;
	private Trait m_Sanity;
	private Trait m_Knowledge;
	private Date m_Birthday;
	private int m_Age;
	private Height m_Height;
	private int m_Weight;
	private List<Hobby> m_Hobbies;

	private List<Card> m_Cards;

	private Color m_Color;
	private Mesh3 m_Mesh;
	private String m_MatName;

	private Tile m_TileStandingOn;

	private Player(String name) {
		m_Name = name;
		m_Hobbies = new ArrayList<>();
		m_Cards = new ArrayList<>();
	}

	@Override
	public void init() {
		m_Mesh = new Mesh3(CubeBuilder.build(false));
		m_Mesh.material(Material.material("player/" + m_MatName));
		m_Mesh.uniform("color", m_Color);
		entity().addComponent(m_Mesh);
	}

	/**
	 * moves the player to the given tile with no effect handling
	 * 
	 * @param tile
	 */
	public void standOnTile(Tile tile) {
		m_TileStandingOn = tile;
	}

	/**
	 * moves the player along the tiles in the given dieciton if possible with no
	 * effect handling
	 * 
	 * @param direction
	 * @return true if moved, false if the player could not move
	 */
	public boolean move(Direction direction) {

		return false;
	}

	/**
	 * @return name
	 */
	@Getter
	public String name() {
		return m_Name;
	}

	/**
	 * @return m_Speed
	 */
	@Getter
	public Trait speed() {
		return m_Speed;
	}

	/**
	 * @return m_Might
	 */
	@Getter
	public Trait might() {
		return m_Might;
	}

	/**
	 * @return m_Sanity
	 */
	@Getter
	public Trait sanity() {
		return m_Sanity;
	}

	/**
	 * @return m_Knowledge
	 */
	@Getter
	public Trait knowledge() {
		return m_Knowledge;
	}

	/**
	 * @return m_Birthday
	 */
	@Getter
	public Date birthday() {
		return m_Birthday;
	}

	@Getter
	public int daysUntilBirthday() {
		return new Date().daysUntil(m_Birthday);
	}

	@Getter
	public int age() {
		return m_Age;
	}

	@Getter
	public Height height() {
		return m_Height;
	}

	@Getter
	public int weight() {
		return m_Weight;
	}

	@Getter
	public Iterator<Card> cards() {
		return m_Cards.iterator();
	}

	@Getter
	public Iterator<Hobby> hobbies() {
		return m_Hobbies.iterator();
	}

	public boolean hasHobby(Hobby hobby) {
		return m_Hobbies.contains(hobby);
	}

	@Getter
	public Color color() {
		return m_Color;
	}

	/**
	 * @return m_TileStandingOn
	 */
	public Tile tileStandingOn() {
		return m_TileStandingOn;
	}

}
