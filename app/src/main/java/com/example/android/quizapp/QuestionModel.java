package com.example.android.quizapp;

public class QuestionModel {
    private String QuestionString;
    private String[] answer;
    private ResponseType responseType;

    public QuestionModel(String questionString, ResponseType responseType, String... answer) {
        QuestionString = questionString;
        this.responseType = responseType;
        this.answer = answer;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public String getQuestionString() {
        return QuestionString;
    }

    public void setQuestionString(String questionString) {
        QuestionString = questionString;
    }

    public String[] getAnswer() {
        return answer;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }

    public static class ResponseType {
        public static final int STRING = 0;
        public static final int CHECKBOX = 1;
        public static final int RADIO_BUTTON = 2;
        public int type;
        private String[] options;

        ResponseType(int type, String... options) {
            this.type = type;
            this.options = options;
        }

        public int getType() {
            return type;
        }

        public String[] getOptions() {
            return options;
        }
    }


}
