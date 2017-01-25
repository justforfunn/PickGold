package com.example.pickgold;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class MyNodeList {

    public static final String HE_WO_XIN_PACKAGE_NAME ="com.jx.cmcc.ict.ibelieve";

    private static int sTimes = 10;
    private static Queue<MyNode> sQueue;

    public static @Nullable MyNode pollNode() {
        if (sQueue ==null){
            sQueue =new LinkedList<>();

//            sQueue.offer(new MyNode("成长","com.jx.cmcc.ict.ibelieve:id/qr","android.widget.RadioButton",0,0));
            sQueue.offer(new MyNode("通信","com.jx.cmcc.ict.ibelieve:id/jw","android.widget.RadioButton",3));
            sQueue.offer(new MyNode("金币","com.jx.cmcc.ict.ibelieve:id/a6o","android.widget.LinearLayout",3));
            for (int i = 0; i< sTimes; i++){
                sQueue.offer(new MyNode("金币主layout","com.jx.cmcc.ict.ibelieve:id/zz","android.widget.LinearLayout",2));
//                   LinearLayout下的可以查看流量的节点
//                   sQueue.offer(new MyNode("总金币数","com.jx.cmcc.ict.ibelieve:id/l8 有变动","android.widget.TextView",0,0));
//                      getText .toString 即可获得金币数
//                   sQueue.offer(new MyNode("金币主text","com.jx.cmcc.ict.ibelieve:id/le","android.widget.TextView",0,0));
//                   sQueue.offer(new MyNode("0 MBor邀请\n+50M","com.jx.cmcc.ict.ibelieve:id/lh","android.widget.TextView",0,0));
                sQueue.offer(new MyNode("换一批","com.jx.cmcc.ict.ibelieve:id/nk","android.widget.LinearLayout",4));
            }
            sQueue.offer(new MyNode("金币主layout","com.jx.cmcc.ict.ibelieve:id/zz","android.widget.LinearLayout",2));
        }

        MyNode myNode=null;

        if (sQueue.size()>0) {
            myNode = sQueue.poll();
        }
        return myNode;
    }

    public static MyNode resetQueue(){
        sQueue=null;
        return pollNode();
    }

    /**
     * 重新装配需要点击的节点
     * @param times 换一批的次数
     * @return 需要点击的第一个节点
     */
    public static MyNode resetQueue(int times){
        sTimes=times;
        return resetQueue();
    }

    public static int getTimes(){
        return sTimes;
    }
}
