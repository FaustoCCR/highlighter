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
import javax.swing.JOptionPane;
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
        vista.getTxt_word().addActionListener(l -> highlight(vista.getJtextArea1(), vista.getTxt_word().getText()));
        vista.getBt_search().addActionListener((e) -> {

            highlight(vista.getJtextArea1(), vista.getTxt_word().getText());

            /*if we want that we our app found out the word without its is into of other 
                we can put...
                highlight(vista.getJtextArea1(), ""+vista.getTxt_word().getText()+"");
             */
        });

    }

    private boolean checkFields() {

        boolean answer = true;

        if (vista.getTxt_word().getText().isEmpty()) {

            JOptionPane.showMessageDialog(vista, "Enter a word");
            answer = false;
        }
        if (vista.getJtextArea1().getText().isEmpty()) {

            JOptionPane.showMessageDialog(vista, "Insert a text or upload a text file");
            answer = false;
        }

        return answer;

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
                FileReader reader = new FileReader(file.getSelectedFile().getPath()); //this read the file that we chose
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

        if (checkFields()) {

            removeHighlights(textComp);

            Highlighter hg = textComp.getHighlighter();//this get the object that highlight
            try {
                String text = textComp.getText(0, textComp.getText().length());//get the text inside the component --> JTextArea

                int pos = 0;

                while ((pos = text.toUpperCase().indexOf(pattern.toUpperCase(), pos)) >= 0) {

                    /* the loop while set as value, the index of the searched word(the word, from index) into a variable*/
                    hg.addHighlight(pos, pos + pattern.length(), myHighlightPainter);

                    /*this highlight the word from its beginning to last character
                pos --> the beginning of the word 
                pos + pattern.lenght --> this add the position and its length()--> to the last position of the string*/
                    pos += pattern.length();/*--> we save the final position of the string, for later do the search from that position and not at the beginning*/

                }

            } catch (BadLocationException ex) {
                Logger.getLogger(ControlVistaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private void removeHighlights(JTextComponent textComp) {

        Highlighter hg = textComp.getHighlighter();//we get the object that highlight
        Highlighter.Highlight[] hgs = hg.getHighlights();//returns within a vector the words that were highlighted

        for (int i = 0; i < hgs.length; i++) {
            //we go through the vector
            if (hgs[i].getPainter() instanceof DefaultHighlighter.HighlightPainter) {

                //we analize if the painter belongs to an instance of the class DefaultHighlighter.HighlightPainter
                hg.removeHighlight(hgs[i]);//we remove the highlighting of the words that were previously highlighted

            }
        }
    }

}
