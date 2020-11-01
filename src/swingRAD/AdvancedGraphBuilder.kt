package swingRAD

import java.awt.*
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.RectangularShape
import java.awt.geom.RoundRectangle2D
import javax.swing.*
import javax.swing.border.AbstractBorder
import javax.swing.border.Border
import javax.swing.plaf.basic.BasicScrollBarUI
import javax.swing.table.DefaultTableCellRenderer

/** @author Cristian Felipe Patiño Cáceres
 * @contributor Jean Carlos Santoya Cabrera */

fun getCustomTable(colorPrincipal: Color?, colorSecundario: Color?, colorSeleccion: Color?, colorFuente: Color?, fuente: Font?
): DefaultTableCellRenderer {
    return object : DefaultTableCellRenderer() {
        override fun getTableCellRendererComponent(
            table: JTable?, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int
        ): Component {
            val celda =
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) as JLabel
            celda.isOpaque = true
            celda.font = fuente
            celda.foreground = colorFuente
            celda.horizontalAlignment = CENTER
            if (row % 2 != 0) celda.background = colorPrincipal else celda.background = colorSecundario
            if (isSelected) {
                celda.background = colorSeleccion
                celda.foreground = Color.WHITE
            }
            return celda
        }
    }
}

fun getCustomScroll(grosor: Int = 7, radio: Int = 10, colorFondo: Color? = semiDarkGrayBlue, colorBarraNormal: Color? = gray,
    colorBarraArrastrada: Color? = darkBlueGray
): BasicScrollBarUI {
    return object : BasicScrollBarUI() {
        private val d = Dimension()
        override fun createDecreaseButton(orientation: Int): JButton {
            val boton = JButton()
            boton.preferredSize = d
            return boton
        }

        override fun createIncreaseButton(orientation: Int): JButton {
            val boton = JButton()
            boton.preferredSize = d
            return boton
        }

        override fun paintTrack(g: Graphics, c: JComponent, r: Rectangle) {
            g.color = colorFondo
            g.fillRect(r.x, r.y, r.width, r.height)
        }

        override fun paintThumb(g: Graphics, c: JComponent, r: Rectangle) {
            val g2 = g.create() as Graphics2D
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE)
            val sb = c as JScrollBar
            if (!sb.isEnabled) return else if (isDragging) g2.paint =
                colorBarraArrastrada else if (isThumbRollover) g2.paint =
                colorBarraNormal else g2.paint = colorBarraNormal
            if (r.width < r.height) g2.fillRoundRect(
                (r.width - grosor) / 2,
                r.y,
                grosor,
                r.height,
                radio,
                radio
            ) else g2.fillRoundRect(r.x, (r.height - grosor) / 2, r.width, grosor, radio, radio)
        }
    }
}

fun getBlurredBorder(colorBase: Color, grosor: Int): Border? {
    var bordeFinal: Border? = null
    val bordeInicial = BorderFactory.createLineBorder(colorBase, 1, true)
    var siguienteColor = Color(colorBase.red + 5, colorBase.green + 5, colorBase.blue + 5)
    var contador = 0
    while (siguienteColor.red < 251 && siguienteColor.green < 251 && siguienteColor.blue < 251 && contador < grosor) {
        val bordeExterno = BorderFactory.createLineBorder(siguienteColor, 1, true)
        bordeFinal =
            if (contador == 0) BorderFactory.createCompoundBorder(
                bordeExterno,
                bordeInicial
            ) else BorderFactory.createCompoundBorder(bordeExterno, bordeFinal)
        siguienteColor = Color(
            siguienteColor.red + 5, siguienteColor.green + 5, siguienteColor.blue + 5
        )
        contador++
    }
    return bordeFinal
}

fun getRoundedBorder(color: Color?, radio: Int, esLineal: Boolean, imagen: Image?): Border {
    return object : Border {
        override fun paintBorder(c: Component, g: Graphics, x: Int, y: Int, ancho: Int, alto: Int) {
            val g2 = g as Graphics2D
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE)
            val area: Area
            val padreContenedor: Component = c.parent
            val rectanguloBordeado: RoundRectangle2D = RoundRectangle2D.Double()
            rectanguloBordeado.setRoundRect(
                x.toDouble(),
                y.toDouble(),
                ancho - 1.toDouble(),
                alto - 1.toDouble(),
                radio.toDouble(),
                radio.toDouble()
            )
            if (esLineal) {
                drawBackground(c, padreContenedor, imagen, g2, ancho, alto)
                area = drawBorder(c, g2, color, x, y, ancho, alto, rectanguloBordeado)
            } else {
                area = drawBorder(c, g2, color, x, y, ancho, alto, rectanguloBordeado)
                drawBackground(c, padreContenedor, imagen, g2, ancho, alto)
            }
            g2.clip = null
            g2.draw(area)
        }

        override fun getBorderInsets(c: Component): Insets {
            return Insets(2, 2, 2, 2)
        }

        override fun isBorderOpaque(): Boolean {
            return false
        }
    }
}

fun getCircularBorder(color: Color?, esLineal: Boolean, imagen: Image?): AbstractBorder {
    return object : AbstractBorder() {
        override fun paintBorder(c: Component, g: Graphics, x: Int, y: Int, ancho: Int, alto: Int) {
            val g2 = g as Graphics2D
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE)
            val area: Area
            val padreContenedor: Component = c.parent
            val circulo: Ellipse2D = Ellipse2D.Double()
            circulo.setFrameFromCenter(
                Point(x + ancho / 2, y + alto / 2),
                Point(ancho, alto)
            )
            if (esLineal) {
                drawBackground(c, padreContenedor, imagen, g2, ancho, alto)
                area = drawBorder(c, g2, color, x, y, ancho, alto, circulo)
            } else {
                area = drawBorder(c, g2, color, x, y, ancho, alto, circulo)
                drawBackground(c, padreContenedor, imagen, g2, ancho, alto)
            }
            g2.clip = null
            g2.draw(area)
        }
    }
}

fun drawBackground(c: Component, padreContenedor: Component, imagen: Image?, g2: Graphics2D, ancho: Int, alto: Int) {
    if (imagen != null) g2.drawImage(
        imagen,
        0, 0, imagen.getWidth(null), imagen.getHeight(null),
        c.x, c.y, imagen.getWidth(null) + c.x, imagen.getHeight(null) + c.y,
        c
    ) else {
        g2.color = padreContenedor.background
        g2.fillRect(0, 0, ancho, alto)
    }
}

fun drawBorder(c: Component, g2: Graphics2D, color: Color?, x: Int, y: Int, ancho: Int, alto: Int, figura: RectangularShape?): Area {
    if (color == null) g2.paint = c.background else g2.paint = color
    val area = Area(figura)
    val rectangulo = Rectangle(0, 0, ancho, alto)
    val regionBorde = Area(rectangulo)
    regionBorde.subtract(area)
    g2.clip = regionBorde
    return area
}