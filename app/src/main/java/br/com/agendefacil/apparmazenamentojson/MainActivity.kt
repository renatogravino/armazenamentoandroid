package br.com.agendefacil.apparmazenamentojson
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {

    // 1. Declarando as variáveis para conectar com a nossa tela
    private lateinit var etInputData: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLoad: Button
    private lateinit var tvResult: TextView

    // Nome do arquivo de texto onde o JSON será salvo
    private val fileName = "meus_dados.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2. Conectando as variáveis do Kotlin com os IDs lá do XML
        etInputData = findViewById(R.id.etInputData)
        btnSave = findViewById(R.id.btnSave)
        btnLoad = findViewById(R.id.btnLoad)
        tvResult = findViewById(R.id.tvResult)

        // 3. Ação do Botão Salvar
        btnSave.setOnClickListener {
            val textoParaSalvar = etInputData.text.toString()
            if (textoParaSalvar.isNotEmpty()) {
                saveData(textoParaSalvar) // Chama o método que vamos criar abaixo
            } else {
                Toast.makeText(this, "Por favor, digite algo para salvar!", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Ação do Botão Carregar
        btnLoad.setOnClickListener {
            loadData() // Chama o método que vamos criar abaixo
        }
    }

    // Método para SALVAR os dados em JSON no armazenamento interno
    private fun saveData(texto: String) {
        try {
            // Criamos a estrutura JSON. Pense nisso como uma etiqueta ("informacao") e o valor (o texto digitado)
            val jsonObject = JSONObject()
            jsonObject.put("informacao", texto)

            // Convertendo o JSON para um texto simples
            val jsonString = jsonObject.toString()

            // Salvando no arquivo privado do aplicativo
            openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }

            Toast.makeText(this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
            etInputData.text.clear() // Limpa o campo de texto da tela

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao salvar os dados.", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para CARREGAR os dados em JSON do armazenamento interno
    private fun loadData() {
        try {
            val file = File(filesDir, fileName)

            // Verifica se o arquivo já existe (se você já salvou algo antes)
            if (file.exists()) {
                // Lê o texto do arquivo
                val jsonString = file.readText()

                // Converte o texto de volta para a estrutura JSON
                val jsonObject = JSONObject(jsonString)

                // Puxa o valor correspondente à etiqueta "informacao"
                val textoRecuperado = jsonObject.getString("informacao")

                // Mostra na tela
                tvResult.text = "Dado recuperado:\n$textoRecuperado"

            } else {
                Toast.makeText(this, "Nenhum dado encontrado. Salve algo primeiro!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao carregar os dados.", Toast.LENGTH_SHORT).show()
        }
    }
}