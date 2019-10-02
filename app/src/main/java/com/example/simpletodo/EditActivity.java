package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText edtChangeItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final String todo = getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT);

        edtChangeItem = findViewById(R.id.edtChangeItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle(todo);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent();
                if (edtChangeItem.getText().toString().isEmpty()) {
                    intentMain.putExtra(MainActivity.KEY_ITEM_TEXT,todo);
                    intentMain.putExtra(MainActivity.KEY_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                    setResult(RESULT_OK,intentMain);
                    finish();
                } else {
                    intentMain.putExtra(MainActivity.KEY_ITEM_TEXT,edtChangeItem.getText().toString());
                    intentMain.putExtra(MainActivity.KEY_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                    setResult(RESULT_OK,intentMain);
                    finish();
                }
            }
        });
    }
}
