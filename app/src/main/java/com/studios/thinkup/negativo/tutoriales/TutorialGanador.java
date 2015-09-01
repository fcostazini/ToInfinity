package com.studios.thinkup.negativo.tutoriales;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.studios.thinkup.negativo.R;

public class TutorialGanador extends AppCompatActivity {
    ImageView hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_ganador);
        hand = (ImageView) findViewById(R.id.hand);

        Button anterior = (Button) findViewById(R.id.btn_anterior);
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TutorialGanador.this, TutorialSimplificar.class);
                startActivity(i);
            }
        });

    }

}
