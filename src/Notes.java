import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;

//document listener per vedere gli aggiornamenti dentro il text
//windows listener per vedere quando esce
    //--> ha tanti metodi anche che non servono --> c'è versione dove non devi implementare tutto --> classe anonima
class Notes extends JFrame implements ActionListener, DocumentListener {

    //ctrl+alt+f mi fa diventare attributi da locali
    private JMenuItem mniNew = null;
    private JMenuItem mniSave = null;
    private JMenuItem mniOpen = null;
    private JMenuItem mniExit = null;
    private JMenuItem mniAbout = null;
    private JTextArea txt = null;

    private boolean isSaved = true; // controlla se a salvato prima di uscire

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {       //classe anonima
            @Override
            public void run() {
                new Notes();
            }
        });

    }

    private Notes(){ //nessun'altro deve chiamarlo al di fuori di qua
        setTitle("Blocco Note");
        setSize(1000,600);
        setLocationRelativeTo(null);
        //non WindowConstants.EXIT_ON_CLOSE perché deve controllare se ha salvato
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        Image icon = Toolkit.getDefaultToolkit().getImage("/home/pollo/Desktop/informatica/codici.java/Blocco Note/icon.png");
        setIconImage(icon);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JMenuBar mnbNorth = new JMenuBar();

        JMenu mnuFile = new JMenu( "File");

        //item
        mniNew = new JMenuItem("New");
        mnuFile.add(mniNew);
        mniNew.addActionListener(this);

        mniOpen = new JMenuItem("Open...");
        mnuFile.add(mniOpen);
        mniOpen.addActionListener(this);

        mniSave = new JMenuItem("Save as...");
        mnuFile.add(mniSave);
        mniSave.addActionListener(this);

        mnuFile.addSeparator(); // fa una riga per devidere

        mniExit = new JMenuItem("Exit");
        mniExit.addActionListener(this);
        mnuFile.add(mniExit);

        JMenu mnuHelp= new JMenu("Help");
        mniAbout = new JMenuItem("About");
        mnuHelp.add(mniAbout);
        mniAbout.addActionListener(this);


        mnbNorth.add(mnuFile);
        mnbNorth.add(mnuHelp);

        txt = new JTextArea();
        txt.setLineWrap(true); //va a capo e non supera il limite della pagina
        txt.setWrapStyleWord(true);

        //mette txt Area con la scroll bar
        JScrollPane scrollPane = new JScrollPane(txt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);

        //setJMenuBar(mnbNorth) mette su una striscia un pochino più alta
        add(mnbNorth, BorderLayout.NORTH);

        //aggiunge document listener a text
        txt.getDocument().addDocumentListener(this);


        //Non posso estendere e uso classe anonima
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                commandExit();
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == mniNew) {
             commandNew();
        }

        if(e.getSource() == mniOpen) {
            commandOpen();
        }

        if(e.getSource() == mniSave) {
            commandSave();
        }

        if(e.getSource() == mniExit) {
            commandExit();
        }

        if(e.getSource() == mniAbout) {
            commandAbout();
        }
    }

    private void commandAbout() {
        //fa comparire una finestrella messaggio
        JOptionPane.showMessageDialog(this, "Giulia Vadelli", "Autore",JOptionPane.INFORMATION_MESSAGE);
    }
    private void commandOpen(){
        //scegliere un file
        JFileChooser fc = new JFileChooser("./");

        //tra parentesi chide il component dialog cisò dove mettere la schemata
        int result = fc.showOpenDialog(this); // mette dove stiamo lavorando --> dove si apre
        // result indica se ha aperto o chiuso o che ha fatto

        //ha premuto ok
        if(result != JFileChooser.APPROVE_OPTION) return ;

        try{
            File file = fc.getSelectedFile(); //fa la catch
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            txt.setText("");
            while ((line = br.readLine()) != null) {
                txt.append(line + "\n");
            }
            br.close();
            setTitle(file.getName() + "- NotePad");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void commandNew(){
        setTitle("Unnamed.txt - NotePad");
        txt.setText(null);
        isSaved = true;
    }
    private void commandSave(){
        JFileChooser fc = new JFileChooser("./");
        int result = fc.showSaveDialog(this);

        if(result != JFileChooser.APPROVE_OPTION) return;

        String fileName = (fc.getSelectedFile().getAbsolutePath());

        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);
            txt.write(fw);
            setTitle(fc.getSelectedFile().getName() + " - NotePad");
            isSaved = true;
            fw.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    private void commandExit(){
        System.exit(0);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {isSaved = false;}

    @Override
    public void removeUpdate(DocumentEvent e) {isSaved = false;}

    @Override
    public void changedUpdate(DocumentEvent e) {isSaved = false;}
}


//document listener per vedere gli aggiornamenti dentro il text
//windows listener per vedere quando esce