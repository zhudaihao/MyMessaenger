package edu.hrbeu.SimpleMathServiceDemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

/**
 * 客户端
 */

public class TextActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian);
        //绑定启动service
        bindService(
                new Intent(this, TextService.class),
                coon,
                Context.BIND_AUTO_CREATE);
    }


    /**
     * 创建发送消息的messenger对象
     */
    private Messenger sendMessenger = null;

    //绑定启动service的回调
    private ServiceConnection coon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //service启动成功
            //创建发送消息的messenger对象
            sendMessenger = new Messenger(iBinder);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //service启动失败
            sendMessenger = null;
        }
    };

    /**
     * 创建接受信息的messenger对象
     */
    private Messenger getMessenger = new Messenger(new MyHandler());

    public class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /**
             * 接受信息
             */
            Toast.makeText(TextActivity.this, msg.getData().get("text").toString(), Toast.LENGTH_SHORT).show();

        }
    }


    //发送消息
    public void send(View view) {
        //发送消息
        Message message = Message.obtain();

        //通过message把messenger传递给service发送消息
        message.replyTo = getMessenger;

        Bundle data = new Bundle();
        data.putString("text", "客服端发送的信息");

        message.setData(data);
        try {
            sendMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
