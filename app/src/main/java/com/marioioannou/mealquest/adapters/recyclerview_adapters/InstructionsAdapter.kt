package com.marioioannou.mealquest.adapters.recyclerview_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marioioannou.mealquest.databinding.DetailInstructionLayoutBinding
import com.marioioannou.mealquest.domain.model.recipes.AnalyzedInstruction
import com.marioioannou.mealquest.domain.model.recipes.Step

class InstructionsAdapter : RecyclerView.Adapter<InstructionsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: DetailInstructionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Step>() {

        override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem.step == newItem.step
        }

        override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DetailInstructionLayoutBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val instruction = differ.currentList[position]
        holder.binding.apply {
            tvStepNumber.text = instruction.number.toString()
            tvInstruction.text = instruction.step.toString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}