package models.exercise;

package eu.heartman.model;

import lombok.Builder;
import lombok.Data;

public class HL7 {
    private Boolean takesBetaBlockers;
    private Boolean takesACEInhibitors;
    private Boolean takesARBs;
    private Boolean takesDiuretics;
    private Boolean takesLoopDiuretics;
    private Integer maxHeartRate;
    private Integer upperHeartRate;
    private Integer lowerHeartRate;
    private Integer exerciseEnduranceDuration;
    private String exerciseEnduranceIntensity;
    private Integer exerciseEnduranceFrequency;
    private String exerciseResistanceIntensity;
    private Integer exerciseResistanceFrequency;
}