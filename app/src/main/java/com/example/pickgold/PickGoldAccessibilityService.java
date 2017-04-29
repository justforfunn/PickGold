package com.example.pickgold;

import android.accessibilityservice.AccessibilityService;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class PickGoldAccessibilityService extends AccessibilityService {
    private static final String TAG="PickGoldService";

    private static MyNode mMyNode;
    private static boolean mFlag;
    private static Queue<Golds> mGoldsQueue;
    private static Queue<Boolean> mBooleanQueue;
    private static double mSum;

    public static void init() {
        mMyNode = MyNodeList.resetQueue();
        //判断是否捡完金币，捡完则置为真然后等待重置
        mFlag=false;

        mSum=0;

        if (mGoldsQueue==null){
            mGoldsQueue=new LinkedList<>();
        }else {
            mGoldsQueue.clear();
        }

        if (mBooleanQueue==null){
            mBooleanQueue=new LinkedList<>();
        }else {
            mBooleanQueue.clear();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        Log.d(TAG, "辅助服务 onCreate");
    }

    /**
     * 接受事件
     * @param event 事件
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        可以获取事件类型并执行相应的方法
//        int type= event.getEventType();
        handleEvent(event);
//        switch (type)
//        {
//            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
//                //通知栏事件，Toast
//                Log.d(TAG,"事件类型：TYPE_NOTIFICATION_STATE_CHANGED");
//
//                break;
//            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//                //窗体内容改变
//                Log.d(TAG,"事件类型：TYPE_WINDOW_CONTENT_CHANGED");
////                handleEvent(event);
//                break;
//            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                //窗体状态改变
//                Log.d(TAG,"事件类型：TYPE_WINDOW_STATE_CHANGED");
////                handleEvent(event);
//                break;
//            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
//                //View获取到焦点
//                Log.d(TAG,"事件类型：TYPE_VIEW_ACCESSIBILITY_FOCUSED");
////                handleEvent(event);
//                break;
//            case AccessibilityEvent.TYPE_VIEW_CLICKED:
//                //点击事件
//                Log.d(TAG,"事件类型：TYPE_VIEW_CLICKED");
////                handleEvent(event);
//                break;
//            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
//                //滑动事件
//                Log.d(TAG,"事件类型：TYPE_VIEW_SCROLLED");
////                handleEvent(event);
//                break;
//            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
//                Log.d(TAG,"事件类型：TYPE_VIEW_ACCESSIBILITY_FOCUSED");
//                break;
//            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
//                Log.d(TAG,"事件类型：TYPE_GESTURE_DETECTION_END");
//                break;
//            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
//                Log.d(TAG,"事件类型：TYPE_VIEW_TEXT_CHANGED");
//                break;
//            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
//                Log.d(TAG,"事件类型：TYPE_VIEW_TEXT_SELECTION_CHANGED");
//                break;
//            case AccessibilityEvent.TYPES_ALL_MASK:
//                Log.d(TAG,"其他类型");
//                break;
//            default:
//                Log.d(TAG,"未匹配到事件类型");
//                break;
//        }

    }

    private void handleEvent(AccessibilityEvent event){
//         AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//         event.getSource()
//         获取活动窗口的节点信息，有时候并不是顶层节点
//         节点一定要可点击，节点的id 或 text 属性可以为空
//         点击的速度太快可能导致软件崩溃，节点需要自行回收
//         1.可以通过getParent(),getChild(int)以及一系列的判断（节点的属性）去找到指定节点
//         2.通过一个节点 返回根节点 然后遍历所有节点  然后过滤节点  然后点击
//         3.通过一个节点 返回根节点 通过findAccessibilityNodeInfosByViewId(int)找到指定节点

        if (event.getText()!=null){
            //当前事件的文本信息
            List<CharSequence> charSequenceList=event.getText();
            Log.d(TAG,"当前事件文本信息："+event.getText().toString());
            //TODO
            if (charSequenceList.size()==1
                    &&(charSequenceList.get(0).toString().equals("亲，你下手慢了哟")
                    ||charSequenceList.get(0).toString().equals("Ta今天已不能再被捡了哟~"))){
                //判断是否捡成功，要么金币数发生变化，要么toast这句话
                mBooleanQueue.add(false);
                Log.d(TAG,"mBooleanQueue add "+false+" "+mBooleanQueue.size());
                return;
            }
        }

        AccessibilityNodeInfo nodeInfo = event.getSource();

        if((nodeInfo==null)||(!MyNodeList.HE_WO_XIN_PACKAGE_NAME.equals(event.getPackageName()))||(!MainActivity.sIsButtonClicked)) {
            return;
        }
        //如果事件源节点信息不为空，且是目标app的事件，则执行相应操作
        //如果需要点击的节点为空则先判断是不是弹空的，然后判断是否要重置节点，然后再返回不执行点击事件
        if (mMyNode == null) {
            //每次捡完金币Toast一次，然后重置值
            //金币为空说明服务（所有属性）被重置了，然后没有开始捡金币
            if (!mFlag){
                mFlag=true;

                BigDecimal count=new BigDecimal(0);

                for (int i=0,length=mGoldsQueue.size();i<length;i++) {
                    Golds golds=mGoldsQueue.poll();
                    Boolean flag=mBooleanQueue.poll();//有可能为空
                    if (flag!=null){
                        if (!flag){
                            continue;
                        }
                    }
                    count=count.add(golds.getNumber());
                    if (i==length-1){
                        DataList.getInstance(PickGoldAccessibilityService.this.getApplicationContext())
                                .addGolds(golds,true);
                    }else {
                        DataList.getInstance(PickGoldAccessibilityService.this.getApplicationContext())
                                .addGolds(golds,false);
                    }
                }

                if (count.compareTo(new BigDecimal(0))==0){
                    //一个金币都没捡到
                    DataList.getInstance(PickGoldAccessibilityService.this.getApplicationContext())
                            .addGolds(new Golds(new BigDecimal(0),"无"),true);
                }

                Toast.makeText(this.getApplicationContext(),"此次所捡的流量："+count.toString()+"MB",Toast.LENGTH_LONG).show();

                MainActivity.sIsButtonClicked=false;

                init();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //返回app
                        for (int i=0;i<3;i++){
                            PickGoldAccessibilityService.this.
                                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

            }
            return;
        }
        clickTargetNode(nodeInfo);
        nodeInfo.recycle();
    }



    private void clickTargetNode(@Nullable AccessibilityNodeInfo nodeInfo) {
        //先返回到根节点，然后再找指定节点
        //nodeInfo非空，只回收自己的创建的节点
        if (nodeInfo==null)
            return;

        AccessibilityNodeInfo top = getTopNode(nodeInfo);
//        logAllChild(top);

        String text=getSubNodeText(top,MyNodeList.GOLD_NUMBER_ID_TEXT_VIEW);

        if (text!=null){
            try {
                //获取总金币数
                double temp=Double.parseDouble(text);
                if (mSum==0&&temp>0){
                    mSum=temp;
                    Log.d(TAG,"初始化mSum="+mSum);
                }else if (temp>mSum){
                    mSum=temp;
                    mBooleanQueue.add(true);
                    Log.d(TAG,"mSum="+mSum);
                    Log.d(TAG,"mBooleanQueue add "+true+" "+mBooleanQueue.size());
                }
            }catch (Exception e){
                Log.e(TAG,"转型异常");
            }
        }

        List<AccessibilityNodeInfo> targetNodeList=top.findAccessibilityNodeInfosByViewId(mMyNode.getId());

        if (targetNodeList==null)
            return;
        //标记当前节点是否点击成功
        boolean isClicked=false;
        for (int i=0,length=targetNodeList.size();i<length;i++){
            AccessibilityNodeInfo targetNode=targetNodeList.get(i);

            if (targetNode==null||(!targetNode.isClickable())){
                //节点为空或者不能点击
                continue;
            }
            if (length==6){
                //如果子节点有6个 说明在捡金币的界面 需要单独处理
                //可以通过目标节点找到相关子节点，然后就可以获取用户名或者流量信息
                String goldNumber = getSubNodeText(targetNode,MyNodeList.GOLD_OWNER_NUMBER_ID_TEXT_VIEW);
                if (goldNumber == null) {
                    continue;
                }
                if (i==5){
                    //到了最后一个节点，不管有没有流量捡，都必须切换节点
                    isClicked=true;
                }
                if (goldNumber.equals("0MB")||goldNumber.equals("邀请\n+50M")||goldNumber.length()<3){
                    continue;
                }
                String owner = getSubNodeText(targetNode, MyNodeList.GOLD_OWNER_NAME_ID_TEXT_VIEW);

                //可能有误差 TODO 等有金币捡的时候看下怎么计算
                BigDecimal bigDecimal=new BigDecimal(Double.parseDouble(goldNumber.substring(0,goldNumber.length()-2)))
                        .divide(new BigDecimal(2))
                        .setScale(2, RoundingMode.HALF_UP);

                mGoldsQueue.add(new Golds(bigDecimal,owner));

                Log.d(TAG,"此次获取到的流量为："+bigDecimal.toString());
            }

            isClicked=targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d(TAG,"正在点击："+mMyNode.getText());

            targetNode.recycle();

            try {
                Thread.sleep(1000*mMyNode.getSleepTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isClicked){
            mMyNode = MyNodeList.pollNode();
        }
    }

    private AccessibilityNodeInfo getTopNode(AccessibilityNodeInfo nodeInfo) {
        AccessibilityNodeInfo top=nodeInfo;
        AccessibilityNodeInfo parent=nodeInfo.getParent();
        while (parent!=null){
            top=parent;
            parent=parent.getParent();
        }
        return top;
    }

    /**
     * 从一个父布局中找子布局中控件的text值
     * @param targetNode
     * @param id
     * @return
     */
    @Nullable
    private String getSubNodeText(AccessibilityNodeInfo targetNode,String id) {
        List<AccessibilityNodeInfo> goldSubNodeList=targetNode.findAccessibilityNodeInfosByViewId(id);
        if (goldSubNodeList==null||goldSubNodeList.size()<=0){
            return null;
        }
        AccessibilityNodeInfo textNode=goldSubNodeList.get(0);
        String text= textNode.getText().toString();

        textNode.recycle();
        return text;
    }

    /**
     * 打印所有子节点信息，供测试使用
     * 如果需要遍历所有节点，则需要先返回到根节点
     * @param nodeInfo 节点
     */
    private void logAllChild(@Nullable AccessibilityNodeInfo nodeInfo){
        if (nodeInfo==null)
            return;
        for (int i=0,length=nodeInfo.getChildCount();i<length;i++)
        {
            AccessibilityNodeInfo accessibilityNodeInfo=nodeInfo.getChild(i);
            if (accessibilityNodeInfo==null)
                continue;
            log(accessibilityNodeInfo);
            logAllChild(accessibilityNodeInfo);

            accessibilityNodeInfo.recycle();
        }
    }

    /**
     * 打印单个节点信息
     * @param nodeInfo 节点
     */
    private void log(@Nullable AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo==null)
            return;
//        if (!nodeInfo.isCheckable())
//            return;
//        if ((!nodeInfo.isCheckable())||nodeInfo.getText()==null)
//            return;
        Log.d(TAG,"节点信息："+nodeInfo.toString());
        Log.d(TAG,"text信息："+nodeInfo.getText());
        Log.d(TAG,"是否可以点击："+nodeInfo.isClickable());
        Log.d(TAG,"包名："+nodeInfo.getPackageName());
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "辅助服务中断");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "辅助服务 onDestroy");
    }
}