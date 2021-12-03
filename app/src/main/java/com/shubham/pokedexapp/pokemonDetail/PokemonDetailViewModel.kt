package com.shubham.pokedexapp.pokemonDetail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.shubham.pokedexapp.data.remote.response.Pokemon
import com.shubham.pokedexapp.repository.PokemonRepository
import com.shubham.pokedexapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
     private val repository: PokemonRepository
): ViewModel() {
    suspend fun getPokemonInfo(pokemonName : String ) : Resource<Pokemon>{
        return repository.getPokemonInfo(pokemonName = pokemonName)
    }
    fun getCoilRequestResult(context : Context, url : String, onCalculated: (Color) -> Unit){
        viewModelScope.launch {
            // Requesting the image using coil's ImageRequest
            val req = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()

            val result = Coil.execute(req)

            if (result is SuccessResult) {

                val bmp = (result.drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

                Palette.from(bmp).generate{ palette ->
                    palette?.dominantSwatch?.rgb?.let { colorValue ->
                        onCalculated(Color(colorValue))
                    }
                }

            }
        }
    }
}