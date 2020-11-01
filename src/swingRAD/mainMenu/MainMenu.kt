package swingRAD.mainMenu

import swingRAD.setProperties
import swingRAD.*
import java.awt.Color
import java.awt.Image
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.*
import kotlin.system.exitProcess

class MainMenu(screenWidth: Int, private val jFrame: JFrame, move: Boolean = true, backgroundColor: Color = darkGray,
               private val fontColor: Color = gray, borderColor: Color = semiDarkGray3
): JPanel(){

    private val mainPanel = JPanel()

    private var iLogo = ImageIcon()
    private val iBtExitOn = ImageIcon("resources/mainBar/btExitOn.png")
    private val iBtExitOff = ImageIcon("resources/mainBar/btExitOff.png")

    private val lLogo = JLabel()
    private val lTitle = JLabel()

    private val btExit = JButton()

    private var x0 = 0
    private var y0 = 0

    private var xFreeOption = 32

    init {
        mainPanel.setProperties(0, 0, screenWidth, 27, backgroundColor, null)

        if(move)
            mainPanel.addMouseMotionListener(object: MouseMotionListener {
                override fun mouseDragged(e: MouseEvent) {
                    if(e.source == mainPanel) {
                        jFrame.setLocation(e.xOnScreen - x0, e.yOnScreen - y0)
                    }
                }

                override fun mouseMoved(e: MouseEvent?) {
                    if(e?.source == mainPanel) {
                        x0 = e.x
                        y0 = e.y
                    }
                }
            })
        add(mainPanel)

        btExit.setProperties(screenWidth - 48, 0, iBtExitOff, defaultCursor)
        btExit.addMouseListener (object: MouseListener{
            override fun mouseExited(e: MouseEvent?) {
                if(e?.source == btExit)
                    btExit.icon = iBtExitOff
            }

            override fun mouseClicked(e: MouseEvent?) {
                if(e?.source == btExit) {
                    if(JOptionPane.showConfirmDialog(null, "Desea salir?") == 0)
                        exitProcess(0)
                }
            }

            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {}

            override fun mouseEntered(e: MouseEvent?) {
                if(e?.source == btExit)
                    btExit.icon = iBtExitOn
            }
        })
        mainPanel.add(btExit)

        mainPanel.add(lLogo)
        mainPanel.add(lTitle)

        setProperties(0, 0, screenWidth, 29, borderColor, null)
    }

    fun setLogo(icon: ImageIcon) {
        iLogo = ImageIcon(icon.image.getScaledInstance(16, 16, Image.SCALE_DEFAULT))
        lLogo.setProperties(5, 5, iLogo)
    }

    fun setTitle(str: String) {
        lTitle.setProperties(400, 0, 570, 28, str, fontTitleMini, fontColor, "CENTER")
    }

    fun add(option: MainOption) {
        option.setLocation(xFreeOption, 2)
        xFreeOption += option.width + 5
        mainPanel.add(option)
    }

}