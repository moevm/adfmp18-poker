package game.poker

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport

class PocketPoker : Game() {
    lateinit var batch: SpriteBatch
    lateinit var img: Sprite
    lateinit var view: Viewport

    override fun create() {
        batch = SpriteBatch()
        img = Sprite(Texture("badlogic.jpg"))
        img.setBounds(200f, 200f, 400f, 400f)
        view = StretchViewport(1080f, 1920f)
        screen = MainMenu(this)
    }

    override fun render() {
        println("Game render")
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.projectionMatrix = view.camera.combined
        batch.begin()
        img.draw(batch)
        screen.render(0f)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        view.update(width, height, true)
    }

    override fun dispose() {
        batch.dispose()
        //img.dispose()
    }
}

class MainMenu(val game: Game) : Screen{

    override fun show(){
        println("MainMenu show")
    }

    override fun render(delta: Float){
        println("MainMenu render")
    }

    override fun resize(width: Int, height: Int){

    }

    override fun pause(){

    }

    override fun resume(){

    }

    override fun hide(){

    }

    override fun dispose(){

    }
}
