package com.marioioannou.mealquest.ui.fragments

import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.marioioannou.mealquest.R
import com.marioioannou.mealquest.adapters.recyclerview_adapters.IngredientsAdapter
import com.marioioannou.mealquest.databinding.FragmentFridgeBinding
import com.marioioannou.mealquest.ui.activities.MainActivity
import com.marioioannou.mealquest.ui.fragments.recipes_fragment.RecipesViewModel
import com.marioioannou.mealquest.utils.Constants
import com.marioioannou.mealquest.utils.Constants.QUERY_RECIPE_BY_INGREDIENTS_API_KEY
import com.marioioannou.mealquest.utils.Constants.QUERY_RECIPE_BY_INGREDIENTS_IGNORE_PANTRY
import com.marioioannou.mealquest.utils.Constants.QUERY_RECIPE_BY_INGREDIENTS_INGREDIENTS
import com.marioioannou.mealquest.utils.Constants.QUERY_RECIPE_BY_INGREDIENTS_NUMBER
import com.marioioannou.mealquest.viewmodel.MainViewModel
import java.util.*
import kotlin.collections.HashMap

class FridgeFragment : Fragment() {

    private lateinit var binding: FragmentFridgeBinding

    private lateinit var ingredientsAdapter: IngredientsAdapter

    lateinit var viewModel: MainViewModel
    lateinit var recipesViewModel: RecipesViewModel

    private var TAG = "FridgeFragment"

    private lateinit var ingredientNames: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFridgeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        viewModel.readIngredient().observe(viewLifecycleOwner, Observer { ingredient ->
            ingredientsAdapter.differ.submitList(ingredient)
            if (ingredientsAdapter.differ.currentList.isEmpty()) {
                hideRecyclerView()
            } else {
                showRecyclerView()
            }
        })

        viewModel.readIngredientsName().observe(viewLifecycleOwner, Observer { name ->
            applyQueries(name)
            ingredientNames = name.joinToString(",+")
            //applyQueries()[QUERY_RECIPE_BY_INGREDIENTS_INGREDIENTS]?.zip(name.toString())?.toMap()
            //Log.d(TAG,"applyQueriesName are : $applyQueriesName")
            //Log.d(TAG,"IngredientNames : $ingredientNames")
            //Log.d(TAG,"Ingredients row names : ${name.joinToString(",+")}")
        })

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fridge_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when (menuItem.itemId) {
                    R.id.menu_add_ingredients -> {
                        if (recipesViewModel.networkStatus) {
                            findNavController().navigate(R.id.action_fridgeFragment_to_searchIngredientsFragment)
                        } else {
                            recipesViewModel.showNetworkStatus()
                        }
                    }

                    R.id.menu_delete_ingredients -> {
                        if (ingredientsAdapter.differ.currentList.isEmpty()) {
                            Snackbar.make(view,
                                "You fridge is empty.",
                                Snackbar.LENGTH_SHORT).apply {

                                setAction("Okay") {
                                    dismiss()
                                }
                                show()
                            }
                        } else {
                            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                            alertDialogBuilder.setIcon(R.mipmap.ic_launcher)
                            alertDialogBuilder.setMessage("Are you sure you want to delete all ingredients?")
                            alertDialogBuilder.setPositiveButton("Yes") { dialog, id ->
                                dialog.cancel()
                                viewModel.deleteAllIngredients()
                                Snackbar.make(view,
                                    "All ingredients are successfully deleted.",
                                    Snackbar.LENGTH_SHORT).apply {

                                    setAction("Okay") {
                                        dismiss()
                                    }
                                    show()
                                }
                            }
                            alertDialogBuilder.setNegativeButton("No") { dialog, id ->
                                dialog.cancel()
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

//        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
//            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        ) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder,
//            ): Boolean {
//                return true
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                val ingredient = ingredientsAdapter.differ.currentList[position]
//                val ingredientName = ingredient.name.replaceFirstChar {
//                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
//                }
//                if (direction == ItemTouchHelper.LEFT) {
//                    viewModel.deleteIngredient(ingredient)
//                    Snackbar.make(view,
//                        "$ingredientName successfully deleted.",
//                        Snackbar.LENGTH_SHORT).apply {
//
//                        setAction("Undo") {
//                            viewModel.saveIngredient(ingredient)
//                        }
//                        show()
//                    }
//                }
//            }
//
//            override fun onChildDraw(
//                c: Canvas,
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                dX: Float,
//                dY: Float,
//                actionState: Int,
//                isCurrentlyActive: Boolean,
//            ) {
//                super.onChildDraw(c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive)
//            }
//        }

//        ItemTouchHelper(itemTouchHelperCallback).apply {
//            attachToRecyclerView(binding.rvFridgeIngredients)
//        }

        binding.fabFridgeSearch.setOnClickListener {
            val action =
                FridgeFragmentDirections.actionFridgeFragmentToRecipesByIngredientsFragment(
                    ingredientNames)
            Log.d(TAG, "Sending $ingredientNames")
            if (recipesViewModel.networkStatus) {
                if (ingredientsAdapter.differ.currentList.isEmpty()) {
                    Snackbar.make(view,
                        "You fridge is empty.",
                        Snackbar.LENGTH_SHORT).apply {

                        setAction("Okay") {
                            dismiss()
                        }
                        show()
                    }
                } else {
                    findNavController().navigate(action)
                }
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

    }

    private fun setupRecyclerView() {
        ingredientsAdapter = IngredientsAdapter(viewModel)
        binding.rvFridgeIngredients.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun applyQueries(names: List<String>): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        val nameList = names.joinToString(",+")
        queries[QUERY_RECIPE_BY_INGREDIENTS_INGREDIENTS] = nameList
        queries[QUERY_RECIPE_BY_INGREDIENTS_API_KEY] = Constants.API_KEY
        queries[QUERY_RECIPE_BY_INGREDIENTS_NUMBER] = "50"
        queries[QUERY_RECIPE_BY_INGREDIENTS_IGNORE_PANTRY] = "false"

        return queries
    }

//    private fun basicAlert() {
//        val builder = AlertDialog.Builder(requireContext())
//        with(builder)
//        {
//            setTitle("Empty Fridge")
//            setIcon(R.drawable.fridge)
//            setMessage("Your fridge is empty.")
//            setNegativeButton("Okay") { _, _ ->
//            }
//            setCancelable(true)
//            show()
//        }
//    }

    private fun showRecyclerView() {
        binding.rvFridgeIngredients.visibility = View.VISIBLE
        binding.layoutEmptyFridge.visibility = View.GONE
        binding.ltLottie.pauseAnimation()
    }

    private fun hideRecyclerView() {
        binding.ltLottie.playAnimation()
        binding.rvFridgeIngredients.visibility = View.GONE
        binding.layoutEmptyFridge.visibility = View.VISIBLE
    }

}