package com.cxd.wallmaker_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cxd.wallmaker.Wall;
import com.cxd.wallmaker.WallMaker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAccountService service = WallMakerService.create(IAccountService.class);
                service.get()
                        .request(new Wall.IAccept<VersionBean>() {
                            @Override
                            public void onSuccess(VersionBean versionBeanCommon) {
                                Log.i("WallMaker", "onSuccess: "+versionBeanCommon.toString());
                            }

                        });
            }
        });
    }
}
