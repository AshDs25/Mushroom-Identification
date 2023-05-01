package com.example.mushroomidentification

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mushroomidentification.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private fun RecyclerView.layoutManager(any: Any) {

}

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment(), RecipeClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
private lateinit var binding: FragmentHomeBinding
private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home,container,false)
        recyclerView = view.findViewById(R.id.recycler_view)


        var binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        populateRecipe()

        val home = this
        recyclerView.apply{
            layoutManager = GridLayoutManager(context,2)
            adapter = CardAdapter(recipeList,home)
        }
        return view


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private fun populateRecipe(){
            val recipe1 = Recipe(
                R.drawable.mush_pizza,
                "Mushroom and Garlic Pizza",
                "Mushroom and Garlic Pizza is a type of pizza that typically consists of a base of pizza dough topped with tomato sauce, shredded cheese, sliced mushrooms, and minced garlic.",
                """Ingredients:
- 1 pound pizza dough
- 1/2 cup pizza sauce
- 1 cup shredded mozzarella cheese
- 1 cup sliced mushrooms
- 4 cloves garlic, minced
- 1/4 cup grated Parmesan cheese
- Salt and pepper to taste
- Olive oil for brushing

Instructions:
1. Preheat your oven to 425°F.
2. Dust your work surface with flour and roll out the pizza dough into a 12-inch circle.
3. Place the dough on a pizza pan or baking sheet and brush lightly with olive oil.
4. Spread the pizza sauce evenly over the dough, leaving about 1/2 inch of space around the edges.
5. Sprinkle the mozzarella cheese over the sauce.
6. Distribute the sliced mushrooms and minced garlic over the cheese.
7. Season with salt and pepper to taste.
8. Sprinkle the grated Parmesan cheese on top of the pizza.
9. Bake the pizza for 12-15 minutes, or until the crust is golden brown and the cheese is melted and bubbly.
10. Remove the pizza from the oven and let it cool for a few minutes before slicing and serving.

""")
            recipeList.add(recipe1)

            val recipe2 = Recipe(
                R.drawable.truff_mush,
                "Mushroom and Truffle Pasta",
                "Mushroom and truffle pasta is a savory pasta dish that features a combination of earthy mushrooms and luxurious truffle flavor.",
                """Ingredients:

-8 oz. pasta
-2 tbsp. olive oil
-2 cloves garlic, minced
-8 oz. mushrooms, sliced
-1 tbsp. truffle oil
-1/4 cup grated parmesan cheese
-Salt and pepper to taste
-Fresh parsley for garnish

Instructions:
1.Cook the pasta according to package directions until firm. Drain and set aside.
2.In a large skillet, heat the olive oil over medium-high heat. Add the minced garlic and cook for 1-2 minutes until fragrant.
3.Add the sliced mushrooms to the skillet and cook for 5-7 minutes until they are tender and browned.
4.Stir in the truffle oil and grated parmesan cheese until the cheese is melted and the mushrooms are coated.
5.Add the cooked pasta to the skillet and toss everything together until the pasta is coated in the mushroom and truffle sauce.
6.Season with salt and pepper to taste.
7.Garnish with fresh parsley and serve hot. """

            )
recipeList.add(recipe2)
            val recipe3 = Recipe(
                R.drawable.stuff_mush,
                "Stuffed Mushrooms",
                "Stuffed mushrooms are a delicious appetizer or side dish made by filling mushroom caps with a flavorful mixture of breadcrumbs, cheese, herbs, and other ingredients, and then baking or grilling until golden brown and tender. They are a popular vegetarian option and can be customized to suit a variety of tastes and dietary restrictions.",
                """Ingredients:

-16 large mushrooms, stems removed and reserved
-1/4 cup breadcrumbs
-1/4 cup grated Parmesan cheese
-2 cloves garlic, minced
-2 tablespoons chopped fresh parsley
-2 tablespoons olive oil
-Salt and pepper to taste

Instructions:
1.Preheat the oven to 375°F (190°C).
2.Clean the mushrooms with a damp cloth and remove the stems. Finely chop the stems and set them aside.
3.In a small bowl, mix together the breadcrumbs, Parmesan cheese, garlic, parsley, and chopped mushroom stems.
4.Drizzle the mushroom caps with olive oil and sprinkle with salt and pepper.
5.Fill each mushroom cap with the breadcrumb mixture.
6.Arrange the stuffed mushrooms on a baking sheet and bake for 15-20 minutes, or until the mushrooms are tender and the filling is golden brown.
7.Serve hot as an appetizer or side dish.""")
            recipeList.add(recipe3)


    val recipe4 = Recipe(
        R.drawable.mush_risotto,
        "Mushroom Risotto",
        "Mushroom risotto is a classic Italian dish made with Arborio rice cooked in a rich and creamy mushroom broth, often finished with grated Parmesan cheese and chopped parsley. It's a delicious and comforting meal that's perfect for a cozy night in.",
        """
Ingredients:
- 1 pound mushrooms, sliced
- 1 onion, finely chopped
- 2 cloves garlic, minced
- 1 1/2 cups Arborio rice
- 4 cups chicken or vegetable broth
- 1/2 cup dry white wine
- 1/2 cup grated Parmesan cheese
- 2 tbsp butter
- Salt and pepper to taste
- Fresh parsley or thyme, chopped (optional)

Instructions:
1. Heat a large pan over medium-high heat. Add 1 tbsp butter and sauté the sliced mushrooms until they release their moisture and start to brown. Remove the mushrooms from the pan and set aside.
2. In the same pan, melt the remaining 1 tbsp butter and add the chopped onion and minced garlic. Sauté for a few minutes until the onions are translucent.
3. Add the Arborio rice to the pan and stir to coat the grains in the butter and onion mixture. Cook for 1-2 minutes until the rice becomes slightly translucent.
4. Pour in the white wine and stir continuously until the liquid has been absorbed.
5. Add 1 cup of the broth to the pan and stir continuously until the liquid has been absorbed. Repeat this process with the remaining broth, adding one cup at a time and stirring constantly until the rice is cooked and creamy.
6. Once the rice is cooked, stir in the sautéed mushrooms and grated Parmesan cheese. Season with salt and pepper to taste.
7. Garnish with chopped parsley or thyme (optional) and serve immediately.""")
    recipeList.add(recipe4)

            val recipe5 = Recipe(
                R.drawable.mush_cream,
                "Cream of mushroom soup",
                "Cream of mushroom is a rich and creamy soup made with sautéed mushrooms, onions, and garlic, blended with cream and seasoned with herbs and spices. It's a comforting and savory dish that's perfect for cooler weather.",
                """Ingredients:
- 1/4 cup unsalted butter
- 1 small onion, diced
- 8 oz. sliced mushrooms
- 1/4 cup all-purpose flour
- 4 cups chicken or vegetable broth
- 1 cup heavy cream
- Salt and pepper to taste

Instructions:
1. In a large pot or Dutch oven, melt the butter over medium heat. Add the diced onion and sauté until translucent, about 5 minutes.
2. Add the sliced mushrooms to the pot and cook until they release their liquid and start to brown, about 10 minutes.
3. Sprinkle the flour over the mushrooms and stir well to coat. Cook for an additional 2-3 minutes, until the flour has been absorbed and the mixture is thickened.
4. Slowly pour in the chicken or vegetable broth while whisking constantly to avoid lumps. Bring the soup to a simmer and cook for 10-15 minutes, until the mushrooms are tender and the soup has thickened.
5. Stir in the heavy cream and season with salt and pepper to taste. Allow the soup to cook for an additional 5-10 minutes to heat through and allow the flavors to meld.
6. Serve hot, garnished with chopped fresh parsley or thyme if desired.

""")
            recipeList.add(recipe5)

}
}


    override fun onClick(recipe: Recipe) {
        val intent = Intent(context,DetailActivity::class.java)
        intent.putExtra(RECIPE_ID_EXTRA,recipe.id)
        startActivity(intent)

    }
}