package com.example.mushroomidentification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.RecyclerView
import com.example.mushroomidentification.databinding.CardCellBinding

class CardAdapter(private val recipe: List<Recipe>,
private val clickListener: RecipeClickListener): RecyclerView.Adapter<CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardCellBinding.inflate(from , parent,false)
        return CardViewHolder(binding,clickListener)
    }

    override fun getItemCount(): Int = recipe.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindRecipe(recipe[position])
    }

}