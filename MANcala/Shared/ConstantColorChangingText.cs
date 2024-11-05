using UnityEngine;
using TMPro;

public class ConstantColorChangingText : MonoBehaviour
{
    public float colorChangeSpeed = 1f; // Adjust the speed of color change
    public float saturation = 1f; // Saturation of the colors
    public float value = 1f; // Value of the colors

    private TextMeshProUGUI textMeshProComponent;
    private float hueValue = 0f;

    void Start()
    {
        // Get the TextMeshProUGUI component attached to this GameObject
        textMeshProComponent = GetComponent<TextMeshProUGUI>();

        // Ensure there's a TextMeshProUGUI component attached
        if (textMeshProComponent == null)
        {
            Debug.LogError("No TextMeshProUGUI component found on this GameObject!");
            enabled = false; // Disable the script to prevent errors
        }
    }

    void Update()
    {
        // Increment hue value based on time and speed
        hueValue += colorChangeSpeed * Time.deltaTime;

        // Ensure hue value remains within the range [0, 1]
        if (hueValue > 1f)
        {
            hueValue -= 1f;
        }

        // Convert HSV color to RGB
        Color color = Color.HSVToRGB(hueValue, saturation, value);

        // Apply the color to the text
        textMeshProComponent.color = color;
    }
}
