package Project;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Child extends User {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "exercises.csv";
    private static float score;
    private List<ExerciseLog> exerciseLogs;
    private Parent parent;
    private Exercise exercise;

    public Child(String username, String password) {
        super(username, password);
        Child.score = 0;
        this.exerciseLogs = new ArrayList<>();
        this.exercise = null;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        Child.score = score;
    }

    public List<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }

    public void setExerciseLogs(List<ExerciseLog> exerciseLogs) {
        this.exerciseLogs = exerciseLogs;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public void startExercise() {
        if (exercise != null) {
            return;
        }
    }


    static void startExerciseMode(Child child) {
        // Display exercise options in a dialog box
        List<String> exerciseNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] exerciseData = line.split(",");
                String name = exerciseData[0];
                if (!exerciseNames.contains(name)) {
                    exerciseNames.add(name);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to read data: " + e.getMessage());
        }

        Object[] options = exerciseNames.toArray();
        int exerciseNumber = JOptionPane.showOptionDialog(null, "Bir alýþtýrma seçiniz:", "Exercise Selection", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (exerciseNumber >= 0 && exerciseNumber < exerciseNames.size()) {
            String chosenExerciseName = exerciseNames.get(exerciseNumber);
            JOptionPane.showMessageDialog(null, "Seçilen alýþtýrma: " + chosenExerciseName);

            // Start the exercise timer
            Instant startTime = Instant.now();

            try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] exerciseData = line.split(",");
                    String name = exerciseData[0];
                    if (name.equals(chosenExerciseName)) {
                        ExerciseLog exerciseLog = new ExerciseLog(Date.from(startTime));

                        JFrame frame = new JFrame();
                        JPanel mainPanel = new JPanel(new GridBagLayout());
                        GridBagConstraints constraints = new GridBagConstraints();
                        constraints.anchor = GridBagConstraints.WEST;
                        constraints.gridx = 0;
                        constraints.gridy = 0;

                        for (int i = 4; i < exerciseData.length; i += 3) {
                            int randomA = Integer.parseInt(exerciseData[i]);
                            int randomB = Integer.parseInt(exerciseData[i + 1]);
                            int expectedAnswer = Integer.parseInt(exerciseData[i + 2]);

                            // Create a panel to hold the question and timer components
                            JPanel panel = new JPanel(new GridBagLayout());

                            // Display the question
                            String question = randomA + " * " + randomB + " = ?";
                            JLabel questionLabel = new JLabel(question);
                            GridBagConstraints questionConstraints = new GridBagConstraints();
                            questionConstraints.anchor = GridBagConstraints.WEST;
                            questionConstraints.gridx = 0;
                            questionConstraints.gridy = 0;
                            panel.add(questionLabel, questionConstraints);

                            // Create and add the timer components
                            JLabel timerLabel = new JLabel("Timer: ");
                            GridBagConstraints timerLabelConstraints = new GridBagConstraints();
                            timerLabelConstraints.anchor = GridBagConstraints.WEST;
                            timerLabelConstraints.gridx = 0;
                            timerLabelConstraints.gridy = 1;
                            panel.add(timerLabel, timerLabelConstraints);

                            JLabel secondsLabel = new JLabel("0");
                            GridBagConstraints secondsLabelConstraints = new GridBagConstraints();
                            secondsLabelConstraints.anchor = GridBagConstraints.WEST;
                            secondsLabelConstraints.gridx = 1;
                            secondsLabelConstraints.gridy = 1;
                            panel.add(secondsLabel, secondsLabelConstraints);

                            constraints.gridy = i / 3;
                            mainPanel.add(panel, constraints);

                            // Start the timer
                            Timer timer = new Timer(1000, e -> {
                                int seconds = (int) ((Instant.now().toEpochMilli() - startTime.toEpochMilli()) / 1000);
                                secondsLabel.setText(String.valueOf(seconds));
                            });
                            timer.start();

                            // Display the input dialog
                            String userAnswer = JOptionPane.showInputDialog(frame, panel, "Soru",
                                    JOptionPane.PLAIN_MESSAGE);
                            timer.stop(); // Stop the timer when the input dialog is closed

                            // Check the answer
                            if (userAnswer != null && userAnswer.equals(String.valueOf(expectedAnswer))) {
                                JLabel correctnessLabel = new JLabel("Cevap Doðru!");
                                GridBagConstraints correctnessLabelConstraints = new GridBagConstraints();
                                correctnessLabelConstraints.anchor = GridBagConstraints.WEST;
                                correctnessLabelConstraints.gridx = 4;
                                correctnessLabelConstraints.gridy = i / 6;
                                mainPanel.add(correctnessLabel, correctnessLabelConstraints);
                                exerciseLog.setCorrectCount(exerciseLog.getCorrectCount() + 1);
                            } else {
                                JLabel correctnessLabel = new JLabel("Yanlýþ. Doðru cevap: " + expectedAnswer);
                                GridBagConstraints correctnessLabelConstraints = new GridBagConstraints();
                                correctnessLabelConstraints.anchor = GridBagConstraints.WEST;
                                correctnessLabelConstraints.gridx = 4;
                                correctnessLabelConstraints.gridy = i / 6;
                                mainPanel.add(correctnessLabel, correctnessLabelConstraints);
                                exerciseLog.setWrongCount(exerciseLog.getWrongCount() + 1);
                            }

                            // Get the elapsed time for the question
                            Instant endTime = Instant.now();
                            Duration duration = Duration.between(startTime, endTime);
                            long questionTime = duration.getSeconds();
                            exerciseLog.addQuestionTime(questionTime);
                        }

                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.add(mainPanel);
                        frame.pack();
                        frame.setVisible(true);

                        exerciseLog.endExercise(Date.from(Instant.now()));
                        exerciseLog.saveExerciseLogData(child.getUsername(), chosenExerciseName);
                    }
                }

                Instant endTime = Instant.now();

                // Calculate and save the elapsed time for solving the questions in seconds
                Duration duration = Duration.between(startTime, endTime);
                long questionTime = duration.getSeconds();
                float score = ExerciseLog.getCorrectCount() * (1.0f / questionTime) * 10000;

                Score scoreObject = new Score(chosenExerciseName, child.getUsername(), questionTime, (int) score);
                scoreObject.saveScoreData();

                // Display the child's score
                JOptionPane.showMessageDialog(null, "Alýþtýrma tamamlandý. Skorunuz: " + score, "Skor",
                        JOptionPane.PLAIN_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to read data: " + e.getMessage());
            }
        }
    }
}