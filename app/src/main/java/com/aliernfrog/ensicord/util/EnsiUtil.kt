package com.aliernfrog.ensicord.util

import android.content.Context
import android.content.SharedPreferences
import com.aliernfrog.ensicord.AddonKey
import com.aliernfrog.ensicord.R
import com.aliernfrog.ensicord.data.UserStatus
import com.aliernfrog.ensigeneration.*

class EnsiUtil {
    companion object {
        private lateinit var ensiGeneration: EnsiGeneration

        fun initialize(addonConfig: SharedPreferences) {
            ensiGeneration = EnsiGeneration(EnsiConfig(
                words = addonConfig.getStringSet(AddonKey.KEY_ENSI_WORDS, setOf())!!.ifEmpty { EnsiConfigDefaults.words },
                verbs = addonConfig.getStringSet(AddonKey.KEY_ENSI_VERBS, setOf())!!.ifEmpty { EnsiConfigDefaults.verbs },
                times = addonConfig.getStringSet(AddonKey.KEY_ENSI_TIMES, setOf())!!.ifEmpty { EnsiConfigDefaults.times },
                chars = addonConfig.getStringSet(AddonKey.KEY_ENSI_CHARS, setOf())!!.ifEmpty { EnsiConfigDefaults.chars },
                places = addonConfig.getStringSet(AddonKey.KEY_ENSI_PLACES, setOf())!!.ifEmpty { EnsiConfigDefaults.places },
                concs = addonConfig.getStringSet(AddonKey.KEY_ENSI_CONCS, setOf())!!.ifEmpty { EnsiConfigDefaults.concs },
                emotions = addonConfig.getStringSet(AddonKey.KEY_ENSI_EMOTIONS, setOf())!!.ifEmpty { EnsiConfigDefaults.emotions },
                others = addonConfig.getStringSet(AddonKey.KEY_ENSI_OTHERS, setOf())!!.ifEmpty { EnsiConfigDefaults.others },
                positions = addonConfig.getStringSet(AddonKey.KEY_ENSI_POSITIONS, setOf())!!.ifEmpty { EnsiConfigDefaults.positions },
                normalTypes = addonConfig.getStringSet(AddonKey.KEY_ENSI_TYPES_NORMAL, setOf())!!.ifEmpty { EnsiConfigDefaults.normalTypes },
                questionTypes = addonConfig.getStringSet(AddonKey.KEY_ENSI_TYPES_QUESTION, setOf())!!.ifEmpty { EnsiConfigDefaults.questionTypes },
                startingTypes = addonConfig.getStringSet(AddonKey.KEY_ENSI_TYPES_STARTING, setOf())!!.ifEmpty { EnsiConfigDefaults.startingTypes },
                sentenceCountRange = getSentenceCountRange(addonConfig, EnsiConfigDefaults.sentenceCountRange),
                wordAsCharAllowed = addonConfig.getBoolean(AddonKey.KEY_ENSI_WORD_AS_CHAR_ALLOWED, EnsiConfigDefaults.wordAsCharAllowed),
                startingSentenceAllowed = addonConfig.getBoolean(AddonKey.KEY_ENSI_STARTING_SENTENCE_ALLOWED, EnsiConfigDefaults.startingSentenceAllowed),
                questionsAllowed = addonConfig.getBoolean(AddonKey.KEY_ENSI_QUESTIONS_ALLOWED, EnsiConfigDefaults.questionsAllowed),
                punctuationsAllowed = addonConfig.getBoolean(AddonKey.KEY_ENSI_PUNCTUATIONS_ALLOWED, EnsiConfigDefaults.punctuationsAllowed),
                subSentencesAllowed = addonConfig.getBoolean(AddonKey.KEY_ENSI_SUB_SENTENCES_ALLOWED, EnsiConfigDefaults.subSentencesAllowed)
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
                startingSentenceAllowed = false,
                questionsAllowed = false,
                addPunctuations = false
            )
        }

        fun generateStatus(context: Context): UserStatus {
            val name = ensiGeneration.generate(
                generationType = listOf(EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.RAW,EnsiGenerationType.LEGIT,EnsiGenerationType.ALLCAPS).random(),
                sentenceCount = 1,
                addPunctuations = false
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

        private fun getSentenceCountRange(addonConfig: SharedPreferences, default: Range): Range {
            val min = addonConfig.getInt(AddonKey.KEY_ENSI_SENTENCE_COUNT_MIN, default.min)
            val max = addonConfig.getInt(AddonKey.KEY_ENSI_SENTENCE_COUNT_MAX, default.max)
            return Range(min, max)
        }
    }
}