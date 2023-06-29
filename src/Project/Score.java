package Project;

import java.io.*;
import java.util.*;

class Score implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "score.csv";
    private static final String HEADER = "exercise,user,time,score";
    private String exerciseName;
    private String childName;
    private float exerciseDuration;
    private int score;

    public Score(String exerciseName, String childName, float exerciseDuration, int score) {
        this.exerciseName = exerciseName;
        this.childName = childName;
        this.exerciseDuration = exerciseDuration;
        this.score = score;
    }

    public float getExerciseDuration() {
        return exerciseDuration;
    }

    public void setExerciseDuration(float exerciseDuration) {
        this.exerciseDuration = exerciseDuration;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void saveScoreData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(exerciseName).append(",");
            sb.append(childName).append(",");
            sb.append(exerciseDuration).append(",");
            sb.append(score).append("\n");

            bw.write(sb.toString());

            System.out.println("Skor baþarýyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Failed to save score data: " + e.getMessage());
        }
    }

    public static List<Score> getSortedScores() {
        List<Score> scores = loadScores();

        // isme ve skora göre sýralama
        scores.sort(Comparator.comparing(Score::getExerciseName)
                .thenComparing(Comparator.comparing(Score::getChildName))
                .thenComparing(Comparator.comparing(Score::getScore).reversed()));

        return scores;
    }

    public static void sortExercisesAndScores() {
        List<Score> scores = loadScores();

        // her alýþtýrma için map oluþturma
        Map<String, List<Score>> exerciseMap = new HashMap<>();

        // skorlarý grup içinde sýralama
        for (Score score : scores) {
            exerciseMap.computeIfAbsent(score.getExerciseName(), k -> new ArrayList<>()).add(score);
        }

        // alýþtýrma sýralama
        List<String> sortedExercises = new ArrayList<>(exerciseMap.keySet());
        Collections.sort(sortedExercises);

        // alýþtýrma içi sýralama
        for (List<Score> exerciseScores : exerciseMap.values()) {
            exerciseScores.sort(Comparator.comparing(Score::getScore).reversed());
        }

        // sýralanmýþ haliyle yeniden yazma
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            bw.write(HEADER);
            bw.newLine();

            for (String exercise : sortedExercises) {
                List<Score> exerciseScores = exerciseMap.get(exercise);
                for (Score score : exerciseScores) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(score.getExerciseName()).append(",");
                    sb.append(score.getChildName()).append(",");
                    sb.append(score.getExerciseDuration()).append(",");
                    sb.append(score.getScore()).append("\n");

                    bw.write(sb.toString());
                }
            }

            System.out.println("Skorlar güncellendi.");
        } catch (IOException e) {
            System.out.println("Failed to update score data: " + e.getMessage());
        }
    }



    private static List<Score> loadScores() {
        List<Score> scores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            // Skip the header line
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] scoreData = line.split(",");
                String exerciseName = scoreData[0];
                String childName = scoreData[1];
                float exerciseDuration = Float.parseFloat(scoreData[2]);
                int score = Integer.parseInt(scoreData[3]);

                scores.add(new Score(exerciseName, childName, exerciseDuration, score));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found.");
        } catch (IOException e) {
            System.out.println("Failed to read data: " + e.getMessage());
        }

        return scores;
    }
}
