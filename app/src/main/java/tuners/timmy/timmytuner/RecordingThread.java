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
    private static final int SAMPLE_RATE = 8000;
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

            // Cal fer el calcul de pitch i enviar la frecuencia que volem (si canvia el mListener
            // cal canviar la variable en el TunerFragment i la linia 12 d'aquest fitxer tmb ! on es declara

            float freq = PitchYin(audioBuffer);

            // Notify waveform
            mListener.onAudioDataReceived(freq);
        }

        record.stop();
        record.release();

        Log.v(LOG_TAG, String.format("Recording stopped. Samples read: %d", shortsRead));
    }

    private float[] yinBuffer;
    private static final float threshold = 0.125f;
    private final float sampleRate = SAMPLE_RATE;

    private float PitchYin(short[] audioBuffer) {
        final int tauEstimate;
        final float pitchInHertz;
        yinBuffer = new float[bufferSize / 2];

        // step 2
        difference(audioBuffer);

        // step 3
        cumulativeMeanNormalizedDifference();

        // step 4
        tauEstimate = absoluteThreshold();

        // step 5
        if (tauEstimate != -1) {
            final float betterTau = parabolicInterpolation(tauEstimate);

            // step 6
            // TODO Implement optimization for the AUBIO_YIN algorithm.
            // 0.77% => 0.5% error rate,
            // using the data of the YIN paper
            // bestLocalEstimate()

            // conversion to Hz
            pitchInHertz = sampleRate / betterTau;
        } else{
            // no pitch found
            pitchInHertz = -1;
        }

        return pitchInHertz;

    }

    /**
     * Implements the difference function as described in step 2 of the YIN
     * paper.
     */
    private void difference(final short[] audioBuffer) {
        int index, tau;
        float delta;
        for (tau = 0; tau < yinBuffer.length; tau++) {
            yinBuffer[tau] = 0;
        }
        for (tau = 1; tau < yinBuffer.length; tau++) {
            for (index = 0; index < (yinBuffer.length - tau); index++) {
                delta = audioBuffer[index] - audioBuffer[index + tau];
                yinBuffer[tau] += delta * delta;
            }
        }
    }

    private void cumulativeMeanNormalizedDifference() {
        int tau;
        yinBuffer[0] = 1;
        float runningSum = 0;
        for (tau = 1; tau < yinBuffer.length; tau++) {
            runningSum += yinBuffer[tau];
            yinBuffer[tau] *= tau / runningSum;
        }
    }

    /**
     * Implements step 4 of the AUBIO_YIN paper.
     */
    private int absoluteThreshold() {
        // Uses another loop construct
        // than the AUBIO implementation
        int tau;
        // first two positions in yinBuffer are always 1
        // So start at the third (index 2)
        for (tau = 2; tau < yinBuffer.length; tau++) {
            if (yinBuffer[tau] < threshold) {
                while (tau + 1 < yinBuffer.length && yinBuffer[tau + 1] < yinBuffer[tau]) {
                    tau++;
                }
                // found tau, exit loop and return
                // store the probability
                // From the YIN paper: The threshold determines the list of
                // candidates admitted to the set, and can be interpreted as the
                // proportion of aperiodic power tolerated
                // within a periodic signal.
                //
                // Since we want the periodicity and and not aperiodicity:
                // periodicity = 1 - aperiodicity

                ////result.setProbability(1 - yinBuffer[tau]);
                break;
            }
        }


        // if no pitch found, tau => -1
        if (tau == yinBuffer.length || yinBuffer[tau] >= threshold) {
            tau = -1;
            //result.setProbability(0);
            //result.setPitched(false);
        } else {
            //result.setPitched(true);
        }

        return tau;
    }

    /**
     * Implements step 5 of the AUBIO_YIN paper. It refines the estimated tau
     * value using parabolic interpolation. This is needed to detect higher
     * frequencies more precisely. See http://fizyka.umk.pl/nrbook/c10-2.pdf and
     * for more background
     * http://fedc.wiwi.hu-berlin.de/xplore/tutorials/xegbohtmlnode62.html
     *
     * @param tauEstimate
     *            The estimated tau value.
     * @return A better, more precise tau value.
     */
    private float parabolicInterpolation(final int tauEstimate) {
        final float betterTau;
        final int x0;
        final int x2;

        if (tauEstimate < 1) {
            x0 = tauEstimate;
        } else {
            x0 = tauEstimate - 1;
        }
        if (tauEstimate + 1 < yinBuffer.length) {
            x2 = tauEstimate + 1;
        } else {
            x2 = tauEstimate;
        }
        if (x0 == tauEstimate) {
            if (yinBuffer[tauEstimate] <= yinBuffer[x2]) {
                betterTau = tauEstimate;
            } else {
                betterTau = x2;
            }
        } else if (x2 == tauEstimate) {
            if (yinBuffer[tauEstimate] <= yinBuffer[x0]) {
                betterTau = tauEstimate;
            } else {
                betterTau = x0;
            }
        } else {
            float s0, s1, s2;
            s0 = yinBuffer[x0];
            s1 = yinBuffer[tauEstimate];
            s2 = yinBuffer[x2];
            // fixed AUBIO implementation, thanks to Karl Helgason:
            // (2.0f * s1 - s2 - s0) was incorrectly multiplied with -1
            betterTau = tauEstimate + (s2 - s0) / (2 * (2 * s1 - s2 - s0));
        }
        return betterTau;
    }

}

