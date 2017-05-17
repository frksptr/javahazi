package szoftechtutor;

import java.io.Serializable;

/**
 * A játéktér adott mezőjének állapotát írja le
 */
public enum CellType implements Serializable {
	Ship, Water, ShipShot, WaterShot, Unknown
}
