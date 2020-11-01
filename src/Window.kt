import swingRAD.*
import swingRAD.mainBar.MainBar
import java.awt.Dimension
import javax.swing.*
import javax.swing.table.DefaultTableModel


class Window: JFrame() {

    private val tfWeight = JTextField() //peso maximo soportado por la mochila
    private val tfNumberOfItems = JTextField() //cantidad de artículos
    private val bDraw = JButton() //boton para dibujar formulario
    private val pItems = JPanel() //panel donde se insertan los pesos y valores
    private val pOut = JPanel() //resultado al calcular
    private var datos: Array<Array<String?>> = arrayOf() //matriz de datos ingresados

    init {
        //decoracion
        val mainBar = MainBar(this)
        mainBar.setTitle("Problema de la mochila")
        mainBar.setLogo(ImageIcon("resources/exampleLogo.png"))
        add(mainBar)

        val title = JLabel()
        title.setProperties(64, 50, 400, 64, "Definición:", fontTitle)
        add(title)

        val text = JLabel()
        text.setProperties(64, 100, 500, 32, "Cualquier valor (V) se consigue:")
        add(text)

        val lFormula = JLabel()
        lFormula.setProperties(70, 130, 500, 28, "V[i, j] = max(V[i-1, j], V[i-1, j - Wi] + Vi)")
        add(lFormula)

        //ingreso de datos
        val lWeightLimit = JLabel()
        lWeightLimit.setProperties(64, 170, 110, 28, "Peso límite =")
        add(lWeightLimit)

        tfWeight.setProperties(260, 170, 80, 28)
        add(tfWeight)

        val lNumberOfItems = JLabel()
        lNumberOfItems.setProperties(64, 200, 210, 28, "Cantidad de artículos =")
        add(lNumberOfItems)

        tfNumberOfItems.setProperties(260, 200, 80, 28)
        add(tfNumberOfItems)

        //paneles inicialmente ocultos
        add(pItems)
        add(pOut)

        //boton para dibujar
        val scroll = JScrollBar()
        scroll.setBounds(276, 2, 20, 396)
        scroll.setUI(getCustomScroll())

        bDraw.setProperties(160, 250, 100, 28, "Dibujar")
        bDraw.addActionListener {
            pItems.removeAll()
            pItems.setProperties(64, 290, 300, 400)

            val pItemsContentPane = JPanel()
            pItemsContentPane.setProperties(2, 2, 276, 396, border = null)
            pItems.add(pItemsContentPane)

            val pItemsContainer = JPanel()
            pItemsContainer.setProperties(
                0,
                0,
                276,
                464 + tfNumberOfItems.text.toInt() * 30,
                border = null,
                background = transparent
            )
            pItemsContentPane.add(pItemsContainer)

            val lPeso = JLabel()
            lPeso.setProperties(140, 20, 80, 30, "Peso")
            pItemsContainer.add(lPeso)

            val lValor = JLabel()
            lValor.setProperties(210, 20, 80, 30, "Valor")
            pItemsContainer.add(lValor)

            //tabla
            for(i in 0 until tfNumberOfItems.text.toInt()) {
                val lText = JLabel()
                lText.setProperties(20, 50 + 30 * i, 100, 28, "Artículo ${i + 1}")
                pItemsContainer.add(lText)

                val tfPeso = JTextField()
                tfPeso.setProperties(130, 50 + 30 * i, 60, 26)
                pItemsContainer.add(tfPeso)

                val tfValor = JTextField()
                tfValor.setProperties(200, 50 + 30 * i, 60, 26)
                pItemsContainer.add(tfValor)
            }

            //bt calcular
            val btCalcular = JButton()
            btCalcular.setProperties(100, 100 + 30 * (tfNumberOfItems.text.toInt() - 1), 100, 32, "Calcular")
            btCalcular.addActionListener {
                calcular()
            }
            pItemsContainer.add(btCalcular)

            //scroll
            if(tfNumberOfItems.text.toInt() > 9) {
                scroll.addAdjustmentListener {
                    pItemsContainer.setLocation(0, it.value * -1 * tfNumberOfItems.text.toInt() / 3)
                    repaint()
                }
                pItems.add(scroll)
                scroll.value = 1
            }

            add(pItems)
            repaint()
        }
        add(bDraw)

        //frame
        setProperties()
    }

    private fun calcular () {
        val placeholdes = arrayListOf("Artículo", "Peso", "Costo") //tabla superior
        for(i in 0 .. tfWeight.text.toInt())
            placeholdes.add(i.toString())
        val cabecera: Array<String> = placeholdes.toArray(arrayOfNulls<String>(0))

        val placeholdes1 = arrayListOf("Artículo", "Peso", "Costo") //tabla inferior
        val pCorner = JPanel()

        val modelo = DefaultTableModel()
        modelo.setColumnIdentifiers(cabecera)
        for (i in 0 until tfNumberOfItems.text.toInt()) {
            modelo.addRow(arrayOf<Any>(i + 1, 0, 0, 0))
        }

        val tabla = JTable()
        tabla.model = modelo
        tabla.rowHeight = 40
        tabla.setDefaultRenderer(Any::class.java, getCustomTable(semiDarkGrayBlue, semiDarkGrayBlue, mdb1, darkWhite, fontText))
        tabla.gridColor = black

        val header = tabla.tableHeader
        header.preferredSize = Dimension(580, 30)
        header.defaultRenderer = getCustomTable(semiDarkGray2, null, null, white, fontText)

        pOut.removeAll()
        pOut.setProperties(384, 50, 846, 640)

        val pTabla = tabla.getPanelBar(2, 2, 843, 300)
        pTabla.background = semiDarkGray2
        pOut.add(pTabla)

        repaint()
    }

}