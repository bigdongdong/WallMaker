package com.cxd.wallmaker_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cxd.wallmaker.Wall;
import com.cxd.wallmaker.WallMaker;

public class MainActivity extends AppCompatActivity {
    final IAccountService service = WallMakerService.create(IAccountService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                service.get("aaa")
//                        .request(new Wall.IAccept<VersionBean>() {
//                            @Override
//                            public void onSuccess(VersionBean versionBeanCommon) {
//                            }
//
//                        });
                service.login("cxd","xxx")
                        .request(new Wall.IAccept<Object>() {
                            @Override
                            public void onSuccess(Object t) {
                                Log.i("WallMaker", "onSuccess: "+t);
                            }
                        });
            }
        });
    }
}
