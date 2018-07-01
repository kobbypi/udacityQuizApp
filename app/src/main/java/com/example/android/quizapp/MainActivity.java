package com.example.android.quizapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.android.quizapp.QuestionModel.ResponseType.CHECKBOX;
import static com.example.android.quizapp.QuestionModel.ResponseType.RADIO_BUTTON;
import static com.example.android.quizapp.QuestionModel.ResponseType.STRING;

// TODO The packages within which the R file was being referenced does not exist
public class MainActivity extends Activity {
    TextView questionLabel, questionCountLabel, scoreLabel;
    EditText answerEdt;
    Button submitButton;
    ProgressBar progressBar;
    ArrayList<QuestionModel> questionModelArraylist;

    int currentPosition = 0;
    int numberOfCorrectAnswer = 0;
    private RadioGroup radioAnswerGroup;
    private LinearLayout checkboxAnswerGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionCountLabel = findViewById(R.id.noQuestion);
        questionLabel = findViewById(R.id.question);
        scoreLabel = findViewById(R.id.score);

        answerEdt = findViewById(R.id.string_answer);
        radioAnswerGroup = findViewById(R.id.radio_answer_group);
        checkboxAnswerGroup = findViewById(R.id.checkbox_answer_group);

        submitButton = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progress);

        questionModelArraylist = new ArrayList<>();

        setUpQuestion();
        setData();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        answerEdt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                Log.e("event.getAction()", event.getAction() + "");
                Log.e("event.keyCode()", keyCode + "");
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    checkAnswer();
                    return true;
                }
                return false;
            }
        });
    }

    public void checkAnswer() {
        String answerString;


        QuestionModel questionModel = questionModelArraylist.get(currentPosition);
        String[] answer = questionModel.getAnswer();
        QuestionModel.ResponseType responseType = questionModel.getResponseType();

        switch (responseType.getType()) {
            case STRING:
                answerString = answerEdt.getText().toString().trim();
                checkStringAnswer(answerString, answer[0]);
                break;

            case CHECKBOX:
                int childCount = checkboxAnswerGroup.getChildCount();
                List<String> actualAnswers = new ArrayList<>(childCount);


                for (int i = 0; i < childCount; i++) {
                    CheckBox checkBox = (CheckBox) checkboxAnswerGroup.getChildAt(i);
                    if (checkBox.isChecked())
                        actualAnswers.add(checkBox.getText().toString().trim());
                }

                checkCheckBoxAnswer(actualAnswers.toArray(new String[]{}), answer);
                break;

            case RADIO_BUTTON:
                answerString = ((RadioButton) radioAnswerGroup.findViewById(radioAnswerGroup.getCheckedRadioButtonId())).getText().toString();
                checkRadioButtonAnswer(answerString, answer[0]);
                break;
        }


        int x = ((currentPosition + 1) * 100) / questionModelArraylist.size();
        progressBar.setProgress(x);
    }

    private void checkRadioButtonAnswer(String actualAnswer, String expectedAnswer) {
        if (actualAnswer.equalsIgnoreCase(expectedAnswer)) {
            numberOfCorrectAnswer++;

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Good job!")
                    .setContentText("Right Asswer")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            currentPosition++;

                            setData();
                            answerEdt.setText("");
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Wrong Answer")
                    .setContentText("The right answer is : " + expectedAnswer)
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();

                            currentPosition++;

                            setData();
                            answerEdt.setText("");
                        }
                    })
                    .show();
        }
    }

    private void checkCheckBoxAnswer(String[] actualAnswers, String[] expectedAnswers) {
        List<String> actualAnswerList = Arrays.asList(actualAnswers);
        List<String> expectedAnswerList = Arrays.asList(expectedAnswers);

        boolean areEqual = actualAnswers.length == expectedAnswers.length;

        int numberOfCorrectAnswers = 0;
        if (areEqual) {
            for (int i = 0; i < expectedAnswers.length; i++) {
                String expectedAnswer = expectedAnswers[i];
                for (int j = 0; j < actualAnswers.length; j++) {
                    String actualAnswer = actualAnswers[j];
                    if (actualAnswer.equalsIgnoreCase(expectedAnswer))
                        numberOfCorrectAnswers++;
                }
            }
        }
//        Collections.sort(actualAnswerList);
//        Collections.sort(expectedAnswerList);

        if (areEqual && numberOfCorrectAnswers == expectedAnswers.length) {
            numberOfCorrectAnswer++;

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Good job!")
                    .setContentText("Right Asswer")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            currentPosition++;

                            setData();
                            answerEdt.setText("");
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < expectedAnswers.length; i++) {
                builder.append(expectedAnswers[i]);
                if (i < expectedAnswers.length - 1)
                    builder.append(", ");
            }

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Wrong Answer")
                    .setContentText("The right answer is : " + builder.toString())
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();

                            currentPosition++;

                            setData();
                            answerEdt.setText("");
                        }
                    })
                    .show();
        }
    }

    private void checkStringAnswer(String answerString, String answer) {
        if (answerString.equalsIgnoreCase(answer)) {
            numberOfCorrectAnswer++;

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Good job!")
                    .setContentText("Right Asswer")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            currentPosition++;

                            setData();
                            answerEdt.setText("");
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Wrong Answer")
                    .setContentText("The right answer is : " + answer)
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();

                            currentPosition++;

                            setData();
                            answerEdt.setText("");
                        }
                    })
                    .show();
        }
    }

    public void setUpQuestion() {
        questionModelArraylist.add(new QuestionModel("What is the square root of 9? ", new QuestionModel.ResponseType(CHECKBOX, "2", "3", "-3", "4"), "3", "-3"));
        questionModelArraylist.add(new QuestionModel("What is the 3 + 3? ", new QuestionModel.ResponseType(RADIO_BUTTON, "2", "6", "-3", "4"), "6"));
        questionModelArraylist.add(new QuestionModel("What is |6-10| ? ", new QuestionModel.ResponseType(STRING), "4"));
        questionModelArraylist.add(new QuestionModel("What is 8*8 ? ", new QuestionModel.ResponseType(STRING), "64"));
        questionModelArraylist.add(new QuestionModel("What is 9*12 ? ", new QuestionModel.ResponseType(STRING), "108"));
        questionModelArraylist.add(new QuestionModel("What is 6*8 ? ", new QuestionModel.ResponseType(STRING), "48"));
        questionModelArraylist.add(new QuestionModel("What is 12/3 ? ", new QuestionModel.ResponseType(STRING), "4"));
    }

    public void setData() {
        if (questionModelArraylist.size() > currentPosition) {


            QuestionModel questionModel = questionModelArraylist.get(currentPosition);
            questionLabel.setText(questionModel.getQuestionString());

            // Reset visibility of all answer types
            answerEdt.setVisibility(View.GONE);
            radioAnswerGroup.setVisibility(View.GONE);
            radioAnswerGroup.removeAllViews();

            checkboxAnswerGroup.setVisibility(View.GONE);
            checkboxAnswerGroup.removeAllViews();

            QuestionModel.ResponseType responseType = questionModel.getResponseType();
            String[] options = responseType.getOptions();

            switch (responseType.getType()) {
                case STRING:
                    answerEdt.setVisibility(View.VISIBLE);
                    break;

                case CHECKBOX:
                    checkboxAnswerGroup.setVisibility(View.VISIBLE);
                    if (options != null) {
                        for (int i = 0; i < options.length; i++) {
                            CheckBox checkBox = new CheckBox(this);
                            checkBox.setId(i * 10 + 1);
                            checkBox.setText(options[i]);
                            checkboxAnswerGroup.addView(checkBox);
                        }
                    }
                    break;

                case RADIO_BUTTON:
                    radioAnswerGroup.setVisibility(View.VISIBLE);
                    if (options != null) {
                        for (int i = 0; i < options.length; i++) {
                            RadioButton radioButton = new RadioButton(this);
                            radioButton.setId(i * 10 + 1);
                            radioButton.setText(options[i]);
                            radioAnswerGroup.addView(radioButton);
                        }
                    }
                    break;
            }


            scoreLabel.setText("Score :" + numberOfCorrectAnswer + "/" + questionModelArraylist.size());
            questionCountLabel.setText("Question No : " + (currentPosition + 1));
        } else {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("You have successfully completed the quiz")
                    .setContentText("Your score is : " + numberOfCorrectAnswer + "/" + questionModelArraylist.size())
                    .setConfirmText("Restart")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismissWithAnimation();
                            currentPosition = 0;
                            numberOfCorrectAnswer = 0;
                            progressBar.setProgress(0);
                            setData();
                        }
                    })
                    .setCancelText("Close")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        }
    }
}
