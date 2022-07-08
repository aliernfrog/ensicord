package com.aliernfrog.ensicord.utils

class EnsiUtil {
    companion object {
        private val dates = listOf("one day","weeks ago","yesterday","years ago","tomorrow")
        private val characters = listOf("frog","mouse","Ensi","Aliern","Exi","Infini","marchmilo","cat","memer","karen","manager")
        private val pronouns = listOf("you","he","she","it","they","that")
        private val places = listOf("hospital","Exi's basement","basement","hotel","parking","shop")
        private val edConcs = listOf("and","but","when","even though","before","after")
        private val ingConcs = listOf("while","when")
        private val otherWords = listOf("unfortunately","fortunately","luckily","finally","thankfully","at least","weirdly")
        private lateinit var edVerbs: List<String>
        private lateinit var ingVerbs: List<String>
        private lateinit var words: List<String>

        private val characterTypes = listOf("%","% and %")
        private val placeTypes = listOf("to %","in %","at %","on top of %")

        private val startingTypes = listOf("T ","T, ","")
        private val normalTypes = listOf("W","G","D","C was G P","C D W","C D W","C D W I C was G","C D","C was a W","C D C","C D C with W","was C G?","was C G P?","was C W?","was C a W?")

        fun prepare(_verbs: List<String>, _words: List<String>) {
            edVerbs = _verbs.filter { it.endsWith("ed") }
            ingVerbs = _verbs.filter { it.endsWith("ing") }
            words = _words
        }

        fun getResponse(
            type: String = "RAW",
            sentenceCount: Int = 1,
            starting: Boolean = false,
            lowCharChance: Boolean = false,
            punctuations: Boolean = false
        ): String {
            var response = ""
            if (starting) response = createStartingSentence(type, lowCharChance, punctuations)+" "
            for (i in 0..sentenceCount) response += createNormalSentence(type, lowCharChance, punctuations)+" "
            return response
        }

        private fun createNormalSentence(
            type: String,
            lowCharChance: Boolean,
            punctuations: Boolean
        ): String {
            val addOtherWord = listOf(false,false,false,false,true).random()
            var base = normalTypes.random()
            if (addOtherWord) base = listOf("O, $base","$base, O").random()
            return manageString(type, base, lowCharChance, punctuations)
        }

        private fun createStartingSentence(
            type: String,
            lowCharChance: Boolean,
            punctuations: Boolean
        ): String {
            val base = startingTypes.random()
            val starting = replaceBase(base)
            val normal = createNormalSentence(type, lowCharChance, punctuations)
            return manageCaps(starting+normal, type)
        }

        private fun manageCaps(string: String, type: String): String {
            return when(type) {
                "RAW" -> string
                "LEGIT" -> string[0].uppercase()+string.drop(1)
                "ALLCAPS" -> string.uppercase()
                else -> string
            }
        }

        private fun managePunctuation(string: String, add: Boolean): String {
            if (!add || string.endsWith("?")) return string
            val chosen = listOf(".",".",".",".",".","...","!").random()
            return string+chosen
        }

        private fun getCharacter(lowCharChance: Boolean): String {
            val chosen = listOf("character","pronoun").random()
            return if (chosen == "character") {
                val base = characterTypes.random()
                val split = base.split("")
                var final = ""
                split.forEach {
                    final += if (it == "%") {
                        if (!lowCharChance) characters.random()
                        else (listOf(words,words,characters).random()).random()
                    } else it
                }
                final
            } else pronouns.random()
        }

        private fun getPlace(): String {
            val base = placeTypes.random()
            val split = base.split("")
            var final = ""
            split.forEach {
                final += if (it == "%") places.random()
                else it
            }
            return final
        }

        private fun replaceBase(string: String, lowCharChance: Boolean = false): String {
            val split = string.split("")
            var final = ""
            split.forEach {
                final += when(it) {
                    "T" -> dates.random()
                    "C" -> getCharacter(lowCharChance)
                    "P" -> getPlace()
                    "E" -> edConcs.random()
                    "I" -> ingConcs.random()
                    "O" -> otherWords.random()
                    "D" -> edVerbs.random()
                    "G" -> ingVerbs.random()
                    "W" -> words.random()
                    else -> it
                }
                //TODO emojis
            }
            return final
        }

        private fun manageString(type: String, string: String, lowCharChance: Boolean, punctuations: Boolean): String {
            return manageCaps(managePunctuation(replaceBase(string, lowCharChance), punctuations), type)
        }
    }
}