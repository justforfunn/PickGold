package com.example.pickgold;

import android.accessibilityservice.AccessibilityService;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;


public class PickGoldAccessibilityService extends AccessibilityService {
    private static final String TAG="PickGoldService";

    private MyNode mMyNode;
    private double mCount;
    private boolean mFlag;
    private Golds mGolds;

    private void init() {
        mMyNode = ClickNodeList.resetQueue();
        /**
         * 捡到的金币数量
         */
        mCount=0;
        mFlag=false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        Log.d(TAG, "辅助服务 onCreate");
    }

    /**
     * 接受事件
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        /**
         * 无论什么事件都扔进去处理
         */
        handleEvent(event);
//        /**
//         * 可以获取事件类型并执行相应的方法
//         */
//        int type= event.getEventType();
//        switch (type)
//        {
//            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
//                /**
//                 * 通知栏事件，Toast
//                 */
//                Log.d(TAG,"事件类型：TYPE_NOTIFICATION_STATE_CHANGED");
//                break;
//            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//                /**
//                 * 窗体内容改变
//                 */
//                Log.d(TAG,"事件类型：TYPE_WINDOW_CONTENT_CHANGED");
//                break;
//            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                /**
//                 * 窗体状态改变
//                 */
//                Log.d(TAG,"事件类型：TYPE_WINDOW_STATE_CHANGED");
//                break;
//            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
//                /**
//                 * View获取到焦点
//                 */
//                Log.d(TAG,"事件类型：TYPE_VIEW_ACCESSIBILITY_FOCUSED");
//                break;
//            case AccessibilityEvent.TYPE_VIEW_CLICKED:
//                /**
//                 * 点击事件
//                 */
//                Log.d(TAG,"事件类型：TYPE_VIEW_CLICKED");
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
//            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
//                Log.d(TAG,"事件类型：TYPE_VIEW_SCROLLED");
//                break;
//            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
//                Log.d(TAG,"事件类型：TYPE_VIEW_TEXT_SELECTION_CHANGED");
//                break;
//            default:
//                Log.d(TAG,"未匹配到事件类型");
//                break;
//        }
    }

    private void handleEvent(AccessibilityEvent event){
        /**
         * AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
         * event.getSource()
         * 获取活动窗口的节点信息，有时候并不是顶层节点
         * 节点一定要可点击，节点的id 或 text 属性可以为空
         * 点击的速度太快可能导致软件崩溃，节点需要自行回收
         * 1.可以通过getParent(),getChild(int)以及一系列的判断（节点的属性）去找到指定节点
         * 2.通过一个节点 返回根节点 然后遍历所有节点  然后过滤节点  然后点击
         * 3.通过一个节点 返回根节点 通过findAccessibilityNodeInfosByViewId(int)找到指定节点
         */
        AccessibilityNodeInfo nodeInfo = event.getSource();

        if((nodeInfo!=null)&&(ClickNodeList.HE_WO_XIN_PACKAGE_NAME.equals(event.getPackageName()))&&(MainActivity.sIsButtonClicked))
        {
            /**
             * 如果事件源节点信息不为空，且是目标app的事件，则执行相应操作
             * 如果需要点击的节点为空则先判断是不是弹空的，然后判断是否要重置节点，然后再返回不执行点击事件
             */
            if (mMyNode == null) {
                /**
                 * 每次捡完金币Toast一次，然后过一段时间重置值
                 *  金币为空说明服务（所有属性）被重置了，然后没有开始捡金币
                 */
                if ((!mFlag)&&(mGolds!=null)){
                    mFlag=true;
                    double temp=Math.round(mCount*100)/100.0;
                    Toast.makeText(this.getApplicationContext(),"此次所捡的流量："+temp+"MB",Toast.LENGTH_LONG).show();

                    mGolds.setNumber(mCount);
                    GoldsList.getInstance(PickGoldAccessibilityService.this.getApplicationContext())
                            .addGolds(mGolds);
                    mGolds=null;

                    MainActivity.sIsButtonClicked=false;

                    init();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 返回app
                             */
                            for (int i=0;i<3;i++){
                                PickGoldAccessibilityService.this.
                                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                                try {
                                    Thread.sleep(200);
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

    }



    private void clickTargetNode(@Nullable AccessibilityNodeInfo nodeInfo) {
        /**
         * 先返回到根节点，然后再找指定节点
         * nodeInfo非空，只回收自己的创建的节点
         */
        if (nodeInfo==null)
            return;
        AccessibilityNodeInfo top=nodeInfo;
        AccessibilityNodeInfo parent=nodeInfo.getParent();
        while (parent!=null){
            top=parent;
            parent=parent.getParent();
        }

        List<AccessibilityNodeInfo> targetNodeList=top.findAccessibilityNodeInfosByViewId(mMyNode.getId());
        if (targetNodeList==null)
            return;
        /**
         * 标记当前节点是否点击成功
         */
        boolean isClicked=false;
        for (int i=0,length=targetNodeList.size();i<length;i++){
            AccessibilityNodeInfo targetNode=targetNodeList.get(i);

            if (targetNode==null||(!targetNode.isClickable())){
                /**
                 * 节点为空或者不能点击
                 */
                continue;
            }
            if (length==6){
                /**
                 * 如果子节点有6个 说明在捡金币的界面 需要单独处理
                 * 可以通过目标节点找到相关子节点，然后就可以获取用户名或者流量信息
                 */
                List<AccessibilityNodeInfo> goldNumberNodeList=targetNode.findAccessibilityNodeInfosByViewId("com.jx.cmcc.ict.ibelieve:id/lh");
                if (goldNumberNodeList==null){
                    continue;
                }
                if (goldNumberNodeList.size()<=0)
                    continue;
                AccessibilityNodeInfo goldNumberNode=goldNumberNodeList.get(0);
                String goldNumber= goldNumberNode.getText().toString();

                goldNumberNode.recycle();

                if (i==1){
                    /**
                     * 开始捡金币的时候，需要实例化金币对象
                     */
                    mGolds=new Golds(0);
                }

                if (i==5){
                    /**
                     *  到了最后一个节点，不管有没有流量捡，都必须切换节点
                     */
                    isClicked=true;
                }
                if (goldNumber.equals("0 MB")||goldNumber.equals("邀请\n+50M")){
                    continue;
                }
                if (goldNumber.length()<3)
                    continue;
                mCount +=(Double.parseDouble(goldNumber.substring(0,goldNumber.length()-2)))/2;
                Log.d(TAG,"获取到的流量为："+mCount);
            }

            isClicked=targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d(TAG,"正在点击："+i);

            targetNode.recycle();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isClicked){
            mMyNode = ClickNodeList.pollNode();
        }
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
//        Log.d(TAG,"包名："+nodeInfo.getPackageName());
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "辅助服务中断");
        Toast.makeText(this.getApplicationContext(), "中断点击", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "辅助服务 onDestroy");
    }
}
