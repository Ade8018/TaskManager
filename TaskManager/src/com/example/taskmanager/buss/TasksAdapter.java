package com.example.taskmanager.buss;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.taskmanager.R;

public class TasksAdapter extends RecyclerView.Adapter<TaskViewHolder>
		implements OnClickListener {
	public interface OnItemListener {
		public void onRemove(View v);

		public void onResume(View v);
	}

	private TaskList mTasks;
	private Context mContext;
	private OnItemListener mRemoveListener;

	public TasksAdapter(Context context, TaskList tl, OnItemListener lis) {
		mTasks = tl;
		mContext = context;
		mRemoveListener = lis;
	}

	@Override
	public int getItemCount() {
		return mTasks.size();
	}

	@Override
	public void onBindViewHolder(TaskViewHolder vh, int position) {
		vh.setIcon(mTasks.getIconDrawable(position));
		vh.setThumbnail(mTasks.getThumbnail(mContext, position));
	}

	@Override
	public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.item_task, parent, false);
		TaskViewHolder vh = new TaskViewHolder(view, this);
		vh.mTvClose.setOnClickListener(this);
		vh.mLayout.setOnClickListener(this);
		return vh;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_close && mRemoveListener != null) {
			mRemoveListener.onRemove((View) v.getParent().getParent());
		} else if (v.getId() == R.id.layout_task_item) {
			mRemoveListener.onResume(v);
		}
	}

}
