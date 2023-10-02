package com.marioioannou.mealquest.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.adapters.recyclerview_adapters.IngredientsAdapter
import com.marioioannou.mealquest.adapters.recyclerview_adapters.SavedRecipesAdapter
import com.marioioannou.mealquest.databinding.FragmentFavouriteBinding
import com.marioioannou.mealquest.databinding.FragmentFridgeBinding
import com.marioioannou.mealquest.domain.database.recipes_database.entities.FavouritesEntity
import com.marioioannou.mealquest.domain.model.recipes.Result
import com.marioioannou.mealquest.ui.activities.MainActivity
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesFragmentArgs
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesFragmentDirections
import com.marioioannou.mealquest.utils.NetworkListener
import com.marioioannou.mealquest.viewmodel.MainViewModel

class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding

    private lateinit var savedRecipesAdapter: SavedRecipesAdapter

    lateinit var viewModel: MainViewModel

    private lateinit var networkListener: NetworkListener

    private var TAG = "FavouriteFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.saved_recipes_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when (menuItem.itemId) {
                    R.id.menu_delete_ingredients -> {
                        if (savedRecipesAdapter.differ.currentList.isEmpty()) {
                            Snackbar.make(view,
                                "Empty favorite recipes list.",
                                Snackbar.LENGTH_SHORT).apply {
                                setAction("Okay") {
                                    dismiss()
                                }
                                show()
                            }
                        }else{
                            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                            alertDialogBuilder.setIcon(R.mipmap.ic_launcher)
                            alertDialogBuilder.setMessage("Are you sure you want to delete all recipes?")
                            alertDialogBuilder.setPositiveButton("Yes") { dialog, id ->
                                dialog.cancel()
                                viewModel.deleteAllFavoriteRecipes()
                            }
                            alertDialogBuilder.setNegativeButton("No"){ dialog, id -> dialog.cancel()
                            }
                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.show()
                        }
                    }

                    R.id.menu_settings -> {
                        findNavController().navigate(R.id.action_global_settingsFragment)
                    }

                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.readFavoriteRecipes.observe(viewLifecycleOwner, Observer { result ->
            savedRecipesAdapter.differ.submitList(result)
            if (savedRecipesAdapter.differ.currentList.isEmpty()){
                hideRecyclerView()
            }else{
                showRecyclerView()
            }
            //watchlistAdapter.notifyDataSetChanged()
        })

        savedRecipesAdapter.setOnItemClickListener { recipe: FavouritesEntity ->
            //Log.d("onRecipeClick", "${result.title} clicked")
            val action = FavouriteFragmentDirections.actionFavouriteFragmentToDetailsActivity(recipe.result)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        savedRecipesAdapter = SavedRecipesAdapter(viewModel)
        binding.rvFavoritesRecipes.apply {
            adapter = savedRecipesAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun showRecyclerView() {
        binding.rvFavoritesRecipes.visibility = View.VISIBLE
        binding.layoutNoSavedRecipes.visibility = View.GONE
    }

    private fun hideRecyclerView() {
        binding.rvFavoritesRecipes.visibility = View.GONE
        binding.layoutNoSavedRecipes.visibility = View.VISIBLE
    }
}