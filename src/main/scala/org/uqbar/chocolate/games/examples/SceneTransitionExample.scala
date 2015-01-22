package org.uqbar.chocolate.games.examples

import org.uqbar.chocolate.core.Game
import org.uqbar.chocolate.core.GameScene
import org.uqbar.chocolate.core.utils.Implicits._
import org.uqbar.chocolate.core.components.GameComponent
import org.uqbar.math.spaces.R2._
import org.uqbar.cacao.Color
import org.uqbar.chocolate.core.reactions.io.Key
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.chocolate.core.reactions.events.Render
import org.uqbar.cacao.Rectangle

object SceneTransitionExample extends Game {

	val title = "Scene Transition Example"

	val displaySize: Vector = (200, 200)

	val redScene = new GameScene
	val blueScene = new GameScene

	redScene.addComponent(new Switch(Color.Red)(blueScene))
	blueScene.addComponent(new Switch(Color.Blue)(redScene))

	currentScene = redScene
}

class Switch(color: Color)(target: GameScene) extends GameComponent {
	in {
		case Render(renderer) ⇒
			renderer.color = color
			renderer fill Rectangle(Origin, game.displaySize)

		case Pressed(Key.Special.Space) ⇒ scene.translateTo(target)
	}
}