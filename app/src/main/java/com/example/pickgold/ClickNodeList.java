package com.example.pickgold;

import android.support.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

public class ClickNodeList {

    public static final String HE_WO_XIN_PACKAGE_NAME ="com.jx.cmcc.ict.ibelieve";

    private static Queue<MyNode> sQueue;

    public static @Nullable MyNode pollNode() {
        if (sQueue ==null){
            sQueue =new LinkedList<>();

            sQueue.offer(new MyNode("通信","com.jx.cmcc.ict.ibelieve:id/qp","android.widget.RadioButton",0,0));
            sQueue.offer(new MyNode("金币","com.jx.cmcc.ict.ibelieve:id/a33","android.widget.LinearLayout",0,0));
            for (int i=0;i<10;i++){
                sQueue.offer(new MyNode("***","com.jx.cmcc.ict.ibelieve:id/a5c","android.widget.LinearLayout",0,0));
                /**
                 *  LinearLayout下的可以查看流量的节点
                 *  sQueue.offer(new MyNode("0 MBor邀请\n+50M","com.jx.cmcc.ict.ibelieve:id/lh","android.widget.TextView",0,0));
                 *  sQueue.offer(new MyNode("成长","com.jx.cmcc.ict.ibelieve:id/qr",0,0));
                 */
                sQueue.offer(new MyNode("换一批","com.jx.cmcc.ict.ibelieve:id/ly","android.widget.LinearLayout",0,0));
            }
            sQueue.offer(new MyNode("***","com.jx.cmcc.ict.ibelieve:id/a5c","android.widget.LinearLayout",0,0));
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

}
