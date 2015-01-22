package org.uqbar.chocolate.games.examples.components

import scala.Int.int2double
import org.uqbar.chocolate.core.appearances.Appearance
import org.uqbar.chocolate.core.collisions.CircularBoundingBox
import org.uqbar.chocolate.core.components.Collisionable
import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.reactions.events.Update
import org.uqbar.chocolate.core.reactions.events.Collision
import org.uqbar.math.spaces.R2._

class Ball(var appearance: Appearance)(initialPosition: Vector)(var speed: Vector) extends Visible with Collisionable {

	val boundingBox = new CircularBoundingBox(15)

	translation = initialPosition

	in { case Update(delta) ⇒ this.advance(delta) }

	def advance(delta: Double) {
		move(speed * delta)

		correctTrajectoryToRemainOnScreen
	}

	def correctTrajectoryToRemainOnScreen {
		val rightOverflow = right - game.displaySize(X)
		if (rightOverflow > 0) {
			move(-rightOverflow, 0)
			speed = (-speed(X), speed(Y))
		}

		val leftOverflow = -left
		if (leftOverflow > 0) {
			move(leftOverflow, 0)
			speed = (-speed(X), speed(Y))
		}

		val bottomOverflow = bottom - game.displaySize(Y)
		if (bottomOverflow > 0) {
			move(0, -bottomOverflow)
			speed = (speed(X), -speed(Y))
		}

		val topOverflow = -top
		if (topOverflow > 0) {
			move(0, topOverflow)
			speed = (speed(X), -speed(Y))
		}
	}
}