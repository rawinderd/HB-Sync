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
import com.hook2book.roomdb1.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var categoriesMains: MutableList<CategoriesMainForRoom>

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
        binding.buttonFetch.setOnClickListener({
            GlobalScope.launch{
               // categoriesMains.addAll(CommonDatabase.getDatabase(activity)).categoriesDao().getAll())
            }
        })
        binding.buttonInsert.setOnClickListener {
            for (i in 0..categoriesMains.size - 1) {
                Log.i("TAG", "onCreateView: $i "+categoriesMains.get(i).CategoryName)
            }
        }

        homeFragmentViewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        /*binding.buttonInsert.setOnClickListener {
            homeFragmentViewModel.insertCategories(getList())
        }

        homeFragmentViewModel.fetchedlist.observe(viewLifecycleOwner) {
            for (category in it) {
                Log.i(
                    "TAG",
                    "onCreateView: ${category.CategoryName} - ${category.description} - ${category.id}"
                )
            }
        }*/
        var db = AppDatabase.getDatabase(requireContext())
        var dao = db.categoryDao()
        binding.buttonFetch.setOnClickListener()
         {
             GlobalScope.launch {
                 Log.i("TAG", "onCreate: " + dao.getAllCategories())
             }
        }

        return view
    }


}