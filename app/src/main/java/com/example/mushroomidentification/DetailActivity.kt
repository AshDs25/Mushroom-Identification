package com.example.mushroomidentification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mushroomidentification.databinding.ActivityDetailBinding

private lateinit var binding: ActivityDetailBinding
class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeId = intent.getIntExtra(RECIPE_ID_EXTRA,-1)
        val recipe = recipeFromID(recipeId)
        if(recipe!=null){
            binding.cover.setImageResource(recipe.cover)
            binding.title.text= recipe.title
            binding.shortDesc.text = recipe.short_desc
            binding.mainDesc.text = recipe.main_desc
        }
    }

    private fun recipeFromID(recipeId: Int): Recipe? {
        for(recipe in recipeList){
            if(recipe.id == recipeId)
                return recipe
        }
        return null
    }
}