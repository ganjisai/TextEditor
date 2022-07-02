package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    private JTextArea textArea;
    private JTextField searchField;
    private JCheckBox useRegexCheckBox;
    private JFileChooser fileChooser;
    private List<MatchResult> searchMatchers;
    private boolean searchFound;
    private int currentFoundMatch = 0;

    public TextEditor() {
        setTitle("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        setLayout(new BorderLayout(20, 20));
        initMenu();
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = initFileMenu();
        menuBar.add(fileMenu);

        JMenu searchMenu = initSearchMenu();
        menuBar.add(searchMenu);

        setJMenuBar(menuBar);
    }

    private JMenu initFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");

        JMenuItem loadMenuItem = initOpenMenuItem();
        fileMenu.add(loadMenuItem);

        JMenuItem saveMenuItem = initSaveMenuItem();
        fileMenu.add(saveMenuItem);

        fileMenu.addSeparator();

        JMenuItem exitMenuItem = initExitMenuItem();
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private JMenuItem initOpenMenuItem() {
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setName("MenuOpen");
        openMenuItem.addActionListener(getLoadListener());

        return openMenuItem;
    }

    private JMenuItem initSaveMenuItem() {
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        saveMenuItem.addActionListener(getSaveListener());

        return saveMenuItem;
    }

    private JMenuItem initExitMenuItem() {
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setName("MenuExit");
        exitMenuItem.addActionListener(getExitListener());

        return exitMenuItem;
    }

    private JMenu initSearchMenu() {
        JMenu searchMenu = new JMenu("Search");
        searchMenu.setName("MenuSearch");

        JMenuItem startSearchMenuItem = initStartSearchMenuItem();
        searchMenu.add(startSearchMenuItem);

        JMenuItem previousMatchMenuItem = initPreviousMatchMenuItem();
        searchMenu.add(previousMatchMenuItem);

        JMenuItem nextMatchMenuItem = initNextMatchMenuItem();
        searchMenu.add(nextMatchMenuItem);

        JMenuItem useRegexMenuItem = initUseRegexMenuItem();
        searchMenu.add(useRegexMenuItem);

        return searchMenu;
    }

    private JMenuItem initStartSearchMenuItem() {
        JMenuItem startSearchMenuItem = new JMenuItem("Start search");
        startSearchMenuItem.setName("MenuStartSearch");
        startSearchMenuItem.addActionListener(getStartSearchListener());

        return startSearchMenuItem;
    }

    private JMenuItem initPreviousMatchMenuItem() {
        JMenuItem previousMatchMenuItem = new JMenuItem("Previous match");
        previousMatchMenuItem.setName("MenuPreviousMatch");
        previousMatchMenuItem.addActionListener(getPreviousMatchListener());

        return previousMatchMenuItem;
    }

    private JMenuItem initNextMatchMenuItem() {
        JMenuItem nextSearchMenuItem = new JMenuItem("Next match");
        nextSearchMenuItem.setName("MenuNextMatch");
        nextSearchMenuItem.addActionListener(getNextMatchListener());

        return nextSearchMenuItem;
    }

    private JMenuItem initUseRegexMenuItem() {
        JMenuItem useRegexMenuItem = new JMenuItem("Use regular expression");
        useRegexMenuItem.setName("MenuUseRegex");
        useRegexMenuItem.addActionListener(e -> useRegexCheckBox.setSelected(!useRegexCheckBox.isSelected()));
        return useRegexMenuItem;
    }

    private void initComponents() {
        JPanel topPanel = new JPanel();

        Component openButton = initOpenButton();
        topPanel.add(openButton);

        Component saveButton = initSaveButton();
        topPanel.add(saveButton);

        Component searchField = initSearchField();
        topPanel.add(searchField);

        Component searchButton = initSearchButton();
        topPanel.add(searchButton);

        Component previousButton = initPreviousButton();
        topPanel.add(previousButton);

        Component nextButton = initNextButton();
        topPanel.add(nextButton);

        Component regexCheckBox = initRegexCheckBox();
        topPanel.add(regexCheckBox);

        add(topPanel, BorderLayout.NORTH);

        Component textArea = initTextArea();
        add(textArea, BorderLayout.CENTER);

        Component fileChooser = initFileChooser();
        add(fileChooser, BorderLayout.SOUTH);
    }

    private Component initOpenButton() {
        JButton openButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("resources/folder.png")));
        openButton.setName("OpenButton");
        openButton.setPreferredSize(new Dimension(32, 32));
        openButton.addActionListener(getLoadListener());
        return openButton;
    }

    private Component initSaveButton() {
        JButton saveButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("resources/floppy-disk.png")));
        saveButton.setName("SaveButton");
        saveButton.setPreferredSize(new Dimension(32, 32));
        saveButton.addActionListener(getSaveListener());
        return saveButton;
    }

    private Component initSearchField() {
        JTextField searchField = new JTextField();
        searchField.setName("SearchField");
        searchField.setPreferredSize(new Dimension(120, 32));
        this.searchField = searchField;
        return searchField;
    }

    private Component initSearchButton() {
        JButton searchButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("resources/search.png")));
        searchButton.setName("StartSearchButton");
        searchButton.setPreferredSize(new Dimension(32, 32));
        searchButton.addActionListener(getStartSearchListener());
        return searchButton;
    }

    private Component initPreviousButton() {
        JButton previousButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("resources/chevron.png")));
        previousButton.setName("PreviousMatchButton");
        previousButton.setPreferredSize(new Dimension(32, 32));
        previousButton.addActionListener(getPreviousMatchListener());
        return previousButton;
    }

    private Component initNextButton() {
        JButton nextButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("resources/right-chevron.png")));
        nextButton.setName("NextMatchButton");
        nextButton.setPreferredSize(new Dimension(32, 32));
        nextButton.addActionListener(getNextMatchListener());
        return nextButton;
    }

    private Component initTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setSize(150, 150);
        this.textArea = textArea;
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setName("ScrollPane");
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        return scrollPane;
    }

    private Component initRegexCheckBox() {
        JCheckBox regexCheckBox = new JCheckBox("Use regex");
        regexCheckBox.setName("UseRegexCheckBox");
        this.useRegexCheckBox = regexCheckBox;
        return regexCheckBox;
    }

    private Component initFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setName("FileChooser");
        fileChooser.setVisible(false);
        this.fileChooser = fileChooser;
        return fileChooser;
    }
    //Listeners

    private ActionListener getSaveListener() {
        return e -> {
            fileChooser.setVisible(true);
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String text = textArea.getText();
                save(file, text);
            }
        };
    }

    private ActionListener getLoadListener() {
        return e -> {
            fileChooser.setVisible(true);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String text = load(file);
                textArea.setText(text);
            }
        };
    }

    private ActionListener getExitListener() {
        return e -> System.exit(0);
    }

    private ActionListener getStartSearchListener() {
        return e -> {
            //new SearchWorker().execute();
            startSearch();
        };
    }

    private ActionListener getNextMatchListener() {
        return e -> nextMatch();
    }

    private ActionListener getPreviousMatchListener() {
        return e -> previousMatch();
    }

    //Helpers

    private void save(File file, String text) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String load(File file) {
        StringBuilder builder = new StringBuilder();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                char[] buffer = new char[1024];
                int count;
                while ((count = reader.read(buffer)) != -1) {
                    String text = new String(buffer, 0, count);
                    builder.append(text);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    void startSearch() {
        String regex = searchField.getText();

        Pattern pattern = Pattern.compile(regex);
        Matcher searchMatcher = pattern.matcher(textArea.getText());
        //reset match results
        searchMatchers = new ArrayList<>();
        currentFoundMatch = 0;
        searchFound = false;

        while (searchMatcher.find()) {
            searchMatchers.add(searchMatcher.toMatchResult());
            searchFound = true;
        }

        for (MatchResult result : searchMatchers) {
            System.out.println(result);
        }

        if (searchFound) {
            highlightMatchResult(currentFoundMatch);
        }
    }

    private void nextMatch() {
        if (searchFound) {
            if (currentFoundMatch != searchMatchers.size() - 1) {
                currentFoundMatch++;
                highlightMatchResult(currentFoundMatch);
            } else {
                currentFoundMatch = 0;
                highlightMatchResult(currentFoundMatch);
            }
        }
    }

    private void previousMatch() {
        if (searchFound) {
            if (currentFoundMatch != 0) {
                currentFoundMatch--;
                highlightMatchResult(currentFoundMatch);
            } else {
                currentFoundMatch = searchMatchers.size() - 1;
                highlightMatchResult(currentFoundMatch);
            }
        }
    }

    private void highlightMatchResult(int currentFoundMatch) {
        textArea.setCaretPosition(searchMatchers.get(currentFoundMatch).end());
        textArea.select(searchMatchers.get(currentFoundMatch).start(), searchMatchers.get(currentFoundMatch).end());
        textArea.grabFocus();
    }


}
