package tuners.timmy.timmytuner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView versionView, versionActual, createdView, createdActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        versionView = findViewById(R.id.versionView);
        versionActual = findViewById(R.id.versionActual);
        createdView = findViewById(R.id.createdView);
        createdActual = findViewById(R.id.createdActual);

        versionActual.setText("10.0");
        createdActual.setText("Aleix Lladonosa\nRicard Benitez\nAlex Bru");
    }
}
