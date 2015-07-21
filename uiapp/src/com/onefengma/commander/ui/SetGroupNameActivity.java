package com.onefengma.commander.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.onefengma.commander.R;
import com.onefengma.commander.model.Group;

public class SetGroupNameActivity extends BaseActivity implements TextWatcher {

    private static final String EXTRA_GROUP = "group";
    
    private static final String EXTRA_TYPE = "type";
    public static final int CREATE_GROUP = 1;
    public static final int CHANGE_NAME = 2;

    private EditText editText;
    private MenuItem saveMenuItem;
    private Group group;
    private int type;
    
    public static void startForCreateGroup(Activity activity) {
        Intent intent = new Intent(activity, SetGroupNameActivity.class);
        intent.putExtra(EXTRA_TYPE, CREATE_GROUP);
        activity.startActivity(intent);
    }
    
    public static void startForSetGroupName(Activity activity, Group group) {
        Intent intent = new Intent(activity, SetGroupNameActivity.class);
        intent.putExtra(EXTRA_GROUP, group);
        intent.putExtra(EXTRA_TYPE, CHANGE_NAME);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_group_name);
        group = (Group) getIntent().getSerializableExtra(EXTRA_GROUP);
        type = getIntent().getIntExtra(EXTRA_TYPE, CHANGE_NAME);

        editText = (EditText) findViewById(R.id.group_name);
        editText.addTextChangedListener(this);
        
        setTitle(type == CHANGE_NAME ? R.string.title_activity_change_group_name : R.string.title_activity_create_group);
        
        if (group != null) {
            editText.setText(group.getGroupName());
            editText.setSelection(group.getGroupName().length());
        } else {
        	group = new Group();
            editText.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_group_name, menu);
        saveMenuItem = menu.findItem(R.id.action_save);
        updateSaveMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
        	if (type == CHANGE_NAME) {
        		// 更改群名称
        	} else if (type == CREATE_GROUP) {
        		// 创建群聊
        		SelectGroupMemberActivity.startFrom(this);	
        	}
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        updateSaveMenuItem();
    }

    private void updateSaveMenuItem() {
        if (saveMenuItem == null) {
            return;
        }
        saveMenuItem.setEnabled(editText.getText().length() != 0);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    	
    }
    
}
