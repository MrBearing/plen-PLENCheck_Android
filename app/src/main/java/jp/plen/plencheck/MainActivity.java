package jp.plen.plencheck;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import jp.plen.plencheck.ble.BLEDevice;


public class MainActivity extends ActionBarActivity implements BLEDevice.BLECallbacks{
    private String TAG = "MainActivity";
    private boolean isClearChecked = false;
    private int checkedNum = R.id.radioButton0;
    private BLEDevice bleDevice;
    private int map[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 12, 13, 14, 15, 16, 17, 18, 19, 20 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final VerticalSeekbar vs = (VerticalSeekbar) findViewById(R.id.vertseek);
        final TextView tv = (TextView) findViewById(R.id.textView);

        tv.setText(String.valueOf(vs.getProgress()));
        vs.setOnSeekBarChangeListener(
                new VerticalSeekbar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tv.setText(String.valueOf(vs.getProgress()));
                        int i = Integer.parseInt(((RadioButton)findViewById(checkedNum)).getText().toString());
                        int value = map[i-1];
                        String hexNum = String.format("%02x", value);
                        String deg = String.format("%03x", vs.getProgress());
                        Log.d(TAG, "#SA" + hexNum + deg);
                        bleDevice.write("#SA" + hexNum + deg);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        tv.setText(String.valueOf(vs.getProgress()));
                        int i = Integer.parseInt(((RadioButton)findViewById(checkedNum)).getText().toString());
                        int value = map[i-1];
                        String hexNum = String.format("%02x", value);
                        String deg = String.format("%03x", vs.getProgress());
                        Log.d(TAG, "#SA" + hexNum + deg);
                        bleDevice.write("#SA" + hexNum + deg);
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
                    vs.setProgress(vs.getMax()/2);
                    tv.setText(String.valueOf(vs.getProgress()));
                    int i = Integer.parseInt(((RadioButton)findViewById(checkedId)).getText().toString());
                    int value = map[i-1];
                    String hexNum = String.format("%02x", value);
                    String deg = String.format("%03x", vs.getProgress());
                    Log.d(TAG, "#SA" + hexNum + deg);
                    bleDevice.write("#SA" + hexNum + deg);

                    checkedNum = checkedId;
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
                    vs.setProgress(vs.getMax()/2);
                    tv.setText(String.valueOf(vs.getProgress()));
                    int i = Integer.parseInt(((RadioButton)findViewById(checkedId)).getText().toString());
                    int value = map[i-1];
                    String hexNum = String.format("%02x", value);
                    String deg = String.format("%03x", vs.getProgress());
                    Log.d(TAG, "#SA" + hexNum + deg);
                    bleDevice.write("#SA" + hexNum + deg);
                    checkedNum = checkedId;
                    isClearChecked = true;
                    radioGroupLeft.clearCheck();
                }
            }
        });

        bleDevice = new BLEDevice(this);
        bleDevice.setBLECallbacks(this);

        Button homeButton = (Button)findViewById(R.id.button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(((RadioButton)findViewById(checkedNum)).getText().toString());
                String hexNum = String.format("%02x", i);
                String deg = String.format("%03x", vs.getProgress());
                bleDevice.write("#HO" + hexNum +deg);
            }
        });

        Button maxButton = (Button)findViewById(R.id.button2);
        maxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(((RadioButton)findViewById(checkedNum)).getText().toString());
                String hexNum = String.format("%02x", i);
                String deg = String.format("%03x", vs.getProgress());
                bleDevice.write("#MA" + hexNum +deg);
            }
        });

        Button minButton = (Button)findViewById(R.id.button3);
        minButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(((RadioButton)findViewById(checkedNum)).getText().toString());
                String hexNum = String.format("%02x", i);
                String deg = String.format("%03x", vs.getProgress());
                bleDevice.write("#MI" + hexNum +deg);
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

    @Override
    public void onConnected(String deviceName) {
        Toast.makeText(this, deviceName + "に接続しました", Toast.LENGTH_LONG).show();
    }
}
