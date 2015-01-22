package org.uqbar.chocolate.games.examples

import scala.reflect.runtime.universe

import org.uqbar.chocolate.core.Game
import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.components.debug.LogPanel
import org.uqbar.chocolate.core.dimensions.Positioned
import org.uqbar.chocolate.core.loaders.ResourceLoader
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.chocolate.games.examples.components.SpawnerCursor
import org.uqbar.math.spaces.R2.touple_to_vector

object MouseExample extends Game {

	def title = "Mouse Example"
	def displaySize = (800, 600)

	// ****************************************************************
	// ** RESOURCE LOADING
	// ****************************************************************

	val cursorAppearance = ResourceLoader.loadSprite("/images/HandCursor.png")
	val blueButtonAppearance = ResourceLoader.loadSprite("/images/Blue Button.png")
	val yellowButtonAppearance = ResourceLoader.loadSprite("/images/Yellow Button.png")
	val redButtonAppearance = ResourceLoader.loadSprite("/images/Red Button.png")

	// ****************************************************************
	// ** SCENE SET-UP
	// ****************************************************************

	currentScene.addComponent(
		new SpawnerCursor[Visible](cursorAppearance,
			{ position => new Visible with Positioned { translation = position; val appearance = blueButtonAppearance } },
			{ position => new Visible with Positioned { translation = position; val appearance = yellowButtonAppearance } },
			{ position => new Visible with Positioned { translation = position; val appearance = redButtonAppearance } }
		)
	)

	mode("debug"){ () =>
		currentScene.addComponent(new LogPanel(ResourceLoader.font('monospaced, 15)) {
			attendToEvent[Pressed]
		})
	}
}