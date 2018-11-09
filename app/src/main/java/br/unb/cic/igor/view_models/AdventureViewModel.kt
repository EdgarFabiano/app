package br.unb.cic.igor.view_models

import android.arch.lifecycle.ViewModel;
import br.unb.cic.igor.classes.Adventure
import br.unb.cic.igor.classes.Master
import br.unb.cic.igor.classes.Player
import br.unb.cic.igor.classes.Session
import java.util.*

class AdventureViewModel : ViewModel() {
    val mockAdventure: Adventure = Adventure("A lenda de OGUH", "Acima de tudo, é fundamental ressaltar que o aumento do diálogo entre os diferentes setores produtivos possibilita uma melhor visão global das condições inegavelmente apropriadas. Percebemos, cada vez mais, que a necessidade de renovação processual aponta para a melhoria do investimento em reciclagem técnica. Ainda assim, existem dúvidas a respeito de como a hegemonia do ambiente político estimula a padronização do levantamento das variáveis envolvidas. É claro que o desafiador cenário globalizado afeta positivamente a correta previsão do impacto na agilidade decisória. Podemos já vislumbrar o modo pelo qual o início da atividade geral de formação de atitudes facilita a criação do fluxo de informações.",
            Master("userId", "name", "char","desc"),
            arrayListOf(Player("uid", "name", "char","desc", "attrs", "info")),
            arrayListOf(
                    Session("session1", Date(), "session summary"),
                    Session("session2", Date(), "session summary 2"),
                    Session("session3", Date(), "session summary 3"),
                    Session("session4", Date(), "session summary 4")
            )
    )
}
