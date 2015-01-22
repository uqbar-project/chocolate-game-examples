package org.uqbar.chocolate.games.examples

import org.uqbar.cacao.Color
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import scala.reflect.runtime.universe
import org.uqbar.chocolate.core.Game
import org.uqbar.chocolate.core.appearances.Appearance
import org.uqbar.chocolate.core.collisions.CircularBoundingBox
import org.uqbar.chocolate.core.collisions.RectangularBoundingBox
import org.uqbar.chocolate.core.components.Collisionable
import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.components.debug.NavigatorCamera
import org.uqbar.chocolate.core.components.debug.StatisticsReader
import org.uqbar.chocolate.core.reactions.ReactionRegistry
import org.uqbar.chocolate.core.reactions.events.Collision
import org.uqbar.chocolate.core.utils.Implicits.double_to_int
import org.uqbar.chocolate.core.utils.Implicits.min
import org.uqbar.chocolate.games.examples.components.Ball
import org.uqbar.math.spaces.R2._
import org.uqbar.chocolate.core.components.debug.LogPanel
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.chocolate.core.reactions.events.Hold
import org.uqbar.chocolate.core.reactions.events.Render
import org.uqbar.cacao.Line
import org.uqbar.cacao.Rectangle
import org.uqbar.cacao.Circle
import org.uqbar.chocolate.core.loaders.ResourceLoader
import java.io.FileNotFoundException

object CollisionExample extends Game {

	def title = "Collision Example"
	def displaySize = (600, 600)

	// ****************************************************************
	// ** CONSTANTS
	// ****************************************************************

	final val MAIN_BALL_COLLISION_GROUP = 1
	final val SECONDARY_BALL_COLLISION_GROUP = 2
	final val STRESS_COUNT = 300
	final val MAX_BALL_SPEED = 40
	final val MIN_BALL_SPEED = 10
	final val DATA_FILE_NAME = "/tmp/CollisionNumbers.data"

	// ****************************************************************
	// ** RESOURCE LOADING
	// ****************************************************************

	val redBallAppearance = ResourceLoader.loadSprite("/images/Red Button.png").scaleHorizontallyTo(30, true)
	redBallAppearance.align(_.center, _.middle)(Origin)
	val yellowBallAppearance = ResourceLoader.loadSprite("/images/Yellow Button.png").scaleHorizontallyTo(30, true)
	yellowBallAppearance.align(_.center, _.middle)(Origin)
	val blueBallAppearance = ResourceLoader.loadSprite("/images/Blue Button.png").scaleHorizontallyTo(30, true)
	blueBallAppearance.align(_.center, _.middle)(Origin)

	// ****************************************************************
	// ** SCENE SET-UP
	// ****************************************************************

	val mainBall = new Ball(yellowBallAppearance)(0, 0)(50, 15)

	for (ball ← extractBallsFromFile(redBallAppearance)) {
		currentScene.addComponents(ball)
		ball in { case Collision(b: Ball) if (b == mainBall) ⇒ ball.appearance = blueBallAppearance }
	}

	currentScene.addComponent(mainBall)

	//	ReactionRegistry.+=[Ball] { case (UpdateEvent(delta), c) ⇒ c.advance(delta) }

	// ****************************************************************
	// ** AUXILIARS
	// ****************************************************************

	protected def extractBallsFromFile(appearance: Appearance) = {
		var in: ObjectInputStream = null
		try {
			in = new ObjectInputStream(new FileInputStream(DATA_FILE_NAME))
		} catch {
			case e: FileNotFoundException =>
				generateRandomVectorsFile
				in = new ObjectInputStream(new FileInputStream(DATA_FILE_NAME))
		}

		val vectors = in.readObject.asInstanceOf[Seq[(Vector, Vector)]]
		in.close

		vectors.map{ case (position: Vector, speed: Vector) ⇒ new Ball(appearance)(position)(speed) }
	}

	def generateRandomVectorsFile {
		def randomBallSpeed = {
			val orientation = if (Math.random > 0.5) 1 else -1
			(Math.random * orientation * (MAX_BALL_SPEED - MIN_BALL_SPEED) + MIN_BALL_SPEED)
		}

		val out = new ObjectOutputStream(new FileOutputStream(DATA_FILE_NAME))
		out.writeObject(1 to STRESS_COUNT map { i =>
			(displaySize.map(_ * Math.random), (randomBallSpeed, randomBallSpeed): Vector)
		})
		out.close
	}

	//*********************************************************************************************
	// GAME MODES
	//*********************************************************************************************

	mode("debug") { () =>

		currentScene addComponent new StatisticsReader(ResourceLoader.font('monospaced, 15), 5)
		currentScene addComponent NavigatorCamera

		currentScene addComponent new LogPanel(ResourceLoader.font('monospaced, 15)) {
			attendToEvent[Hold]
		}

		ReactionRegistry.+=[Visible] {
			case (Render(renderer), target: Visible) ⇒
				val dx = target.translation(X)
				val dy = target.translation(Y)
				val bounds = target.appearance

				renderer.color = Color.Yellow
				renderer.draw(
					Line(target.translation, target.translation + bounds.translation),
					Rectangle ((bounds.left, bounds.top) + target.translation, bounds.size),
					Rectangle ((bounds.left, bounds.top) + target.translation, (5, 5)),
					Rectangle ((bounds.right - 5, bounds.top) + target.translation, (5, 5)),
					Rectangle ((bounds.right - 5, bounds.bottom - 5) + target.translation, (5, 5)),
					Rectangle ((bounds.left, bounds.bottom - 5) + target.translation, (5, 5))
				)
		}

		ReactionRegistry.+=[Collisionable] {
			case (Render(renderer), collisionable) ⇒
				val box = collisionable.boundingBox

				val crossSegmentSize = min(15, box.size(X), box.size(Y)) / 3
				val crossSize: Vector = (crossSegmentSize, crossSegmentSize)
				val basePosition = collisionable.translation

				val shape = box match {
					case c: CircularBoundingBox ⇒ Circle(basePosition, c.size(X) / 2)
					case r: RectangularBoundingBox ⇒ Rectangle(basePosition + (r.left, r.top), r.size)
				}

				renderer.color = Color.Blue
				renderer.draw(
					// Center
					Line(basePosition - crossSize, basePosition + crossSize),
          //TODO: Ver por qué el zipWith me está pidiendo parámetros
					Line(basePosition + crossSize.zipWith[Double, Double](_ * _)(1, -1), basePosition + crossSize.zipWith[Double, Double](_ * _)(-1, +1)),

					// Line to component position
					Line(basePosition, basePosition + box.translation),

					// Contour
					shape
				)
		}
	}

}