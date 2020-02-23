package id.putraprima.skorbola;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.Serializable;

public class MatchActivity extends AppCompatActivity implements Serializable {

    TextView homeName, awayName;
    TextView awayScore, homeScore;
    ImageView homeLogo, awayLogo;
    Uri uri1, uri2;
    Bitmap bitmap1, bitmap2;
    String homeTeam, message, result, awayTeam;
    int scoreHome, scoreAway;
    String scorerNameHome = "";
    String scorerNameAway = "";
    String homeGoals = "";
    String awayGoals = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        //TODO
        homeName = findViewById(R.id.txt_home);
        awayName = findViewById(R.id.txt_away);
        homeLogo = findViewById(R.id.home_logo);
        awayLogo = findViewById(R.id.away_logo);
        awayScore = findViewById(R.id.score_away);
        homeScore = findViewById(R.id.score_home);


        //TODO
        //1.Menampilkan detail match sesuai data dari main activity
        //2.Tombol add score menambahkan satu angka dari angka 0, setiap kali di tekan
        //3.Tombol Cek Result menghitung pemenang dari kedua tim dan mengirim nama pemenang ke ResultActivity, jika seri di kirim text "Draw"
        Bundle extras = getIntent().getExtras();
        homeTeam = extras.getString("inputHome");
        awayTeam = extras.getString("inputAway");


        if (extras != null) {
            // TODO: display value here

            uri1 = Uri.parse(extras.getString("logoHome"));
            uri2 = Uri.parse(extras.getString("logoAway"));
            bitmap1 = null;
            bitmap2 = null;

            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            homeName.setText(homeTeam);
            awayName.setText(awayTeam);
            homeLogo.setImageBitmap(bitmap1);
            awayLogo.setImageBitmap(bitmap2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }
        if (requestCode == 1) {
            homeGoals = data.getStringExtra("homeGoalers") + ", " + homeGoals;
            scoreHome = data.getIntExtra("scoreHome", 0);
            homeScore.setText(String.valueOf(scoreHome));

        } else if (requestCode == 2) {
            awayGoals = data.getStringExtra("awayGoalers") + ", " + awayGoals;
            scoreAway = data.getIntExtra("scoreAway", 0);
            awayScore.setText(String.valueOf(scoreAway));
        }
    }

    public void scoreHomeAdd(View view) {
        Intent intent = new Intent(this, ScorerActivity.class);
        intent.putExtra("key", 1);
        intent.putExtra("dataHome", scoreHome);
        startActivityForResult(intent, 1);
    }

    public void scoreAwayAdd(View view) {
        Intent intent = new Intent(this, ScorerActivity.class);
        intent.putExtra("key", 2);
        intent.putExtra("dataAway", scoreAway);
        startActivityForResult(intent, 2);
    }

    public void result(View view) {
        if (scoreHome > scoreAway) {
            result = scoreHome + " - " + scoreAway;
            message = homeTeam + " WIN";
            scorerNameHome = "Goal Scorer : " + homeGoals;
        } else if (scoreHome < scoreAway) {
            result = scoreHome + " - " + scoreAway;
            message = awayTeam + " WIN";
            scorerNameAway = "Goal Scorer " + awayGoals;
        } else if (scoreHome == scoreAway) {
            result = scoreHome + " - " + scoreAway;
            message = "DRAW";
//            scorerNameHome = "Home : "+homeGoals;
//            scorerNameAway = "Away : "+awayGoals;
        }
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("result", result);
        intent.putExtra("messages", message);
        intent.putExtra("scorerHome", scorerNameHome);
        intent.putExtra("scorerAway", scorerNameAway);
        startActivity(intent);
    }
}
