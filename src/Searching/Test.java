/**
 * Here we make the GUI and call the functions from other classes,
 * which are used to implement the searching.
 */

package Searching;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Test implements ActionListener {
    /*making objects for GUI*/

    private JFrame frame = new JFrame("Dialog box. Searching");
    private JLabel jLabel = new JLabel("Searching");
    private JLabel choose_text_document = new JLabel("Choose a directory");
    private JLabel subtext = new JLabel("Subtext");
    private JLabel algorithm_to_use = new JLabel("Algorithm to use");
    private JFileChooser fileChooser = new JFileChooser();
    private JTextField textField = new JTextField();
    private JTextField textField1 = new JTextField();
    private String[] algorithms = {"SimpleTextSearch", "KnuthMorrisPratt", "RabinKarp", "BoyerMoore"};
    private JComboBox jComboBox = new JComboBox(algorithms);

    private ButtonGroup buttonGroup = new ButtonGroup();
    private JButton choose = new JButton("...");
    private JButton search = new JButton("SEARCH");
    private JButton reset = new JButton("RESET");

    private JLabel countOfFoundFiles = new JLabel();

    private JTextArea theResult;
    private JScrollPane scrollPane;

    private JLabel notSelected = new JLabel("");
    private JLabel theTime = new JLabel("");
    private File folder = new File("");

    private Test() {
        /*giving properties to the GUI objects*/

        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setSize(610, 700);
        jLabel.setBounds(200, 40, 220, 20);
        jLabel.setForeground(Color.BLACK);
        jLabel.setFont(new Font("Copperplate Gothic Bold", Font.ITALIC, 22));
        choose_text_document.setBounds(130, 100, 230, 45);
        subtext.setBounds(130, 200, 230, 45);
        algorithm_to_use.setBounds(130, 300, 230, 45);
        choose_text_document.setFont(new Font("Copperplate Gothic Bold", Font.ITALIC, 12));
        subtext.setFont(new Font("Copperplate Gothic Bold", Font.ITALIC, 13));
        algorithm_to_use.setFont(new Font("Copperplate Gothic Bold", Font.ITALIC, 12));
        frame.add(jLabel);
        frame.add(choose_text_document);
        frame.add(subtext);
        frame.add(algorithm_to_use);
        textField.setBounds(290, 100, 200, 50);
        frame.add(textField);
        textField1.setBounds(290, 200, 200, 50);
        frame.add(textField1);

        jComboBox.setBounds(290, 300, 180, 40);
        jComboBox.setRenderer(new MyComboBoxRenderer("Choose"));
        jComboBox.setSelectedIndex(-1);
        frame.add(jComboBox);

        search.setBounds(200, 390, 100, 45);
        frame.add(search);
        search.setFont(new Font("Copperplate Gothic Bold", Font.ITALIC, 12));
        search.setForeground(Color.darkGray);
        search.addActionListener(this);

        choose.setBounds(500, 120, 45, 30);
        frame.add(choose);
        choose.addActionListener(this);

        reset.setBounds(440, 400, 100, 30);
        frame.add(reset);
        reset.addActionListener(this);

        countOfFoundFiles.setBounds(40, 430, 400, 20);
        countOfFoundFiles.setFont(new Font("Copperplate Gothic Bold", Font.ITALIC, 12));
        frame.add(countOfFoundFiles);

        theResult = new JTextArea(8, 50);
        theResult.setBounds(40, 450, 520, 140);
        theResult.setFont(new Font("Copperplate Gothic Bold", Font.ITALIC, 12));
        theResult.setForeground(Color.BLACK);
        theResult.setEditable(false);
        theResult.setLineWrap(true);
        scrollPane = new JScrollPane(theResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(40, 450, 520, 140);


        theTime.setBounds(40, 590, 500, 50);
        theTime.setFont(new Font("Copperplate Gothic Bold", Font.ITALIC, 13));
        theTime.setForeground(Color.BLACK);
        frame.add(theTime);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        notSelected.setForeground(Color.RED);
        frame.add(notSelected);
        notSelected.setBounds(290, 330, 140, 50);
    }


    public static void main(String[] args) {
        Test myInput = new Test();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == choose) {
            frame.add(fileChooser);
            fileChooser.showSaveDialog(null);
            textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            folder = new File(fileChooser.getSelectedFile().getAbsolutePath());
        }
        int count = 0;            //to count the number of found files which contain the subtext
        String stringToAdd = "";  //to add all the files which contain the subtext

        /*by clicking RESET button, we should give the former state to all the boxes*/
        if (e.getSource() == reset) {
            textField.setText("");
            textField1.setText("");
            buttonGroup.clearSelection();
            theResult.setVisible(false);
            textField.setBorder(new LineBorder(Color.LIGHT_GRAY));
            textField1.setBorder(new LineBorder(Color.LIGHT_GRAY));
            notSelected.setText("");
            theTime.setText("");
            jComboBox.setSelectedIndex(-1);
            count = 0;
            countOfFoundFiles.setText("");
            stringToAdd = "";
            theResult.setText("");
            scrollPane.setVisible(false);
        }

        /*by clicking SEARCH button, firstly we should check whether all the boxes
         * are filled, and whether they are properly filled
         */
        if (e.getSource() == search) {
            if (textField.getText().equals("") || !folder.exists()) {

                /*if not proper, then the border color will become red*/
                textField.setBorder(new LineBorder(Color.RED));
            } else {
                textField.setBorder(new LineBorder(Color.LIGHT_GRAY));
            }
            if (textField1.getText().equals("")) {
                textField1.setBorder(new LineBorder(Color.RED));
            } else {
                textField1.setBorder(new LineBorder(Color.LIGHT_GRAY));
            }

            /*variable to keep the current time, which will be initialized later*/
            long start;

            /*check whether any algorithm is chosen or not*/
            if (jComboBox.getSelectedIndex() == -1) {
                notSelected.setText("Select one");
            }

            /*else if everything is fine, then start searching*/
            else if (!(textField.getText().equals("")) && !(textField1.getText().equals("")) && folder.exists()) {
                notSelected.setText("");
                String ourText = "";
                int result = 0;
                start = System.currentTimeMillis();  //current time for starting

                /*an arrayList to keep all the files and directories from the chosen directory*/
                ArrayList<File> listOfFilesAndDirectories = new ArrayList<>();

                /*final files list, which contains all the files from the chosen directory and
                 * also from its sub-directories
                 */
                List<File> finalFilesList = new ArrayList<>();

                /*getting that list of files from the method. listOnlyFiles*/
                finalFilesList = listOnlyFiles(folder.getAbsolutePath(), listOfFilesAndDirectories);

                /*going through the all files*/
                for (File file : finalFilesList) {
                    BufferedReader r = null;
                    try {
                        r = new BufferedReader(new FileReader(file));
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    String line = null;
                    while (true) {
                        try {
                            if ((line = r.readLine()) == null) break;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        ourText += line;
                    }

                    /*get the chosen item from the combo box and call the appropriate
                     * function for it
                     */
                    if (jComboBox.getItemAt(jComboBox.getSelectedIndex()).equals("SimpleTextSearch")) {
                        result = SimpleTextSearch.simpleTextSearch(textField1.getText(), ourText);
                        if (result > 0) {
                            stringToAdd += file.getAbsolutePath() + "\r\n";  //get the path of the found file
                            count++;                                         //iterating the number of found files
                        }
                    } else if (jComboBox.getItemAt(jComboBox.getSelectedIndex()).equals("KnuthMorrisPratt")) {
                        result = KnuthMorrisPratt.KnuthMorrisPrattSearch(textField1.getText(), ourText);
                        if (result > 0) {
                            stringToAdd += file.getAbsolutePath() + "\r\n";
                            count++;
                        }
                    } else if (jComboBox.getItemAt(jComboBox.getSelectedIndex()).equals("RabinKarp")) {
                        result = RabinKarp.RabinKarpMethod(textField1.getText(), ourText);
                        if (result > 0) {
                            stringToAdd += file.getAbsolutePath() + "\r\n";
                            count++;
                        }
                    } else if (jComboBox.getItemAt(jComboBox.getSelectedIndex()).equals("BoyerMoore")) {
                        result = BoyerMoore.BoyerMooreHorspoolSimpleSearch(textField1.getText(), ourText);
                        if (result > 0) {
                            stringToAdd += file.getAbsolutePath() + "\r\n";
                            count++;
                        }
                    }
                    ourText = "";
                }

                //add the text area where all the found file paths will be written
                scrollPane.setVisible(true);
                theResult.setVisible(true);
                frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
                frame.add(scrollPane);

                /*if no file contains the subtext, there will be a message,
                 * telling not found
                 */
                if (stringToAdd.equals("")) {
                    theResult.setText("");
                    countOfFoundFiles.setText("Nowhere found");
                }

                /*if found files, then it will give their number and their paths*/
                else {
                    countOfFoundFiles.setText("Found " + count + " files");
                    theResult.setText(stringToAdd);
                }

                long now = System.currentTimeMillis();  //current time, after searching

                /*time difference between now and the start of searching*/
                theTime.setText("Time: " + (now - start) + " mls");

                result = 0;
            } else {
                notSelected.setText("");
            }
        }
    }

    /*method for finding all the files from the directory and its
     * sub-directories, which is done recursively
     */
    public static java.util.List<File> listOnlyFiles(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {

                /*if found file, then add it to the files List*/
                if (file.isFile() && (file.getName().endsWith(".txt") || file.getName().endsWith(".docx"))) {
                    files.add(file);
                }

                /*if a directory, then call again this function*/
                else if (file.isDirectory()) {
                    listOnlyFiles(file.getAbsolutePath(), files);
                }
            }
        }
        return files; //returning the files list, which are all only files
    }
}

/*method for choosing a directory in the GUI*/
class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
    private String _title;

    MyComboBoxRenderer(String title) {
        _title = title;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        if (index == -1 && value == null) setText(_title);
        else setText(value.toString());
        return this;
    }
}
