package com.example.notificationscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Switches for setting job options.
    private Switch mDeviceIdleSwitch;
    private Switch mDeviceChargingSwitch;

    // Override deadline seekbar.
    private SeekBar mSeekBar;

    private RadioGroup networkOptions;

    private JobScheduler jobScheduler;

    private static final int JOB_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDeviceIdleSwitch = findViewById(R.id.idleSwitch);
        mDeviceChargingSwitch = findViewById(R.id.chargingSwitch);
        mSeekBar = findViewById(R.id.seekBar);
        networkOptions = findViewById(R.id.networkOptions);

        final TextView seekBarProgress = findViewById(R.id.seekBarProgress);


        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress > 0) {

                    seekBarProgress.setText(": RomiRain " + progress);
                } else {

                    seekBarProgress.setText("Not Set");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void scheduleJob(View view) {

        int selectedNetworkId = networkOptions.getCheckedRadioButtonId();
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
        int seekBarInteger = mSeekBar.getProgress();
        boolean seekBarSet = seekBarInteger > 0;

        switch (selectedNetworkId) {

            case R.id.noNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;

            case R.id.anyNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;

            case R.id.wifiNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }


        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationScheduler.class.getName());

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(selectedNetworkOption)
                .setRequiresDeviceIdle(mDeviceIdleSwitch.isChecked())
                .setRequiresCharging(mDeviceChargingSwitch.isChecked());

        if (seekBarSet) {

            builder.setOverrideDeadline(seekBarInteger * 1000);
        }

        boolean constraintSet = selectedNetworkOption
                != JobInfo.NETWORK_TYPE_NONE
                || mDeviceChargingSwitch.isChecked()
                || mDeviceIdleSwitch.isChecked()
                || seekBarSet;

        if (constraintSet) {

            JobInfo myJobInfo = builder.build();

            jobScheduler.schedule(myJobInfo);

            Toast.makeText(this, "Romi Rain Job scheduled", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "Constraint not set", Toast.LENGTH_SHORT).show();

        }

    }

    public void cancelJobs(View view) {

        if (jobScheduler != null) {

            jobScheduler.cancelAll();
            jobScheduler = null;

            Toast.makeText(this, "Romi Rain Job canceled", Toast.LENGTH_SHORT).show();
        }
    }

    public void asyncJob(View view) {

        boolean constraints = mDeviceChargingSwitch.isChecked() || mDeviceIdleSwitch.isChecked();

        if (constraints) {

            ComponentName componentName = new ComponentName(getPackageName(),
                    NotificationAsyncScheduler.class.getName());

            JobInfo.Builder builder = new JobInfo.Builder(3, componentName)
                    .setRequiresCharging(mDeviceChargingSwitch.isChecked())
                    .setRequiresDeviceIdle(mDeviceIdleSwitch.isChecked())
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setOverrideDeadline(4000);

            jobScheduler.schedule(builder.build());

            Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();

        }
    }
}
