package com.hook2book.hbsync.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hook2book.hbsync.R
import com.hook2book.hbsync.RoomDatabase.CategoriesMainForRoom
import com.hook2book.hbsync.ViewModels.HomeFragmentViewModel
import com.hook2book.hbsync.databinding.FragmentHomeBinding
import java.lang.reflect.InvocationTargetException

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)
        try {
            homeFragmentViewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)

        } catch (e: InvocationTargetException) {

            Log.i("TAG", "onCreateView1: "+e)
        } catch (e:Exception) {

            // generic exception handling
            Log.i("TAG", "onCreateView2: "+e)
        }
        homeFragmentViewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        binding.buttonInsert.setOnClickListener {
            homeFragmentViewModel.insertCategories(getList())
        }
        binding.buttonFetch.setOnClickListener({
            homeFragmentViewModel.getAllCategories()

        })
        homeFragmentViewModel.fetchedlist.observe(viewLifecycleOwner) {
            for (category in it) {
                Log.i(
                    "TAG",
                    "onCreateView: ${category.CategoryName} - ${category.description} - ${category.id}"
                )
            }
        }
        binding.buttonFetch.setOnClickListener()
         {
            homeFragmentViewModel.clickCheck()
        }
        return view
    }

    private fun getList(): List<CategoriesMainForRoom> {
        lateinit var list: MutableList<CategoriesMainForRoom>
        list.add(
            CategoriesMainForRoom(
                count = 0,
                description = "Sample Description",
                id = 1,
                CategoryName = "3455 Category",
                CategoryParent = 0
            )
        )
        list.add(
            CategoriesMainForRoom(
                count = 0,
                description = "Sample Description",
                id = 1,
                CategoryName = "4554 Category",
                CategoryParent = 0
            )
        )
        list.add(
            CategoriesMainForRoom(
                count = 0,
                description = "Sample Description",
                id = 1,
                CategoryName = "6767 Category",
                CategoryParent = 0
            )
        )
        list.add(
            CategoriesMainForRoom(
                count = 0,
                description = "Sample Description",
                id = 1,
                CategoryName = "8989 Category",
                CategoryParent = 0
            )
        )

        list.add(
            CategoriesMainForRoom(
                count = 0,
                description = "Sample Description",
                id = 1,
                CategoryName = "323 Category",
                CategoryParent = 0
            )
        )

        list.add(
            CategoriesMainForRoom(
                count = 0,
                description = "Sample Description",
                id = 1,
                CategoryName = "454554 Category",
                CategoryParent = 0
            )
        )
        return list
    }
}