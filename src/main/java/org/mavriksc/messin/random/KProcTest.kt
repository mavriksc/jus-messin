package org.mavriksc.messin.random

import processing.core.PApplet

class KProcTest : PApplet() {
    override fun settings() {
        size(1200, 1200)
    }

    override fun setup() {
        super.setup()
        background(0)
    }

    override fun draw() {
        ellipse(mouseX.toFloat(), mouseY.toFloat(), 20f, 20f)
    }
}

fun main() {
    PApplet.main("org.mavriksc.messin.random.KProcTest")
}
