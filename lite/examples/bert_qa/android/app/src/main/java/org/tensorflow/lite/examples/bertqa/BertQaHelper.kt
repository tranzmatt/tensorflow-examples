package org.tensorflow.lite.examples.bertqa

import android.content.Context
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.text.qa.BertQuestionAnswerer
import org.tensorflow.lite.task.text.qa.BertQuestionAnswerer.BertQuestionAnswererOptions
import org.tensorflow.lite.task.text.qa.QaAnswer
import java.lang.IllegalStateException

class BertQaHelper(
    val context: Context,
    var numThreads: Int = 2,
    var currentDelegate: Int = DELEGATE_CPU, // Default to CPU
    val answererListener: AnswererListener?
) {

    private var bertQuestionAnswerer: BertQuestionAnswerer? = null

    init {
        setupBertQuestionAnswerer()
    }

    fun clearBertQuestionAnswerer() {
        bertQuestionAnswerer = null
    }

    private fun setupBertQuestionAnswerer() {
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(numThreads)

        when (currentDelegate) {
            DELEGATE_CPU -> {
                // Default, no action needed
            }
            DELEGATE_GPU -> {
                if (CompatibilityList().isDelegateSupportedOnThisDevice) {
                    baseOptionsBuilder.useGpu()
                    Log.i(TAG, "Using GPU Delegate")
                } else {
                    answererListener?.onError("GPU is not supported on this device")
                    Log.e(TAG, "GPU is not supported on this device")
                    return // Exit setup if GPU is not supported
                }
            }
            DELEGATE_NNAPI -> {
                baseOptionsBuilder.useNnapi()
                Log.i(TAG, "Using NNAPI Delegate")
            }
            else -> {
                answererListener?.onError("Invalid delegate option")
                Log.e(TAG, "Invalid delegate option: $currentDelegate")
                return // Exit setup if an invalid delegate is provided
            }
        }

        val options = BertQuestionAnswererOptions.builder()
            .setBaseOptions(baseOptionsBuilder.build())
            .build()

        try {
            bertQuestionAnswerer =
                BertQuestionAnswerer.createFromFileAndOptions(context, BERT_QA_MODEL, options)
            Log.i(TAG, "BertQuestionAnswerer created successfully with delegate: $currentDelegate")
        } catch (e: IllegalStateException) {
            answererListener
                ?.onError("Bert Question Answerer failed to initialize. See error logs for details")
            Log.e(TAG, "TFLite failed to load model with error: ${e.message}")
            // Try fallback to CPU if GPU initialization fails
            if (currentDelegate != DELEGATE_CPU) {
                Log.i(TAG, "Falling back to CPU delegate")
                currentDelegate = DELEGATE_CPU
                setupBertQuestionAnswerer()
            }
        }
    }

    fun answer(contextOfQuestion: String, question: String) {
        if (bertQuestionAnswerer == null) {
            setupBertQuestionAnswerer()
        }

        if (bertQuestionAnswerer == null) {
            answererListener?.onError("Bert Question Answerer is not initialized")
            return
        }

        // Measure inference time
        val startTime = SystemClock.uptimeMillis()
        val answers = bertQuestionAnswerer?.answer(contextOfQuestion, question)
        val inferenceTime = SystemClock.uptimeMillis() - startTime

        answererListener?.onResults(answers, inferenceTime)
    }

    interface AnswererListener {
        fun onError(error: String)
        fun onResults(
            results: List<QaAnswer>?,
            inferenceTime: Long
        )
    }

    companion object {
        private const val BERT_QA_MODEL = "mobilebert.tflite"
        private const val TAG = "BertQaHelper"
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DELEGATE_NNAPI = 2
    }
}
