import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Zarzadzanie_Zadaniami_GUI extends JFrame {
    private ArrayList<Czlonek_Zespolu> czlonkowie_zespolu = new ArrayList<>();
    private ArrayList<Zadania> zadania = new ArrayList<>();
    private DefaultListModel<Zadania> model_listy_zadan = new DefaultListModel<>();
    private JList<Zadania> lista_zadan = new JList<>(model_listy_zadan);
    private DefaultListModel<Czlonek_Zespolu> model_listy_czlonkow = new DefaultListModel<>();
    private JList<Czlonek_Zespolu> lista_czlonkow = new JList<>(model_listy_czlonkow);
    private JLabel etykiety_przypisane_do = new JLabel(" ");
    private JTable tabela_przypisanych_zadan;
    private DefaultTableModel model_tabeli_przypisanych_zadan;

    public Zarzadzanie_Zadaniami_GUI() {
        setTitle("System Zarządzania Zadaniami");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel_czlonkow_zespolu = new JPanel(new BorderLayout());
        panel_czlonkow_zespolu.setBorder(BorderFactory.createTitledBorder("Członkowie Zespołu"));
        panel_czlonkow_zespolu.add(new JScrollPane(lista_czlonkow), BorderLayout.CENTER);

        JTextField pole_nazwy_czlonka = new JTextField();
        JButton przycisk_dodaj_czlonka = new JButton("Dodaj Członka Zespołu");
        przycisk_dodaj_czlonka.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nazwa = pole_nazwy_czlonka.getText();
                if (!nazwa.isEmpty()) {
                    Czlonek_Zespolu czlonek = new Czlonek_Zespolu(nazwa);
                    czlonkowie_zespolu.add(czlonek);
                    model_listy_czlonkow.addElement(czlonek);
                    pole_nazwy_czlonka.setText("");
                }
            }
        });
        JPanel panel_dodawania_czlonka = new JPanel(new BorderLayout());
        panel_dodawania_czlonka.add(pole_nazwy_czlonka, BorderLayout.CENTER);
        panel_dodawania_czlonka.add(przycisk_dodaj_czlonka, BorderLayout.EAST);
        panel_czlonkow_zespolu.add(panel_dodawania_czlonka, BorderLayout.SOUTH);

        JPanel panel_zadan = new JPanel(new BorderLayout());
        panel_zadan.setBorder(BorderFactory.createTitledBorder("Zadania"));
        panel_zadan.add(new JScrollPane(lista_zadan), BorderLayout.CENTER);

        JTextField pole_opisu_zadania = new JTextField();
        JButton przycisk_dodaj_zadanie = new JButton("Dodaj Zadanie");
        przycisk_dodaj_zadanie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String opis = pole_opisu_zadania.getText();
                if (!opis.isEmpty()) {
                    Zadania zadanie = new Zadania(opis);
                    zadania.add(zadanie);
                    model_listy_zadan.addElement(zadanie);
                    pole_opisu_zadania.setText("");
                }
            }
        });
        JPanel panel_dodawania_zadania = new JPanel(new BorderLayout());
        panel_dodawania_zadania.add(pole_opisu_zadania, BorderLayout.CENTER);
        panel_dodawania_zadania.add(przycisk_dodaj_zadanie, BorderLayout.EAST);
        panel_zadan.add(panel_dodawania_zadania, BorderLayout.SOUTH);

        JButton przycisk_przypisz_zadanie = new JButton("Przypisz Zadanie");
        przycisk_przypisz_zadanie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Zadania wybrane_zadanie = lista_zadan.getSelectedValue();
                Czlonek_Zespolu wybrany_czlonek = lista_czlonkow.getSelectedValue();
                if (wybrane_zadanie != null && wybrany_czlonek != null) {
                    wybrane_zadanie.przypisz_do(wybrany_czlonek);
                    lista_zadan.repaint();
                    aktualizuj_etykiety_przypisane_do();
                    odswiez_tabele_przypisanych_zadan();
                }
            }
        });
        JButton przycisk_zakoncz_zadanie = new JButton("Zakończ Zadanie");
        przycisk_zakoncz_zadanie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Zadania wybrane_zadanie = lista_zadan.getSelectedValue();
                if (wybrane_zadanie != null) {
                    wybrane_zadanie.zakoncz();
                    lista_zadan.repaint();
                    aktualizuj_etykiety_przypisane_do();
                    odswiez_tabele_przypisanych_zadan();
                }
            }
        });
        JButton przycisk_generuj_raport = new JButton("Generuj Raport");
        przycisk_generuj_raport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generuj_raport();
            }
        });
        JPanel panel_akcji = new JPanel();
        panel_akcji.add(przycisk_przypisz_zadanie);
        panel_akcji.add(przycisk_zakoncz_zadanie);
        panel_akcji.add(przycisk_generuj_raport);

        JPanel panel_przypisanych_zadan = new JPanel(new BorderLayout());
        panel_przypisanych_zadan.setBorder(BorderFactory.createTitledBorder("Przypisane Zadania"));
        model_tabeli_przypisanych_zadan = new DefaultTableModel(new String[]{"Użytkownik", "Zadanie"}, 0);
        tabela_przypisanych_zadan = new JTable(model_tabeli_przypisanych_zadan);
        panel_przypisanych_zadan.add(new JScrollPane(tabela_przypisanych_zadan), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panel_czlonkow_zespolu, BorderLayout.WEST);
        add(panel_zadan, BorderLayout.CENTER);
        add(panel_akcji, BorderLayout.SOUTH);
        add(panel_przypisanych_zadan, BorderLayout.EAST);
    }
    private void aktualizuj_etykiety_przypisane_do() {
        Zadania wybrane_zadanie = lista_zadan.getSelectedValue();
        if (wybrane_zadanie != null && wybrane_zadanie.getPrzypisany_Czlonek() != null) {
            etykiety_przypisane_do.setText("Przypisane do: " + wybrane_zadanie.getPrzypisany_Czlonek().getNazwa());
        } else {
            etykiety_przypisane_do.setText(" ");
        }
    }
    private void odswiez_tabele_przypisanych_zadan() {
        model_tabeli_przypisanych_zadan.setRowCount(0);
        for (Zadania zadanie : zadania) {
            if (zadanie.getPrzypisany_Czlonek() != null) {
                Object[] wiersz = {zadanie.getPrzypisany_Czlonek().getNazwa(), zadanie.getOpis()};
                model_tabeli_przypisanych_zadan.addRow(wiersz);
            }
        }
    }
    private void generuj_raport() {
        StringBuilder raport = new StringBuilder();
        raport.append("Raport zadań:\n\n");
        for (Zadania zadanie : zadania) {
            raport.append(zadanie.getOpis())
                    .append(" - ")
                    .append(zadanie.getStatus())
                    .append(" - Przypisane do: ")
                    .append(zadanie.getPrzypisany_Czlonek() != null ? zadanie.getPrzypisany_Czlonek().getNazwa() : "Brak")
                    .append("\n");
        }
        JOptionPane.showMessageDialog(this, raport.toString(), "Raport zadań", JOptionPane.INFORMATION_MESSAGE);
        try (FileWriter writer = new FileWriter("raport_zadan.txt")) {
            writer.write(raport.toString());
            JOptionPane.showMessageDialog(this, "Raport zapisany do pliku raport_zadan.txt", "Raport zadań", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisywania raportu", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Zarzadzanie_Zadaniami_GUI().setVisible(true);
            }
        });
    }
}
