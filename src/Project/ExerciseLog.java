package Project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ExerciseLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "exercisesLog.csv";
    private Date startTime;
    Date endTime;
    private static int correctCount;
    private int wrongCount;
    private List<Long> questionTimes;

    public ExerciseLog(Date startTime) {
        this.startTime = startTime;
        this.endTime = null;
        ExerciseLog.correctCount = 0;
        this.wrongCount = 0;
        this.questionTimes = new ArrayList<>();
    }

    public void endExercise(Date endTime) {
        this.endTime = endTime;
    }


    public void addQuestionTime(float questionTime) {
        long questionTimeInSeconds = (long) (questionTime * 1000); // Convert to milliseconds
        questionTimes.add(questionTimeInSeconds);
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public static int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        ExerciseLog.correctCount = correctCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }

    public List<Long> getQuestionTimes() {
        return questionTimes;
    }

    public void setQuestionTimes(List<Long> questionTimes) {
        this.questionTimes = questionTimes;
    }

    public void saveExerciseLogData(String username, String exerciseName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            StringBuilder sb = new StringBuilder();
            sb.append(username).append(",");
            sb.append(exerciseName).append(",");
            sb.append(sdf.format(startTime)).append(",");
            sb.append(sdf.format(endTime)).append(",");
            sb.append(correctCount).append(",");

            // Format the question times
            if (!questionTimes.isEmpty()) {
                Instant questionInstant = startTime.toInstant(); // Start time for the first question
                for (int i = 0; i < questionTimes.size(); i++) {
                    long questionTime = questionTimes.get(i);
                    Instant endTime;
                    if (i == questionTimes.size() - 1 && this.endTime != null) {
                        endTime = this.endTime.toInstant(); // Use the overall endTime for the last question
                    } else {
                        endTime = questionInstant.plusMillis(questionTime);
                    }
                    Duration duration = Duration.between(questionInstant, endTime);
                    long questionDurationSeconds = duration.getSeconds();
                    sb.append(questionDurationSeconds).append(",");
                    questionInstant = endTime; // Update the start time for the next question
                }
                sb.deleteCharAt(sb.length() - 1); // Remove the trailing comma
            }

            sb.append("\n");

            bw.write(sb.toString());

            System.out.println("Alýþtýrma bilgileri baþarýyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Alýþtýrma kayýt hatasý: " + e.getMessage());
        }
    }
}