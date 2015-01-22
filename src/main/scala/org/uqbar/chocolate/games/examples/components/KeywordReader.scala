package org.uqbar.chocolate.games.examples.components

import org.uqbar.cacao.Color
import org.uqbar.chocolate.core.appearances.Label
import java.awt.Font
import org.uqbar.chocolate.core.appearances.Appearance
import org.uqbar.chocolate.core.components.Visible
import org.uqbar.math.spaces.R2._
import org.uqbar.chocolate.core.reactions.events.Typed
import org.uqbar.chocolate.games.examples.KeyboardExample
import org.uqbar.chocolate.core.loaders.ResourceLoader

class KeywordReader(initialPosition: Vector, keys: List[String]) extends Visible {
	val keywords = keys.map(_.toUpperCase)
	var readedSoFar = ""
	var appearance = nextLabel

	translation = initialPosition

	def nextLabel = new Label(ResourceLoader.font('monospaced, 15),
		if (keywords.contains(readedSoFar)) KeyboardExample.KEYWORD_COMPLETE_FONT_COLOR
		else if (keywords.exists(_.startsWith(readedSoFar))) KeyboardExample.KEYWORD_IN_PROGRESS_FONT_COLOR
		else KeyboardExample.KEYWORD_WRONG_COLOR
	)(readedSoFar)

	// ****************************************************************
	// ** TRIGGERS
	// ****************************************************************

	in {
		case Typed(char) ⇒
			val readed = readedSoFar + char.toUpper
			readedSoFar = if (keywords.exists(_.startsWith(readed))) readed else readed.last.toString
			appearance = nextLabel
	}
}