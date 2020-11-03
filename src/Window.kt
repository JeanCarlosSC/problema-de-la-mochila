import swingRAD.*
import swingRAD.mainBar.MainBar
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.table.DefaultTableModel

class Window: JFrame() {

    private val tfWeight = JTextField() //peso maximo soportado por la mochila
    private val tfNumberOfItems = JTextField() //cantidad de artículos
    private val bDraw = JButton() //boton para dibujar formulario
    private val pItems = JPanel() //panel donde se insertan los pesos y valores
    private val pOut = JPanel() //resultado al calcular
    private val pOut1 = JPanel() //resultado al calcular
    private var pesos: Array<JTextField?> =  arrayOf()//pesos ingresados
    private var valores: Array<JTextField?> =  arrayOf()//valores ingresados
    private val lResultado = JLabel()

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
        add(pOut1)
        add(lResultado)

        //boton para dibujar
        val scroll = JScrollBar()
        scroll.setBounds(276, 2, 20, 396)
        scroll.setUI(getCustomScroll())

        bDraw.setProperties(160, 250, 100, 28, "Dibujar")
        bDraw.addActionListener {
            valores = Array(tfNumberOfItems.text.toInt()){null}
            pesos = Array(tfNumberOfItems.text.toInt()){null}

            pItems.removeAll()
            pItems.setProperties(64, 290, 300, 400)

            val pItemsContentPane = JPanel()
            pItemsContentPane.setProperties(2, 2, 276, 396, border = null)
            pItems.add(pItemsContentPane)

            val pItemsContainer = JPanel()
            pItemsContainer.setProperties(0, 0, 276, 464 + tfNumberOfItems.text.toInt() * 30, border = null, background = transparent)
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
                pesos[i] = tfPeso
                pItemsContainer.add(pesos[i])

                val tfValor = JTextField()
                tfValor.setProperties(200, 50 + 30 * i, 60, 26)
                valores[i] = tfValor
                pItemsContainer.add(valores[i])
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

            //add(pItems)
            repaint()
        }
        add(bDraw)

        //frame
        val lBackground = JLabel()
        lBackground.setProperties(0,0, ImageIcon("resources/background.png"))
        add(lBackground)

        setProperties()
    }

    private fun calcular () {
        Algoritmo.informacion = Array(tfNumberOfItems.text.toInt() + 1) { IntArray(tfWeight.text.toInt() + 1) } //datos procesados
        val matriz = Array(tfNumberOfItems.text.toInt()) { IntArray(2) }
        for (i in 0 until tfNumberOfItems.text.toInt()) {
            matriz[i][0] = pesos[i]!!.text.toInt()
            matriz[i][1] = valores[i]!!.text.toInt()
            if(pesos[i]!!.text.toInt() == 0 || valores[i]!!.text.toInt() == 0){
                JOptionPane.showMessageDialog(null, "Valor ingresado inválido", "Error", JOptionPane.ERROR_MESSAGE)
                throw Exception("Se ha ingresado un cero")
            }
        }
        Algoritmo.calcular(tfNumberOfItems.text.toInt(), tfWeight.text.toInt(), matriz)

        calcularTabla1()
        lResultado.setProperties(400, 450, 200, 32, "Resultados", fontTitle)
        calcularTabla2()
    }

    private fun calcularTabla1() {
        val placeholdes = arrayListOf("Artículo", "Peso", "Costo") //tabla superior
        for(i in 0 .. tfWeight.text.toInt())
            placeholdes.add(i.toString())
        val cabecera: Array<String> = placeholdes.toArray(arrayOfNulls<String>(0))

        val modelo = DefaultTableModel()
        modelo.setColumnIdentifiers(cabecera)
        for (i in 0 until tfNumberOfItems.text.toInt()) {
            modelo.addRow(arrayOf<Any>(i + 1, pesos[i]!!.text.toInt(), valores[i]!!.text.toInt(), 0))
            for(j in 0 until tfWeight.text.toInt()){
                modelo.setValueAt(Algoritmo.informacion[i+1][j+1], i, 4 + j)
            }
        }

        val tabla = JTable()
        tabla.model = modelo
        tabla.rowHeight = 40
        tabla.setDefaultRenderer(
            Any::class.java, getCustomTable(
                semiDarkGrayBlue,
                semiDarkGrayBlue,
                mdb1,
                darkWhite,
                fontText
            )
        )
        tabla.gridColor = black
        tabla.addMouseListener(object: MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                Algoritmo.hallarItems(tabla.selectedRow+1, tabla.selectedColumn-3)
                var itemsList = ""
                var peso = 0
                for(i in 0 until Algoritmo.items.size) {
                    itemsList += "${Algoritmo.datosClass[Algoritmo.items[i] - 1][1]}:${Algoritmo.items[i]} ${
                        if(i < Algoritmo.items.size - 1) 
                            "+ "
                        else 
                            " "
                    }"
                    peso += Algoritmo.datosClass[Algoritmo.items[i] - 1][0]
                }
                JOptionPane.showMessageDialog(null, "Este valor se obtuvo a través de: $itemsList\n" +
                        "Donde n:m significa el valor n del artículo m\nEl peso total es: $peso\nLa complejidad de este algoritmo es polinomial: O(nW)")
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

        val header = tabla.tableHeader
        header.preferredSize = Dimension(580, 30)
        header.defaultRenderer = getCustomTable(semiDarkGray2, null, null, white, fontText)

        pOut.removeAll()
        pOut.setProperties(384, 50, 845, 361)

        val pTabla = tabla.getPanelBar(2, 2, 844, 360)
        pTabla.background = semiDarkGray2
        pTabla.verticalScrollBar.setUI(getCustomScroll())
        pOut.add(pTabla)

        repaint()
    }

    private fun calcularTabla2() {
        Algoritmo.hallarItems()

        val placeholdes = arrayOf("Artículo", "Peso", "Costo") //tabla superior

        val modelo = DefaultTableModel()
        modelo.setColumnIdentifiers(placeholdes)

        var peso = 0
        var costo = 0
        for (i in 0 until Algoritmo.items.size) {
            modelo.addRow(arrayOf<Any>(Algoritmo.items[i], Algoritmo.datosClass[Algoritmo.items[i] - 1][0],
                Algoritmo.datosClass[Algoritmo.items[i] - 1][1]))
            peso+=Algoritmo.datosClass[Algoritmo.items[i] - 1][0]
            costo+=Algoritmo.datosClass[Algoritmo.items[i] - 1][1]
        }
        modelo.addRow(arrayOf<Any>("Total", peso, costo))

        val tabla = JTable()
        tabla.model = modelo
        tabla.rowHeight = 40
        tabla.setDefaultRenderer(
            Any::class.java, getCustomTable(
                semiDarkGrayBlue,
                semiDarkGrayBlue,
                mdb1,
                darkWhite,
                fontText
            )
        )
        tabla.gridColor = black

        val header = tabla.tableHeader
        header.preferredSize = Dimension(580, 30)
        header.defaultRenderer = getCustomTable(semiDarkGray2, null, null, white, fontText)

        pOut1.removeAll()
        pOut1.setProperties(384, 500, 845, 191)

        val pTabla = tabla.getPanelBar(2, 2, 844, 190)
        pTabla.background = semiDarkGray2
        pTabla.verticalScrollBar.setUI(getCustomScroll())
        pOut1.add(pTabla)

        repaint()
    }

}