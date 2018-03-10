package game.poker.gui.table

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import game.poker.core.Chip
import game.poker.staticFiles.Textures


class Chipstack() : Widget() {
    var money: Long = 0
        private set

    private val stacksCount = 8

    private val stacks: Array<MutableList<Image>> =
            Array(stacksCount) { mutableListOf<Image>() }

    fun setChips(newMoney: Long){
        money = newMoney
        var count = newMoney

        // below is copy paste from javascript code
        val amounts = mutableListOf<MutableList<Pair<Chip, Long>>>()

        for(chip in Chip.values().reversed()){
            if(chip != Chip.DEALER){
                if(count >= chip.price()){
                    val intAmount = Math.floor(count.toDouble() / chip.price()).toLong()
                    amounts += mutableListOf(Pair(chip, intAmount))
                    count -= intAmount * chip.price()
                }
            }
        }

        while (amounts.size > stacksCount){
            var minBetweens = -1
            var indexBetweens = -1

            for(i in 0 until amounts.size-1){

                var currBetween = 0

                for (j in amounts[i]){
                    currBetween += j.second.toInt()
                }

                for (j in amounts[i+1]){
                    currBetween += j.second.toInt()
                }

                if(currBetween <= minBetweens || minBetweens == -1){
                    minBetweens = currBetween
                    indexBetweens = i
                }

            }

            for (i in amounts[indexBetweens + 1]){
                amounts[indexBetweens].add(i)
            }

            amounts.removeAt(indexBetweens + 1)
        }

        for(stack in stacks){
            stack.clear()
        }

        for((i, currAmounts) in amounts.withIndex()){
            //println("" + (i + 1) + ")")
            for(currChip in currAmounts){
                //println("    " + currChip.first.price() + " * " + currChip.second)
                for(currChipAmount in 1..currChip.second){
                    stacks[i].add(Image(SpriteDrawable(Sprite(Textures.getChip(currChip.first)))))
                }
            }
        }
        var col = 0
        stacks.reverse()
        stacks.forEach {
            var row = 0
            it.forEach {
                it.setSize(50f, 50f)
                if(col < stacksCount/2){
                    it.setPosition(x + stacksCount*25f - (col + 1)*50f, y + row * 5f)
                } else {
                    it.setPosition(x + stacksCount*25f - (col + 1 - stacksCount/2)*50f, y + row * 5f - 50f)
                }
                row += 1
            }
            col += 1
        }
    }
    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        stacks.forEach { it.forEach {it.draw(batch, parentAlpha)} }
    }
}