package com.moose.foodies.domain.repositories

import com.moose.foodies.data.local.ItemsDao
import com.moose.foodies.data.remote.ApiEndpoints
import com.moose.foodies.domain.models.Item
import com.moose.foodies.domain.models.Profile
import com.moose.foodies.domain.models.Recipe
import com.moose.foodies.util.Preferences
import javax.inject.Inject

interface RecipeRepository {
    suspend fun getItem(id: String): Item

    suspend fun getRecipe(id: String): Recipe

    suspend fun getFavorite(id: String):Recipe?

    suspend fun updateRecipe(recipe: Recipe)
}

class RecipeRepositoryImpl @Inject constructor(val api: ApiEndpoints, val itemsDao: ItemsDao, val preferences: Preferences): RecipeRepository {

    override suspend fun getItem(id: String): Item {
        return itemsDao.getItemById(id)
    }

    override suspend fun getRecipe(id: String): Recipe {
        return itemsDao.getRecipeById(id) ?: api.getRecipeById(id)
    }

    override suspend fun getFavorite(id: String): Recipe? {
        return itemsDao.getFavoriteById(id)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        return itemsDao.updateRecipe(recipe)
    }
}