package com.example.taskmanager.buss;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taskmanager.R;

public class TaskViewHolder extends ViewHolder {
	LinearLayout mLayout;
	ImageView mIvIcon;
	ImageView mIvThumb;
	TextView mTvClose;

	public TaskViewHolder(View layout, TasksAdapter adapter) {
		super(layout);
		mLayout = (LinearLayout) layout;
		mIvIcon = (ImageView) mLayout.findViewById(R.id.iv_task_icon);
		mIvThumb = (ImageView) mLayout.findViewById(R.id.iv_task_thumbnail);
		mTvClose = (TextView) mLayout.findViewById(R.id.tv_close);
	}

	public void setIcon(Drawable d) {
		mIvIcon.setImageDrawable(d);
	}

	public void setThumbnail(Bitmap bm) {
		mIvThumb.setImageBitmap(bm);
	}

	public void hide() {
		mLayout.setVisibility(View.GONE);
	}
}
