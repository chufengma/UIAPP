package com.onefengma.commander;

import java.util.ArrayList;
import java.util.List;

import com.onefengma.commander.model.Group;
import com.onefengma.commander.model.Member;
import com.onefengma.commander.ui.BaseActivity;
import com.onefengma.commander.ui.ChatMemberLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MultiVoiceActivity extends BaseActivity {

	private static final String EXTRA_GROUP = "group";
	
	private ChatMemberLayout memberLayout;
	private ImageView vedioChat;

	public static void startFrom(Activity activity, Group group) {
		Intent intent = new Intent(activity, MultiVoiceActivity.class);
		intent.putExtra(EXTRA_GROUP, group);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_voice);
		
		Group group = (Group) getIntent().getSerializableExtra(EXTRA_GROUP);
		setTitle(group.getGroupName());
		
		memberLayout = (ChatMemberLayout) findViewById(R.id.member_layout);
		memberLayout.setOneRowCount(5);
		memberLayout.isNeedAdd(false);
		memberLayout.addMembers(getMemebers());
		
		vedioChat = (ImageView) findViewById(R.id.vedio_chat);
		vedioChat.setSelected(true);
	}

	private List<Member> getMemebers() {
		// FIXME mock
		List<Member> members = new ArrayList<Member>();
		for (int i = 0; i < 100; i++) {
			Member m = new Member();
			m.setId(i + "");
			m.setMemberName("张三丰" + i);
			members.add(m);
		}
		return members;
	}

	public void onItemClick(View view) {
		// 控制面板控制事件处理
	}
}
