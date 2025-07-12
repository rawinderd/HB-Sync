package com.hook2book.hbsync.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hook2book.hbsync.Activities.AccountActivity
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.Preferences
import com.hook2book.hbsync.UtilityClass.Prevalent
import com.hook2book.hbsync.ViewModels.UserViewModel
import com.hook2book.hbsync.databinding.FragmentProductsBinding
import com.hook2book.hbsync.databinding.FragmentUserBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        userViewModel=ViewModelProvider(this).get(UserViewModel::class.java)
        val view:View = inflater.inflate(R.layout.fragment_user, container, false)
        binding= FragmentUserBinding.bind(view)
        // Inflate the layout for this fragment
        binding.nameEditButton.setOnClickListener(View.OnClickListener {
            val intent= Intent(context, AccountActivity::class.java)
            startActivity(intent)
            // Handle the name edit button click
        })
        binding.firstNameLastNameAa.text=Preferences.loadAccountInfo(context).data_outer.get(0).name
















        return view
    }

}