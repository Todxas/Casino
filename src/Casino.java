import java.util.Random;

public class Casino {
    // MONITOR COMPARTIDO
    private int banca = 50000;
    private int apuestas = 0;
    private int finalizados = 0;
    private int resultado = -1;
    private int turno = 0;
    private boolean activo = true;
    private final int TOTAL = 12;
    private Random random = new Random();

    public static void main(String[] args) {
        Casino casino = new Casino();
        Ruleta ruleta = new Ruleta(casino);
        for (int i = 1; i <= 4; i++) {
            new Jugador("Numero " + i, casino, 1, 1000).start();
        }
        for (int i = 5; i <= 8; i++) {
            new Jugador("ParImpar " + i, casino, 2, 1000).start();
        }
        for (int i = 9; i <= 12; i++) {
            new Jugador("Martingala " + i, casino, 3, 1000).start();
        }
        ruleta.start();
    }

    public synchronized boolean jugar(Jugador jugador, int tipo, int valor, int cantidad) {
        int miTurno = turno;
        if (!activo) return false;
        apuestas++;
        System.out.println(jugador.getNombre() + " apuesta " + cantidad + " €");
        notifyAll();
        while (resultado == -1 && activo) {
            try {
                wait();
            } catch (InterruptedException e) {
                return false;
            }
        }

        if (!activo) return false;
        boolean gana = false;
        int premio = 0;

        if (resultado != 0) {
            if (tipo == 1 && valor == resultado) {
                gana = true;
                premio = cantidad * 36;
            }
            if (tipo == 2 &&
                    ((valor == 0 && resultado % 2 == 0) ||
                            (valor == 1 && resultado % 2 != 0))) {
                gana = true;
                premio = cantidad * 2;
            }
        }
        jugador.setUltimaVictoria(gana);
        if (gana) {
            if (banca < premio) {
                activo = false;
                System.out.println("La banca no puede pagar.");
                notifyAll();
                return false;
            }
            banca -= premio;
            jugador.saldo += premio;
            System.out.println(jugador.getNombre() + " GANA " + premio + " €");
        } else {
            jugador.saldo -= cantidad;
            banca += cantidad;
            System.out.println(jugador.getNombre() + " PIERDE");
        }
        System.out.println("Saldo jugador: " + jugador.saldo + " €");
        System.out.println("Saldo banca: " + banca + " €");
        finalizados++;
        if (finalizados == TOTAL) {
            finalizados = 0;
            apuestas = 0;
            resultado = -1;
            turno++;
            notifyAll();
        }
        return true;
    }

    public synchronized boolean girar() {
        int miTurno = turno;
        while (apuestas < TOTAL && activo) {
            try {
                wait();
            } catch (InterruptedException e) {
                return false;
            }
        }
        if (!activo) return false;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            return false;
        }
        resultado = random.nextInt(37);
        System.out.println("\nResultado de la tirada: " +
                resultado + "\n");
        notifyAll();
        while (turno == miTurno && activo) {
            try {
                wait();
            } catch (InterruptedException e) {
                return false;
            }
        }
        return activo;
    }
}
