package InterfacciaGrafica;

import mainRubrica.rubrica; 

import javax.swing.*;
import javax.swing.text.AbstractDocument; 
import javax.swing.text.AttributeSet;     
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ArrayList;

public class gui extends JFrame {

    // --Componenti principali dell'interfaccia
    private JList<String> contactList;
    private DefaultListModel<String> listModel;
    private JScrollPane scrollPane;

    // --Pannello per Aggiungere Contatti
    private JPanel addPanel;
    private JTextField nameField;
    private JTextField numberField;
    private JButton addButton;

    // --Pannello per Cerca/Elimina
    private JPanel searchPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton deleteButton;

    // --Pannello per Azioni Generali
    private JPanel utilPanel;
    private JButton showAllButton;
    private JButton clearAllButton;

    // --Etichetta di stato
    private JLabel statusLabel;

    
    public gui() {
        
        rubrica.creaRubrica();

        // --Impostazioni finestra
        setTitle("Rubrica");
        setSize(500, 500); // Dimensioni finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //-- Lista Contatti
        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);
        contactList.setFont(new Font("Arial", Font.PLAIN, 14));
        scrollPane = new JScrollPane(contactList);

        add(scrollPane, BorderLayout.CENTER);

        //-- Pannello di Controllo
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        //-- Pannello Aggiungi Contatto
        addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addPanel.setBorder(BorderFactory.createTitledBorder("Aggiungi Contatto"));
        nameField = new JTextField(10);
        numberField = new JTextField(10);
        ((AbstractDocument) numberField.getDocument()).setDocumentFilter(new DocumentFilter() {
            
            //-- Metodo per le operazioni di modifica
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                // 1. Calcola quale sarebbe la stringa risultante dopo la modifica
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String futureText = currentText.substring(0, offset) + (text == null ? "" : text) + currentText.substring(offset + length);

                // 2. Applica le regole di validazione
                if (isValid(futureText)) {
                    super.replace(fb, offset, length, text, attrs); // Applica la modifica
                }
                // Altrimenti, non fare nulla (la modifica viene ignorata)
            }

            //-- Inserimento
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                replace(fb, offset, 0, string, attr);
            }

           //-- Controllo sulla lunghezza del numero
            private boolean isValid(String text) {
                return text.matches("\\d*") && text.length() <= 10;
            }
        });
        addButton = new JButton("Aggiungi");
        addPanel.add(new JLabel("Nome:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Numero:"));
        addPanel.add(numberField);
        addPanel.add(addButton);

        //--  Pannello Cerca/Elimina
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Cerca / Elimina per Prefisso"));
        searchField = new JTextField(15);
        searchButton = new JButton("Cerca");
        deleteButton = new JButton("Elimina");
        searchPanel.add(new JLabel("Testo:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(deleteButton);

        //-- Pannello Utility
        utilPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        showAllButton = new JButton("Mostra Tutti");
        clearAllButton = new JButton("Svuota Rubrica");
        utilPanel.add(showAllButton);
        utilPanel.add(clearAllButton);

        
        statusLabel = new JLabel("Rubrica inizializzata. 0/" + rubrica.MAX_DIM + " contatti.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //-- Sotto-pannelli al pannello di controllo principale
        controlPanel.add(addPanel);
        controlPanel.add(searchPanel);
        controlPanel.add(utilPanel);
        controlPanel.add(statusLabel);

        //-- Pannello di controllo
        add(controlPanel, BorderLayout.SOUTH);

        //-- 5. Collegamento Azioni
        setupActionListeners();

        //-- 6. Aggiorna la lista
        refreshContactList();
        setVisible(true);
    }

    //-- Collegamento bottoni
    private void setupActionListeners() {
    	
        //-- Bottone Aggiungi
        addButton.addActionListener(e -> addContact());

        //-- Bottone Cerca
        searchButton.addActionListener(e -> searchContacts());

        //-- Bottone Elimina
        deleteButton.addActionListener(e -> deleteContacts());

        //-- Bottone Mostra Tutti
        showAllButton.addActionListener(e -> {
            searchField.setText(""); // Svuota il campo di ricerca
            refreshContactList(); // Ricarica tutti i contatti
        });

        //-- Bottone Svuota Rubrica
        clearAllButton.addActionListener(e -> clearAllContacts());
    }

    //-- Metodi di azione --
    
    //-- Aggiunta di un contatto
    private void addContact() {
        String name = nameField.getText().trim();
        String number = numberField.getText().trim();

        if (name.isEmpty() || number.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nome e Numero non possono essere vuoti.",
                    "Errore Inserimento",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String contactString = name + "=" + number;
        int result = rubrica.aggiungiContatto(contactString);

        // Interpreta il codice di ritorno dalla classe rubrica
        switch (result) {
            case 1: // Successo
                JOptionPane.showMessageDialog(this,
                        "Contatto '" + name + "' aggiunto con successo!",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
                numberField.setText("");
                break;
            case 0: // Già presente
                JOptionPane.showMessageDialog(this,
                        "Il contatto '" + contactString + "' è già presente.",
                        "Errore",
                        JOptionPane.WARNING_MESSAGE);
                break;
            case -1: // Rubrica piena
                JOptionPane.showMessageDialog(this,
                        "La rubrica è piena (Max " + rubrica.MAX_DIM + " contatti).",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                break;
        }

        refreshContactList(); // Aggiorna la lista
    }

    //-- Ricerca di un conttatto
    private void searchContacts() {
        String prefix = searchField.getText().trim();
        ArrayList<String> results = rubrica.ricercaContatto(prefix);

        //mostra il risultato dopo un'azione 
        listModel.clear();
        for (String contact : results) {
            listModel.addElement(contact);
        }

        statusLabel.setText("Trovati " + results.size() + " contatti per '" + prefix + "'.");
    }

    //-- Cancella contatto
    private void deleteContacts() {
        String prefix = searchField.getText().trim();
        if (prefix.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Per eliminare tutti i contatti, usa 'Svuota Rubrica'.\n" +
                    "Altrimenti, inserisci un prefisso da eliminare.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Chiedi conferma
        int choice = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare tutti i contatti che iniziano con '" + prefix + "'?",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            boolean result = rubrica.cancellaContatto(prefix);
            if (result) {
                JOptionPane.showMessageDialog(this,
                        "Contatti eliminati con successo.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Nessun contatto trovato da eliminare con prefisso '" + prefix + "'.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            refreshContactList(); // Aggiorna la lista
        }
    }

    //-- Cancella l'intera rubrica
    private void clearAllContacts() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler svuotare l'intera rubrica?",
                "Conferma Svuota",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            rubrica.svuotaRubrica();
            refreshContactList(); // Aggiorna la lista
            JOptionPane.showMessageDialog(this,
                    "Rubrica svuotata.",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
    private void refreshContactList() {
        // ricercaContatto("") restituisce tutti gli elementi
        ArrayList<String> allContacts = rubrica.ricercaContatto("");
        
        listModel.clear(); // Pulisci la lista visualizzata
        for (String contact : allContacts) {
            listModel.addElement(contact); // Aggiungi gli elementi aggiornati
        }
        
        updateStatusLabel(); // Aggiorna il contatore
    }

   
    private void updateStatusLabel() {
        int count = rubrica.numElem();
        statusLabel.setText("Contatti: " + count + "/" + rubrica.MAX_DIM);
    }


    //-- Avviamento GUI
    public static void main(String[] args) {
      
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new gui();
            }
        });
    }
}