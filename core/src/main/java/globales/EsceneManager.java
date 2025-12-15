package globales;

import niveles.EscenaBase;

public abstract class EsceneManager {
	private static EscenaBase escenaActual;

	public static void setEscenaActual(EscenaBase escena) {
        escenaActual = escena;
	}

	public static EscenaBase getEscenaActual() {
        return escenaActual;
	}
}
