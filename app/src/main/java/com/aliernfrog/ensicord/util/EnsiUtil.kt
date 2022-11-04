package com.aliernfrog.ensicord.util

import android.content.Context
import android.content.SharedPreferences
import com.aliernfrog.ensicord.ConfigKey
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.UserStatus
import com.aliernfrog.ensigeneration.EnsiConfig
import com.aliernfrog.ensigeneration.EnsiGeneration
import com.aliernfrog.ensigeneration.EnsiGenerationType

class EnsiUtil {
    companion object {
        private lateinit var ensiGeneration: EnsiGeneration

        fun initialize(config: SharedPreferences) {
            ensiGeneration = EnsiGeneration(EnsiConfig(
                words = config.getStringSet(ConfigKey.KEY_ENSI_WORDS, setOf("me","you","we","they","alierns","indinibee","bees","momes","frogs","mouse","chicken","furries","frog","Exi's basement","free candies","ensi","van","laptop","marchmilos","mouse"))!!,
                verbs = config.getStringSet(ConfigKey.KEY_ENSI_VERBS, setOf("sobbed","adsed","feed","featured","faced","undefined","petted","mousing"))!!
            ))
        }

        fun generateResponse(userInput: String = ""): String {
            val input = userInput.lowercase()
            val args = input.split(" ")
            if (args.contains("hi") || args.contains("hello")) return "wow hi bro"
            if (args.contains("gn") || input.contains("good night")) return "gn my,"
            if (args.contains("give") && args.contains("nick")) return generateNickname()
            if (args.contains("tell") && (args.contains("story") || args.contains("stories"))) return generateStory()
            return generateNormalResponse()
        }

        private fun generateNormalResponse(): String {
            return ensiGeneration.generate(
                generationType = listOf(EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.LEGIT,EnsiGenerationType.ALLCAPS).random(),
                sentenceCount = 1
            )
        }

        private fun generateStory(): String {
            return ensiGeneration.generate(
                generationType = EnsiGenerationType.LEGIT,
                sentenceCount = (1..50).random()
            )
        }

        private fun generateNickname(): String {
            return ensiGeneration.generate(
                generationType = listOf(EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.LEGIT,EnsiGenerationType.ALLCAPS).random(),
                types = setOf("%WORD_VERB% %WORD_VERB%","%WORD_VERB%"),
                sentenceCount = 1,
                addStartingSentence = false,
                questionsAllowed = false,
                punctuationsAllowed = false
            )
        }

        fun generateStatus(context: Context): UserStatus {
            val name = ensiGeneration.generate(
                generationType = listOf(EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.LEGIT,EnsiGenerationType.ALLCAPS).random(),
                sentenceCount = 1,
                addStartingSentence = false
            )
            val type = listOf(
                context.getString(R.string.status_playing),
                context.getString(R.string.status_streaming),
                context.getString(R.string.status_watching),
                context.getString(R.string.status_listening),
                context.getString(R.string.status_competing),
                null
            ).random()
            return UserStatus(type, name)
        }
    }
}