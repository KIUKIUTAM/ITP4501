package com.example.itp4915assignment_69;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Random;

public class PlayGame extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    long startTime = 0;
    SQLiteDatabase db;
    private static final String TAG = "PlayGame";
    String [][] questions = new String[10][2];
    TextView tvTimer;
    TextView tvQuestionTitle;
    TextView tvQuestionName;
    EditText etQuestionAns;
    TextView tvIsCorrect;
    TextView tvShowAns;
    Button btnStartAndDone;
    Button btnNext_Question;
    TextView tvTimerHead;
    TextView tvTimerTail;
    int GameMode;
    int QuestionIndex;
    boolean start;
    int correntQuestion;
    boolean isStartBackToMenu;
    private final Handler handler = new Handler(Looper.getMainLooper());
    Thread t;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Play Game");
        setSupportActionBar(toolbar);
        tvTimer = findViewById(R.id.tvTimer);
        tvTimerHead = findViewById(R.id.tvTimerHead);
        tvTimerTail = findViewById(R.id.tvTimerTail);
        tvQuestionTitle = findViewById(R.id.tvQuestionTitle);
        tvQuestionName = findViewById(R.id.tvQuestionName);
        etQuestionAns =findViewById(R.id.etQuestionAns);
        tvIsCorrect = findViewById(R.id.tvIsCorrect);
        tvShowAns = findViewById(R.id.tvShowAns);
        btnStartAndDone = findViewById(R.id.btnStartAndDone);
        btnNext_Question = findViewById(R.id.btnNext_Question);
        isStartBackToMenu =false;
        timerShowToggle(false);
        tvQuestionTitle.setVisibility(View.INVISIBLE);
        tvQuestionName.setVisibility(View.INVISIBLE);
        etQuestionAns.setVisibility(View.INVISIBLE);
        tvIsCorrect.setVisibility(View.INVISIBLE);
        tvShowAns.setVisibility(View.INVISIBLE);
        btnNext_Question.setVisibility(View.INVISIBLE);
        btnStartAndDone.setVisibility(View.VISIBLE);
        etQuestionAns.setInputType(InputType.TYPE_CLASS_NUMBER);
        etQuestionAns.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    done();
                    return true;
                }
                return false;
            }
        });

        start =true;
        correntQuestion = 0;
        sharedPreferences = getSharedPreferences("GameMode", MODE_PRIVATE);
    }

    protected void onResume() {
        super.onResume();
        GameMode = sharedPreferences.getInt("GameModeSetting", 0);
    }
    private  void randomQuestion() {

            for (int i = 0; i < 10; i++) {
                Random random = new Random();
                int operand1 = random.nextInt(100) + 1;
                int operand2 = random.nextInt(100) + 1;
                String[] operators = {"+", "-", "*", "/"};
                String operator = operators[random.nextInt(operators.length)];
                String question = operand1 + operator + operand2;
                String answer = "";
                switch (operator) {
                    case "+":
                        answer = String.valueOf(operand1 + operand2);

                        break;
                    case "-":
                        if (operand1 - operand2 < 0) {
                            question = operand2 + operator + operand1;
                            answer = String.valueOf(operand2 - operand1);
                        } else {
                            answer = String.valueOf(operand1 - operand2);
                        }
                        break;
                    case "*":
                        answer = String.valueOf(operand1 * operand2);
                        break;
                    default:
                        if (operand1 - operand2 < 0) {
                            question = operand2 + operator + operand1;
                            answer = String.valueOf(operand2 / operand1);
                        } else {
                            answer = String.valueOf(operand1 / operand2);
                        }
                        break;
                }
                questions[i][0] = question;
                questions[i][1] = answer;
                Log.d("question", "" + question);
                Log.d("answer", "" + answer);
            }
        }

    private void Timer() {
        startTime = System.currentTimeMillis();
        t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                handler.post(() -> {
                    long currentTime = System.currentTimeMillis();
                    long time = currentTime - startTime;
                    int seconds = (int) (time / 1000);

                    tvTimer.setText(seconds+"");
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        t.start();
    }


    public void gameStartOrDone(View view) {

        if(start){
            start = false;
            isStartBackToMenu=false;
            QuestionIndex=0;
            randomQuestion();
            tvQuestionTitle.setText("Question "+(QuestionIndex+1));
            tvQuestionName.setText(questions[QuestionIndex][0]);
            btnStartAndDone.setText("DONE");
            timerShowToggle(true);
            tvQuestionTitle.setVisibility(View.VISIBLE);
            tvQuestionName.setVisibility(View.VISIBLE);
            etQuestionAns.setVisibility(View.VISIBLE);

            btnNext_Question.setVisibility(View.INVISIBLE);
            btnNext_Question.setText("Next Question");
            Timer();
        }else{
            done();
        }
    }

    private void done(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etQuestionAns.getWindowToken(), 0);
        String ans = String.valueOf(etQuestionAns.getText());
        String ansWithoutSpaces = ans.replace(" ", "");
        btnNext_Question.setVisibility(View.VISIBLE);
        if(ansWithoutSpaces.equals(questions[QuestionIndex][1])){
            tvIsCorrect.setText("Correct!");
            correntQuestion++;
        }else{
            tvIsCorrect.setText("Incorrect");
            tvShowAns.setText("Answer is "+questions[QuestionIndex][1]);
            tvShowAns.setVisibility(View.VISIBLE);
        }
        tvIsCorrect.setVisibility(View.VISIBLE);
        etQuestionAns.setText("");
        readonlyToggle(true);
        btnStartAndDone.setVisibility(View.INVISIBLE);
        btnNext_Question.setVisibility(View.VISIBLE);
        QuestionIndex++;
        if(QuestionIndex==10) {
            btnNext_Question.setText("Finish!");
        }
    }


    public void Next_Question(View view) {
        if(isStartBackToMenu){
            finish();
            return;
        }

        if(QuestionIndex<10){
            readonlyToggle(false);
            tvQuestionTitle.setText("Question "+(QuestionIndex+1));
            tvQuestionName.setText(questions[QuestionIndex][0]);
            btnNext_Question.setVisibility(View.INVISIBLE);
            tvIsCorrect.setVisibility(View.INVISIBLE);
            tvShowAns.setVisibility(View.INVISIBLE);
            btnStartAndDone.setVisibility(View.VISIBLE);
        }else{
                timerShowToggle(true);
                readonlyToggle(true);
                btnStartAndDone.setVisibility(View.VISIBLE);
                btnStartAndDone.setText("Restart");
                start =true;
                tvQuestionTitle.setVisibility(View.VISIBLE);
                tvQuestionTitle.setText("Your have "+ correntQuestion +" Corrent Questions");
                tvQuestionName.setVisibility(View.INVISIBLE);
                etQuestionAns.setVisibility(View.INVISIBLE);
                tvIsCorrect.setVisibility(View.INVISIBLE);
                tvShowAns.setVisibility(View.INVISIBLE);
                btnNext_Question.setText("Back To Menu");
                isStartBackToMenu =true;
                insertDB(correntQuestion,Integer.valueOf(tvTimer.getText().toString()));
                if (t != null && !t.isInterrupted()) {
                    t.interrupt();
                }
            }
        }



    private void readonlyToggle(boolean readonlyToggle){
        if(readonlyToggle){
            etQuestionAns.setInputType(InputType.TYPE_NULL);
            etQuestionAns.setFocusable(false);
            etQuestionAns.setFocusableInTouchMode(false);
        }else{

            etQuestionAns.setInputType(InputType.TYPE_CLASS_NUMBER);
            etQuestionAns.setFocusable(true);
            etQuestionAns.setFocusableInTouchMode(true);
        }

    }

    private void timerShowToggle(boolean timerShowToggle){
        int i;
        if(timerShowToggle){
            i = View.VISIBLE;

        }else{
            i = View.INVISIBLE;
        }
        tvTimer.setVisibility(i);
        tvTimerHead.setVisibility(i);
        tvTimerTail.setVisibility(i);
    }
    private void insertDB(int correctQuestion, int playingTime) {
        String str = "";

        try {
            str = "Normal Mode";
            db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.itp4915assignment_69/eBidDB", null);
            String sql = "INSERT INTO GameLogNormal (playDate, playTime, GameMode, correctQuestion, playingTime) VALUES (date('now'), time('now'), ?, ?, ?);";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, str);
            statement.bindLong(2, correctQuestion);
            statement.bindLong(3, playingTime);
            statement.executeInsert();

        } catch (SQLiteException e) {
            Log.e(TAG, "Database error: ", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

}