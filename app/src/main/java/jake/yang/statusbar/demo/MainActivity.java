package jake.yang.statusbar.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jake.yang.statusbar.library.CoreStatusBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void statusBarColor(View view){
        CoreStatusBar.setStatusBarColor(this, Color.YELLOW);
        //CoreStatusBar.translucentStatusBarNoFixedPosition(this,true);
    }
}
