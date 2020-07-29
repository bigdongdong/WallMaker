package com.cxd.wallmaker_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cxd.wallmaker.Wall;

public class MainActivity extends AppCompatActivity {
    final ITestService service = WallMakerService.create(ITestService.class);
    private Button get,getp,post,postp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        get = findViewById(R.id.get);
        getp = findViewById(R.id.getp);
        post = findViewById(R.id.post);
        postp = findViewById(R.id.postp);

        final Wall.IAccept<Integer> success = new Wall.IAccept<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                Toast.makeText(MainActivity.this,String.valueOf(integer),Toast.LENGTH_SHORT).show();
            }
        };

        final Wall.IFAccept fail = new Wall.IFAccept() {
            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.get().request(success,fail);
            }
        });
        getp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.get("getp").request(success,fail);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.post().request(success,fail);
            }
        });
        postp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.post("postp").request(success,fail);
            }
        });
    }
}
