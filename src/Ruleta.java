class Ruleta extends Thread {
    private Casino casino;
    public Ruleta(Casino casino) {
        this.casino = casino;
    }
    @Override
    public void run() {
        while (casino.girar()) {
            System.out.println("Nueva ronda de apuestas.");
        }
        System.out.println("La simulación ha terminado.");
    }
}