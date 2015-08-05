package com.meidusa.amoeba.monitor.net;

import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.meidusa.amoeba.context.ProxyRuntimeContext;
import com.meidusa.amoeba.monitor.MonitorConstant;
import com.meidusa.amoeba.monitor.io.MonitorPacketInputStream;
import com.meidusa.amoeba.monitor.packet.MonitorCommandPacket;
import com.meidusa.amoeba.net.Connection;
import com.meidusa.amoeba.net.io.PacketInputStream;
import com.meidusa.amoeba.net.io.PacketOutputStream;

public class MonitorClientConnection extends Connection {
	private static Logger logger = Logger.getLogger(MonitorClientConnection.class);
	
	public MonitorClientConnection(SocketChannel channel, long createStamp) {
		super(channel, createStamp);
	}

	@Override
	protected PacketInputStream createPacketInputStream() {
		return new MonitorPacketInputStream();
	}

	@Override
	protected PacketOutputStream createPacketOutputStream() {
		return null;
	}

	protected void doReceiveMessage(byte[] message){
		MonitorCommandPacket packet = new MonitorCommandPacket();
		packet.init(message, this);
		switch(packet.funType){
		case MonitorConstant.FUN_TYPE_PING:
			packet.funType = MonitorConstant.FUN_TYPE_OK;
			this.postMessage(packet.toByteBuffer(this));
			break;
		case MonitorConstant.FUN_TYPE_AMOEBA_SHUTDOWN:{
			logger.warn("shutdown command from IP="+this.getSocketId()+" , amoeba shutting down....");
			this.postMessage(packet.toByteBuffer(this));
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
			}
			System.exit(0);
			break;
		}
		//增加reload命令
		case MonitorConstant.FUN_TYPE_AMOEBA_RELOAD:{
			logger.warn("reloading amoeba ....");
			//初始化数据库使用率
			ProxyRuntimeContext.getInstance().loadDbserverUsage();
			//初始化数据库信息
			ProxyRuntimeContext.getInstance().loadDbserverInfo();
			//初始化用户和数据库对照关系
			ProxyRuntimeContext.getInstance().loadUserDbserver();
			packet.funType = MonitorConstant.FUN_TYPE_OK;
			this.postMessage(packet.toByteBuffer(this));
			break;
		}
			default:{
				packet.funType = MonitorConstant.FUN_TYPE_OK;
				this.postMessage(packet.toByteBuffer(this));
				break;
			}
		}
		
    }
    
    protected void messageProcess() {
    }
}
