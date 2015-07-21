package com.onefengma.commander.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.onefengma.commander.MultiVoiceActivity;
import com.onefengma.commander.R;
import com.onefengma.commander.config.Constant.RequesetCode;
import com.onefengma.commander.model.BaseChatMessage;
import com.onefengma.commander.model.Group;
import com.onefengma.commander.model.TextChatMessage;
import com.onefengma.commander.utils.CaptureImageHelper;
import com.onefengma.commander.utils.InputUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity implements View.OnClickListener,
		View.OnFocusChangeListener {

	private static String TAG = "----" + ChatActivity.class;

	private static String EXTRA_GROUP = "extra_group";

	private ListView chatListView;
	private ChatListAdapter chatListAdapter;
	private View addView;
	private EditText editView;
	private View panelView;
	private Button sendButton;
	private Group group;
	
	private Runnable scrollToBottomRunnable = new Runnable() {
		@Override
		public void run() {
			chatListView.smoothScrollToPosition(chatListAdapter.getCount());
		}
	};

	private Runnable setSelectionRunnable = new Runnable() {
		@Override
		public void run() {
			chatListView.setSelection(chatListAdapter.getCount());
		}
	};

	private DialogInterface.OnClickListener onCaptreImageListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface arg0, int pos) {
			if (pos == 0) {
				CaptureImageHelper.captureFromCamera(ChatActivity.this);
			} else if (pos == 1) {
				CaptureImageHelper.captureFromAublm(ChatActivity.this);
			}
		}
	};
	
	private DialogInterface.OnClickListener onCaptreVedioListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface arg0, int pos) {
			if (pos == 0) {
				CaptureImageHelper.captureVideoFromCamera(ChatActivity.this);
			} else if (pos == 1) {
				CaptureImageHelper.captureVideoFromAublm(ChatActivity.this);
			}
		}
	};
	
	public static void startFrom(Activity activity, Group group) {
		Intent intent = new Intent(activity, ChatActivity.class);
		intent.putExtra(EXTRA_GROUP, group);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		group = (Group) getIntent().getSerializableExtra(EXTRA_GROUP);
		initActionBar();
		initViews();
	}

	public void initActionBar() {
		// FIXME mock
		getSupportActionBar().setIcon(R.mipmap.ic_launcher);
		getSupportActionBar().setTitle(group.getGroupName());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void initViews() {
		chatListView = (ListView) findViewById(R.id.chat_list);
		chatListAdapter = new ChatListAdapter(this);
		chatListAdapter.setChatMessages(getChatMessages());
		chatListView.setAdapter(chatListAdapter);

		chatListView.post(setSelectionRunnable);

		addView = findViewById(R.id.add);
		addView.setOnClickListener(this);

		sendButton = (Button) findViewById(R.id.send);
		sendButton.setOnClickListener(this);

		editView = (EditText) findViewById(R.id.edit);
		editView.setOnClickListener(this);

		editView.setOnFocusChangeListener(this);

		panelView = findViewById(R.id.panel);
	}

	public List<BaseChatMessage> getChatMessages() {
		// FIXME mock
		List<BaseChatMessage> messages = new ArrayList<BaseChatMessage>();
		for (int i = 0; i < 54; i++) {
			TextChatMessage textChatMessage = new TextChatMessage();
			textChatMessage.setMessage("这是一条自动生成的消息：" + i);
			if (i % 5 == 0) {
				textChatMessage.setIsLoginUser(true);
				textChatMessage.setUserName("金刚狼");
			} else {
				textChatMessage.setIsLoginUser(false);
				textChatMessage.setUserName("诸葛亮");
			}
			messages.add(textChatMessage);
		}
		return messages;
	}

	public void onItemClick(View view) {
		// TODO 按钮逻辑
		switch (view.getId()) {
		case R.id.pic:
			getCaptureImageDialog().show();
			break;
		case R.id.record:
			getCaptureVedioDialog().show();
			break;
		case R.id.voice:
			MultiVoiceActivity.startFrom(this, group);
			break;
		case R.id.vedio:
			break;
		case R.id.position:
			break;
		case R.id.file:
			CaptureImageHelper.captureFile(this);
			break;
		}
	}
	
	private AlertDialog getCaptureVedioDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(new String[]{"录像", "选择视频"}, onCaptreVedioListener);
		builder.setNegativeButton(R.string.dialog_item, null);
		return builder.create();
	}
	
	private AlertDialog getCaptureImageDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(new String[]{"拍照", "从相册选择"}, onCaptreImageListener);
		builder.setNegativeButton(R.string.dialog_item, null);
		return builder.create();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			ChatSettingActivity.startFrom(this, group);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			Log.i(TAG, "add Button clicked");
			onAddClick();
			break;
		case R.id.edit:
			Log.i(TAG, "edit clicked");
			onEditClick();
			break;
		case R.id.send:
			Log.i(TAG, "send clicked");
			onSendClick();
			break;
		}
	}

	private void onEditClick() {
		panelView.setVisibility(View.GONE);
		panelView.postDelayed(setSelectionRunnable, 150);
	}

	private void onAddClick() {
		updatePanle(panelView.getVisibility() == View.VISIBLE);
	}

	private void updatePanle(boolean needGone) {
		if (!needGone) {
			InputUtils.hideSoftKeyboard(panelView);
			panelView.setVisibility(View.VISIBLE);
		} else {
			InputUtils.showSoftKeyboard(panelView);
			panelView.setVisibility(View.GONE);
		}
		panelView.postDelayed(setSelectionRunnable, 150);
	}

	private void onSendClick() {
		// FIXME mock
		TextChatMessage textChatMessage = new TextChatMessage();
		textChatMessage.setIsLoginUser(true);
		textChatMessage.setUserName("金刚狼");
		textChatMessage.setMessage(editView.getText().toString());
		sendMessage(textChatMessage);

		TextChatMessage textChatMessageResp = new TextChatMessage();
		textChatMessageResp.setUserName("诸葛亮");
		textChatMessageResp
				.setMessage(editView.getText().toString() + ", 说得对！");
		sendMessage(textChatMessageResp);

		editView.setText("");
	}

	private void sendMessage(BaseChatMessage message) {
		chatListAdapter.addChatMessage(message);
		chatListView.post(scrollToBottomRunnable);
	}

	@Override
	public void onFocusChange(View view, boolean b) {
		if (view.getId() == R.id.edit) {
			panelView.setVisibility(View.GONE);
			editView.postDelayed(setSelectionRunnable, 150);
		}
	}
	
	@Override
	protected void onActivityResult(int resultCode, int requestCode, Intent data) {
		super.onActivityResult(resultCode, requestCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		
		if (requestCode == RequesetCode.CAPTURE_FROM_CAMERA) {
			// capture photo from camera
		} else if (requestCode == RequesetCode.CAPTURE_FROM_ALBUM) {
			// capture photo from album
		} else if (requestCode == RequesetCode.VEDIO_FROM_CAMERA) {
			// capture video from camera
		} else if (requestCode == RequesetCode.VEDIO_FROM_ALBUM) {
			// capture video from album
		}
	}

}
