public class System_Zarzadzania_Zadaniami {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Zarzadzanie_Zadaniami_GUI().setVisible(true);
            }
        });
    }
}
