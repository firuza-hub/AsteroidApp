package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapters.AsteroidAdapter
import com.udacity.asteroidradar.data.services.AsteroidApiStatus
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application))[MainViewModel::class.java]
    }
    private lateinit var adapter : AsteroidAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)

         adapter = AsteroidAdapter(AsteroidAdapter.AsteroidClickListener{ asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))}
                            )
        binding.asteroidRecycler.adapter = adapter
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.getPictureOfTheDay()
        viewModel.pictureOfTheDay.observe( viewLifecycleOwner, Observer {
            it?.let{
                binding.textView.text = it.title
                Picasso.with(requireContext()).load(it.url).into(binding.activityMainImageOfTheDay)
                Log.i("MEOW", "getPictureOfTheDay: " + it.toString())
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAsteroidTodayData()
        viewModel.asteroids.observe( viewLifecycleOwner, Observer {
            it?.let{

                adapter.data = it
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = when (item.itemId) {
            R.id.show_week_menu -> {
                viewModel.getAsteroidWeekData()
                true
            }
            R.id.show_saved_menu -> {
                viewModel.getAsteroidPastData()
                true
            }
            R.id.show_today_menu -> {
                viewModel.getAsteroidTodayData()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
        viewModel.asteroids.observe( viewLifecycleOwner, Observer {
            it?.let{
                adapter.data = it
            }
        })

        return result
    }
}
