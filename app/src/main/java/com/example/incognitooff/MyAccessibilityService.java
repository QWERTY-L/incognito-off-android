package com.example.incognitooff;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName().equals("com.android.chrome")) {
            // Access the root node of the UI hierarchy
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();

            if (rootNode != null) {
                // logViewHierarchy(rootNode, 0);
                standardIncogClose(rootNode);
                if(findByContentDescr(rootNode, "Incognito") != null){
                    AccessibilityNodeInfo openTabs = findByContentDescr(rootNode, "Switch or close tabs");
                    if(openTabs != null) openTabs.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

        }
    }

    @Override
    public void onInterrupt() {
        // Handle interruptions
    }


    /*
     *
     * Content Descriptions:
     *
     * Close New Incognito
     * Switch or close tabs
     *
     *
     * */

    public static void standardIncogClose(AccessibilityNodeInfo rootNode){
        AccessibilityNodeInfo closeButton = findByContentDescr(rootNode, "Close New Incognito");
        if(closeButton != null){
            closeButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            closeButton = findByContentDescr(rootNode, "Close New Incognito");
        }
    }

    public static void logViewHierarchy(AccessibilityNodeInfo nodeInfo, final int depth) {

        if (nodeInfo == null) return;

        String spacerString = "";

        for (int i = 0; i < depth; ++i) {
            spacerString += '-';
        }
        //Log the info you care about here... I choce classname and view resource name, because they are simple, but interesting.
        if(nodeInfo.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK)) Log.d("TAG", spacerString + nodeInfo.getClassName() + " " + nodeInfo.getContentDescription() + " " + nodeInfo.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK));
        //if(String.valueOf(nodeInfo.getContentDescription()).contains("Close New Incognito")) nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        //Log.d("NumT", "incogInst: " + nodeInfo.findAcces)

        for (int i = 0; i < nodeInfo.getChildCount(); ++i) {
            logViewHierarchy(nodeInfo.getChild(i), depth+1);
        }
    }

    public static AccessibilityNodeInfo findByContentDescr(AccessibilityNodeInfo nodeInfo, String contentDescr) {

        if (nodeInfo == null) return null;

        if(String.valueOf(nodeInfo.getContentDescription()).contains(contentDescr)) return nodeInfo;

        for (int i = 0; i < nodeInfo.getChildCount(); ++i) {
            AccessibilityNodeInfo out = findByContentDescr(nodeInfo.getChild(i), contentDescr);
            if(out != null) return out;
        }
        return null;
    }

}
