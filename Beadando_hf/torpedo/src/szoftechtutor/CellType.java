package szoftechtutor;

import java.io.Serializable;

/**
 * A jatekter adott mezojenek allapotat irja le.
 */
public enum CellType implements Serializable {
	Ship, Water, ShipShot, WaterShot, Unknown
}
