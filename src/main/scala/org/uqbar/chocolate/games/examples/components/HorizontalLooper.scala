package org.uqbar.chocolate.games.examples.components

import org.uqbar.chocolate.core.appearances.Appearance
import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.reactions.events.Update
import org.uqbar.math.spaces.R2._

class HorizontalLooper(val appearance: Appearance, val speed: Double) extends Visible {

	protected def advance(delta: Double) {
		this.move(this.speed * delta, 0)

		if (left > game.displaySize(X)) alignHorizontally(_.right)(0)
	}

	// ****************************************************************
	// ** TRIGGERS
	// ****************************************************************

	in { case Update(delta) ⇒ advance(delta) }
}