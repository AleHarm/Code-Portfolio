using UnityEngine;
using UnityEngine.UI;

public class ButtonProperties : MonoBehaviour
{
    public bool hasOnClickAction = false;
}

public class AssignButtonClickSound : MonoBehaviour
{
    void Update()
    {
        GameObject[] objectsWithTag = GameObject.FindGameObjectsWithTag("Button");

        // Do something with the found objects
        foreach (GameObject obj in objectsWithTag)
        {
            ButtonProperties buttonProperties = obj.GetComponent<ButtonProperties>();

            // If ButtonProperties component doesn't exist, add it
            if (buttonProperties == null)
            {
                buttonProperties = obj.AddComponent<ButtonProperties>();
            }

            // Check if the GameObject has a Button component
            Button button = obj.GetComponent<Button>();
            if (button != null)
            {
                // Check if the ButtonProperties component hasOnClickAction set to false
                if (!buttonProperties.hasOnClickAction)
                {
                    button.onClick.AddListener(OnClickAction);
                    buttonProperties.hasOnClickAction = true;
                }
            }
        }
    }

    void OnClickAction()
    {
        AudioManager.Instance.PlaySFX("ButtonClick");
    }
}
