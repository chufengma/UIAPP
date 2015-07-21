package com.onefengma.commander.ui;

import com.onefengma.commander.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;

public class DialogManager {
	
	/*询问是否加入指挥组*/
	public static AlertDialog getAskJoinGroupDialog(Activity activity, String who, String groupName, DialogInterface.OnClickListener listener) {
		Builder builder = new Builder(activity);
		builder.setTitle(R.string.hint);
		builder.setMessage(activity.getString(R.string.joint_group, who, groupName));
		builder.setPositiveButton(R.string.btn_join, listener);
		builder.setNegativeButton(R.string.btn_refuse, listener);
		return builder.create();
	}
	
	/*已经加入，是否进入*/
	public static AlertDialog getJoindGroupDialog(Activity activity, String groupName, DialogInterface.OnClickListener listener) {
		Builder builder = new Builder(activity);
		builder.setTitle(R.string.hint);
		builder.setMessage(activity.getString(R.string.joined_group, groupName));
		builder.setPositiveButton(R.string.btn_view_now, listener);
		builder.setNegativeButton(R.string.btn_view_later, listener);
		return builder.create();
	}
	
	/*删除消息记录*/
	public static AlertDialog getEmptyMessageDialog(Activity activity, DialogInterface.OnClickListener listener) {
		Builder builder = new Builder(activity);
		builder.setMessage(R.string.remove_all_message);
		builder.setPositiveButton(R.string.btn_empty, listener);
		builder.setNegativeButton(R.string.btn_cancle, null);
		return builder.create();
	}
	
	
}
