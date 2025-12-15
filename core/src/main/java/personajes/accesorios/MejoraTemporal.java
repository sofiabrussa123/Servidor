package personajes.accesorios;

// Sistema de mejoras para estadísticas del jugador
public class MejoraTemporal {

    private static final int MAX_MEJORAS = 3;

    // Bonus específicos por cada mejora
    private static final float BONUS_VIDA = 20f;
    private static final float BONUS_VELOCIDAD = 1f;
    private static final float BONUS_SALTO = 0.5f;
    private static final float BONUS_DAÑO = 10f;

    private int mejorasVida;
    private int mejorasVelocidad;
    private int mejorasSalto;
    private int mejorasDaño;

    public MejoraTemporal() {
        this.mejorasVida = 0;
        this.mejorasVelocidad = 0;
        this.mejorasSalto = 0;
        this.mejorasDaño = 0;
    }

    public boolean mejorarVida() {
        if (mejorasVida < MAX_MEJORAS) {
            mejorasVida++;
            return true;
        }
        return false;
    }

    public boolean mejorarVelocidad() {
        if (mejorasVelocidad < MAX_MEJORAS) {
            mejorasVelocidad++;
            return true;
        }
        return false;
    }

    public boolean mejorarSalto() {
        if (mejorasSalto < MAX_MEJORAS) {
            mejorasSalto++;
            return true;
        }
        return false;
    }

    public boolean mejorarDaño() {
        if (mejorasDaño < MAX_MEJORAS) {
            mejorasDaño++;
            return true;
        }
        return false;
    }

    public float getBonusVida() {
        return mejorasVida * BONUS_VIDA;
    }

    public float getBonusVelocidad() {
        return mejorasVelocidad * BONUS_VELOCIDAD;
    }

    public float getBonusSalto() {
        return mejorasSalto * BONUS_SALTO;
    }

    public float getBonusDaño() {
        return mejorasDaño * BONUS_DAÑO;
    }

    // Getters para la UI
    public int getMejorasVida() {
        return mejorasVida;
    }

    public int getMejorasVelocidad() {
        return mejorasVelocidad;
    }

    public int getMejorasSalto() {
        return mejorasSalto;
    }

    public int getMejorasDaño() {
        return mejorasDaño;
    }

    public int getMaxMejoras() {
        return MAX_MEJORAS;
    }

    public void reset() {
        mejorasVida = 0;
        mejorasVelocidad = 0;
        mejorasSalto = 0;
        mejorasDaño = 0;
    }
}
