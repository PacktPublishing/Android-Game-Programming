package com.packtpub.mathgamechapter3b.mathgamechapter3b;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends Activity implements View.OnClickListener{

    int correctAnswer;
    Button buttonObjectChoice1;
    Button buttonObjectChoice2;
    Button buttonObjectChoice3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The next line loads our UI design to the screen
        setContentView(R.layout.activity_game);

        //Here we initialize all our variables
        int partA = 9;
        int partB = 9;
        correctAnswer = partA * partB;
        int wrongAnswer1 = correctAnswer - 1;
        int wrongAnswer2 = correctAnswer + 1;

        /*Here we get a working object based on either the button
          or TextView class and base as well as link our new objects
          directly to the appropriate UI elements that we created previously*/

        TextView textObjectPartA = (TextView)findViewById(R.id.textPartA);
        TextView textObjectPartB = (TextView)findViewById(R.id.textPartB);
        buttonObjectChoice1 = (Button)findViewById(R.id.buttonChoice1);
        buttonObjectChoice2 = (Button)findViewById(R.id.buttonChoice2);
        buttonObjectChoice3 = (Button)findViewById(R.id.buttonChoice3);

        //Now we use the setText method of the class on our objects
        //to show our variable values on the UI elements.

        textObjectPartA.setText("" + partA);
        textObjectPartB.setText("" + partA);

        //which button receives which answer, at this stage is arbitrary.

        buttonObjectChoice1.setText("" + correctAnswer);
        buttonObjectChoice2.setText("" + wrongAnswer1);
        buttonObjectChoice3.setText("" + wrongAnswer2);

        buttonObjectChoice1.setOnClickListener(this);
        buttonObjectChoice2.setOnClickListener(this);
        buttonObjectChoice3.setOnClickListener(this);


    }//onCreate ends here

    @Override
    public void onClick(View view) {
        //declare a new int to be used in all the cases
        int answerGiven=0;
        switch (view.getId()) {

            case R.id.buttonChoice1:
                //initialize a new int with the value contained in buttonObjectChoice1
                //Remember we put it there ourselves previously
                answerGiven = Integer.parseInt("" + buttonObjectChoice1.getText());

                //is it the right answer?
                if(answerGiven==correctAnswer) {//yay it's the right answer
                    Toast.makeText(getApplicationContext(), "Well done!", Toast.LENGTH_LONG).show();
                }else{//uh oh!
                    Toast.makeText(getApplicationContext(),"Sorry that's wrong", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.buttonChoice2:
                //same as previous case but using the next button
                answerGiven = Integer.parseInt("" + buttonObjectChoice2.getText());
                if(answerGiven==correctAnswer) {
                    Toast.makeText(getApplicationContext(), "Well done!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Sorry that's wrong", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.buttonChoice3:
                //same as previous case but using the next button
                answerGiven = Integer.parseInt("" + buttonObjectChoice3.getText());
                if(answerGiven==correctAnswer) {
                    Toast.makeText(getApplicationContext(), "Well done!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Sorry that's wrong", Toast.LENGTH_LONG).show();
                }
                break;

        }

    }

}