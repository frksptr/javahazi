package szoftechtutor;

import java.io.Serializable;

/*
 * Jelzi, hogy az aktu�lis mez�n milyen t�pus� dolog tal�lhat�,
 * haj�, v�z, vagy ezeknek m�r kil�tt v�ltozata
 */
public enum CellType implements Serializable {
	Ship, Water, ShipShot, WaterShot, Unknown
}
