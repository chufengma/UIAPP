package com.onefengma.commander.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.onefengma.commander.R;
import com.onefengma.commander.config.Constant.RequesetCode;
import com.onefengma.commander.model.Member;

import java.util.ArrayList;
import java.util.List;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;

public class SelectGroupMemberActivity extends BaseActivity implements MemberListAdapter.OnCheckedStatusChangeListener{

    public static final String EXTRA_TYPE = "TYPE";
    public static final String EXTRA_MEMBERS = "members";

    public static final int TYPE_ADD_MEMBER = 1;
    public static final int TYPE_CREATE_GROUP = 2;

    private PinnedHeaderListView memberList;
    private MemberListAdapter memberListAdapter;

    private MenuItem conformMenu;
    private SearchView searchView;
    private int type;

    public static void startFrom(Activity activity) {
        Intent intent = new Intent(activity, SelectGroupMemberActivity.class);
        intent.putExtra(EXTRA_TYPE, TYPE_CREATE_GROUP);
        activity.startActivityForResult(intent, RequesetCode.SELECT_GROUP_MEMBER);
    }

    public  static void startForAddMemberFrom(Activity activity) {
        Intent intent = new Intent(activity, SelectGroupMemberActivity.class);
        intent.putExtra(EXTRA_TYPE, TYPE_ADD_MEMBER);
        activity.startActivityForResult(intent, RequesetCode.ADD_GROUP_MEMBER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_CREATE_GROUP);

        memberList = (PinnedHeaderListView) findViewById(R.id.member_list);
        memberListAdapter = new MemberListAdapter(this);
        memberListAdapter.setMembers(getMemebers());
        memberList.setAdapter(memberListAdapter);

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint(getString(R.string.search_member));
        searchView.onActionViewExpanded();

        memberListAdapter.setOnCheckedStatusChangeListener(this);
        searchView.post(new Runnable() {
            @Override
            public void run() {
                searchView.clearFocus();
            }
        });
    }

    private List<Member> getMemebers() {
        // FIXME mock
        List<Member> members = new ArrayList<Member>();
        for (int i = 0; i < 12; i++) {
            Member m = new Member();
            m.setId(i + "");
            m.setMemberName("张三丰" + i);
            members.add(m);
        }
        return members;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        conformMenu = menu.findItem(R.id.action_comform);
        updateMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_comform) {
        	Member[] members = new Member[memberListAdapter.getCheckedMembers().size()];
            getIntent().putExtra(EXTRA_MEMBERS, memberListAdapter.getCheckedMembers().toArray(members));
            if (type == TYPE_CREATE_GROUP) {
                // TODO 创建群聊
                DialogManager.getAskJoinGroupDialog(this, "廖凡", "南京海关指挥组", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == DialogInterface.BUTTON_POSITIVE) {
							DialogManager.getJoindGroupDialog(SelectGroupMemberActivity.this, "南京海关指挥组", null).show();
						} else if (which == DialogInterface.BUTTON_NEGATIVE) {
			                ContextToast.show(SelectGroupMemberActivity.this, "您已拒绝加入南京海关指挥组", Toast.LENGTH_SHORT);
						}
					}
				}).show();
            } if (type == TYPE_ADD_MEMBER) {
                // TODO 添加成员
                setResult(RESULT_OK, getIntent());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMenuItems() {
        if (memberListAdapter == null) {
            return;
        }
        conformMenu.setEnabled(memberListAdapter.getCheckedMembers().size() > 0);
    }

    @Override
    public void onCheckStatusChange(List<Member> members) {
        updateMenuItems();
    }
}
