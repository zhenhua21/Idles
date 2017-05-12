package com.example.wanqing;

import android.content.Context;
import android.content.Intent;

import com.example.wanqing.activity.MainActivity;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

/**
 * Created by dahuahua on 2017/4/28.
 */

public class MessageHandler extends BmobIMMessageHandler {
    private Context context;

    public MessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        //当接收到服务器发来的消息时，此方法被调用
        new executeMessage(messageEvent);
    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        Map<String,List<MessageEvent>> map = offlineMessageEvent.getEventMap();
        //Logger.i("离线消息属于" + map.size() + "个用户");
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list =entry.getValue();
            int size = list.size();
            for(int i=0;i<size;i++){
                new executeMessage(list.get(i));
            }
        }
    }

    private class executeMessage extends BmobListener1{
        public executeMessage(final MessageEvent event) {
            BmobIMMessage msg = event.getMessage();

            if (BmobNotificationManager.getInstance(context).isShowNotification()) {//如果需要显示通知栏，SDK提供以下两种显示方式：
                Intent pendingIntent = new Intent(context, MainActivity.class);
                pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //1、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
                BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);
                //2、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
//                        BmobIMUserInfo info =event.getFromUserInfo();
//                        //这里可以是应用图标，也可以将聊天头像转成bitmap
//                        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//                        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
//                                info.getName(),msg.getContent(),"您有一条新消息",pendingIntent);
            } else {//直接发送消息事件
                //Logger.i("当前处于应用内，发送event");
                EventBus.getDefault().post(event);

            }
        }

        @Override
        protected void postDone(Object o, BmobException e) {

        }
    }

//    /**
//     * 处理消息
//     * @param event
//     */
//    private void excuteMessage(final MessageEvent event){
//        //检测用户信息是否需要更新
//        UserModel.getInstance().updateUserInfo(event, new UpdateCacheListener() {
//            @Override
//            public void done(BmobException e) {
//                BmobIMMessage msg = event.getMessage();
//                if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {//用户自定义的消息类型，其类型值均为0
//                    processCustomMessage(msg, event.getFromUserInfo());
//                } else {//SDK内部内部支持的消息类型
//                    if (BmobNotificationManager.getInstance(context).isShowNotification()) {//如果需要显示通知栏，SDK提供以下两种显示方式：
//                        Intent pendingIntent = new Intent(context, MainActivity.class);
//                        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        //1、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
//                        BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);
//                        //2、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
////                        BmobIMUserInfo info =event.getFromUserInfo();
////                        //这里可以是应用图标，也可以将聊天头像转成bitmap
////                        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
////                        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
////                                info.getName(),msg.getContent(),"您有一条新消息",pendingIntent);
//                    } else {//直接发送消息事件
//                        //Logger.i("当前处于应用内，发送event");
//                        EventBus.getDefault().post(event);
//                    }
//                }
//            }
//        });
//    }
}
