@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "SENSELESS_COMPARISON", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNI4B6D4A0
import io.dcloud.uniapp.*
import io.dcloud.uniapp.extapi.*
import io.dcloud.uniapp.framework.*
import io.dcloud.uniapp.runtime.*
import io.dcloud.uniapp.vue.*
import io.dcloud.uniapp.vue.shared.*
import io.dcloud.uts.*
import io.dcloud.uts.Map
import io.dcloud.uts.Set
import io.dcloud.uts.UTSAndroid
import kotlin.properties.Delegates
import uts.sdk.modules.uniGetbatteryinfo.GetBatteryInfoOptions as GetBatteryInfoOptions
import uts.sdk.modules.sFilePicker.PickFileOptions
import uts.sdk.modules.uniGetbatteryinfo.getBatteryInfo as uni_getBatteryInfo
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import uts.sdk.modules.sFilePicker.pickFile
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesIndexIndex : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesIndexIndex) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesIndexIndex
            val _cache = __ins.renderCache
            val title = ref("Hello")
            val goto = fun(path: String){
                uni_navigateTo(NavigateToOptions(url = path))
            }
            val chooseFile2 = fun(){
                pickFile(PickFileOptions(mimeTypes = _uA(
                    "*/*"
                ), multiple = false))
            }
            uni_getBatteryInfo(GetBatteryInfoOptions(success = fun(res) {
                console.log(res)
                uni_showToast(ShowToastOptions(title = "当前电量：" + res.level + "%", icon = "none"))
            }
            ))
            return fun(): Any? {
                return _cE("view", _uM("class" to "page"), _uA(
                    _cE("image", _uM("class" to "logo", "src" to "/static/logo.png")),
                    _cE("view", null, _uA(
                        _cE("button", _uM("onClick" to fun(){
                            goto("/pages/test/index")
                        }
                        ), "测试 页面", 8, _uA(
                            "onClick"
                        )),
                        _cE("button", _uM("onClick" to fun(){
                            goto("/pages/webview/index")
                        }
                        ), "webview 页面", 8, _uA(
                            "onClick"
                        )),
                        _cE("button", _uM("onClick" to chooseFile2), "chooseFile")
                    ))
                ))
            }
        }
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("page" to _pS(_uM("display" to "flex", "flexDirection" to "column", "height" to "100%")), "logo" to _pS(_uM("height" to 100, "width" to 100, "marginTop" to 100, "marginRight" to "auto", "marginBottom" to 25, "marginLeft" to "auto")), "title" to _pS(_uM("fontSize" to 18, "color" to "#8f8f94", "textAlign" to "center")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
