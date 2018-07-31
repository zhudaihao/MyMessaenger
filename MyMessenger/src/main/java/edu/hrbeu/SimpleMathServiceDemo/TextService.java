package edu.hrbeu.SimpleMathServiceDemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * 服务端
 */

public class TextService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        //通过messenger获取IBinder对象
        return new Messenger(new MyHandler()).getBinder();
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /**
             * 接
             */
            //接受客服端消息
            Toast.makeText(TextService.this, msg.getData().get("text").toString(), Toast.LENGTH_SHORT).show();

            /**
             * 发
             */

            //通过message获取客户端传递过来的messenger对象；发送消息
            Messenger messenger = msg.replyTo;//获取messenger对象

            //通过bundle转载发送的信息
            Bundle bundle = new Bundle();
            bundle.putString("text", "服务端发给客户端");

            //同message装载bundle
            Message message = Message.obtain();
            message.setData(bundle);

            try {
                messenger.send(message);//通过messenger发送消息
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }
    }


}
