package swingRAD.mainMenu

import swingRAD.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JLabel
import javax.swing.JPanel

class MainOption(name: String): JPanel() {
    private val options = mutableListOf<JPanel>()
    val pOptions = JPanel()

    init{
        pOptions.setProperties(height = 5, background = darkGray)
        pOptions.addMouseListener(object: MouseListener{
            override fun mouseClicked(e: MouseEvent?) {}

            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {}

            override fun mouseEntered(e: MouseEvent?) {}

            override fun mouseExited(e: MouseEvent?) {
                parent.parent.parent.parent.remove(pOptions)
                parent.parent.parent.parent.repaint()
            }

        })

        addMouseListener(object: MouseListener{
            override fun mouseClicked(e: MouseEvent?) {
                pOptions.setLocation(location.x - 5, 27)
                parent.parent.parent.parent.add(pOptions)
                parent.parent.parent.parent.repaint()
            }

            override fun mousePressed(e: MouseEvent?) {
            }

            override fun mouseReleased(e: MouseEvent?) {
            }

            override fun mouseEntered(e: MouseEvent?) {
            }

            override fun mouseExited(e: MouseEvent?) {
            }
        })

        val lText = JLabel()
        lText.setProperties(5, 0, name.length*7, 20, name, font = fontTextMini)
        add(lText)

        setProperties(0, -5, name.length*7+10, 24, border = null, background = null)
    }

    fun addOption (name: String, mouseListener: MouseListener) {
        val pOption = JPanel()
        pOption.addMouseListener(mouseListener)
        pOption.setProperties(2, options.size*27, border = null, background = null)

        val lOption = JLabel()
        lOption.setProperties(5, 5, name.length*7, 20, name, font = fontTextMini)
        pOption.add(lOption)

        pOptions.setSize(
            if(name.length*7+10 > pOptions.width)
                name.length*7+10
            else
                pOptions.width
            ,
            pOptions.height + 27
        )

        options.add(pOption)
        pOptions.add(options[options.lastIndex])

        for(p in options){
            p.setSize(pOptions.width - 4, 27)
        }
    }
}