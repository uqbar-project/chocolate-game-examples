package org.uqbar.chocolate.games.examples.components

import org.uqbar.chocolate.core.components.GameComponent
import org.uqbar.chocolate.core.dimensions.Positioned
import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.reactions.io.Key
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.math.spaces.R2._

class StressGenerator[T <: GameComponent with Positioned](increaseRate: Int = 10)(prototype: Vector => T) extends GameComponent {

	// ****************************************************************
	// ** TRIGGERS
	// ****************************************************************

	in {
		case Pressed(Key.Special.Space) ⇒ for (i ← 1 to increaseRate) {
			scene addComponent prototype(0, Math.random * game.displaySize(Y))
		}
	}
}