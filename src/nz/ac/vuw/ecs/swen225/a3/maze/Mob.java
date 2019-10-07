package nz.ac.vuw.ecs.swen225.a3.maze;

import javax.json.JsonReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Mob are NPCs in the game that move and interact without any input from the user.
 * Functionally, they act as a spider that latches onto a Tile and scale the board.
 * Although the Constructor takes a Board, the board is not modified at all.
 */
public abstract class Mob {

  String imageUrl = "unknown.png";
  String mobName = "Unnamed Mob.";
  Tile host;
  boolean active;
  Tile.Direction direction;
  Map<Tile.Direction, String> images = new HashMap<>();
  Set<Tile.Type> safeTiles = new HashSet<>(Arrays.asList(Tile.Type.Free));
  Board board;


  /**
   * Sets the board for the mob. Handled by the MobManager.
   * Not all mobs needs this, can't be done at creation.
   *
   * @param board to set.
   */
  void setBoard(Board board) {
    this.board = board;
  }

  /**
   * Sets the name of the mob. Handled by concrete sub-class.
   * @param name to set.
   */
  void setMobName(String name) {
    mobName = name;
  }

  /**
   * @param newImageUrl new filename of mob image.
   */
  public void setImageUrl(String newImageUrl) {
    imageUrl = newImageUrl;
  }

  /**
   * Sets the active state of the mob.
   * Will not change if mob has no host.
   *
   * @param active state of mob.
   */
  public void setActive(boolean active) {
    if (host != null) this.active = active;
  }

  /**
   * Gets the active state of the mob.
   *
   * @return boolean active state of mob.
   */
  public boolean getActive() {
    return active;
  }

  /**
   * Moves the Mob by one Cell.
   * Default implementation, this can be override
   * for advanced behaviour.
   */
  public void advanceByTick() {
    if (getHost() == null) return;

    Tile target;

    do {
      if (--remaining < 0) {
        remaining = getNextSeed();
        direction = getNextDirection();
      }

      target = host.getDir(direction);

    } while (!safeTiles.contains(target.getType()));

    setImageUrl(images.get(direction));
    occupyHost(target);
  }

  /**
   * Occupies a new tile as a host.
   *
   * @param target cell to occupy.
   */
  void occupyHost(Tile target) {
    // Reset old host.
    if (host != null) {
      host.setTileUnoccupied();
      host.setOccupied(false);
    }

    // Set new host.
    host = target;

    if (target != null) {
      target.setTileOccupied(target.getImageUrl() + "-" + imageUrl);
      target.setOccupied(true);
    }
    active = target != null;
  }

  /**
   * Calls occupy host with null as the target.
   */
  public void leaveHost() {
    occupyHost(null);
    active = false;
  }

  /**
   * Returns host of Mob.
   *
   * @return host of Mob.
   */
  public Tile getHost() {
    return host;
  }

  public abstract String getJson();

  public abstract void setMobFromJson(JsonReader json);

  /**
   * Set host of this mob.
   *
   * @param t Tile to set as host
   */
  public void setHost(Tile t) {
    host = t;
  }


  /**
   * Seed used for pseudo-random behaviour.
   */
  private final String[] seed = new String[]{
      "351567484564846135285846541156461354156163153324856515",
      "351358563565415134691316431343415641651521354516135354",
      "612825353486541354564825865453464651325463548654153413",
      "653258235468871689315366486465135235964135486531654343",
      "285846545464984165465465748913486468164984633546542835",
      "654896451834358865564946541345445452523156465454313541",
      "561352374845631564875645321547465415314543545646531254",
      "235418466543113135453564651534516354315446153451312341",
      "313241531355635432135134654564564153123354576486358651",
      "321345561345632354351324651234532135324154322456335321",
      "324545451256426126456126546543242633465462346532156423",
      "235453125645133253213245321543123254562315345643215645",
      "456525435245135534135231531453215342315321534523135335"};

  int remaining = 0;
  int seedRow = 0;
  int seedCol = 0;

  /**
   * Gets next seed based off indexes.
   * Seed indexes automatically increment.
   * @return next pseudo-random number. [0-9].
   */
  private int getNextSeed() {
    ++seedCol;

    if (seedCol == seed[seedRow].length())
      seedCol = 0;

    return seed[seedRow].charAt(seedCol) - '0';
  }

  /**
   * Gets next pseudo-random direction based off seed and indexes.
   * @return pseudo-random direction.
   */
  private Tile.Direction getNextDirection() {
    int seed = getNextSeed();
    // Less than 3 straight.
    if (seed <= 2) return direction;
    // Equal to 9 reverse.
    else if (seed == 9) return direction.reverse();
    // Even clockwise.
    else if (seed % 2 == 0) return direction.clockWise();
    // Odd anticlockwise.
    else return direction.antiClockWise();
  }

}
