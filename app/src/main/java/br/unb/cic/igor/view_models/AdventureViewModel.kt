package br.unb.cic.igor.view_models

import android.arch.lifecycle.ViewModel;
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Master
import br.unb.cic.igor.classes.Player
import br.unb.cic.igor.classes.Session
import java.util.*

class AdventureViewModel : ViewModel() {
    val mockAdventure: Adventure = Adventure(name = "A lenda de OGUH", summary = "Acima de tudo, é fundamental ressaltar que o aumento do diálogo entre os diferentes setores produtivos possibilita uma melhor visão global das condições inegavelmente apropriadas. Percebemos, cada vez mais, que a necessidade de renovação processual aponta para a melhoria do investimento em reciclagem técnica. Ainda assim, existem dúvidas a respeito de como a hegemonia do ambiente político estimula a padronização do levantamento das variáveis envolvidas. É claro que o desafiador cenário globalizado afeta positivamente a correta previsão do impacto na agilidade decisória. Podemos já vislumbrar o modo pelo qual o início da atividade geral de formação de atitudes facilita a criação do fluxo de informações.",
            master = Master("userId", "name", "char","desc"),
            players = arrayListOf(Player("uid", "name", "char","desc", "attrs", "info")),
            sessions = arrayListOf(
                    Session(name = "session1", date = Date(), summary = "session summary"),
                    Session(name = "session2", date = Date(), summary = "session summary 2"),
                    Session(name = "session3", date = Date(), summary = "session summary 3"),
                    Session(name = "session4", date = Date(), summary = "session summary 4")
            )
    )
}
