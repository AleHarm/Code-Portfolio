using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class MidGameMenu : MonoBehaviour
{

  private GameObject holes;
  private bool isPvC;

  void Update(){

    if(holes == null){

      holes = GameObject.Find("Holes");

      if(holes == null){

        holes = GameObject.Find("Holes(Clone)");
      }

      if(holes == null){

        holes = GameObject.Find("Holes PvC");

        if(holes == null){
          holes = GameObject.Find("HolesPvC(Clone)");
        }
        isPvC = true;
      }else{

        isPvC = false;
      }
    }

    if (Input.GetKeyDown(KeyCode.Escape))
    {
      if(isPvC){
        holes.GetComponent<HolesPvC>().SetIsInMenu(true);
      }else{
        holes.GetComponent<Holes>().SetIsInMenu(true);
      }
      ShowMidGameMenu();
    }
  }

  void ShowMidGameMenu(){
    IterateOverChildren(true);
  }

  public void HideMidGameMenu(){
    IterateOverChildren(false);
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
