package com.ld.pool.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

//两个泛型，TBinding代表databinding为fragment的layout文件生成的XXXBinding，TModel代表fragment对应的viewmodel。
//比如MainFragment的layout是fragment_main.xml，viewmodel是MainViewModel，那对应的TBinding和TModel就分别是FragmentMainBinding和MainViewModel。
//两个变量，brId是layout里viewmodel的variable变量名在BR类里对应生成的id，modelClass则是viewmodel对应的class文件
//比如MainViewModel在fragment_main.xml里的variable为mainViewModel，那么brId和modelClass则分别为BR.mainViewModel与MainViewModel::class.java。
abstract class BaseFragment/*<TBinding: ViewDataBinding, TModel: ViewModel>(private val brId: Int, modelClass: Class<TModel>)*/ : Fragment() {
//
//    protected lateinit var binding: TBinding //也可以用by lazy，放在onVreateView里初始化更方便
//
//    protected val viewModel by lazy { ViewModelProviders.of(this).get(modelClass) }
//
//    protected val activity by lazy { getActivity()  } //让具体子Fragment持有Activity的引用，方便Fragment与Activity交互
//
//    protected var viewHolder: View? = null //缓存界面，便于栈内复用，这样栈内的fragment回到栈顶时不用再初始化界面
//
//    protected var isViewCached = false //检查界面是否缓存
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        if (viewHolder == null) {
//            //TBinding的具体类型不确定，只能用DataBindingUtil.inflate()，而不能用XXXBinding.inflate()
//            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
//            binding.lifecycleOwner = this //绑定viewmodel的lifecycle
//        }
//        return binding.root
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        if (!isViewCached) {
//            binding.setVariable(brId, viewModel)
//        }
//        activity.currentFragment = this //currentFragment是Activity里的变量，用来持有显示的当前Fragment，便于Activity与Fragment交互
//    }
//
//    protected abstract fun getLayoutId(): Int //获取具体子Fragment对应的layoutId

}