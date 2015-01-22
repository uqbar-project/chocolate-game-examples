package org.uqbar.chocolate.games.examples

import org.uqbar.chocolate.core.Game
import org.uqbar.chocolate.core.loaders.ResourceLoader
import org.uqbar.chocolate.games.examples.components.HorizontalLooper
import org.uqbar.chocolate.games.examples.components.StressGenerator
import org.uqbar.math.spaces.R2._

object StressExample extends Game {

	def title = "Stress Example"
	def displaySize = (500, 500)

	// ****************************************************************
	// ** CONSTANTS
	// ****************************************************************

	final val BURGER_SPEED = 250
	final val BURGER_WIDTH = 50
	final val INCREASE_RATE = 5

	// ****************************************************************
	// ** RESOURCE LOADING
	// ****************************************************************

	val burgerAppearance = ResourceLoader.loadSprite("/images/Burger.png").scaleHorizontallyTo(BURGER_WIDTH, true)
	burgerAppearance.align(_.center, _.middle)(0, 0)

	// ****************************************************************
	// ** SCENE SET-UP
	// ****************************************************************

	currentScene.addComponent(
		new StressGenerator[HorizontalLooper](INCREASE_RATE)({ position =>
			new HorizontalLooper(burgerAppearance, BURGER_SPEED) { translation = position }
		})
	)
}