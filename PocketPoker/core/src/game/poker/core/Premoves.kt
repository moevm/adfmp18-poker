package game.poker.core

import game.poker.Settings
import game.poker.core.handle.shortcut
import game.poker.screens.TableScreen


class Premoves(val table: TableScreen) {

    var first = false
    var second = false
    var third = false
    var isVisible = false
    var inTwoChoices = false

    fun hide(){
        if(isVisible){
            isVisible = false
            reset()
            table.currView.setPremoves(false)
        }
    }

    fun show(){
        if(!isVisible){
            isVisible = true
            reset()
            table.currView.setPremoves(true)
        }
    }

    fun checkFold(){
        show()
        table.currView.setPremoveText(Settings.getText(Settings.TextKeys.CHECK) + "/" +
                Settings.getText(Settings.TextKeys.FOLD),
                Settings.getText(Settings.TextKeys.CHECK),
                Settings.getText(Settings.TextKeys.CALL) + " " +
                        Settings.getText(Settings.TextKeys.ANY))
    }

    fun callFold(money: Long){
        show()
        table.currView.setPremoveText(Settings.getText(Settings.TextKeys.FOLD),
                Settings.getText(Settings.TextKeys.CALL) + " " + money.shortcut(),
                Settings.getText(Settings.TextKeys.CALL) + " " +
                        Settings.getText(Settings.TextKeys.ANY))
    }

    fun allInFold(money: Long){
        show()
        twoChoicesMode()
        table.currView.setPremoveText(Settings.getText(Settings.TextKeys.FOLD),
                Settings.getText(Settings.TextKeys.CALL) + " " + money.shortcut(),
                "")
    }

    fun twoChoicesMode(){
        if(!inTwoChoices){
            inTwoChoices = true
            if(third){
                set(2, true)
            }
            else if(second){
                set(2, true)
            }
            table.currView.setThirdPremoveHidden(true)
        }
    }

    fun threeChoicesMode(){
        if(inTwoChoices){
            inTwoChoices = false
            table.currView.setThirdPremoveHidden(false)
        }
    }

    fun reset(){
        threeChoicesMode()
        set(0, false)
    }

    fun set(ans: Int, checked: Boolean){
        first = false
        second = false
        third = false
        when(ans) {
            1 -> first = checked
            2 -> second = checked
            3 -> third = checked
        }
        table.currView.setPremovesChecked(first, second, third)
    }

}