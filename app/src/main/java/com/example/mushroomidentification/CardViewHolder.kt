package com.example.mushroomidentification

import androidx.recyclerview.widget.RecyclerView
import com.example.mushroomidentification.databinding.CardCellBinding

class CardViewHolder(private val cardCellBinding: CardCellBinding,
                     private val clickListener: RecipeClickListener):RecyclerView.ViewHolder(cardCellBinding.root)
{
    fun bindRecipe(recipe: Recipe)
    {
        cardCellBinding.cover.setImageResource(recipe.cover)
        cardCellBinding.title.text = recipe.title
        cardCellBinding.shortDesc.text = recipe.short_desc

        cardCellBinding.cardView.setOnClickListener{
            clickListener.onClick(recipe)
        }
    }
}