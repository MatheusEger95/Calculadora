package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityMainBinding
import org.mariuszgromada.math.mxparser.Expression

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textoDigitado = findViewById<TextView>(R.id.tv_text_typed)
        val tvResultado = findViewById<TextView>(R.id.tv_result)
        val buttons = arrayOf<Button>(
            findViewById(R.id.btn_1),
            findViewById(R.id.btn_2),
            findViewById(R.id.btn_3),
            findViewById(R.id.btn_4),
            findViewById(R.id.btn_5),
            findViewById(R.id.btn_6),
            findViewById(R.id.btn_7),
            findViewById(R.id.btn_8),
            findViewById(R.id.btn_9),
            findViewById(R.id.btn_0),
            findViewById(R.id.btn_ponto),
            findViewById(R.id.btn_menos),
            findViewById(R.id.btn_multiplicar),
            findViewById(R.id.btn_dividir),
            findViewById(R.id.btn_mais)

        )

        val btnC = findViewById<Button>(R.id.btn_C)
        val btntnNegativo = findViewById<Button>(R.id.btn_negativo)
        val btnApagar = findViewById<Button>(R.id.btn_apagar)
        val btnResultado = findViewById<Button>(R.id.btn_resultado)
        val btnPorcentagem = findViewById<Button>(R.id.btn_porcentagem)

        buttons.forEach { button ->
            button.setOnClickListener {
                val textoAtual = textoDigitado.text.toString()
                textoDigitado.text = textoAtual + "" + button.text
                }
            }

        btnC.setOnClickListener {
            textoDigitado.text = ""
            tvResultado.text = ""
        }

        btnApagar.setOnClickListener {
            textoDigitado.text = textoDigitado.text.dropLast(1)
        }

        btnPorcentagem.setOnClickListener {
            val textoTemp = textoDigitado.text.toString()

            if (textoTemp.isNotEmpty() && textoTemp.last() !in "+-*/") {
                textoDigitado.text = textoTemp + "%"
            }
        }

        btntnNegativo.setOnClickListener {
            val textoTemp = textoDigitado.text.toString()
            if (textoTemp.isNotEmpty() && textoTemp[0] != '-'){
                textoDigitado.text ="-$textoTemp"
             }else if (textoTemp.isNotEmpty()){
                textoDigitado.text = textoTemp.substring(1)
             }
        }

        fun ultimoCaractereOperador(): Boolean {
            val texto = textoDigitado.text.toString()
            return texto.isNotEmpty() && texto.last() in "+-*/"
        }

        fun substituirOperador(novoOperador: Char) {
            val texto = textoDigitado.text.toString()

            if (ultimoCaractereOperador()) {
                textoDigitado.text = texto.dropLast(1) + novoOperador
            } else {
                textoDigitado.text = texto + novoOperador
            }
        }

        fun temPonto(): Boolean {
            val texto = textoDigitado.text.toString()
            val partes = texto.split(Regex("[+\\-*/]"))
            return partes.last().contains(".")
        }


        findViewById<Button>(R.id.btn_ponto).setOnClickListener {
            if (!temPonto()) {
                textoDigitado.text = textoDigitado.text.toString() + "."
            }
        }

        findViewById<Button>(R.id.btn_mais).setOnClickListener {
            substituirOperador('+')
        }

        findViewById<Button>(R.id.btn_menos).setOnClickListener {
            substituirOperador('-')
        }

        findViewById<Button>(R.id.btn_multiplicar).setOnClickListener {
            substituirOperador('*')
        }

        findViewById<Button>(R.id.btn_dividir).setOnClickListener {
            substituirOperador('/')
        }

        btnResultado.setOnClickListener {
            val expressao = textoDigitado.text.toString()

            if (expressao.isNotEmpty() && expressao.last() in "+-*/") {
                tvResultado.text = "Erro: Operador final inválido"
                return@setOnClickListener
            }
            try {
                var resultadoFinal = 0.0
                var i = 0
                val operadores = mutableListOf<Char>()
                val regex = Regex("\\d+(\\.\\d+)?")

                while (i < expressao.length) {
                    val charAtual = expressao[i]

                    if (charAtual.isDigit() || charAtual == '.') {

                        val numero = regex.findAll(expressao.drop(i)).first().value
                        i += numero.length
                        val numDouble = numero.toDouble()

                        if (i < expressao.length && expressao[i] == '%') {
                            resultadoFinal += numDouble * 0.01 * resultadoFinal
                            i++
                        } else {
                            if (operadores.isEmpty() || operadores.last() == '+') {
                                resultadoFinal += numDouble
                            } else if (operadores.last() == '-') {
                                resultadoFinal -= numDouble
                            } else if (operadores.last() == '*') {
                                resultadoFinal *= numDouble
                            } else if (operadores.last() == '/') {
                                resultadoFinal /= numDouble
                            }
                        }
                    } else {
                        operadores.add(charAtual)
                        i++
                    }
                }
                tvResultado.text = if(resultadoFinal == resultadoFinal.toInt().toDouble()){
                    resultadoFinal.toInt().toString()
                }else{
                    resultadoFinal.toString()
                }
            } catch (e: Exception) {
                tvResultado.text = "Erro"
            }
        }
    }
}
