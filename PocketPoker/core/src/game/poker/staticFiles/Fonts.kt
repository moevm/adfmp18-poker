package game.poker.staticFiles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

object Fonts {
    private fun generateMenuButtonFont():BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.characters = FONT_CHARS
        parameter.size = 70
        parameter.color = Color.BLACK
        return generator.generateFont(parameter)
    }
    private fun generateMenuLogoFont():BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/Esteban-Regular.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.characters = FONT_CHARS
        parameter.size = 150
        parameter.color = Color.BLACK
        return generator.generateFont(parameter)
    }
    private val FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>"
    val mainMenuButtonFont = generateMenuButtonFont()
    val mainMenuLogoFont = generateMenuLogoFont()
    val mainMenuLabelFont = mainMenuButtonFont
}

