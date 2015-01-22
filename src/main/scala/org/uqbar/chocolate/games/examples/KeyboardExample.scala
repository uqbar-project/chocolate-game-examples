package org.uqbar.chocolate.games.examples

import scala.reflect.runtime.universe

import org.uqbar.cacao.Color
import org.uqbar.chocolate.core.Game
import org.uqbar.chocolate.core.appearances.Label
import org.uqbar.chocolate.core.components.debug.LogPanel
import org.uqbar.chocolate.core.loaders.ResourceLoader
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.chocolate.core.reactions.events.Released
import org.uqbar.chocolate.core.reactions.events.Typed
import org.uqbar.chocolate.games.examples.components.HorizontalLooper
import org.uqbar.chocolate.games.examples.components.KeywordReader
import org.uqbar.math.spaces.R2.touple_to_vector

object KeyboardExample extends Game {

	def title = "Keyboard Example"
	def displaySize = (600, 500)

	// ****************************************************************
	// ** CONSTANTS
	// ****************************************************************

	final val KEYWORD_COMPLETE_FONT_COLOR = Color.Green
	final val KEYWORD_IN_PROGRESS_FONT_COLOR = Color.Yellow
	final val KEYWORD_WRONG_COLOR = Color.Red

	// ****************************************************************
	// ** RESOURCE LOADING
	// ****************************************************************

	val monitorImage = ResourceLoader.loadSprite("/images/Monitor.png").scaleHorizontallyTo(500, true)

	// ****************************************************************
	// ** SCENE SET-UP
	// ****************************************************************

	currentScene addComponent new HorizontalLooper(monitorImage, 0) {
		translation = (10, 10)
	}
	currentScene addComponent new HorizontalLooper(new Label(ResourceLoader.font('monospaced, 15), KEYWORD_IN_PROGRESS_FONT_COLOR)("Try to guess the cheats..."), 0) {
		translation = (150, 100)
	}

	currentScene addComponent new KeywordReader((150, 150), List("letitbe", "yamirocuei"))

	mode("debug") { () =>
		currentScene addComponent new LogPanel(ResourceLoader.font('monospaced, 15), 10) {
			attendToEvent[Pressed]
			attendToEvent[Released]
			attendToEvent[Typed]
		}
	}
}