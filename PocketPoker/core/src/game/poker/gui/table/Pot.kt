package game.poker.gui.table

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import game.poker.Settings
import game.poker.staticFiles.Fonts
import game.poker.staticFiles.Textures

class Pot() : Group(){
    var money = 9999L
        set(value) {
            field = value
            chipstack.setChips(money)
        }
    var count = "9 999"
        set(value) {
            field = value
            label.setText(Settings.getText(Settings.TextKeys.POT) + ":\n" + count)
        }
    val label = Label(Settings.getText(Settings.TextKeys.POT) + ":\n" + count,
                        Label.LabelStyle(Fonts.gameLabelFont, Color.BLACK))
    val chipstack = Chipstack()

    init {
        label.style.background = SpriteDrawable(Sprite(Textures.labelBg))
        label.setSize(240f, 100f)
        label.setAlignment(Align.center)
        chipstack.setChips(money)
        addActor(label)
        addActor(chipstack)
    }

    fun update(){
        label.setText(Settings.getText(Settings.TextKeys.POT) + ":\n" + money)
    }
}