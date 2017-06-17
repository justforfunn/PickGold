package com.example.pickgold;

import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.Queue;

public class MyNodeList {
    public static final String HE_WO_XIN_PACKAGE_NAME ="com.jx.cmcc.ict.ibelieve";//和我信包名
    public static final String COMMUNICATION_LAYOUT = "com.jx.cmcc.ict.ibelieve:id/ny";//通信
    public static final String STRING_OF_GOLD = "com.jx.cmcc.ict.ibelieve:id/anh";//金币
    public static final String GOLD_OWNER_LAYOUT = "com.jx.cmcc.ict.ibelieve:id/ri";//金币主layout
    public static final String CHANGE_LAYOUT = "com.jx.cmcc.ict.ibelieve:id/rb";//换一批
    //上面都是可点击的 下面的不一定 但是都是有text属性的
    public static final String GOLD_NUMBER_ID_TEXT_VIEW ="com.jx.cmcc.ict.ibelieve:id/qp";//个人的总金币数TextView
    public static final String GOLD_OWNER_NUMBER_ID_TEXT_VIEW ="com.jx.cmcc.ict.ibelieve:id/qx";//金币主的金币数TextView
    public static final String GOLD_OWNER_NAME_ID_TEXT_VIEW ="com.jx.cmcc.ict.ibelieve:id/qu";//金币主名字TextView


    private static int sTimes = 10;
    private static Queue<MyNode> sQueue;

    public static @Nullable MyNode pollNode() {
        if (sQueue ==null){
            sQueue =new LinkedList<>();
            sQueue.offer(new MyNode("通信", COMMUNICATION_LAYOUT,"android.widget.RadioButton",3));
            sQueue.offer(new MyNode("金币", STRING_OF_GOLD,"android.widget.LinearLayout",3));
            for (int i = 0; i< sTimes; i++){
                sQueue.offer(new MyNode("金币主", GOLD_OWNER_LAYOUT,"android.widget.LinearLayout",2));
                sQueue.offer(new MyNode("换一批", CHANGE_LAYOUT,"android.widget.LinearLayout",4));
            }
            sQueue.offer(new MyNode("金币主",GOLD_OWNER_LAYOUT,"android.widget.LinearLayout",4));
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
