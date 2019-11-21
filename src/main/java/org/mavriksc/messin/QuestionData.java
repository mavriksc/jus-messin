package org.mavriksc.messin;

import java.util.Arrays;
import java.util.Collection;

public class QuestionData {
    private String question;
    private String answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QuestionData(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public QuestionData() {
    }

    public static Collection<QuestionData> generate() {
         return Arrays.asList(new QuestionData("Does the Device Power On and Off?","Yes"),
                new QuestionData("Is the Activation Lock turned off? (e.g. Find My iPhone or Reactivation Lock)","Yes"),
                new QuestionData("Is the screen functioning correctly and intact, free of any chips, bruised/dead pixels or burn-in?","Yes"),
                new QuestionData("Is the device otherwise free of breaks or cracks in the housing, keypad, hinge, or battery door?","Yes"),
                new QuestionData("Enter IMEI or Serial Number","018327531790464"),
                new QuestionData("Customer's Mobile number","1234567890"));
    }
}
