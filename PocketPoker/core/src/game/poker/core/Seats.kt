package game.poker.core

import com.google.gson.JsonObject
import game.poker.core.handle.insertSpaces
import game.poker.core.handle.shortcut

class Seats(val table: TableScreen, data: JsonObject, gameMode: Boolean) {

    val playersLeft: Int
    val bb: Long
    val sb: Long
    val ante: Long

    var toCall: Long = 0
    val tableNumber: Int
    val isFinal: Boolean
    val myId: Int
    val places: Array<Int>
    val seats: MutableMap<Int, Player>
    val idToLocalSeat: MutableMap<Int, Int>
    val realSeatToLocalSeat: MutableMap<Int, Int>
    var me: Player = Player(-1)

    var idInDecision = -1
    var mainChips: Long = 0

    init {
        var seatsShift = 0

        playersLeft = data["players_left"].asInt

        bb = data["bb"].asLong
        sb = data["sb"].asLong
        ante = data["ante"].asLong

        val players = data["players"].asJsonArray
        tableNumber = data["table_number"].asInt
        isFinal = data["is_final"].asBoolean

        if(gameMode){
            while (players[0].asJsonObject["id"] == null ||
                    !players[0].asJsonObject["controlled"].asBoolean){
                players.add(players.remove(0))
                seatsShift++
            }
            myId = players[0].asJsonObject["id"].asInt
        }
        else{
            myId = -1
        }

        val seatsCount = data["seats"].asInt

        places = getPlaces(seatsCount)

        seats = mutableMapOf()

        idToLocalSeat = mutableMapOf()
        realSeatToLocalSeat = mutableMapOf()

        for((i, localSeat) in places.withIndex()){
            realSeatToLocalSeat[(seatsShift + i) % seatsCount] = localSeat

            val curr = players[i].asJsonObject

            if(curr["id"] != null){

                val player = Player(curr, localSeat)
                seats[localSeat] = player

                table.setPlayer(localSeat,
                        empty=false,
                        disconnected=player.disconnected,
                        name=player.name,
                        stack=player.stack.insertSpaces())

                if(gameMode && player.id == myId){
                    me = player
                }

                idToLocalSeat[player.id] = localSeat

            }
            else{
                table.setEmptyPlayer(localSeat)
            }
        }

        table.isFinal = isFinal

        TODO("add strange chipstack timeout")
    }

    private fun getPlaces(seats: Int) = when(seats){
        2 -> arrayOf(1, 6)
        3 -> arrayOf(1, 4, 7)
        4 -> arrayOf(1, 3, 6, 8)
        5 -> arrayOf(1, 3, 5, 6, 8)
        6 -> arrayOf(1, 2, 4, 6, 7, 9)
        7 -> arrayOf(1, 2, 4, 5, 6, 7, 9)
        8 -> arrayOf(1, 2, 3, 4, 6, 7, 8, 9)
        9 -> arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        else -> arrayOf()
    }

    fun addPlayer(data: JsonObject){
        val localSeat = realSeatToLocalSeat[data["seat"].asInt]
            ?: throw IllegalArgumentException("Bad seat")

        val player = Player(data, localSeat)

        seats[localSeat] = player

        idToLocalSeat[player.id] = localSeat

        table.setPlayer(localSeat,
                empty=false,
                disconnected=player.disconnected,
                name=player.name,
                stack=player.stack.insertSpaces())

        if(isFinal){
            updateInfo(player.id, "")
        }
        else{
            updateInfo(player.id, "New player")
        }

    }

    fun deletePlayer(id: Int){
        val player = getById(id)

        table.deleteCards(player.localSeat)

        table.setEmptySeat(player.localSeat)

        seats.remove(player.localSeat)
        idToLocalSeat.remove(player.localSeat)
    }

    fun all(): Collection<Player> = seats.values

    fun getById(id: Int) = seats[idToLocalSeat[id]]
            ?: throw IllegalArgumentException("No such id")

    fun clearDecisions(){
        for(player in seats.values){
            table.updatePlayerInfo(player.localSeat, "")
        }
    }

    fun clearDecisionStates(){
        table.clearInDecision()
    }

    fun setBet(id: Int, count: Long, reason: String = ""){
        if(id != -1){
            val seat = getById(id)
            var moneySpent: Long = 0

            if(reason == "Win"){
                moneySpent = count
                seat.stack += moneySpent
            }
            else if(reason != "Clear"){
                moneySpent = count - seat.gived
                seat.stack -= moneySpent
            }

            seat.gived = count

            if(reason != "Clear"){
                updateInfo(id, reason, moneySpent)
            }

        }

        var potCount = mainChips

        if(reason != "Win"){
            for(seat in all()){
                potCount += seat.gived
            }
        }
        else if(id != -1){
            potCount -= count
        }

        if(reason != "Clear"){
            table.setPotCount(potCount.insertSpaces())
        }

        if(id != -1){
            table.setChips(idToLocalSeat[id], count)
        }
        else{
            table.setPotChips(count)
        }

    }

    fun clearBets(){
        TODO("work around chipstack timeout")
    }

    fun clear(){
        table.clearCommonCards()

        clearDecisionStates()

        for(seat in all()){
            table.clearCards(seat.localSeat)
            setBet(seat.id, 0, "Clear")
        }

        setBet(-1, 0, "")
    }

    fun setEmptySeat(localSeat: Int){
        val player = seats[localSeat]
                ?: throw IllegalArgumentException("No such seat")

        table.deletePlayer(player.localSeat)
    }

    fun updateInfo(id: Int, reason: String, count: Long = 0){
        var info = reason
        if(reason != "" && count > 0){
            info += " " + count.shortcut()
        }
        val player = getById(id)
        table.setPlayerInfo(player.localSeat, player.name,
                player.stack.insertSpaces(), info)
    }
}