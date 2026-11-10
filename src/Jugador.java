import java.util.Random;

class Jugador extends Thread {

    private Casino casino;
    private int tipo;
    public int saldo;
    private int cantidad = 10;
    private int numero;
    private Random random = new Random();
    private boolean ultimaVictoria;

    public Jugador(String nombre, Casino casino, int tipo, int saldo) {
        super(nombre);
        this.casino = casino;
        this.tipo = tipo;
        this.saldo = saldo;
        numero = random.nextInt(36) + 1;
    }

    public String getNombre() {
        return getName();
    }
    public void setUltimaVictoria(boolean gana) {
        ultimaVictoria = gana;
    }
    @Override
    public void run() {
        while (saldo >= cantidad) {
            int valor = 0;
            if (tipo == 1) {
                valor = random.nextInt(36) + 1;
            }
            if (tipo == 2) {
                valor = random.nextInt(2);
            }
            if (tipo == 3) {
                valor = numero;
            }
            boolean continuar = casino.jugar(this, tipo, valor, cantidad);
            if (!continuar) break;
            if (tipo == 3) {
                if (ultimaVictoria) {
                    cantidad = 10;
                } else {
                    cantidad *= 2;
                }
            }
        }
        casino.eliminarJugador();
    }
}
