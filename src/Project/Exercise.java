package Project;

import java.io.Serializable;

class Exercise implements Serializable {
	private static final long serialVersionUID = 1L;
    private String name;
    private int a;
    private int b;
    private int questionCount;

    public Exercise(String name, int a, int b, int questionCount) {
        this.name = name;
        this.a = a;
        this.b = b;
        this.questionCount = questionCount;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}