package com.ld.pool.codeutilspools.ui.bottomaction.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.ui.bottomaction.ui.home.viewModel.BottomHomeViewModel


class BottomHomeFragment : Fragment() {

    private lateinit var bottomHomeViewModel: BottomHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomHomeViewModel =
            ViewModelProviders.of(this).get(BottomHomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home_bottom, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        bottomHomeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener(view)
    }

    private fun setListener(view: View) {
        view.findViewById<View>(R.id.button_home).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionHomeFragmentToHomeSecondFragment("From HomeFragment")
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home2).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionHomeFragmentToFloatingBarFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        // 跳转到指定id=action_HomeFragment_to_stickyRecyclerviewFragment的页面
        // Navigation.findNavController(view)
        // .navigate(R.id.action_HomeFragment_to_stickyRecyclerviewFragment)
        view.findViewById<View>(R.id.button_home3).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionHomeFragmentToStickyRecyclerviewFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home4).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionHomeFragmentToRecyclerPinnedHeaderFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home5).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionNavigationBottomhomeToRecyclerPinnedHeader2Fragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home6).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionNavigationBottomhomeToRecyclerExpandFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home7).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionNavigationBottomhomeToStickyFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home8).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionNavigationBottomhomeToStickyGridFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home9).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionNavigationBottomhomeToPowerfulStickyFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home10).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionNavigationBottomhomeToPowerfulStickyGridFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home11).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionNavigationBottomhomeToBeautifulFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

        view.findViewById<View>(R.id.button_home12).setOnClickListener {
            val action = BottomHomeFragmentDirections
                .actionNavigationBottomhomeToExpandableFragment()
            NavHostFragment.findNavController(this@BottomHomeFragment)
                .navigate(action)
        }

    }

//    fun toPowerfulSticky(view: View?) {
//        startActivity(Intent(this, PowerfulStickyActivity::class.java))
//    }
//
//    fun toPowerfulStickyGrid(view: View?) {
//        val intent = Intent(this, PowerfulStickyGridActivity::class.java)
//        startActivity(intent)
//    }
//
//    fun toPowerfulSticky2(view: View?) {
//        startActivity(Intent(this, BeautifulActivity::class.java))
//    }
//
//    fun toExpandableList(view: View?) {
//        startActivity(Intent(this, ExpandableActivity::class.java))
//    }
}
