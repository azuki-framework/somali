/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkwf.somali.task;

import java.util.ArrayList;
import java.util.List;

/**
 * このクラスは、タスク管理を行うマネージャークラスです。
 * 
 * @author Kawakicchi
 */
public class TaskManager {

	private static final TaskManager INSTANCE = new TaskManager();

	private List<ThreadTask> tasks;

	private TaskManagerStatus status;

	private boolean stop;

	/**
	 * コンストラクタ
	 */
	private TaskManager() {
		tasks = new ArrayList<ThreadTask>();
		status = TaskManagerStatus.Idle;
		stop = false;
	}

	/**
	 * インスタンスを取得する。
	 * 
	 * @return インスタンス
	 */
	public static TaskManager getInstance() {
		return INSTANCE;
	}

	/**
	 * タスクをキューに追加する。
	 * 
	 * @param task タスク
	 */
	public void queue(final Task task) {
		ThreadTask t = new ThreadTask(task);
		tasks.add(t);
	}

	public synchronized void start() {
		synchronized (status) {
			if (TaskManagerStatus.Idle == status) {
				status = TaskManagerStatus.Active;
				stop = false;
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						main();
						status = TaskManagerStatus.Idle;
					}
				});
				thread.start();
			}
		}
	}

	public void stop() {
		stop = true;
	}

	public void stopWait() {
		try {
			while (TaskManagerStatus.Idle != status) {
				stop = true;
				Thread.sleep(500);
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private void main() {
		while (!stop) {
			int activeCount = getActiveTaskCount();
			if (5 <= activeCount) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {

				}
				continue;
			}

			int j = 0;
			int taskSize = tasks.size();
			for (int i = activeCount; i < 5; i++) {
				for (; j < taskSize; j++) {
					ThreadTask tt = tasks.get(j);
					if (TaskStatus.Waiting == tt.getStatus()) {
						tt.execute();
						break;
					}
				}
				if (taskSize <= j) {
					break;
				}
			}
			if (j == taskSize) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {

				}
			}
		}
	}

	private int getActiveTaskCount() {
		int cnt = 0;
		synchronized (status) {
			for (ThreadTask task : tasks) {
				if (TaskStatus.Running == task.getStatus()) {
					cnt++;
				}
			}
		}
		return cnt;
	}

	private class ThreadTask {

		private Task task;

		private TaskStatus status;

		private Thread thread;

		public ThreadTask(final Task t) {
			task = t;
			status = TaskStatus.Waiting;
			thread = null;
		}

		public void execute() {
			synchronized (status) {
				if (TaskStatus.Waiting == status) {
					status = TaskStatus.Running;
					thread = new Thread(new Runnable() {
						@Override
						public void run() {
							task.execute();
							status = TaskStatus.Completed;
						}
					});
					thread.start();
				}
			}
		}

		public TaskStatus getStatus() {
			return status;
		}

	}
}
