package services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SMSListener extends BroadcastReceiver {

    OTPListener otpReceiveInterface;

    public void setOnOtpListeners(OTPListener otpReceiveInterface) {
        this.otpReceiveInterface = otpReceiveInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();
            if(extras!=null)
            {
                Status mStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                if(mStatus!=null)
                {
                    if (mStatus.getStatusCode() == CommonStatusCodes.SUCCESS) {
                        // Get SMS message contents'
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        if (otpReceiveInterface != null && message != null) {
                            String otp = message.replaceAll("[^0-9]", "");
                            otpReceiveInterface.onOTPReceived(otp);
                        }
                    }
                }
            }
        }
    }

    public interface OTPListener {
        void onOTPReceived(String otp);
    }

}