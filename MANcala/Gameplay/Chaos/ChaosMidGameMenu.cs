using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ChaosMidGameMenu : MonoBehaviour
{

  private GameObject holes;
  private bool isPvC;

  void Update(){

    if(holes == null){

      holes = GameObject.Find("Holes");
    }

    if (Input.GetKeyDown(KeyCode.Escape))
    {
      holes.GetComponent<ChaosHoles>().SetIsInMenu(true);
      ShowMidGameMenu();
    }
  }

  void ShowMidGameMenu(){
    IterateOverChildren(true);
  }

  public void HideMidGameMenu(){
    IterateOverChildren(false);
    holes.GetComponent<ChaosHoles>().SetIsInMenu(false);
  }

  private void IterateOverChildren(bool value){
    foreach (Transform child in transform)
    {
      if (child.GetComponent<Button>() != null)
      {
        Button button = child.GetComponent<Button>();
        if (button != null)
        {
          button.interactable = value;
        }
      }
      child.gameObject.SetActive(value);
    }
  }
}
