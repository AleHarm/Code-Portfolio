using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class StartMenuNavigation : MonoBehaviour
{

    public GameObject SceneLoaderObject;

    private GameObject easyButton;
    private GameObject normalButton;
    private GameObject backButton;
    private GameObject creditsButton;
    private GameObject PvPButton;
    private GameObject PvCButton;
    private GameObject rulesButton;
    private GameObject settingsButton;
    private GameObject exitButton;
    private GameObject rulesText;
    private GameObject musicCreditsText;
    private GameObject alexCreditsText;
    private GameObject musicText;
    private GameObject SFXText;
    private GameObject musicSlider;
    private GameObject SFXSlider;

    public void Awake(){

        easyButton = this.transform.GetChild(FindChildIndexByName(this.transform, "Easy Button")).gameObject;
        normalButton = this.transform.GetChild(FindChildIndexByName(this.transform, "Normal Button")).gameObject;
        backButton = this.transform.GetChild(FindChildIndexByName(this.transform, "Back Button")).gameObject;
        creditsButton = this.transform.GetChild(FindChildIndexByName(this.transform, "Credits Button")).gameObject;
        PvPButton = this.transform.GetChild(FindChildIndexByName(this.transform, "PvP Button")).gameObject;
        PvCButton = this.transform.GetChild(FindChildIndexByName(this.transform, "PvC Button")).gameObject;
        rulesButton = this.transform.GetChild(FindChildIndexByName(this.transform, "Rules Button")).gameObject;
        settingsButton = this.transform.GetChild(FindChildIndexByName(this.transform, "Audio Settings Button")).gameObject;
        exitButton = this.transform.GetChild(FindChildIndexByName(this.transform, "Exit Button")).gameObject;
        rulesText = this.transform.GetChild(FindChildIndexByName(this.transform, "Rules Text")).gameObject;
        musicCreditsText = this.transform.GetChild(FindChildIndexByName(this.transform, "Music Credits Text")).gameObject;
        alexCreditsText = this.transform.GetChild(FindChildIndexByName(this.transform, "Alex Credits Text")).gameObject;
        musicText = this.transform.GetChild(FindChildIndexByName(this.transform, "Music Text")).gameObject;
        musicSlider = this.transform.GetChild(FindChildIndexByName(this.transform, "Music Slider")).gameObject;
        SFXText = this.transform.GetChild(FindChildIndexByName(this.transform, "SFX Text")).gameObject;
        SFXSlider = this.transform.GetChild(FindChildIndexByName(this.transform, "SFX Slider")).gameObject;
        
        DisplayMainMenu();
    }

    public void QuitGame(){
        #if UNITY_EDITOR
            UnityEditor.EditorApplication.isPlaying = false;
        #else
            Application.Quit();
        #endif
    }

    public void StartPvP(){

        SceneLoaderObject.GetComponent<SceneLoader>().LoadScene("PvP");
    }

    private void StartPvC(){

        SceneLoaderObject.GetComponent<SceneLoader>().LoadScene("PvC");
    }

    public void StartCPU(bool isEasy){

        PlayerPrefs.SetInt("isEasy", isEasy ? 1 : 0);
        PlayerPrefs.Save();
        StartPvC();
    }

    public void DisplayCPUDifficultyOptions(){

        DisableAllMenuObjects();
        easyButton.SetActive(true);
        normalButton.SetActive(true);
        backButton.SetActive(true);
    }

    public void DisplayRules(){

        DisableAllMenuObjects();

        rulesText.SetActive(true);
        backButton.SetActive(true);
    }

    public void DisplaySettings(){

        DisableAllMenuObjects();
        musicText.SetActive(true);
        musicSlider.SetActive(true);
        SFXText.SetActive(true);
        SFXSlider.SetActive(true);
        backButton.SetActive(true);
    }

    public void DisplayMainMenu(){

        DisableAllMenuObjects();

        PvPButton.SetActive(true);
        PvCButton.SetActive(true);
        rulesButton.SetActive(true);
        settingsButton.SetActive(true);
        creditsButton.SetActive(true);
        exitButton.SetActive(true);
    }

    public void DisplayCredits(){

        DisableAllMenuObjects();

        musicCreditsText.SetActive(true);
        alexCreditsText.SetActive(true);
        backButton.SetActive(true);
    }

    public void DisableAllMenuObjects(){

        foreach(Transform menuItem in this.transform){
            menuItem.gameObject.SetActive(false);
        }
    }

    int FindChildIndexByName(Transform parent, string childName){
        
        for (int i = 0; i < parent.childCount; i++){
            if (parent.GetChild(i).name == childName){
                return i;
            }
        }
        return -1;
    }
}
