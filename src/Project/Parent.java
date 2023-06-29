package Project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Parent extends User {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "exercises.csv";
    private List<Child> children;
    private boolean isAdmin;


    public Parent(String username, String password) {
        super(username, password);
        this.children = new ArrayList<>();
        this.isAdmin = false;
    }
    
    public List<String> getChildrenUsernames() {
        List<String> childrenUsernames = new ArrayList<>();
        for (Child child : children) {
            childrenUsernames.add(child.getUsername());
        }
        return childrenUsernames;
    }

    public void addChild(Child child) {
        children.add(child);
    }

    public void removeChild(Child child) {
        children.remove(child);
    }

    public List<Child> getChildren() {
        return children;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    
    public void addChildExercise(String name, int a, int b, int questionCount) {
        Exercise exercise = new Exercise(name, a, b, questionCount);

        Random random = new Random();

        for (int i = 0; i < questionCount; i++) {
            int randomA = random.nextInt(a) + 1; // 1 ile a arasýnda rastgele bir sayý
            int randomB = random.nextInt(b) + 1; // 1 ile b arasýnda rastgele bir sayý

            int expectedAnswer = randomA * randomB;

            saveExerciseToCSV(exercise, randomA, randomB, expectedAnswer);
        }
    }


    
    private void saveExerciseToCSV(Exercise exercise, int randomA, int randomB, int expectedAnswer) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            String exerciseData = exercise.getName() + "," +
                    exercise.getA() + "," + exercise.getB() + "," + exercise.getQuestionCount() + "," +
                    randomA + "," + randomB + "," + expectedAnswer;
            bw.write(exerciseData);
            bw.newLine();
            System.out.println("Alýþtýrma baþarýyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Kayýt hatasý: " + e.getMessage());
        }
    }


}