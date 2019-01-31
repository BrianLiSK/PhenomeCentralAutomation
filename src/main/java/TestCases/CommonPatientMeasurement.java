package TestCases;

/**
 * This class acts as a public struct for patient measurements, too many fields to just pass as a List of Strings.
 * All the possible numerical fields for a patient's measurements should go here.
 */
public class CommonPatientMeasurement
{
    // Ctor
    public CommonPatientMeasurement(float weight, float armSpan, float headCircumference, float outerCanthalDistance,
        float leftHandLength, float rightHandLength, float height, float sittingHeight, float philtrumLength,
        float inntercanthalDistance, float leftPalmLength, float rightPalmLength, float leftEarLength,
        float palpebralFissureLength, float leftFootLength, float rightFootLength, float rightEarLength,
        float interpupilaryDistance)
    {
        this.weight = weight;
        this.armSpan = armSpan;
        this.headCircumference = headCircumference;
        this.outerCanthalDistance = outerCanthalDistance;
        this.leftHandLength = leftHandLength;
        this.rightHandLength = rightHandLength;
        this.height = height;
        this.sittingHeight = sittingHeight;
        this.philtrumLength = philtrumLength;
        this.inntercanthalDistance = inntercanthalDistance;
        this.leftPalmLength = leftPalmLength;
        this.rightPalmLength = rightPalmLength;
        this.leftEarLength = leftEarLength;
        this.palpebralFissureLength = palpebralFissureLength;
        this.leftFootLength = leftFootLength;
        this.rightFootLength = rightFootLength;
        this.rightEarLength = rightEarLength;
        this.interpupilaryDistance = interpupilaryDistance;
    }

    public float weight;
    public float armSpan;
    public float headCircumference;
    public float outerCanthalDistance;
    public float leftHandLength;
    public float rightHandLength;

    public float height;
    public float sittingHeight;
    public float philtrumLength;
    public float inntercanthalDistance;
    public float leftPalmLength;
    public float rightPalmLength;

    public float leftEarLength;
    public float palpebralFissureLength;
    public float leftFootLength;
    public float rightFootLength;

    public float rightEarLength;
    public float interpupilaryDistance;
}
