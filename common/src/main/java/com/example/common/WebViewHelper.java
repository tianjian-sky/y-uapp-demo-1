package com.example.common;
import android.webkit.WebView;
public class WebViewHelper {
    public WebView webview;

    public WebViewHelper(WebView webview) {
        this.webview = webview;
    }

    public void consoleLog (String str) {
        this.webview.evaluateJavascript("console.log(\'[app:] " + str + "\')", null);
    }

    public String test () {
        return "webview title: " + this.webview.getTitle() + " webview url:" + this.webview.getUrl();
    }

    public int resolveJsPromise(String globalVarName, String promiseId, String valueStr) {
        this.webview.evaluateJavascript("try {" + globalVarName + "['" + promiseId + "'].resolve(" + valueStr + ");} catch(e) {console.error('promise异常', e)}", null);
        return 1;
    }

    public int rejectJsPromise(String globalVarName, String promiseId, String valueStr) {
        this.webview.evaluateJavascript("try {" + globalVarName + "['" + promiseId + "'].reject(" + valueStr + ");} catch(e) {console.error('promise异常', e)}", null);
        return 1;
    }

}
