package com.example.taskmanager.buss;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.taskmanager.R;
import com.example.taskmanager.buss.TasksAdapter.OnItemListener;

public class TaskFragment extends Fragment implements OnItemListener,
		OnClickListener {
	private TaskList mTasks;
	private RecyclerView mRv;
	private TasksAdapter mAdapter;
	private LinearLayoutManager mLayoutManager;
	private View mView;
	private Button mBtnRemoveAll;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_tasks, container, false);
			findViews();
		}
		setDatas();
		return mView;
	}

	private void findViews() {
		mRv = (RecyclerView) mView.findViewById(R.id.rv_task_recenttask);
		mBtnRemoveAll = (Button) mView.findViewById(R.id.btn_task_removeall);
		mBtnRemoveAll.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	private void setDatas() {
		mLayoutManager = new LinearLayoutManager(getActivity());
		mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRv.setLayoutManager(mLayoutManager);
	}

	public void onResume() {
		super.onResume();
		ActivityManager am = (ActivityManager) getActivity().getSystemService(
				Context.ACTIVITY_SERVICE);
		mTasks = new TaskList(am, getActivity().getPackageManager());
		mAdapter = new TasksAdapter(getActivity(), mTasks, this);

		mRv.setAdapter(mAdapter);
	}

	@Override
	public void onRemove(View v) {
		int position = mLayoutManager.getPosition(v);
		if (position >= 0) {// 如果用户多次点击第一个或最后一个view，可能会因为找不到那个View而返回position为-1。此时只要忽略就好
			mTasks.removeTask(position);
			mAdapter.notifyItemRemoved(position);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnRemoveAll) {
			while (mTasks.size() > 0) {
				mTasks.removeTask(0);
				mAdapter.notifyItemRemoved(0);
			}
		}
	}

	@Override
	public void onResume(View v) {
		int position = mLayoutManager.getPosition(v);
		if (position >= 0) {
			mTasks.resumeTask(getActivity(), position);
		}
	}

}
