package jp.plen.plencheck;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private boolean isClearChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final VerticalSeekbar vs = (VerticalSeekbar) findViewById(R.id.vertseek);
        final TextView tv = (TextView) findViewById(R.id.textView);

        tv.setText(String.valueOf(vs.getProgress()));
        vs.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tv.setText(String.valueOf(vs.getProgress()));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        final Button bup = (Button) findViewById(R.id.buttonup);
        final Button dup = (Button) findViewById(R.id.buttondown);

        bup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vs.setProgress(vs.getProgress() + 1);
                    }
                }
        );

        dup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vs.setProgress(vs.getProgress() - 1);
                    }
                }
        );

        final RadioGroup radioGroupLeft = (RadioGroup) findViewById(R.id.radioGroupLeft);
        final RadioGroup radioGroupRight = (RadioGroup) findViewById(R.id.radioGroupRight);

        radioGroupLeft.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(isClearChecked){
                    isClearChecked = false;
                } else if (checkedId != -1) {
                    isClearChecked = true;
                    radioGroupRight.clearCheck();
                }
            }
        });

        radioGroupRight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(isClearChecked){
                    isClearChecked = false;
                } else if (checkedId != -1) {
                    isClearChecked = true;
                    radioGroupLeft.clearCheck();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
