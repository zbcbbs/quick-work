package com.dongzz.quick.modules.mnt.websocket;

import lombok.Data;

/**
 * 消息
 */
@Data
public class SocketMsg {

    private String msg;
    private MsgType msgType;


    public SocketMsg(String msg, MsgType msgType) {
        this.msg = msg;
        this.msgType = msgType;
    }

}
