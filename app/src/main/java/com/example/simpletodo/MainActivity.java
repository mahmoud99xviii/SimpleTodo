package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item text";
    public static final String KEY_ITEM_POSITION = "time position";
    public static final int EDIT_CODE = 1999;
    private List<String> items;
    private Button btnAdd;
    private EditText etdItem;
    private RecyclerView rvItems;
    private ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etdItem = findViewById(R.id.edtItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        itemsAdapter = new ItemsAdapter(items, new ItemsAdapter.OnLongClickListener() {

            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Removed",Toast.LENGTH_LONG).show();
                saveItems();
            }
        }, new ItemsAdapter.OnClickListener() {

            @Override
            public void onItemClicked(int position) {
                Intent intentEdit = new Intent(MainActivity.this,EditActivity.class);
                intentEdit.putExtra(KEY_ITEM_TEXT,items.get(position));
                intentEdit.putExtra(KEY_ITEM_POSITION,position);
                startActivityForResult(intentEdit,EDIT_CODE);
            }
        });
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String todo = etdItem.getText().toString();
                if (todo.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Value",Toast.LENGTH_LONG).show();
                } else {
                    items.add(todo);
                    itemsAdapter.notifyItemInserted(items.size() - 1);
                    saveItems();
                }
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(),"data.text");
    }
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main","Error reading items",e);
            items = new ArrayList<>();
        }
    }
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("Main","Error write items",e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_CODE) {
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(position,itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();
        }
    }
}
