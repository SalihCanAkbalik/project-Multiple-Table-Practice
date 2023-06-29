package Project;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class UygulamaGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4076460323698455086L;
	private static final String DATA_FILE = "users.csv";
	private static DefaultListModel<String> model;
	private static JList<String> userList;
	private static JScrollPane scrollPane;
	private static JButton loginButton;
	private static JButton signupButton;
	private static JButton exitButton;
	private static JTextField usernameField;
	private static JPasswordField passwordField;
	private static List<User> users;
	private static Parent admin;
	
	public UygulamaGUI() {
	    setTitle("Multiplication Table Practice");
	    setSize(500, 450);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    setLocationRelativeTo(null);

	    initializeComponents();
	    loadUsers();

	    setVisible(true);
	}

	private void initializeComponents() {
	    setLayout(new BorderLayout());

	    model = new DefaultListModel<>();
	    userList = new JList<>(model);
	    scrollPane = new JScrollPane(userList);
	    add(scrollPane, BorderLayout.CENTER);

	    JPanel buttonPanel = new JPanel(new GridLayout(4, 1));

	    loginButton = new JButton("Giriþ Yap");
	    loginButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            login();
	        }
	    });
	    buttonPanel.add(loginButton);

	    signupButton = new JButton("Kayýt Ol");
	    signupButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            signup();
	        }
	    });
	    buttonPanel.add(signupButton);

	    exitButton = new JButton("Çýkýþ");
	    exitButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            saveData();
	            System.exit(0);
	        }
	    });
	    buttonPanel.add(exitButton);

	    JPanel inputPanel = new JPanel(new GridLayout(2, 2));

	    JLabel usernameLabel = new JLabel("Kullanýcý adý:");
	    inputPanel.add(usernameLabel);

	    usernameField = new JTextField();
	    inputPanel.add(usernameField);

	    JLabel passwordLabel = new JLabel("Þifre:");
	    inputPanel.add(passwordLabel);

	    passwordField = new JPasswordField();
	    inputPanel.add(passwordField);

	    add(buttonPanel, BorderLayout.EAST);
	    add(inputPanel, BorderLayout.SOUTH);
	}

	private void login() {
	    String username = usernameField.getText();
	    String password = new String(passwordField.getPassword());

	    boolean isAuthenticated = authenticateUser(username, password);

	    if (isAuthenticated) {
	        User user = getUser(username);
	        if (user instanceof Parent) {
	            parentMenu((Parent) user);
	        } else if (user instanceof Child) {
	            childMenu((Child) user);
	        } else {
	            JOptionPane.showMessageDialog(this, "Invalid user type.");
	        }
	    } else {
	        JOptionPane.showMessageDialog(this, "Kullanýcý adý veya þifre hatasý.");
	    }
	}

	private void signup() {
	    String username = usernameField.getText();
	    String password = new String(passwordField.getPassword());

	    if (isUsernameTaken(username)) {
	        JOptionPane.showMessageDialog(this, "Kullanýcý adý kullanýmda. Yeni kullanýcý adý giriniz.");
	        return;
	    }

	    if (username.equals("admin") && password.equals("admin")) {
	        if (admin != null) {
	            JOptionPane.showMessageDialog(this, "Admin kullanýcýs mevcut.");
	            return;
	        }
	        admin = new Parent(username, password);
	        admin.setAdmin(true);
	        users.add(admin);
	        JOptionPane.showMessageDialog(this, "Admin kullanýcýsý eklendi.");
	    } else {
	        User newUser = new Child(username, password);
	        users.add(newUser);
	        JOptionPane.showMessageDialog(this, "Kullanýcý baþarýyla eklendi.");
	    }
	    model.addElement(username);
	    saveData();
	}

	boolean isUsernameTaken(String username) {
	    for (User user : users) {
	        if (user.getUsername().equals(username)) {
	            return true;
	        }
	    }
	    return false;
	}

	boolean authenticateUser(String username, String password) {
	    for (User user : users) {
	        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
	            return true;
	        }
	    }
	    return false;
	}

	User getUser(String username) {
	    for (User user : users) {
	        if (user.getUsername().equals(username)) {
	            return user;
	        }
	    }
	    return null;
	}

	private void parentMenu(Parent parent) {
	    JOptionPane.showMessageDialog(this, "--- Parent Menu ---\n" +
	            "1. Çocuk kullanýcýsý ekleme\n" +
	            "2. Çocuk kullanýcýsý silme\n" +
	            "3. Alýþtýrma Ekleme Ayarlarý\n" +
	            "4. Rapor görüntüleme\n" +
	            "5. Çýkýþ");

	    String choice = JOptionPane.showInputDialog(this, "Bir seçim yapýnýz:");
	    switch (choice) {
	        case "1":
	            addChild(parent);
	            break;
	        case "2":
	            removeChild(parent);
	            break;
	        case "3":
	            String name = JOptionPane.showInputDialog(this, "Alýþtýrma ismi giriniz:");
	            int a = Integer.parseInt(JOptionPane.showInputDialog(this, "'a' Deðerini giriniz:"));
	            int b = Integer.parseInt(JOptionPane.showInputDialog(this, "'b' Deðerini giriniz:"));
	            int questionCount = Integer.parseInt(JOptionPane.showInputDialog(this, "Soru sayýsýný giriniz:"));
	            parent.addChildExercise(name, a, b, questionCount);
	            break;
	        case "4":
	        	showReport(parent);
	            break;
	        case "5":
	            break;
	        default:
	            JOptionPane.showMessageDialog(this, "Hatalý seçim. Tekrar deneyiniz.");
	            break;
	    }
	}
	
	private void showReport(Parent parent) {
        List<Score> scores = loadScores();
        List<ExerciseLog> exerciseLogs = loadExerciseLogs();

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("--- Rapor ---\n");

        // Filter scores and exercise logs for the parent's children
        List<String> childrenUsernames = parent.getChildrenUsernames();
        List<Score> filteredScores = filterScoresByChildren(scores, childrenUsernames);
        List<ExerciseLog> filteredExerciseLogs = filterExerciseLogsByChildren(exerciseLogs, childrenUsernames);

        // Generate report for each child
        for (String childUsername : childrenUsernames) {
            reportBuilder.append("Kullanýcý: ").append(childUsername).append("\n");

            // Get scores for the child
            List<Score> childScores = filterScoresByChild(filteredScores, childUsername);

            if (childScores.isEmpty()) {
                reportBuilder.append("Skor bulunamadý.\n");
            } else {
                reportBuilder.append("Scores:\n");
                for (Score score : childScores) {
                    reportBuilder.append("Alýþtýrma: ").append(score.getExerciseName())
                            .append(", Skor: ").append(score.getScore())
                            .append(", Süre: ").append(score.getExerciseDuration()).append(" saniye\n");
                }
            }

            // Get exercise logs for the child
            List<ExerciseLog> childExerciseLogs = filterExerciseLogsByChild(filteredExerciseLogs, childUsername);

            if (childExerciseLogs.isEmpty()) {
                //reportBuilder.append("No exercise logs available.\n");
            } else {
                reportBuilder.append("Alýþtýrma Kayýtlarý:\n");
                for (ExerciseLog exerciseLog : childExerciseLogs) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String startTime = sdf.format(exerciseLog.getStartTime());
                    String endTime = (exerciseLog.getEndTime() != null) ? sdf.format(exerciseLog.getEndTime()) : "Tamamlanmadý";
                    reportBuilder.append("Baþlama zamaný: ").append(startTime)
                            .append(", Bitiþ zamaný: ").append(endTime)
                            .append(", Doðru sayýsý: ").append(ExerciseLog.getCorrectCount())
                            .append(", Yanlýþ sayýsý: ").append(exerciseLog.getWrongCount())
                            .append(", Tamamlama süresi: ").append(exerciseLog.getQuestionTimes()).append("\n");
                }
            }

            reportBuilder.append("\n");
        }

        JOptionPane.showMessageDialog(this, reportBuilder.toString());
    }

    private List<Score> loadScores() {
        List<Score> scores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("score.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] scoreData = line.split(",");
                String exerciseName = scoreData[0];
                String childName = scoreData[1];
                float exerciseDuration = Float.parseFloat(scoreData[2]);
                int score = Integer.parseInt(scoreData[3]);

                scores.add(new Score(exerciseName, childName, exerciseDuration, score));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scores;
    }

    private List<ExerciseLog> loadExerciseLogs() {
        List<ExerciseLog> exerciseLogs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("exerciseLog.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] logData = line.split(",");
                //String username = logData[0];
                Date startTime = new SimpleDateFormat("HH:mm:ss").parse(logData[1]);
                //Date endTime = (logData[2].equals("null")) ? null : new SimpleDateFormat("HH:mm:ss").parse(logData[2]);
                //int correctCount = Integer.parseInt(logData[3]);
                //int wrongCount = Integer.parseInt(logData[4]);
                int[] questionTimes = new int[logData.length - 5];
                for (int i = 5; i < logData.length; i++) {
                    questionTimes[i - 5] = Integer.parseInt(logData[i]);
                }

                exerciseLogs.add(new ExerciseLog(startTime));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return exerciseLogs;
    }

    private List<Score> filterScoresByChildren(List<Score> scores, List<String> childrenUsernames) {
        List<Score> filteredScores = new ArrayList<>();
        for (Score score : scores) {
            if (childrenUsernames.contains(score.getChildName())) {
                filteredScores.add(score);
            }
        }
        return filteredScores;
    }

    private List<Score> filterScoresByChild(List<Score> scores, String childUsername) {
        List<Score> filteredScores = new ArrayList<>();
        for (Score score : scores) {
            if (score.getChildName().equals(childUsername)) {
                filteredScores.add(score);
            }
        }
        return filteredScores;
    }

    private List<ExerciseLog> filterExerciseLogsByChildren(List<ExerciseLog> exerciseLogs, List<String> childrenUsernames) {
        List<ExerciseLog> filteredExerciseLogs = new ArrayList<>();
        for (ExerciseLog exerciseLog : exerciseLogs) {
            if (childrenUsernames.contains(childrenUsernames)) {
                filteredExerciseLogs.add(exerciseLog);
            }
        }
        return filteredExerciseLogs;
    }

    private List<ExerciseLog> filterExerciseLogsByChild(List<ExerciseLog> exerciseLogs, String childUsername) {
        List<ExerciseLog> filteredExerciseLogs = new ArrayList<>();
        for (ExerciseLog exerciseLog : exerciseLogs) {
            if (childUsername.equals(childUsername)) {
                filteredExerciseLogs.add(exerciseLog);
            }
        }
        return filteredExerciseLogs;
    }


	void addChild(Parent parent) {
	    String username = JOptionPane.showInputDialog(this, "Çocuk için kullanýcý adý giriniz:");

	    if (isUsernameTaken(username)) {
	        JOptionPane.showMessageDialog(this, "Kullanýcý adý kullanýmda. Yeni bir kullanýcý adý giriniz.");
	        return;
	    }

	    String password = JOptionPane.showInputDialog(this, "Çocuk için þifre giriniz:");

	    Child child = new Child(username, password);
	    parent.addChild(child);
	    users.add(child);
	    JOptionPane.showMessageDialog(this, "Baþarýyla eklendi.");
	    model.addElement(username);
	    saveData();
	}

	private void removeChild(Parent parent) {
	    String username = JOptionPane.showInputDialog(this, "Çocuk için kullanýcý adý:");

	    Child childToRemove = null;
	    for (Child child : parent.getChildren()) {
	        if (child.getUsername().equals(username)) {
	            childToRemove = child;
	            break;
	        }
	    }

	    if (childToRemove != null) {
	        parent.removeChild(childToRemove);
	        users.remove(childToRemove);
	        JOptionPane.showMessageDialog(this, "Baþarýyla silindi.");
	        model.removeElement(username);
	        saveData();
	    } else {
	        JOptionPane.showMessageDialog(this, "Kullanýcý bulunamadý.");
	    }
	}

	private void childMenu(Child child) {
	    JOptionPane.showMessageDialog(this, "--- Child Menu ---\n" +
	            "1. Alýþtýrma Seçme\n" +
	            "2. Skor\n" +
	            "3. Çýkýþ");

	    String choice = JOptionPane.showInputDialog(this, "Bir seçim yapýnýz:");
	    switch (choice) {
	        case "1":
	            Child.startExerciseMode(child);
	            break;
	        case "2":
	            //child.incrementScore();
	            JOptionPane.showMessageDialog(this, "Skorunuz: " + child.getScore());
	            break;
	        case "3":
	            break;
	        default:
	            JOptionPane.showMessageDialog(this, "Tekrar Deneyiniz.");
	            break;
	    }
	}

	private void loadUsers() {
		users = new ArrayList<>();

	    try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] userData = line.split(",");
	            String username = userData[0];
	            String password = userData[1];
	            boolean isAdmin = Boolean.parseBoolean(userData[2]);

	            if (isAdmin) {
	                admin = new Parent(username, password);
	                admin.setAdmin(true);
	                users.add(admin);
	                model.addElement(username);
	            } else {
	                Child child = new Child(username, password);
	                users.add(child);
	                if (admin != null) {
	                    admin.addChild(child);
	                }
	                model.addElement(username);
	            }
	        }        
	    } catch (FileNotFoundException e) {
	        JOptionPane.showMessageDialog(this, "No saved data found.");
	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(this, "Failed to load data: " + e.getMessage());
	    }
	}

	private void saveData() {
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
	        for (User user : users) {
	            String username = user.getUsername();
	            String password = user.getPassword();
	            boolean isAdmin = (user instanceof Parent) && ((Parent) user).isAdmin();

	            String userData = username + "," + password + "," + isAdmin;
	            bw.write(userData);
	            bw.newLine();
	        }
	        JOptionPane.showMessageDialog(this, "Kaydetme baþarýlý.");
	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(this, "Failed to save data: " + e.getMessage());
	    }
	    Score.sortExercisesAndScores();
	}

	public static void main(String[] args) {
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            new UygulamaGUI();
	        }
	    });
	}
}