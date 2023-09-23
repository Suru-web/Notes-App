package com.javaproject.notes;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class fingerprint {
    FingerPrintCallBack callBack;
    public fingerprint(Context context) {
    }
    public void setfingerprintCallback(FingerPrintCallBack callBack){
        this.callBack = callBack;
    }

    public void fingerprint(Context context,String what, int position){
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(context);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(context,"This device doesnot have a fingerprint sensor",Toast.LENGTH_SHORT).show();
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(context,"The biometric sensor is currently unavailable",Toast.LENGTH_SHORT).show();
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(context,"Your device doesn't have fingerprint saved,please check your security settings",Toast.LENGTH_SHORT).show();
                break;
        }
        Executor executor = ContextCompat.getMainExecutor(context);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                callBack.onFail(what,position);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                callBack.onSuccess(what,position);
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                callBack.onFail(what,position);
                Toast.makeText(context,"Fingerprint authentication failed",Toast.LENGTH_SHORT).show();
            }
        });
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Note ")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();
        biometricPrompt.authenticate(promptInfo);
    }


    public interface FingerPrintCallBack{
        void onSuccess(String what, int position);
        void onFail(String what, int position);
    }
}
