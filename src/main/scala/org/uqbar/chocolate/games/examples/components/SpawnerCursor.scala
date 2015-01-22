package org.uqbar.chocolate.games.examples.components

import org.uqbar.chocolate.core.appearances.Appearance
import org.uqbar.chocolate.core.collisions.NoBoundingBox
import org.uqbar.chocolate.core.utils.Implicits._
import org.uqbar.chocolate.core.components.GameComponent
import org.uqbar.chocolate.core.dimensions.Positioned
import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.chocolate.core.reactions.io.MouseButton
import org.uqbar.chocolate.core.reactions.io.Key
import org.uqbar.math.spaces.R2._
import org.uqbar.chocolate.core.reactions.events.MouseMoved

class SpawnerCursor[T <: GameComponent with Positioned](
	val appearance: Appearance,
	val leftButtonPrototype: Vector => T,
	val anyButtonPrototype: Vector => T,
	val rightButtonPrototype: Vector => T) extends Visible {

	var addedComponents: List[T] = List()
	val boundingBox = NoBoundingBox

	// ****************************************************************
	// ** TRIGGERS
	// ****************************************************************

	in {
		case MouseMoved(position) => translation = position

		case Pressed(mouseButton: MouseButton) =>
			List((60, 0), (-60, 0), (0, 60), (0, -60)) foreach (d ⇒ addCopyToScene(anyButtonPrototype, translation + d))

			mouseButton match {
				case MouseButton.Left => addCopyToScene(leftButtonPrototype, translation)
				case MouseButton.Right => addCopyToScene(rightButtonPrototype, translation)
				case _ =>
			}

		case Pressed(Key.Special.Esc) =>
			addedComponents foreach (_.destroy)
			addedComponents = List()
	}

	// ****************************************************************
	// ** OPERATIONS
	// ****************************************************************

	protected def addCopyToScene(gc: Vector => T, gcPosition: Vector) {
		val marker = gc(gcPosition)
		marker.z = this.z - 1

		scene.addComponent(marker)

		addedComponents ::= marker
	}
}