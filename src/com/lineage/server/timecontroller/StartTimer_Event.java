package com.lineage.server.timecontroller;

import com.lineage.server.timecontroller.event.T_Special;

/**
 * 活動專用時間軸 初始化啟動<BR>
 * 不能利用活動設置控制的例外時間軸
 * 
 * @author dexc
 * 
 */
public class StartTimer_Event {

	public void start() throws InterruptedException {

		//final WorldChatTimer worldChat = new WorldChatTimer();
		//worldChat.start();
		//Thread.sleep(50);// 延遲

		T_Special.getStart();
		//Thread.sleep(50);// 延遲
	}
}
