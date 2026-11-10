class Ruleta extends Thread {
    private Casino casino;
    public Ruleta(Casino casino) {
        this.casino = casino;
    }
    @Override
    public void run() {
        int contador = 1;
        while (casino.girar()) {
            contador++;
            System.out.println("\nNueva ronda de apuestas.\nRonda " + contador);
        }
        System.out.println("La simulación ha terminado.");
    }
}