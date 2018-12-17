package tuners.timmy.timmytuner;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.Arrays;


public class RecordingThread {

    public interface Listener {
        void onAudioDataReceived(float max);
    }

    private static final String LOG_TAG = RecordingThread.class.getSimpleName();
    private static final int SAMPLE_RATE = 16000;
    private int bufferSize;

    public RecordingThread(Listener listener) {
        mListener = listener;
    }

    private boolean mShouldContinue; //para el bucle while de lectura audio
    private Listener mListener;
    private Thread mThread;
    private int max;

    public boolean recording() {
        return mThread != null;
    }

    public void startRecording() {
        if (mThread != null)
            return;

        mShouldContinue = true;
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                record();
            }
        });
        mThread.start();
    }

    public void stopRecording() {
        if (mThread == null)
            return;

        mShouldContinue = false;
        mThread = null;
    }

    private void record() {
        Log.v(LOG_TAG, "Start");
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

        // buffer size in bytes
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = SAMPLE_RATE * 2;
        }

        short[] audioBuffer = new short[bufferSize / 2];
        resultBuffer = new float[bufferSize / 2];
        // Work array for pitch algorithm
        wave = new float[bufferSize / 2];

        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOG_TAG, "Audio Record can't initialize!");
            return;
        }
        record.startRecording();

        Log.v(LOG_TAG, "Start recording");

        long shortsRead = 0;
        while (mShouldContinue) {
            int numberOfShort = record.read(audioBuffer, 0, audioBuffer.length);
            shortsRead += numberOfShort;



            //calcul amplitud
            //max = calculaAmplitud(audioBuffer);







            // Cal fer el calcul de pitch i enviar la frecuencia que volem (si canvia el mListener
            // cal canviar la variable en el TunerFragment i la linia 12 d'aquest fitxer tmb ! on es declara

            float freq = calculaFreq(audioBuffer);// + (float)Math.random()*0.01f;


            // prova per veure que funciona
            //float freq = calculaAvrg(audioBuffer);



            // Notify waveform
            mListener.onAudioDataReceived(freq);
        }

        record.stop();
        record.release();

        Log.v(LOG_TAG, String.format("Recording stopped. Samples read: %d", shortsRead));
    }

    private int calculaAmplitud(short[] audioBuffer) {
        int max = 0;
        for (short s : audioBuffer) {
            if (Math.abs(s) > max)
            {
                max = Math.abs(s);
            }
        }
        return max;
    }




    // Càlcul de la freqüència


    // According to the YIN Paper, the threshold should be between 0.10 and 0.15
    private static final float ABSOLUTE_THRESHOLD = 0.125f;

    private final float sampleRate = SAMPLE_RATE;
    private float[] resultBuffer;
    private int maxBufferData = 32767;

    public float calculaFreq(short[] audioBuffer) {
        int tau;

        // First, perform the functions to normalize the wave data --> passem de (0-32767) a (0-1)
        float[] wave = normalizeBufferData(audioBuffer);

        // The first and second steps in the YIN algorithm
        autoCorrelationDifference(wave);

        // The third step in the YIN algorithm
        cumulativeMeanNormalizedDifference();

        // Then perform the functions to retrieve the tau (the approximate period)

        // The fourth step in the YIN algorithm
        tau = absoluteThreshold();

        // The fifth step in the YIN algorithm (period)
        float betterTau = parabolicInterpolation(tau);

        // TODO implement the sixth and final step of the YIN algorithm
        // (it isn't implemented in the Tarsos DSP project but is briefly explained in the YIN
        // paper).

        // The fundamental frequency (note frequency) is the sampling rate divided by the tau (index
        // within the resulting buffer array that marks the period).
        // The period is the duration (or index here) of one cycle.
        // Frequency = 1 / Period, with respect to the sampling rate, Frequency = Sample Rate / Period
        return sampleRate / betterTau;

    }



    /**
     * Normalitza el señal de -1 a 1 en funció del buffer size.
     * @param audioBuffer
     * @return
     */

    private float[] wave;
    private float average;

    private float[] normalizeBufferData(short[] audioBuffer) {
        for (int i = 0; i < wave.length; i++) {
            wave[i] = (float)audioBuffer[i] / ((float)maxBufferData * 2) + 0.5f;
        }
        return wave;
    }
    // prova per veure que funciona la normalització!!!!!!!!!!!!!!!!!!!!!
    private float calculaAvrg(short[] audioBuffer) {
        for (int i = 0; i < wave.length; i++) {
            average += (float)audioBuffer[i] / ((float)maxBufferData); // * 2) + 0.5f;;
        }
        return average / wave.length;
    }


    /**
     * Performs the first and second step of the YIN Algorithm on the provided array buffer values.
     * This is a "combination" of the AutoCorrelation Method and the Difference Function. The
     * AutoCorrelation Method multiplies the array value at the specified index with the array value
     * at the specified index plus the "tau" (greek letter used in the formula). Whereas the
     * Difference Function takes the square of the difference of the two values. This is supposed to
     * provide a more accurate result (from about 10% to about 1.95% error rate). Note that this
     * formula is a riemann sum, meaning the operation specified above is performed and accumulated
     * for every value in the array. The result of this function is stored in a global array,
     * {@link #resultBuffer}, which the subsequent steps of the algorithm should use.
     *
     * @param wave The waveform data to perform the AutoCorrelation Difference function on.
     */
    private void autoCorrelationDifference(final float[] wave) {
        // Note this algorithm is currently slow (O(n^2)). Should look for any possible optimizations.
        int length = resultBuffer.length;
        int i, j;

        for (j = 1; j < length; j++) {
            for (i = 0; i < length-j; i++) {
                // d sub t (tau) = (x(i) - x(i - tau))^2, from i = 1 to result buffer size
                float x = (wave[i] - wave[i + j]);
                resultBuffer[j] += x*x;
            }
        }
    }


    /**
     * Performs the third step in the YIN Algorithm on the {@link #resultBuffer}. The result of this
     * function yields an even lower error rate (about 1.69% from 1.95%). The {@link #resultBuffer}
     * is updated when this function is performed.
     */
    private void cumulativeMeanNormalizedDifference() {
        // newValue = oldValue / (runningSum / tau)
        // == (oldValue / 1) * (tau / runningSum)
        // == oldValue * (tau / runningSum)

        // Here we're using index i as the "tau" in the equation
        int i;
        int length = resultBuffer.length;
        float runningSum = 0;

        // Set the first value in the result buffer to the value of one
        Arrays.fill(resultBuffer, 1);

        for (i = 1; i < length; i++) {
            // The sum of this value plus all the previous values in the buffer array
            runningSum += resultBuffer[i];

            // The current value is updated to be the current value multiplied by the index divided by the running sum value
            resultBuffer[i] *= i / runningSum;
        }
    }

    /**
     * Performs step four of the YIN Algorithm on the {@link #resultBuffer}. This is the first step
     * in the algorithm to attempt finding the period of the wave data. When attempting to determine
     * the period of a wave, it's common to search for the high or low peaks or dips of the wave.
     * This will allow you to determine the length of a cycle or its period. However, especially
     * with a natural sound sample, it is possible to have false dips. This makes determining the
     * period more difficult. This function attempts to resolve this issue by introducing a
     * threshold. The result of this function yields an even lower rate (about 0.78% from about
     * 1.69%).
     *
     * @return The tau indicating the approximate period.
     */
    private int absoluteThreshold() {
        int tau;
        int length = resultBuffer.length;

        // The first two values in the result buffer should be 1, so start at the third value
        for (tau = 2; tau < length; tau++) {
            // If we are less than the threshold, continue on until we find the lowest value
            // indicating the lowest dip in the wave since we first crossed the threshold.
            if (resultBuffer[tau] < ABSOLUTE_THRESHOLD) {
                while (tau + 1 < length && resultBuffer[tau + 1] < resultBuffer[tau]) {
                    tau++;
                }

                // We have the approximate tau value, so break the loop
                break;
            }
        }

        // Some implementations of this algorithm set the tau value to -1 to indicate no correct tau
        // value was found. This implementation will just return the last tau.
        tau = tau >= length ? length - 1 : tau;
        return tau;
    }

    /**
     * Further lowers the error rate by using parabolas to smooth the wave between the minimum and
     * maximum points. Especially helps to detect higher frequencies more precisely. The result of
     * this function results in only a small error rate decline from about 0.78% to about 0.77%.
     */
    private float parabolicInterpolation(final int currentTau) {
        // Finds the points to fit the parabola between
        int x0 = currentTau < 1 ? currentTau : currentTau - 1;
        int x2 = currentTau + 1 < resultBuffer.length ? currentTau + 1 : currentTau;

        // Finds the better tau estimate
        float betterTau;

        if (x0 == currentTau) {
            if (resultBuffer[currentTau] <= resultBuffer[x2]) {
                betterTau = (float) currentTau;
            } else {
                betterTau = x2;
            }
        } else if (x2 == currentTau) {
            if (resultBuffer[currentTau] <= resultBuffer[x0]) {
                betterTau = (float) currentTau;
            } else {
                betterTau = x0;
            }
        } else {
            // Fit the parabola between the first point, current tau, and the last point to find a
            // better tau estimate.
            float s0 = resultBuffer[x0];
            float s1 = resultBuffer[currentTau];
            float s2 = resultBuffer[x2];

            betterTau = (float) currentTau + (s2 - s0) / (2 * (2 * s1 - s2 - s0));
        }

        return betterTau;
    }

}

