package com.nurul.medicareplus.interfaces;

import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;

/**
 * This is Created by Nurul Islam Tipu on 11/22/2018
 */
public interface Patient_interface {
    void appointedProfile(AppoinmentedProfile profile);
    void patientProfile(PatientSignupProfile patientSignupProfile);
}
