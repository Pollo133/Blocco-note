import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;

class Notes extends JFrame implements ActionListener {
    private JMenuItem mniNew = null;
    private JMenuItem mniSave = null;
    private JMenuItem mniOpen = null;
    private JMenuItem mniExit = null;
    private JMenuItem mniAbout = null;

    JTextArea txt = null;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {       //classe anonima
            @Override
            public void run() {
                new Notes();
            }
        });

    }

    public Notes(){
        setTitle("Blocco Note");
        setSize(400,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

        mniSave = new JMenuItem("Save");
        mnuFile.add(mniSave);
        mniSave.addActionListener(this);

        mnuFile.addSeparator();

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
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);
        add(mnbNorth, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == mniNew) {
            setTitle("Unnamed.txt");
            txt.setText(null);
        }

        if(e.getSource() == mniOpen) {
            open();
        }

        if(e.getSource() == mniSave) {
            JFileChooser fc = new JFileChooser("./");
            int result = fc.showSaveDialog(this);

            if(result != JFileChooser.APPROVE_OPTION) return;

            String fileName = (fc.getSelectedFile().getAbsolutePath());

            FileWriter fw = null;
            try {
                fw = new FileWriter(fileName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                txt.write(fw);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource() == mniExit) {
            System.exit(0);
        }

        if(e.getSource() == mniAbout) {
            //fa comparire una finestrella messaggio
            JOptionPane.showMessageDialog(this, "Giulia Vadelli", "Autore",JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void open(){
        //scegliere un file
        JFileChooser fc = new JFileChooser("./");

        //tra parentesi chide il component dialog cisò dove mettere la schemata
        int result = fc.showOpenDialog(this); // mette dove stiamo lavorando
        // result indica se ha aperto o chiuso o che ha fatto

        //ha premuto ok
        if(result != JFileChooser.APPROVE_OPTION) return ;

        try{
            File file = fc.getSelectedFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            txt.setText("");
            while ((line = br.readLine()) != null) {
                txt.append(line + "\n");
            }
            br.close();
            setTitle(file.getName() + "- NotePad");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

