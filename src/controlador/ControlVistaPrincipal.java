package controlador;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import vista.TextPrompt;
import vista.VistaPrincipal;

public class ControlVistaPrincipal {

    private VistaPrincipal vista;

    /*here we declare a class that incorporate the interface Highlighter 
        and a method that allow paint or underline*/
    Highlighter.HighlightPainter myHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);

    public ControlVistaPrincipal(VistaPrincipal vista) {
        this.vista = vista;

        //set place holder
        TextPrompt holder1 = new TextPrompt("Write a word", vista.getTxt_word());
        TextPrompt holder2 = new TextPrompt("Insert a text or upload a text file", vista.getJtextArea1());
        holder2.setHorizontalAlignment(JLabel.CENTER);

        vista.setVisible(true);
        vista.setTitle("Highlight word");
        vista.setResizable(false);
        vista.setLocationRelativeTo(null);

    }

    public void functionality() {

        vista.getBt_upload().addActionListener(l -> searchfile());
        vista.getBt_search().addActionListener(l -> highlight(vista.getJtextArea1(), vista.getTxt_word().getText()));

    }

    private void searchfile() {

        //this open a window to search any file
        JFileChooser file = new JFileChooser();
        //filter according to file type
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "TXT");
        file.setFileFilter(filter);
        //set a title in the window
        file.setDialogTitle("Buscador de archivos");
        int seleccion = file.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {

            try {
                FileReader reader = new FileReader(file.getSelectedFile().getPath()); // this read the file that we chose
                BufferedReader br = new BufferedReader(reader);//this save the entry of a reader, is used to improve efficiency

                String line = "";
                while (line != null) {

                    try {

                        line = br.readLine();

                        if (line != null) {

                            vista.getJtextArea1().append(line + "\n");

                        }

                    } catch (IOException ex) {
                        Logger.getLogger(ControlVistaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ControlVistaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void highlight(JTextComponent textComp, String pattern) {

        removeHighlights(textComp);
        
        Highlighter hg = textComp.getHighlighter();
        try {
            String text = textComp.getText(0, textComp.getText().length());

            int pos = 0;
            while ((pos = text.toUpperCase().indexOf(pattern.toUpperCase(), pos)) >= 0) {

                hg.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                pos += pattern.length();

            }

        } catch (BadLocationException ex) {
            Logger.getLogger(ControlVistaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void removeHighlights(JTextComponent textComp) {

        Highlighter hg = textComp.getHighlighter();
        Highlighter.Highlight[] hgs = hg.getHighlights();

        for (int i = 0; i < hgs.length; i++) {

            if (hgs[i].getPainter() instanceof DefaultHighlighter.HighlightPainter) {

                hg.removeHighlight(hgs[i]);

            }
        }
    }

}
