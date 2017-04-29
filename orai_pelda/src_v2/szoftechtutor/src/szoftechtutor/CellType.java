package szoftechtutor;

import java.io.Serializable;

/*
 * Jelzi, hogy az aktuális mezõn milyen típusú dolog található,
 * hajó, víz, vagy ezeknek már kilõtt változata
 */
public enum CellType implements Serializable {
	Ship, Water, ShipShot, WaterShot, Unknown
}
